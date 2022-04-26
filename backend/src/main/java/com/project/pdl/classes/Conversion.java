package com.project.pdl.classes;

import boofcv.struct.image.GrayU8;
import boofcv.struct.image.Planar;

public class Conversion {

    public static GrayU8 rgbToGray(Planar<GrayU8> input){
        GrayU8 output = input.getBand(0).createSameShape();
		for(int y = 0; y<input.height; y++){
		  for(int x = 0; x<input.width; x++){
			int red = input.getBand(0).get(x, y);
			int green = input.getBand(1).get(x, y);
			int blue = input.getBand(2).get(x, y);
			double r = 0.3*red + 0.59*green + 0.11*blue;
			for(int i = 0; i<input.getNumBands(); i++){
			  output.set(x, y, (int)r);
			}
		  }
		}
        return output;
	}

    public static void rgbToHsv(int r, int g, int b, float[] hsv){
        float max = (Math.max(r, g) > Math.max(g, b)) ? Math.max(r, g) : Math.max(g, b);
        float min = (Math.min(r, g) < Math.min(g, b)) ? Math.min(r, g) : Math.min(g, b);

        if(max == min){
            hsv[0] = 0;
        }
        else if(max == r){
            hsv[0] = (60 * (((g - b) / (max - min)) + 360))%360;
        }
        else if(max == g){
            hsv[0] = (60 * ((b - r) / (max - min)) + 120);
        }
        else if(max == b){
            hsv[0]  = (60 * ((r - g) / (max - min)) + 240);
        }
        if(max == 0){
            hsv[1] = 0;
        }
        else{
            hsv[1] = 1 - (min / max);
        }
        hsv[2] = max;
    }

    public static void hsvToRgb(float h, float s, float v, int[] rgb) {
        int t = (int) (h / 60) % 6;
        float f = (h / 60) - t;
        float l = v * (1 - s);
        float m = v * (1 - (f * s));
        float n = v * (1 - ((1 - f) * s));
        switch (t) {
            case 0:
                rgb[0] = (int) v;
                rgb[1] = (int) n;
                rgb[2] = (int) l;
                break;
            case 1:
                rgb[0] = (int) m;
                rgb[1] = (int) v;
                rgb[2] = (int) l;
                break;
            case 2:
                rgb[0] = (int) l;
                rgb[1] = (int) v;
                rgb[2] = (int) n;
                break;
            case 3:
                rgb[0] = (int) l;
                rgb[1] = (int) m;
                rgb[2] = (int) v;
                break;
            case 4:
                rgb[0] = (int) n;
                rgb[1] = (int) l;
                rgb[2] = (int) v;
                break;
            case 5:
                rgb[0] = (int) v;
                rgb[1] = (int) l;
                rgb[2] = (int) m;
                break;
        }
    }
}
