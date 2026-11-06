# Studo (Android)

Application Android qui installe des IA en local (chat, code, images) dans des « machines » isolées,
et qui écrit des logiciels complets que tu télécharges en .zip.

## Compiler l'APK avec GitHub
1. Envoie tout le contenu de ce dossier dans ton dépôt GitHub (y compris `.github/` et `app/fixed.keystore`).
2. Onglet **Actions** → workflow **Build APK** (≈ 20-40 min la 1re fois, ensuite beaucoup plus court grâce au cache des moteurs).
3. Télécharge l'artefact **Studo-apk**, décompresse, installe l'APK. Ne mets PAS `local.properties` sur GitHub.

## Fonctions (version 0.6)
- **Un seul écran** : une zone de texte, Studo choisit discussion, logiciel ou image (ou tu forces le choix), avec choix des IA.
- **Vue en direct** : texte mot à mot, fichiers du logiciel pendant leur écriture, progression et journal de l'image.
- **Fichiers joints comme Claude** : pastilles (code, texte, zip, PDF, images), contenu envoyé sans être collé dans le message.
- **Historique** : sessions par date, catégories (Discussions, Logiciels, Images, Favoris), renommer, favori, supprimer.
- **Modèles** : détection du type par l'en-tête GGUF (chat / image / complémentaire), installation depuis un fichier du téléphone,
  catalogue, Hugging Face, navigateur web intégré, vérifier / désinstaller, fichiers complémentaires Flux/SD3.
- **Logiciels** : génération vue en direct, aperçu HTML, export .zip, modification par simple demande.
- **Images** : création et modification (image à image), enregistrement dans Photos.
- **Réglages** : thème clair / sombre, machines, réglages IA et images, analyse complète, notice.

## Limites à connaître
- ARM64 uniquement. 4 Go de RAM minimum, 6 Go+ conseillés.
- Génération d'images sur CPU : plusieurs minutes. Commence en 256×256.
- Une IA de téléphone reste bien moins puissante qu'une grande IA en ligne.
- Le code n'a pas pu être compilé sur l'environnement de développement : si le build GitHub échoue,
  envoie le journal d'erreur.
