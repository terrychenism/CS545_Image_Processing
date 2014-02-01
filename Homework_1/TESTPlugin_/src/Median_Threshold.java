

import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

public class Median_Threshold implements PlugInFilter {

	public int setup(String arg, ImagePlus img) {
		return DOES_8G; 
	}
	int median;
	int length;
	public void run(ImageProcessor ip) {
		
		int w = ip.getWidth();
		int h = ip.getHeight();
		int pixel[] = new int [w*h];
		int i=0;
		
		for (int v = 0; v < h; v++) {
			for (int u = 0; u < w; u++) {
				pixel[i] = ip.getPixel(u, v);
				if(pixel[i] == 0) {
					System.out.println(pixel[i]);
					continue;
				}
				else i++;
				//ip.putPixelValue(u, v, Math.pow(i,0.9));
			}
			
		}
		length = i;
		System.out.println(length);

			quickSort(pixel, 0, length-1);	
		
		 

		median = pixel[length/2];
		System.out.println(median);
     
		for (int v = 0; v < h; v++) {
			for (int u = 0; u < w; u++) {
				int p = ip.getPixel(u, v);
				if(p<median) ip.putPixelValue(u, v, 0);
				else ip.putPixelValue(u, v, 255);
					
			}
		}
	}

	public static void quickSort(int[] a, int p, int r)
    {
        if(p<r)
        {
            int q=partition(a,p,r);
            quickSort(a,p,q);
            quickSort(a,q+1,r);
        }
    }

    private static int partition(int[] a, int p, int r) {

        int x = a[p];
        int i = p-1 ;
        int j = r+1 ;

        while (true) {
            i++;
            while ( i< r && a[i] < x)
                i++;
            j--;
            while (j>p && a[j] > x)
                j--;

            if (i < j)
                swap(a, i, j);
            else
                return j;
        }
    }

    private static void swap(int[] a, int i, int j) {
        // TODO Auto-generated method stub
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
}
