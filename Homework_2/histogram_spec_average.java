import ij.IJ;
import ij.ImagePlus; 
import ij.WindowManager;

import ij.gui.GenericDialog;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;


public class istogram_spec_average implements PlugInFilter {

	public int setup(String arg, ImagePlus img) {
		return DOES_8G; 
	}

			
	 	//ImageProcessor ipA; // target image IA (to be modified)
		ImagePlus fgIm = null; // reference image IR		
			
		
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
		


		public void run(ImageProcessor ipA) {
			
			if(runDialog()) {
			// TODO Auto-generated method stub
				int[] hA = ipA.getHistogram(); // get the histogram for IA
				ImageProcessor ipR = fgIm.getProcessor().convertToByte(false);
				int[] hR = ipR.getHistogram(); // get the histogram for IR
				int[] F = matchHistograms(hA, hR); // mapping function fhs(a)
				ipA.applyTable(F);
			}
		}
		
		
		boolean runDialog() {
			// get list of open images
			int[] windowList = WindowManager.getIDList();
			if(windowList==null){
				IJ.noImage();
				return false;
			}
			// get image titles
			String[] windowTitles = new String[windowList.length];
			for (int i = 0; i < windowList.length; i++) {
				ImagePlus imp = WindowManager.getImage(windowList[i]);
				if (imp != null)
					windowTitles[i] = imp.getShortTitle();
				else
					windowTitles[i] = "untitled";
			}
			// create dialog and show
			GenericDialog gd = new GenericDialog("Select Reference Image");
			gd.addChoice("Reference Image:", windowTitles, windowTitles[0]);
			gd.showDialog(); 
			if (gd.wasCanceled()) 
				return false;
			else {
				int img2Index = gd.getNextChoiceIndex();
				fgIm = WindowManager.getImage(windowList[img2Index]);
				return true;
			}
		}

}
		


