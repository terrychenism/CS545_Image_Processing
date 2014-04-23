import java.util.Vector;

import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

public class Chain_codes implements PlugInFilter {

	public int setup(String arg, ImagePlus img) {
		return DOES_ALL; 
	}
	int head_w, head_h;
	static Vector<Integer> checkx = new Vector<Integer>();
    static Vector<Integer> checky = new Vector<Integer>();
    static Vector<Integer> direct = new Vector<Integer>();
	public void run(ImageProcessor ip) {
		
		int w = ip.getWidth();
		int h = ip.getHeight();
		boolean flag = false;
		int x = 0, y = 0;
		for (int v = 0; v < h; v++) {
			for (int u = 0; u < w; u++) {
				int i = ip.getPixel(u, v);
				if(i > 150) 
					ip.putPixelValue(u, v, 255);
				else 
					ip.putPixelValue(u, v, 0);
								
			}
		}
		
		for(int k=0;k<h;k++)
	    {
	      for(int j=0;j<w;j++)
	      {
	        int Value= ip.getPixel(j,k);
	 
	        if(Value == 0)
	        {
	          x = j;
	          y = k;
	 
	          flag = true;
	 
	          break;
	        }
	      }
	 
	      if(flag)
	        break;
	    }
		
		
		System.out.println("Success");
	    System.out.println( x + " " + y);
	    
	    int[] lx = new int[8];
	    int[] ly = new int[8];
	    lx[0] = 0;
	    lx[1] = 1;
	    lx[2] = 1;
	    lx[3] = 1;
	    lx[4] = 0;
	    lx[5] = -1;
	    lx[6] = -1;
	    lx[7] = -1;
	    ly[0] = -1;
	    ly[1] = -1;
	    ly[2] = 0;
	    ly[3] = 1;
	    ly[4] = 1;
	    ly[5] = 1;
	    ly[6] = 0;
	    ly[7] = -1;
	    int l = 0;
	    int a = 0;
	    int b = 0;
	    int beforex = 0;
	    int beforey = 0;
	 
	 
	    boolean flagz = false;
	    for(int j = 0; j < 500; j++)
	    {
	      if(flagz)
	        break;
	      for (int i = 0; i < 8; i++)
	      {
	    	if(check(x + lx[i],y + ly[i]) && !((x + lx[i] == beforex) && (y + ly[i] == beforey)))
	        {	
	          if((ip.getPixel(x + lx[i], y + ly[i])  == 0))
	          {
	            l = i;
	 
	            a = x + lx[i];
	            b = y + ly[i];
	 
	            checkx.add(x);
	            checky.add(y);
	            beforex = x;
	            beforey = y;
	 
	            x = a;
	            y = b;
	 
	            break;
	          }
	          checkx.add(x);
	          checky.add(y);
	        }
	 
	        if(i == 7)
	          flagz = true;
	      }
	 
	      System.out.println(x + " " + y + " = " + l);
	      direct.add(l);
	 
	    }
	 
	 
	    Integer[] chain= new Integer[direct.size()];
		direct.copyInto(chain);
		for(int i = 0; i < chain.length; i++)
		{
			System.out.println( "chaincode = " + chain[i]);
		}
	 
		int count = 0;
	 
		for(int i = 0; (i + 2) < chain.length; i= i+2)  
		{
			for(int j = 0; j < 8; j++)  
			{
				if(chain[i] == j)  
				{
					//if(chain[i+1] == j+1 || chain[i+1] == j-1) 					
						count++;					
				}
				
			}
		}
		
		System.out.println("total input =" + count);

		 

	 
	  }
	 
	public static boolean check(int x, int y){
		Integer[] xed= new Integer[checkx.size()];
		Integer[] yed= new Integer[checky.size()];
		checkx.copyInto(xed);
		checky.copyInto(yed);
		for( int i = 0; i < xed.length; i++)
		{
			if((x == xed[i]) && (y == yed[i])){
				return false;
			}
		}
		return true;	 
	}
	
	
}
