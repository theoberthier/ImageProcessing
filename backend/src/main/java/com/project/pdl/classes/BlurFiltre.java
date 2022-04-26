package com.project.pdl.classes;

import boofcv.abst.filter.blur.BlurFilter;
import boofcv.alg.filter.blur.GBlurImageOps;
import boofcv.factory.filter.blur.FactoryBlurFilter;
import boofcv.struct.image.GrayU8;
import boofcv.struct.image.Planar;

public class BlurFiltre {
    public static void apply(Planar<GrayU8> input, Planar<GrayU8> output, int size, int filter){
        if(filter == 1){
            BlurFilter<Planar<GrayU8>> filterMean = FactoryBlurFilter.mean(input.getImageType(), size);
            filterMean.process(input, output);
        }
        else if(filter == 2){
            GBlurImageOps.gaussian(input, output, -1, size, null);
        }
    }
}
