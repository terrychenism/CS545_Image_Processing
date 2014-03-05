
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

public class Power_Transform implements PlugInFilter {

	public int setup(String arg, ImagePlus img) {
		return DOES_8G; 
	}
    
	public void run(ImageProcessor ip) {
		
		int w = ip.getWidth();
		int h = ip.getHeight();

		for (int v = 0; v < h; v++) {
			for (int u = 0; u < w; u++) {
				double i = ip.getPixel(u, v);
				i=i/255;
				ip.putPixelValue(u, v, 255*Math.pow(i,0.3));// 3 is best for runway.jpg and 0.3 is best for spine.jpg
			}
		}

		
	}
}