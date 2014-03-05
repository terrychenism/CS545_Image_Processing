import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

public class restore_img_d implements PlugInFilter {
 
	public int setup(String arg, ImagePlus im) {
		return DOES_RGB; 
	}

	public void run(ImageProcessor orig) {
		ImageProcessor copy = orig.duplicate();
		int w = orig.getWidth();
		int h = orig.getHeight();
		double c = -7;
		double [][] filter = {
				{0,c/4,0},
				{c/4,1-c,c/4},
				{0,c/4,0}
		};
		
		
		for (int v = 1;v <= h-2;v++){
			for (int u = 1;u <= w-2;u++){
			   
				double sumr = 0;
				double sumg = 0;
				double sumb = 0;
				for (int j = -1; j <= 1;j++){
					for (int i = -1; i <= 1; i++){
	                     int p = copy.getPixel(u+i,v+j);
	                     int pr = (p & 0xff0000) >> 16;
	                     int pg = (p & 0x00ff00) >> 8;
	                     int pb = (p & 0x0000ff);
	                     double d = filter[j+1][i+1];
	                     
	                     sumr = sumr+d*pr;
	                     sumg = sumg+d*pg;
	                     sumb = sumb+d*pb;
					}
				}
	  
		                 int r = (int) Math.round(sumr);
		                 int g = (int) Math.round(sumg);
		                 int b = (int) Math.round(sumb);
		                   if (r < 0) 
			                   r = 0;
		                   if (r > 255) 
			                   r = 255;
		                   if (g < 0) 
			                   g = 0;
		                   if (g > 255) 
			                   g = 255;
		                   if (b < 0) 
			                   b = 0;
		                   if (b > 255) 
			                   b = 255;
		int pix = ((r & 0xff)<<16) | ((g & 0xff)<<8) | (b & 0xff);
		orig.putPixel(u, v, pix );
			}
		}
	}
}