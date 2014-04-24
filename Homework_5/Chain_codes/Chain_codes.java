import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.gui.Overlay;
import ij.plugin.filter.PlugInFilter;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;

import java.awt.Point;
import java.util.List;

import contours.BinaryRegion;
import contours.RegionContourLabeling;
import contours.Contour;


public class Chain_codes implements PlugInFilter {
	
	static boolean listRegions = true;
	static boolean listContourPoints = false;
	static boolean showContours = true;
	
	public int setup(String arg, ImagePlus im) { 
		return DOES_ALL + NO_CHANGES; 
	}
	
	public void run(ImageProcessor ip) {
	   	
	   	
	   	// Make sure we have a proper byte image:
	   	ByteProcessor bp = (ByteProcessor) ip.convertToByte(false);
	   	
	   	bp.autoThreshold();
	 //  	bp.invert();
	   	
	   	// Create the region labeler / contour tracer:
		RegionContourLabeling segmenter = new RegionContourLabeling(bp);
		
		// Retrieve the list of detected regions:
		List<BinaryRegion> regions = segmenter.getRegions(true);	// regions are sorted by size
		if (listRegions) {
			IJ.log("Detected regions (sorted by size): " + regions.size());
			for (BinaryRegion r: regions) {
				IJ.log(r.toString());
			}
		}
		
		// See how the list of regions is put into an array: 
		BinaryRegion[] regionArray = regions.toArray(new BinaryRegion[0]);
		
		if (regionArray.length > 0) {
			ImagePlus im = IJ.createImage("chain_codes to contours pix"," ",ip.getWidth(),ip.getHeight(),1);
	        ImageProcessor np = im.getProcessor();
	        //np.insert(ip.convertToByte(false),0,0);
	      
	        
			for(int m=0;m<regionArray.length;m++){
			BinaryRegion presentRegion = regionArray[m];
			IJ.log("The present region is of size " + presentRegion.getSize());
		
			// Obtain the outer contour of the largest region:
			//Contour oc = largestRegion.getOuterContour();
			
			// Display the contours if desired:
			
		
			Contour oc = presentRegion.getOuterContour();
			Point[] points = oc.getPointArray();
                Chain_code chaincode = new Chain_code(points);
                for(int i = 0; i< points.length;i++){
                IJ.log(" "+chaincode.code[i]);
                }
                
                
			
			
		        np.putPixel(points[0].x,points[0].y,0);
		        
		        for (int i=0; i<points.length-1; i++) {
		        	if(chaincode.code[i]==0) np.putPixel(points[i].x+1,points[i].y,0);
		        	if(chaincode.code[i]==1) np.putPixel(points[i].x+1,points[i].y+1,0);
		        	if(chaincode.code[i]==2) np.putPixel(points[i].x,points[i].y+1,0);
		        	if(chaincode.code[i]==3) np.putPixel(points[i].x-1,points[i].y+1,0);
		        	if(chaincode.code[i]==4) np.putPixel(points[i].x-1,points[i].y,0);
		        	if(chaincode.code[i]==5) np.putPixel(points[i].x-1,points[i].y-1,0);
		        	if(chaincode.code[i]==6) np.putPixel(points[i].x,points[i].y-1,0);
		        	if(chaincode.code[i]==7) np.putPixel(points[i].x+1,points[i].y-1,0);
		        				
		        }
		}
		
		im.show();
		im.updateAndDraw();
		
		
	}
	}
	
}


class Chain_code{
    
    int length;
    // int[][] H;
    int[] code;
    Point[] point;
    
    Chain_code(Point[] point){
        this.point = point;
        this.length = point.length;
        code = new int[length];
        absolute_chain_code();
        
    }
    
    int[] getChaincode(){
        return code;
    }
    
    
    
    public void absolute_chain_code(){
        int x;
        int y;
        
        for(int i = 0; i <length; i++){
            if(i!=length-1){
                x = point[i+1].x - point[i].x;
                y = point[i+1].y - point[i].y;
                code[i] = generatecode(x,y);
            }
            else{
                x = point[0].x - point[i].x;
                y = point[0].y - point[i].y;
                code[i] = generatecode(x,y);
            }
        }
    }
    
    
    public void draw(){
        
    }
    
    int generatecode(int x,int y){
        int H;
        if(x==1&&y==0)
            return 0;
        else if(x==1&&y==1)
            return 1;
        else if(x==0&&y==1)
            return 2;
        else if(x==-1&&y==1)
            return 3;
        else if(x==-1&&y==0)
            return 4;
        else if(x==-1&&y==-1)
            return 5;
        else if(x==0&&y==-1)
            return 6;
        else if(x==1&&y==-1)
            return 7;
        return -1;
    }
    
    
}