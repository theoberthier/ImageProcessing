# Undo / Redo

Bouton "Retour en arrière" / "Suivant" avec une seule couche de profondeur pour le retour et le suivant.  
Les boutons apparaitront quand il y a une image précédente ou suivante de l'image selectionnée.

**Comment savoir qu'une image a une image précédente ou non ???**
Nous allons utiliser les méta-data, lors de la récupération de la liste des images (GET /images), nous allons ajouter les valeurs "undo" et "redo" comme étant des **booleans**.

Voilà ce que ça donnera :
```json
{
    "id":1,
    "name":"limage.png",
    "type":"PNG",
    "size":"350*240*3",
    "undo":true,
    "redo":false
}
```

Une fois ces informations récupérée nous pouvons afficher ou non les boutons.
Ces boutons auront comme fonctionnalité d'appeler le back avec les routes **/undo/{id}** et **/redo/{id}**.

**Du côté serveur :**
Les images undo et redo seront stockés dans les dossiers **/images/undo** et **/images/redo** avec le nom de l'image correspondante.  

Lors de l'initialisation de l'image, avec la classe **Image** nous vérifirons si les images existent dans les dossier correspondants et traiteront les paramètres (bool) en fonction des résultats.  

**Que ce passe t'il lors de l'appel de la route "undo" ?**
Lors de chaque traitement avec algorithme, puis ensuite l'image sauvegardé, l'image originale sera automatiquement déplacée dans le dossier **/images/undo**. Il suffira seulement de remplacer l'images dans **images/monimage.png** par **images/undo/monimage.png** en déplaçant l'image de **images/monimage.png** dans le dossier **images/redo/monimage.png** et bien evidemment de remplacer le paramètre de l'image pour mettre le paramètre "undo" à **FAUX**.

Puis on renvoi l'image au format **byte[]**.


**Que ce passe t'il lors de l'appel de la route "redo" ?**
Même principe que pour le "undo", on met l'image **images/monimage.png** dans le dossier **images/undo/monimage.png** et l'image **images/redo/monimage.png** dans le dossier **images/monimage.png**. Et on modifie les paramètres redo et undo.

Puis on renvoi l'image au format **byte[]**.


# Preview & Replace

Lors d'une application d'algorithmes, l'image traitée sera chargée à la place de l'"image originale" mais ne sera pas sauvegardé à la place de l'image originale. C'est ce qu'on appel la **preview**.

Lors de ces changements, l'utilisateur pourra cliquer sur "save on server" ce qui remplacera l'image originale par l'image transformée. Le seul moyen de retrouver l'image original sera le "undo".

**Côté serveur :**
Une route **/replace/{id}** permettra de replacer l'image temporaire renvoyé comme preview (**/images/tmp/monimage.png**) directement à la place de l'image (**/images/tmp/monimage.png**) et juste avant de déplacer l'image original dans le dossier **/images/undo/**.

# Multi-filtres

## Explication
Cette fonctionnalité permettra d'appliquer plusieurs filtres à une image.

## Côté front
L'utilisateur fait toutes ses modifications de filtres, une fois fait il faudra envoyer successivement l'appel à plusieurs routes.  
Tout d'abord la route **/reset/{id}** pour indiquer au serveur qu'on souhaite repartir sur l'image actuelle du serveur.  
Ensuite plusieurs appels à la route **/images/{id}/algorithm=?...**.  
L'image renvoyée par le dernier appel de la route sera l'image appliquée avec tous les filtres.  

## Côté back
**Route /reset/{id} :**
Reset le fichier stocké dans le dossier tmp par l'image dans le dossier original. Ce qui nous permettra d'appliquer les filtres directement sur l'image du dossier tmp un par un et de remplacer cette image à chaque fois par sa propre image avec le filtre.

**Route /images/{id}/algorithm=?... :**
Cette fonction ne changera pas, mis à part un petit détail, nous ne traiteront plus les images depuis le dossier **/images** mais depuis le dossier **/images/tmp**, si l'image dans le dossier **/tmp** n'existe pas, alors il la créera.

# New algorithms