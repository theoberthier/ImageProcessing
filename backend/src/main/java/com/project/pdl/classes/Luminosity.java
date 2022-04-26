package com.project.pdl.classes;

import boofcv.struct.image.GrayU8;
import boofcv.struct.image.Planar;

public class Luminosity {

    public static void apply(Planar<GrayU8> input, Planar<GrayU8> output, int d){
        for(int y = 0; y < input.height; ++y){
			for(int x = 0; x < input.width; ++x){
				for(int i = 0; i< input.getNumBands(); i++){
					int gl = input.getBand(i).get(x, y);
					gl += d;
					if(gl < 0) gl = 0;
					if(gl > 255) gl = 255;
					output.getBand(i).set(x, y, gl);
				}
			}
		}
    }

}
