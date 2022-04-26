package com.project.pdl.classes;

import boofcv.struct.image.GrayU8;
import boofcv.struct.image.Planar;

public class Mirror {
    public static void apply(Planar<GrayU8> input, Planar<GrayU8> output) {

        for(int y = 0; y < input.height; ++y){
			for(int x = 0; x < input.width; ++x){
				for(int i = 0; i< input.getNumBands(); i++){
					int gl = input.getBand(i).get(x, y);
					output.getBand(i).set(input.width - x - 1, y, gl);
				}
			}
		}
    }
}
