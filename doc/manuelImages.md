# Manuel d'utilisation des fonctions de traitement d'image

## Initialisation
    BufferedImage inputImage = UtilImageIO.loadImage(inputPath);
    Planar<GrayU8> input = ConvertBufferedImage.convertFromPlanar(inputImage, null, true, GrayU8.class);
    Planar<GrayU8> output = input.createSameShape();

## Luminosité
**Luminosity.apply(input, output, d); :**  
input : ``Planar<GrayU8>``  
output : ``Planar<GrayU8>``  
d : ``int`` , delta servant a augmenter ou diminuer la luminosité  

## Egalisateur d'histogramme
**HistogramEqualization.apply(input, output, band)**  
input : ``Planar<GrayU8>``  
output : ``Planar<GrayU8>``  
band : ``int`` , **1** pour appliquer l'égalisation sur le canal S, **2** pour le canal V  

## Filtre coloré
**ColorFiltre.apply(input, output, d)**  
input : ``Planar<GrayU8>``  
output : ``Planar<GrayU8>``  
d : ``int`` , delta servant à modifier la teinte  

## Filtre de flou
**BlurFiltre.apply(input, output, radius, filtre)**  
input : ``Planar<GrayU8>``  
output : ``Planar<GrayU8>``  
radius : ``int`` , le rayon du kernel 
filtre : ``int`` , 1 pour appliquer le filtre moyenneur, 2 pour le gaussien

## Filtre de contour
**RidgeFilter.apply(input, output)**  
input : ``Planar<GrayU8>``  
output : ``Planar<GrayU8>``  

## Sauvegarde de l'image output
        final String outputPath = args[1];
        UtilImageIO.saveImage(output, outputPath);
        System.out.println("Image saved in: " + outputPath);
