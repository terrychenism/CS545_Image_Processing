import ij.*;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;



public class Gaussian_Blur implements PlugInFilter {


    public int setup(String arg, ImagePlus imp) {
        return DOES_32;
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
    
                
    public void run(ImageProcessor orig) {
        int w = orig.getWidth();
        int h = orig.getHeight();
        //3x3 filter matrix
        

       float[] filter = makeGaussKernel1d(5);

       int len = filter.length;
       int k = len/2;
      // System.out.println(len);
        ImageProcessor copy = orig.duplicate();
      
        for (int v = k; v <= h -k- 1; v++) {
            for (int u = k; u <= w - k-1; u++) {
                // compute filter result for position (u,v)
                
                  double sum = 0;
                  double coeff = 0; 
                  for (int j = -k; j <= k; j++) {
                        for (int i = -k; i <= k; i++) {
	                        int p = copy.getPixel(u + i, v+ j );
	                        // get the corresponding filter coefficient
	                        double c = filter[j+k]*filter[i+k];
	                        coeff += c;
	                        sum = sum +  c * p ;
	                        //System.out.println(sum);
                        }
                   
                  }
                  //System.out.println(coeff);
                  double s = 1.0/coeff;

                  int q = (int) Math.round(s*sum);
                  //if (q < 0) q = 0;
                  //if (q > 255) q = 255;
                  orig.putPixel(u, v, q);
                  
            }
            
        }
               

    }

}
