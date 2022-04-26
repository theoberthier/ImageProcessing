## Fonctionnalités présentes
**Les routes fonctionnelles :**  
- GET       /images
- POST      /images
- DELETE    /images/{id}
- GET       /images/{id}
    - paramètres possibles
        - "algorithm" :  
            "luminosity"    -> "delta"  
          | "color"         -> "delta"  
          | "ridge"         -> pas de paramètres supp'  
          | "histo"         -> "band"  
          | "blur"          -> "radius" & "filter"  

**Les formats acceptés :**  
- JPG
- JPEG
- PNG

**Algorithmes dispo :**
- Luminosité
- Filtre de couleur
- Egalisation de l'histogramme
- Filtre de flou
- Filtre de contour

**Développement réalisé et testé sous :**
- Linux Ubuntu
- MacOS X 12.1
    - Google Chrome : 99.0.4844.83
    - Maven         : 3.8.4
    - Java          : 17.0.2

**Fonctionnalités front :**
- Liste des images disponibles sur le serveur ** fait **  
- Ajouter une image sur le serveur ** in progress ** 
- Télécharger une image sur le disque ** in progress **
- Carouselle d'image ** in progress **
- Selection d'image depuis la liste ** in progress **
- Application de filtres ** in progress **

**Fonctionnalités back :**
- Routes ** fait **
- Arborescence dossier images avec sous dossiers jusqu'à la couche N ** fait **
- Retour d'erreur sur les routes ** fait **
- Algorithmes ** fait **
