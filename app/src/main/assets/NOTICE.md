# Notice de Studo

Tout fonctionne sur ton téléphone : tes discussions, ton code et tes images ne quittent jamais l'appareil. Internet sert seulement à télécharger les IA.

## Avant de commencer
- **Téléphone** : ARM64 (presque tous les récents), Android 8 ou plus. Android 10 ou plus pour enregistrer les images et les .zip dans le téléphone.
- **Mémoire** : 4 Go de RAM minimum, 6 Go ou plus conseillés. L'écran Modèles affiche ta RAM et le type de modèle adapté.
- **Espace** : prévois 2 à 10 Go libres. Wi-Fi conseillé (de 0,4 à 5 Go par IA).
- L'écran reste allumé pendant qu'une IA travaille ou qu'un modèle se télécharge. Garde Studo ouvert au premier plan, sinon Android peut l'arrêter.
- Une machine nommée « principale » est créée toute seule : tu n'as rien à configurer pour commencer.

## Un seul écran
- Tu écris dans **une seule zone de texte**. Studo choisit tout seul : **discussion**, **logiciel** complet ou **image**. Le choix est affiché sous la réponse (« choisi par Studo »).
- Tu peux forcer le choix avec les boutons **Auto / Discussion / Logiciel / Image** au-dessus de la zone de texte.
- Les boutons **💬** et **🖼** choisissent l'IA de chat/code et l'IA d'images. **Auto** prend une IA adaptée (une IA « Coder » pour les logiciels).
- Le menu **☰** (en haut à gauche) ouvre l'historique. Les icônes du haut ouvrent **Modèles** et **Réglages**, et **+** démarre une nouvelle session.
- Tu vois tout **en direct** : le texte s'écrit mot après mot, chaque fichier d'un logiciel apparaît pendant qu'il s'écrit, et l'image affiche sa progression et son journal.
- Le bouton ■ arrête le travail en cours.

## Fichiers joints (comme Claude)
- Le trombone 📎 joint des fichiers : code, texte, .zip de code, PDF, images. Ils apparaissent comme des **pastilles** : leur contenu n'est jamais collé dans ton message. Il est envoyé à l'IA en coulisses.
- **PDF** : le texte est extrait au mieux. Un PDF scanné (photo de pages) n'est pas lisible.
- **Images** : une IA de chat ne peut pas « voir » une image. Une image jointe sert de base pour la **modifier** (par exemple « mets-lui un chapeau »). Sous une image générée, **Modifier** la reprend comme base.
- Un très long fichier est raccourci pour tenir dans la mémoire de l'IA.

## Historique et catégories
- Chaque conversation est une **session** sauvegardée automatiquement, rangée par date : Aujourd'hui, Hier, 7 derniers jours…
- Filtres : **Tout**, **Discussions**, **Logiciels**, **Images**, **Favoris**.
- Les trois points de chaque session permettent de **renommer**, mettre en **favori** ou **supprimer**.
- Les sessions sont propres à chaque machine.

## Installer une IA (écran Modèles)
- **Catalogue** : appuie sur **Installer**. La barre de progression s'affiche avec un bouton **Annuler**. Si le téléchargement s'interrompt, relance-le : il reprend là où il s'était arrêté.
- **Vérifier les liens** teste chaque lien du catalogue.
- **Hugging Face** : cherche un nom (« qwen coder », « llama », « stable diffusion »). Touche un modèle pour voir ses fichiers, leur taille et s'ils sont adaptés à ta RAM.
- **Web** : un navigateur intégré. Ouvre un modèle sur Hugging Face, onglet « Files », touche le téléchargement d'un .gguf : Studo propose de l'installer.
- **Installer depuis un fichier du téléphone** (onglet Installés) : choisis des fichiers déjà présents sur l'appareil (.gguf, .safetensors…). Studo lit l'en-tête et reconnaît tout seul une **IA de chat**, une **IA d'images** ou un **fichier complémentaire**. Si le type est faux, le bouton **Type…** de la carte le corrige.
- Pour discuter : Qwen2.5 1.5B ou 3B, Llama 3.2. Pour le code : Qwen2.5-Coder. Pour les images : SD-Turbo (rapide) ou Stable Diffusion 1.5.

## IA d'images : modèles .gguf et complémentaires
- Les modèles Stable Diffusion 1.x/2.x/XL en .gguf marchent seuls.
- **Flux**, **SD3** et similaires ont besoin de fichiers en plus : **VAE** (ae.safetensors), **clip_l**, **t5xxl** (et **clip_g** pour SD3). Installe-les avec « Installer depuis un fichier » : Studo les reconnaît à leur nom et les utilise tout seul. Un avertissement rouge indique ce qui manque.
- Ces modèles sont lourds : sur téléphone ils demandent beaucoup de RAM et de temps.

## Désinstaller et vérifier (onglet Installés)
- **Désinstaller** efface le fichier du téléphone après confirmation. **Vérifier** contrôle qu'un fichier est complet et dit comment il est reconnu. **Tout vérifier** le fait pour tous.
- **Téléchargements interrompus** : les morceaux inachevés. Supprime-les ou relance l'installation.

## Créer un logiciel
- Écris par exemple : « crée une calculatrice en page web avec historique et mode sombre ». Studo prépare un plan, puis écrit chaque fichier sous tes yeux.
- Compte de 5 à 30 minutes. Sur la carte du logiciel : touche un fichier pour le lire et le copier, **Aperçu** ouvre une page HTML, **Télécharger le .zip** l'enregistre dans Téléchargements › Studo.
- Pour modifier le logiciel, écris simplement : « ajoute un bouton pour effacer l'historique ». Studo choisit les fichiers concernés.
- Pour lancer un logiciel web, décompresse le .zip et ouvre index.html. Teste toujours le résultat.
- Demande des logiciels simples : une IA de téléphone se perd sur les gros projets.

## Créer ou modifier une image
- Écris par exemple : « dessine un chat astronaute sur la lune ». L'IA de chat réécrit ta demande en description anglaise (réglable), puis l'IA d'images dessine.
- Commence en **256×256** pour tester vite (Réglages), puis passe à 512.
- **Enregistrer** envoie l'image dans Photos › Studo. **Modifier** la reprend comme base : décris ce que tu veux changer.
- Réglages : **force de modification** (bas = proche de l'original, 50 à 70 % conseillé).

## Réglages
- **Apparence** : Système, Clair ou Sombre.
- **Machines** : espaces séparés (IA, projets, images, sessions).
- **Créativité** : bas (0,2) = précis, idéal pour le code. **Mémoire de conversation** : 2048 sur un petit téléphone, 8192 avec 10 Go de RAM ou plus.
- **Instructions personnalisées** : une consigne ajoutée à chaque discussion.
- **Analyse complète** : vérifie le téléphone, les moteurs, tous les modèles et la connexion. **Copier le rapport** permet de l'envoyer si quelque chose ne marche pas.

## Problèmes courants
- **Le moteur s'arrête** : code 137 = mémoire insuffisante. Choisis une IA plus petite, mets la mémoire de conversation à 2048, ferme les autres applis.
- **Moteur absent** : l'APK a été compilé sans les moteurs. Relance le workflow GitHub et lis son journal.
- **Une IA d'images n'est pas reconnue** : ouvre Modèles › Installés, touche **Type…** et choisis « IA d'images ».
- **Plan illisible pour un logiciel** : reformule en une phrase claire, ou prends un modèle plus grand.
- **Réponses lentes ou fausses** : normal sur processeur. Une IA locale est bien moins puissante qu'une grande IA en ligne.
