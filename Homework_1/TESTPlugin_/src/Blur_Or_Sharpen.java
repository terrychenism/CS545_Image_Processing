

import ij.ImagePlus;
import ij.plugin.filter.Convolver;
import ij.plugin.filter.PlugInFilter;
import ij.process.Blitter;
import ij.process.ImageProcessor;


public class Blur_Or_Sharpen implements PlugInFilter {



	public int setup(String arg, ImagePlus imp) {
		return DOES_ALL;
	}
	float[] makeGaussKernel1d(double sigma) {
        
        // create the kernel
        int center = (int) (3.0*sigma);
        float[] kernel = new float[2*center+1]; // odd size
       
        // fill the kernel
        double sigma2 = sigma * sigma; // σ2
        for (int i=0; i<kernel.length; i++) {
            double r = center - i;
            kernel[i] = (float) Math.exp(-0.5 * (r*r) / sigma2);
        }
       
        return kernel;
   }
	public void run(ImageProcessor ip) {

			unsharpMask(ip, 10,1);
		
	}

	public void unsharpMask(ImageProcessor ip, double sigma, double w) {
		ImageProcessor I = ip.convertToFloat();
		//int w = ip.getWidth();
        //int h = ip.getHeight();
		//create a blurred version of the original
		ImageProcessor J = I.duplicate();
		float[] H = makeGaussKernel1d(sigma);
		//float[] H ={-1, -1, -1,-1, 12, -1,-1, -1, -1};
		Convolver cv = new Convolver();
		cv.setNormalize(true);
		cv.convolve(J, H, 1, H.length);
		cv.convolve(J, H, H.length, 1);

		// multiply the original image by (1+a)
		I.multiply(1 + w);
		// multiply the mask image by a
		J.multiply(w);
		// subtract the weighted mask from the original
		I.copyBits(J, 0, 0, Blitter.SUBTRACT);
//		 for (int v = 0; v < h; v++) {
//             for (int u = 0; u < w; u++) {  
//            	 int p = I.getPixel(u, v);
//            	 //System.out.println(p);
//            	 int q = J.getPixel(u, v);
//            	 ip.putPixelValue(u,v, p-q); 
//             }
//		 }
		
		//copy result back into original byte image
		ip.insert(I.convertToRGB(), 0, 0);
	}
	



}