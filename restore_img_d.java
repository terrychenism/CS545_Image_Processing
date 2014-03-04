import ij.*;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;

public class restore_img_d implements PlugInFilter {

    public int setup(String arg, ImagePlus imp) {
        return DOES_ALL;
    }

    public void run(ImageProcessor orig) {
        int w = orig.getWidth();
        int h = orig.getHeight();
        //3x3 filter matrix
        double[][] filter = {{-1, -1, -1},
        		{ -1, 12, -1},
        	{ -1, -1, -1}};  
        
        ImageProcessor copy1 = orig.duplicate();
       ImageProcessor copy2 = orig.duplicate();


		for (int v = 1; v <= h - 2; v++) {
			for (int u = 1; u <= w - 2; u++) {
				// compute filter result for position (u,v)
				double rsum = 0,gsum = 0,bsum = 0;
				double coeff = 0; 
				for (int j = -1; j <= 1; j++) {
					for (int i = -1; i <= 1; i++) {
						int p = copy1.getPixel(u + i, v + j);
						int r = (p & 0xff0000) >> 16;          
						int g = (p & 0x00ff00) >> 8; 
						int b = (p & 0x0000ff);


						double c = filter[j + 1][i + 1];
						coeff += c;
						rsum = Math.min(rsum + c * r, 255);
						gsum = Math.min(gsum + c * g, 255);
						bsum = Math.min(bsum + c * b, 255);
						//sum = (((int)rsum & 0xff)<<16) | (((int)gsum & 0xff)<<8) | (int)bsum & 0xff; 
					}
				}
				double s = 1.0/coeff;

               // int q = (int) Math.round(s*sum);
				int q = (((int)rsum & 0xff)<<16) | (((int)gsum & 0xff)<<8) | (int)bsum & 0xff;
				int qq = (int) Math.round(s*q);
				copy2.putPixel(u, v, qq);
			}
		}
		
		int pix;
		int value;
		for (int v = 0; v < h; v++) {
			for (int u = 0; u < w; u++) {
				pix = copy2.getPixel(u, v);
				value = orig.getPixel(u,v);
				int pixel =value-pix;
				orig.putPixel(u,v,pixel);
					
				
			}
		}	
		
		
    }

}
