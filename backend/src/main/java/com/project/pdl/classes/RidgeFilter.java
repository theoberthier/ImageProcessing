package com.project.pdl.classes;

import boofcv.struct.image.GrayU8;
import boofcv.struct.image.Planar;

public class RidgeFilter {
    public static void apply(Planar<GrayU8> input, Planar<GrayU8> output) {

        GrayU8 inputGrayU8 = Conversion.rgbToGray(input);
        int[][] h1 = {{-1,-2,-1},
                      {0,0,0},
                      {1,2,1}};
        int[][] h2 = {{-1,0,1},
                      {-2,0,2},        
                      {-1,0,1}};
        int radius = 1;
        for(int y = 0; y< inputGrayU8.height; y++){
            for(int x = 0; x<inputGrayU8.width; x++){
                float r1 = 0;
                float r2 = 0;
                for(int u = -radius; u <= radius; u++){
                    for(int v = -radius; v <= radius; v++){
                        int gl = 0;
                        int x2 = x+u;
                        int y2 = y+v;
                        if(x2 >= 0 && x2 < inputGrayU8.width && y2 >= 0 && y2 < inputGrayU8.height){
                            gl = inputGrayU8.get(x2, y2);
                        }
                        r1 += (gl * h1[u + radius][v + radius]);
                        r2 += (gl * h2[u + radius][v + radius]);
                    }
                }
                double m = Math.sqrt(((r1*r1)+(r2*r2)));
                if(m>255) m=255;
                for(int i = 0; i< input.getNumBands(); i++){
                    output.getBand(i).set(x, y, (int)m);
                }
            }
        }
    }
}
