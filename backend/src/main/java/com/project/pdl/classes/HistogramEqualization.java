package com.project.pdl.classes;

import boofcv.struct.image.GrayU8;
import boofcv.struct.image.Planar;

public class HistogramEqualization {

	public static void apply(Planar<GrayU8> input, Planar<GrayU8> output, int band){
		int[] rgb = new int[3];
		float[] hsv = new float[3];
		int[] LUT = new int[256];
		int[] LUTC = new int[256];

		for(int y = 0; y<input.height; y++){
			for(int x = 0; x<input.width; x++){
				for(int i = 0; i<input.getNumBands(); i++){
				rgb[i] = input.getBand(i).get(x, y);
				}

                Conversion.rgbToHsv(rgb[0], rgb[1], rgb[2], hsv);
				if(band == 2){
					LUT[(int)hsv[band]] ++;
				}
				else if(band == 1){
					LUT[(int)(hsv[band] * 255)] ++;
				}

			}
		}
		LUTC[0] = LUT[0];
		for(int i = 1; i <= 255; i++ ){
			LUTC[i] = LUTC[i-1] + LUT[i];
		}

		int size = input.height * input.width;
		for(int y = 0; y<input.height; y++){
			for(int x = 0; x<input.width; x++){
				for(int i = 0; i<input.getNumBands(); i++){
				rgb[i] = input.getBand(i).get(x, y);
				}
				Conversion.rgbToHsv(rgb[0], rgb[1], rgb[2], hsv);
                if(band == 2){
				    Conversion.hsvToRgb(hsv[0], hsv[1], (LUTC[(int)hsv[band]] * 255)/size, rgb);
                }
                if(band == 1){
				    Conversion.hsvToRgb(hsv[0], (float)(LUTC[(int)(hsv[band]*255)])/size, hsv[2], rgb);
                }
				for(int i = 0; i<input.getNumBands(); i++){
				output.getBand(i).set(x, y, rgb[i]);;
				}
			}
		}

	}

}
