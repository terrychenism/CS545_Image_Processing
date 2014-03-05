import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;
import java.util.Random;
import java.util.Arrays;
 
 
public class NonLinear_Binning implements PlugInFilter {
		 
	    private String title;
	    @Override
	    public int setup(String arg, ImagePlus imp) {
	        // TODO Auto-generated method stub
	        title = imp.getTitle();
	        return DOES_8G + NO_CHANGES;
	    }
	 
	    @Override
	    public void run(ImageProcessor ip) {
	        // TODO Auto-generated method stub
	        int w = 256;
	        int h = 150;
	        Random generator = new Random();

	        int[] hist = ip.getHistogram();
	        int[] H = new int[256]; 
	        int[] interval = new int[11]; 
	        interval[0]=0;
	        interval[10] = 255;
	        for(int a=1;a < 10;a++){
	        	
		        interval[a] = generator.nextInt(25)+25*(a-1);
		        System.out.println(interval[a]);
		        int count = 0;
		        for (int i = interval[a-1]; i <= interval[a]; i++) {
		        	count = count + hist[i];	        	
		        }
		        for (int i = interval[a-1]; i <= interval[a]; i++) {
		        	H[i] = count;	        	
		        }

	        }
	        
	        
	        int[] histCup = Arrays.copyOf(H, 256);
	        Arrays.sort(histCup);
	        int maxNumPixel = histCup[255];

	        double scaling = h / (maxNumPixel * 1.0 );

	        ImageProcessor histIp = new ByteProcessor(w,h);

	        histIp.setValue(255);// white background
	        histIp.fill(); //clear image
	         

	        for( int i = 0; i < 256; i++ ){
	            int pixelNum = (int)(H[i] * scaling);
	            for( int j = 0; j <= H[i]; j++ ){
	                histIp.putPixel(i, h - pixelNum - 1, 0);
	                pixelNum--;
	            }
	        }
	         
	        String hTitle = "Histogram of " + title;
	        ImagePlus histIm = new ImagePlus(hTitle,histIp);
	        histIm.show();
	        histIm.updateAndDraw();
	    }
	 
	}