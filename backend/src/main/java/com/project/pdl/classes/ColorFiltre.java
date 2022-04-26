package com.project.pdl.classes;

import boofcv.struct.image.GrayU8;
import boofcv.struct.image.Planar;

public class ColorFiltre {
    
    public static void apply(Planar<GrayU8> input, Planar<GrayU8> output, int d) {
        int rgb[] = new int[3];
		float[] hsv = new float[3];
		for(int y = 0; y<input.height; y++){
			for(int x = 0; x<input.width; x++){
				for(int i = 0; i<input.getNumBands(); i++){
				rgb[i] = input.getBand(i).get(x, y);
				}
				Conversion.rgbToHsv(rgb[0], rgb[1], rgb[2], hsv);
				hsv[0] = d;
				Conversion.hsvToRgb(hsv[0], hsv[1], hsv[2], rgb);
				for(int i = 0; i<input.getNumBands(); i++){
				output.getBand(i).set(x, y, rgb[i]);;
				}
			}
		}
    }

}
