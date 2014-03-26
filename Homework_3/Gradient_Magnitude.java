import ij.*;
import ij.plugin.filter.Convolver;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;

public class Gradient_Magnitude implements PlugInFilter {
	
	double sigma = (double)2;
	
    public int setup(String arg, ImagePlus imp) {
        return DOES_ALL;
    }

    public void run(ImageProcessor orig) {
        int w = orig.getWidth();
        int h = orig.getHeight();
        //3x3 filter matrix   c=2
        double[][] filterX = {{0,0,0},
                             {1,0,-1},
                             {0,0,0}};
        double[][] filterY ={{0,1,0},
        					{0,0,0},
        					{0,-1,0}};
        float kernel[][] = GaussianKernel2d();
        //ImageProcessor copy = orig.duplicate();
        
        ImageProcessor copy = orig.convertToFloat();
        ImageProcessor J = copy.duplicate();
        float[] H = this.makeGaussKernel1d(2);
        Convolver cv = new Convolver();
        cv.setNormalize(true);
        cv.convolve(J, H, 1, H.length);
        cv.convolve(J, H, H.length, 1);
        
        copy.multiply(0);
        J.multiply(-1);
        copy.copyBits(J,0,0,Blitter.SUBTRACT);
        orig.insert(copy.convertToByte(false), 0, 0);
        
       ImageProcessor copy2 = orig.duplicate();
       
		for (int v = 1; v <= h - 2; v++) {
			for (int u = 1; u <= w - 2; u++) {
				// compute filter result for position (u,v)
				double sum1 = 0, sum2 = 0;
				for (int j = -1; j <= 1; j++) {
					for (int i = -1; i <= 1; i++) {
						float p = copy2.getPixel(u + i, v + j);
						// get the corresponding filter coefficient
						double c = 0.5* filterX[j + 1][i + 1];
						sum1 = sum1 + c * p;
						double d = 0.5* filterY[j + 1][i + 1];
						sum2 = sum2 + d * p;
						
					}
				}
				

				double q = Math.sqrt(sum1*sum1+sum2*sum2);
				
				//System.out.println(q);
				orig.putPixelValue(u, v, q);
			}
		}
		
		
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
    
    
    public float[][] GaussianKernel2d()
    {
        int center = (int) (3.0*sigma);
        int size = center * 2 + 1;
        float[][] H = new float[size][size];
        double sigma2 = sigma * sigma;
        for(int i=-center; i<=center; i++)
        {
            for(int j=-center; j<=center; j++)
            {
                double r2 = i*i + j*j;
                double sum = -(r2/(2*sigma2));
                H[i + center][j + center] = (float)Math.exp(sum);
            }
        }
        return H;
    }

}
