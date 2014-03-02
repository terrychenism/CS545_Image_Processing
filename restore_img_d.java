import ij.*;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;

public class restore_img_d implements PlugInFilter {

    public int setup(String arg, ImagePlus imp) {
        return DOES_RGB;
    }

    public void run(ImageProcessor orig) {
        int w = orig.getWidth();
        int h = orig.getHeight();
        //3x3 filter matrix
        double[][] filter = {{0,1,0},
                {1,-4,1},
                {0,1,0}};   
        
        ImageProcessor copy1 = orig.duplicate();
        ImageProcessor copy2 = orig.duplicate();


		for (int v = 1; v <= h - 2; v++) {
			for (int u = 1; u <= w - 2; u++) {
				// compute filter result for position (u,v)
				double sum = 0;
				for (int j = -1; j <= 1; j++) {
					for (int i = -1; i <= 1; i++) {
						int p = copy1.getPixel(u + i, v + j);
						// get the corresponding filter coefficient
						double c = filter[j + 1][i + 1];
						sum = sum + c * p;
						
					}
				}
				int q = (int) sum;
				copy2.putPixel(u, v, q);
			}
		}
		double pix;
		double value;
		for (int v = 0; v < h; v++) {
			for (int u = 0; u < w; u++) {
				pix = copy2.getPixel(u, v);
				value = orig.getPixel(u,v);
				int pixel = (int)(value-pix);
				orig.putPixel(u,v,pixel);
					
				
			}
		}
		
		
		
    }

}
