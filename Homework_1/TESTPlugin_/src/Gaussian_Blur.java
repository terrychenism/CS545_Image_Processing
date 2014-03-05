import ij.*;
import ij.plugin.filter.Convolver;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;



public class Gaussian_Blur implements PlugInFilter {


    public int setup(String arg, ImagePlus imp) {
        return DOES_32;
    }

    
    
    public void run(ImageProcessor orig) {
        
        ImageProcessor I = orig.convertToFloat();
        ImageProcessor J = I.duplicate();
        float[] H = this.makeGaussKernel1d(5);
        Convolver cv = new Convolver();
        cv.setNormalize(true);
        cv.convolve(J, H, 1, H.length);
        cv.convolve(J, H, H.length, 1);
        
        I.multiply(0);
        J.multiply(-1);
        I.copyBits(J,0,0,Blitter.SUBTRACT);
        orig.insert(I.convertToByte(false), 0, 0);
   
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

     public void extend(ImageProcessor orig,float[] filter)//hanlde boundary
    	{     
          int p; 
          int w = orig.getWidth();
          int len = filter.length;
        int k = len/2;
        int h = orig.getHeight();
          for (int v = 0; v < h  ; v++) {
              for (int u = 0; u < w ; u++) {
                  // four corner
                  if(u<=k && v<=k){
                       p = orig.get(k, k);
                      
                  }
                  else if (u>=w-k-1 && v >= h- k-1){
                      p = orig.get(w-k-1,h - k-1);
                      
                  }
                  else if (u<=k && v >= h - k-1){
                      p = orig.get(k,h - k-1);
                      
                  }
                  else if (u>=w-k-1 && v<=k){
                      p = orig.get(w-k-1,k);
                      
                  }
                  
                  //four rectangle
                  else if(u>k && u<w-k-1 && v<k){
                      p = orig.get(u,k);                  
                  } 
                  else if(u>k && u<w-k-1 && v>h - k-1){
                      p = orig.get(u,h - k-1);                    
                  }
                  else if(u< k && v< h - k-1 && v>k){
                      p = orig.get(k,v);                  
                  }
                  else if(u> w-k-1 && v< h - k-1 && v>k){
                      p = orig.get(w-k-1,v);                  
                  }
                  //center
                  else {
                      p = orig.get(u,v);
                  }
                  
                  orig.putPixel(u, v, p);
                      
              }
          }
              
    	}  
}
          


