import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.GenericDialog;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;

public class histogram_spec_average implements PlugInFilter {
    static double alpha = 0.5; 
    ImagePlus[] fgIm = new ImagePlus[100]; // reference image
    int[] windowList = WindowManager.getIDList();
    ;
    
    public int setup(String arg, ImagePlus imp) {
         return DOES_ALL;
        }
    
    int[] matchHistograms (int[] hA, int[] hR) {
		 // hA . . . histogram hA of target image IA
		 // hR . . . reference histogram hR
		 // returns the mapping function fhs() to be applied to image IA
		
			 int K = hA.length; // hA, hR must be of length K
			 double[] PA = Cdf(hA); // get CDF of histogram hA
			 double[] PR = Cdf(hR); // get CDF of histogram hR
			 int[] F = new int[K]; // pixel mapping function fhs()
			
			// compute mapping function fhs()
			 for (int a = 0; a < K; a++) {
				 int j = K-1;
				 do {
					 F[a] = j;
					 j--;
				 } while (j>=0 && PA[a]<=PR[j]);
			 }
			 return F;
		 }
	
	double[] Cdf (int[] h) {
		 // returns the cumulative distribution function for histogram h
			 int K = h.length;
			 int n = 0; // sum all histogram values
			 for (int i=0; i<K; i++) {
				 n += h[i];
			 }
			
			 double[] P = new double[K]; // create cdf table P
			 int c = h[0];  // cumulate histogram values
			 P[0] = (double) c / n;
			 for (int i=1; i<K; i++) {
				 c += h[i];
				 P[i] = (double) c / n;
			 }
			 return P;
		 }
	
     public void run(ImageProcessor bgIp) { //target image 
         if(runDialog()) {           
             ImageProcessor Ip[] = new ImageProcessor[windowList.length];
             int[][] hist = new int[windowList.length][255];
             int[] bg_ave_hist = new int[255];
             int sum = 0;
             
             for(int i =0; i<Ip.length;i++){
             Ip[i] = fgIm[i].getProcessor().convertToByte(false);
             hist[i] = Ip[i].getHistogram();
             }
             
             for(int i= 0; i< 255;i++){
                 for(int j=0; j<Ip.length-1;j++){
                     sum = sum + hist[j][i];
                }
                 
                 bg_ave_hist[i] = sum/(Ip.length-1);

                 sum = 0;
             }
             
             int[] F = matchHistograms(hist[Ip.length-1], bg_ave_hist);
             Ip[Ip.length-1].applyTable(F);
             
             }
         }
    boolean runDialog() {
        
        if (windowList == null){
             IJ.noImage();
             return false;
             }

        String[] windowTitles = new String[windowList.length];
        for (int i = 0; i < windowList.length; i++) {
             ImagePlus im = WindowManager.getImage(windowList[i]);
            if (im == null)
                 windowTitles[i] = "untitled";
             else
                 windowTitles[i] = im.getShortTitle();
             }

        GenericDialog gd = new GenericDialog("Histogram Matching");
         gd.addMessage("target images:");
         gd.addMessage("         "+windowTitles[windowList.length-1]);

        
         gd.showDialog();
         if (gd.wasCanceled())
             return false;
        else {
            
            for(int i=0;i<windowList.length;i++){
             fgIm[i] = WindowManager.getImage(windowList[i]);
            }
             alpha = gd.getNextNumber();
             return true;
             }
         }
    
    
    
     } 
