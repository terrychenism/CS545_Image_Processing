import java.util.Vector;

import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;

public class Chain_codes implements PlugInFilter {

	public int setup(String arg, ImagePlus img) {
		return DOES_ALL; 
	}
	int head_w, head_h;
	static Vector<Integer> checkx = new Vector<Integer>();
    static Vector<Integer> checky = new Vector<Integer>();
    static Vector<Integer> direct = new Vector<Integer>();
    static Vector<Integer> checkx1 = new Vector<Integer>();
    static Vector<Integer> checky1 = new Vector<Integer>();
    static Vector<Integer> direct1 = new Vector<Integer>();
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
	    int fixed_x = x;
		int fixed_y = y;
	    
	    int[] lx = new int[8];
	    int[] ly = new int[8];
//	    lx[0] = 0;
//	    lx[1] = 1;
//	    lx[2] = 1;
//	    lx[3] = 1;
//	    lx[4] = 0;
//	    lx[5] = -1;
//	    lx[6] = -1;
//	    lx[7] = -1;
//	    
//	    ly[0] = -1;
//	    ly[1] = -1;
//	    ly[2] = 0;
//	    ly[3] = 1;
//	    ly[4] = 1;
//	    ly[5] = 1;
//	    ly[6] = 0;
//	    ly[7] = -1;

	   // int aa = 4, bb=5,cc=6,dd=7,ee=0,ff=1,gg=2,hh=3;
	    //int aa = 5, bb=6,cc=7,dd=0,ee=1,ff=2,gg=3,hh=4;
	    //int aa = 6, bb=7,cc=0,dd=1,ee=2,ff=3,gg=4,hh=5;
	    
	    int aa = 0, bb=1,cc=2,dd=3,ee=4,ff=5,gg=6,hh=7;
	    //int aa = 7, bb=0,cc=1,dd=2,ee=3,ff=4,gg=5,hh=6;
	    lx[aa] = 1;
	    lx[bb] = 1;
	    lx[cc] = 0;
	    lx[dd] = -1;
	    lx[ee] = -1;
	    lx[ff] = -1;
	    lx[gg] = 0;
	    lx[hh] = 1;
	    
	    ly[aa] = 0;
	    ly[bb] = 1;
	    ly[cc] = 1;
	    ly[dd] = 1;
	    ly[ee] = 0;
	    ly[ff] = -1;
	    ly[gg] = -1;
	    ly[hh] = 0;
	    
	    
	    int l = 0;
	    int a = 0;
	    int b = 0;
	    int beforex = 0;
	    int beforey = 0;
	 
	 
	    boolean flagz = false;
	    for(int j = 0; j < 50000; j++)
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
		
		
		
		ImageProcessor recovered_img = new ByteProcessor(w, h);
		
		int m=0;
//		for (int v = y; v < h; v++) {
//			for (int u = x; u < w; u++) {
		
		for (int v = 0; v < h; v++) {
			for (int u = 0; u < w; u++) {
				recovered_img.putPixel(u,v,255);
			}
		}
		
		int v = fixed_y;
		int u = fixed_x;
		//recovered_img.putPixel(u,v,0);
//		while(m < chain.length){
//				int p = chain[m];
//				
//				if(p == 0){
//					v--;					
//				}
//					
//				
//				else if(p == 1){
//					u++;
//					v--;
//					
//					
//				}
//					
//				
//				else if(p == 2){
//					u++;
//					
//					
//				}
//					
//				else if(p == 3){
//					
//					u++;
//					v++;
//				}
//					
//				else if(p == 4){
//					
//					v++;
//				}
//					
//				else if(p == 5){
//					
//					u--;
//					v++;
//				}
//					
//				else if(p == 6){
//					
//					v--;
//				}
//					
//				else if(p == 7){
//					
//					u--;
//					v--;
//				}
//					
//				recovered_img.putPixelValue(u, v, 0);
//				m++;
//				//if(m >= count) break;
//					
//			}
		while(m < chain.length){
			recovered_img.putPixelValue(u, v, 0);
			int p = chain[m];
			if(p == aa){
				//recovered_img.putPixelValue(u, v, 0);
				u = u+1;
				
			}
				
			
			else if(p == bb){
				//recovered_img.putPixelValue(u+1, v+1, 0);
				u++;
				v++;
			}
				
			
			else if(p == cc){
				//recovered_img.putPixelValue(u, v+1, 0);
				v++;
			}
				
			else if(p == dd){
				//recovered_img.putPixelValue(u-1, v+1, 0);
				u--;
				v++;
			}
				
			else if(p == ee){
				//recovered_img.putPixelValue(u-1, v, 0);
				u--;
			}
				
			else if(p == ff){
				//recovered_img.putPixelValue(u-1, v-1, 0);
				u--;
				v--;
			}
				
			else if(p == gg){
				//recovered_img.putPixelValue(u, v-1, 0);
				v--;
			}
				
			else if(p == hh){
				//recovered_img.putPixelValue(u+1, v-1, 0);
				u++;
				
			}
				
			
			m++;
			//if(m >= count) break;
				
		}
		
		
		
		//-------------------other circle------------- 
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
	    int fixed_x1 = x;
		int fixed_y1 = y;
		
		int aa1 = 7, bb1=0,cc1=1,dd1=2,ee1=3,ff1=4,gg1=5,hh1=6;
		//int aa1 = 0, bb1=1,cc1=2,dd1=3,ee1=4,ff1=5,gg1=6,hh1=7;
	    lx[aa1] = 1;
	    lx[bb1] = 1;
	    lx[cc1] = 0;
	    lx[dd1] = -1;
	    lx[ee1] = -1;
	    lx[ff1] = -1;
	    lx[gg1] = 0;
	    lx[hh1] = 1;
	    
	    ly[aa1] = 0;
	    ly[bb1] = 1;
	    ly[cc1] = 1;
	    ly[dd1] = 1;
	    ly[ee1] = 0;
	    ly[ff1] = -1;
	    ly[gg1] = -1;
	    ly[hh1] = 0;
	    
	    
	    l = 0;
	    a = 0;
	    b = 0;
	    beforex = 0;
	   beforey = 0;
	 
	 
	    flagz = false;
	    for(int j = 0; j < 50000; j++)
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
	 
	            checkx1.add(x);
	            checky1.add(y);
	            beforex = x;
	            beforey = y;
	 
	            x = a;
	            y = b;
	 
	            break;
	          }
	          checkx1.add(x);
	          checky1.add(y);
	        }
	 
	        if(i == 7)
	          flagz = true;
	      }
	 
	      System.out.println(x + " " + y + " = " + l);
	      direct1.add(l);
	 
	    }
	 

	    Integer[] chain1= new Integer[direct1.size()];
		direct1.copyInto(chain1);
		for(int i = 0; i < chain1.length; i++)
		{
			System.out.println( "chaincode = " + chain1[i]);
		}
	 
		int count1 = 0;
	 
		for(int i = 0; (i + 2) < chain1.length; i= i+2)  
		{
			for(int j = 0; j < 8; j++)  
			{
				if(chain1[i] == j)  
				{
					//if(chain[i+1] == j+1 || chain[i+1] == j-1) 					
						count1++;					
				}
				
			}
		}
		
		System.out.println("total input =" + count1);
		
		
		
		//ImageProcessor recovered_img = new ByteProcessor(w, h);
		
		int m1=0;

		

		
		int v1 = fixed_y1;
		int u1 = fixed_x1;
		
		while(m1 < chain1.length){
			recovered_img.putPixelValue(u1, v1, 0);
			int p = chain1[m1];
			if(p == aa1){
				//recovered_img.putPixelValue(u, v, 0);
				u1 = u1+1;
				
			}
				
			
			else if(p == bb1){
				//recovered_img.putPixelValue(u+1, v+1, 0);
				u1++;
				v1++;
			}
				
			
			else if(p == cc1){
				//recovered_img.putPixelValue(u, v+1, 0);
				v1++;
			}
				
			else if(p == dd1){
				//recovered_img.putPixelValue(u-1, v+1, 0);
				u1--;
				v1++;
			}
				
			else if(p == ee1){
				//recovered_img.putPixelValue(u-1, v, 0);
				u1--;
			}
				
			else if(p == ff1){
				//recovered_img.putPixelValue(u-1, v-1, 0);
				u1--;
				v1--;
			}
				
			else if(p == gg1){
				//recovered_img.putPixelValue(u, v-1, 0);
				v1--;
			}
				
			else if(p == hh1){
				//recovered_img.putPixelValue(u+1, v-1, 0);
				u1++;
				
			}
				
			
			m1++;
			//if(m >= count) break;
				
		}	
		
		//------------------------------
		ImagePlus picm = new ImagePlus("result",recovered_img);
		picm.show();
		picm.updateAndDraw();
		
	 
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
