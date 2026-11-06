package com.iastudio.vm

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.OpenableColumns
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.zip.Inflater
import java.util.zip.ZipInputStream

private const val MAX_TEXT = 24000

/** Lit au plus n octets (readNBytes n'existe pas avant Android 13). */
private fun java.io.InputStream.readUpTo(n: Int): ByteArray {
    val bo = ByteArrayOutputStream()
    val buf = ByteArray(8192)
    var left = n
    while (left > 0) {
        val r = read(buf, 0, minOf(buf.size, left))
        if (r < 0) break
        bo.write(buf, 0, r)
        left -= r
    }
    return bo.toByteArray()
}

/** Lecture des fichiers joints : le contenu part à l'IA sans jamais être collé dans le message. */
object Attachments {
    private val TEXT_EXT = setOf(
        "txt", "md", "json", "xml", "html", "htm", "css", "js", "ts", "tsx", "jsx", "py", "java", "kt", "kts", "c", "h",
        "cpp", "hpp", "cs", "go", "rs", "rb", "php", "sh", "bat", "ps1", "sql", "yml", "yaml", "toml", "ini", "cfg",
        "csv", "tsv", "log", "gradle", "properties", "swift", "dart", "lua", "r", "vue", "svelte", "tex", "srt",
    )
    private val IMAGE_EXT = setOf("png", "jpg", "jpeg", "webp", "bmp", "gif")

    fun displayName(ctx: Context, uri: Uri): String {
        try {
            ctx.contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { c ->
                if (c.moveToFirst()) {
                    val n = c.getString(0)
                    if (!n.isNullOrBlank()) return n
                }
            }
        } catch (e: Exception) {
        }
        return Uri.decode(uri.lastPathSegment ?: "fichier").substringAfterLast('/')
    }

    fun sizeOf(ctx: Context, uri: Uri): Long {
        try {
            ctx.contentResolver.query(uri, arrayOf(OpenableColumns.SIZE), null, null, null)?.use { c ->
                if (c.moveToFirst() && !c.isNull(0)) return c.getLong(0)
            }
        } catch (e: Exception) {
        }
        return -1L
    }

    fun load(ctx: Context, uri: Uri, outDir: File): Att {
        val name = displayName(ctx, uri)
        val size = sizeOf(ctx, uri)
        val ext = name.substringAfterLast('.', "").lowercase()
        val mime = ctx.contentResolver.getType(uri).orEmpty()
        val cr = ctx.contentResolver
        return when {
            mime.startsWith("image/") || ext in IMAGE_EXT -> {
                val bmp = decode({ cr.openInputStream(uri) }, 1024) ?: throw RuntimeException("image illisible")
                val f = File(outDir, "_att_${System.currentTimeMillis()}.png")
                FileOutputStream(f).use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
                Att(name, "image", imagePath = f.path, size = size)
            }
            ext == "pdf" || mime == "application/pdf" -> {
                val bytes = cr.openInputStream(uri)?.use { it.readBytes() } ?: throw RuntimeException("PDF illisible")
                if (bytes.size > 40_000_000) throw RuntimeException("PDF trop gros (40 Mo maximum)")
                val t = PdfText.extract(bytes)
                if (t.isBlank()) {
                    Att(name, "pdf", size = size, note = "texte non extractible (PDF scanné ou police spéciale)")
                } else {
                    val cut = t.length > MAX_TEXT
                    Att(name, "pdf", t.take(MAX_TEXT), size = size, note = if (cut) "début du document seulement" else "")
                }
            }
            ext == "zip" || mime == "application/zip" -> {
                val sb = StringBuilder()
                var count = 0
                cr.openInputStream(uri)?.use { raw ->
                    ZipInputStream(raw.buffered()).use { z ->
                        while (sb.length < MAX_TEXT) {
                            val e = z.nextEntry ?: break
                            if (e.isDirectory) continue
                            val en = e.name.substringAfterLast('.', "").lowercase()
                            if (en !in TEXT_EXT) continue
                            val data = z.readBytes()
                            if (data.size > 80_000 || data.contains(0)) continue
                            sb.append("=== ").append(e.name).append(" ===\n").append(String(data, Charsets.UTF_8)).append("\n\n")
                            count++
                        }
                    }
                }
                if (count == 0) throw RuntimeException("aucun fichier texte ou code dans cette archive")
                Att(name, "zip", sb.toString().take(MAX_TEXT), size = size, note = "$count fichier(s) lu(s)")
            }
            mime.startsWith("text/") || ext in TEXT_EXT || looksLikeText(cr.openInputStream(uri)?.use { it.readUpTo(4096) }) -> {
                val bytes = cr.openInputStream(uri)?.use { it.readUpTo(400_000) } ?: throw RuntimeException("fichier illisible")
                val s = String(bytes, Charsets.UTF_8)
                Att(name, "text", s.take(MAX_TEXT), size = size, note = if (s.length > MAX_TEXT) "début du fichier seulement" else "")
            }
            else -> throw RuntimeException("type de fichier non pris en charge ($name)")
        }
    }

    private fun looksLikeText(b: ByteArray?): Boolean = b != null && b.isNotEmpty() && b.none { it.toInt() == 0 }

    private fun decode(open: () -> java.io.InputStream?, max: Int): Bitmap? {
        val b = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        open()?.use { BitmapFactory.decodeStream(it, null, b) }
        var s = 1
        while (b.outWidth / s > max * 2 || b.outHeight / s > max * 2) s *= 2
        val o = BitmapFactory.Options().apply { inSampleSize = s }
        return open()?.use { BitmapFactory.decodeStream(it, null, o) }
    }
}

/** Extraction de texte d'un PDF sans bibliothèque (au mieux : les PDF scannés ne marchent pas). */
object PdfText {
    private val TOKEN = Regex("\\((?:\\\\.|[^\\\\)])*\\)|\\[|\\]|TJ|Tj|ET|T\\*|Td|TD")

    fun extract(bytes: ByteArray): String {
        val s = String(bytes, Charsets.ISO_8859_1)
        val out = StringBuilder()
        var i = 0
        while (out.length < 200_000) {
            val a = s.indexOf("stream", i)
            if (a < 0) break
            var st = a + 6
            if (st < s.length && s[st] == '\r') st++
            if (st < s.length && s[st] == '\n') st++
            val e = s.indexOf("endstream", st)
            if (e < 0) break
            val objStart = s.lastIndexOf("obj", a)
            val dict = if (objStart >= 0) s.substring(objStart, a) else ""
            var data = bytes.copyOfRange(st, e)
            if (dict.contains("FlateDecode")) data = inflate(data)
            val txt = String(data, Charsets.ISO_8859_1)
            if (txt.contains("BT") && txt.contains("ET")) out.append(ops(txt))
            i = e + 9
        }
        val res = out.toString().replace(Regex("[ \\t]+"), " ").replace(Regex("\\n{3,}"), "\n\n").trim()
        val letters = res.count { it.isLetter() }
        return if (letters < 20) "" else res
    }

    private fun inflate(d: ByteArray): ByteArray {
        val inf = Inflater()
        inf.setInput(d)
        val bo = ByteArrayOutputStream()
        val buf = ByteArray(8192)
        try {
            while (!inf.finished()) {
                val n = inf.inflate(buf)
                if (n == 0 && (inf.needsInput() || inf.needsDictionary())) break
                bo.write(buf, 0, n)
            }
        } catch (e: Exception) {
        } finally {
            inf.end()
        }
        return bo.toByteArray()
    }

    private fun unescape(lit: String): String {
        val sb = StringBuilder()
        var i = 1
        val end = lit.length - 1
        while (i < end) {
            val c = lit[i]
            if (c == '\\' && i + 1 < end) {
                val n = lit[i + 1]
                when {
                    n == 'n' -> { sb.append('\n'); i += 2 }
                    n == 'r' || n == 't' -> { sb.append(' '); i += 2 }
                    n in '0'..'7' -> {
                        var j = i + 1
                        var v = 0
                        var k = 0
                        while (j < end && k < 3 && lit[j] in '0'..'7') { v = v * 8 + (lit[j] - '0'); j++; k++ }
                        if (v >= 32) sb.append(v.toChar())
                        i = j
                    }
                    else -> { sb.append(n); i += 2 }
                }
            } else {
                sb.append(c)
                i++
            }
        }
        return sb.toString()
    }

    private fun ops(t: String): String {
        val sb = StringBuilder()
        TOKEN.findAll(t).forEach { m ->
            val v = m.value
            when {
                v.startsWith("(") -> sb.append(unescape(v))
                v == "Td" || v == "TD" || v == "T*" || v == "ET" -> if (sb.isNotEmpty() && sb.last() != '\n') sb.append('\n')
                else -> {}
            }
        }
        return sb.toString() + "\n"
    }
}
