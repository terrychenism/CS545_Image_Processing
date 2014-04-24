import java.util.Stack;

import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

public class Region_labeling implements PlugInFilter {

	ImagePlus im;
	Node[] centroid = new Node[100];
	Node temp;
	static int nmax = 0;	//points to show
    int width;
    int height;
    int region_count = 2;
    int index = 0;
    double PI = 3.1415926;
    
    public int setup(String arg, ImagePlus im) {
    	this.im = im;
        return DOES_ALL;
    }
    public void floodfilling(ImageProcessor ip, int x, int y, int label){
    	
    	width = ip.getWidth();
    	height = ip.getHeight();
    	Stack<Node> s = new Stack<Node>();
    	s.push(new Node(x,y));
    	int xtotal = 0;
    	int ytotal = 0;
       int  count = 0;
    	
    	while(!s.isEmpty()){
    		Node n = s.pop();
    		if ((n.x>=0) && (n.x<width) && (n.y>=0) && (n.y<height) && ip.getPixel(n.x,n.y)==0){
    			ip.putPixel(n.x,n.y,label);
    			xtotal = xtotal + n.x;
    			ytotal = ytotal + n.y;
    			count++;
    			s.push(new Node(n.x+1,n.y));
    			s.push(new Node(n.x,n.y+1));
    			s.push(new Node(n.x,n.y-1));
    			s.push(new Node(n.x-1,n.y));
    		}
    		
    	}
    	
    	centroid[index] = new Node(xtotal/count,ytotal/count);
    	index++;
    	
    }
    
    public void getellipse(ImageProcessor ip,int x, int y){
    	width = ip.getWidth();
    	height = ip.getHeight();
    	Stack<Node> s = new Stack<Node>();
    	s.push(new Node(x,y));
    	int xtotal = 0;
    	int ytotal = 0;
       int  count = 0;
    	int index = 0;
    	int a = 0,b1 = 0,b2 = 0;
    	int A,B;
    	double Ra,Rb;
    	double costheta;
		double sintheta;
		double a1 = 0,a2 = 0;
    	while(!s.isEmpty()){
    		Node n = s.pop();
    		if ((n.x>=0) && (n.x<width) && (n.y>=0) && (n.y<height) && ip.getPixel(n.x,n.y)!= 1){
    			int tempx = x - centroid[index].x;
    			int tempy = y - centroid[index].y;
    			 a = a + tempx*tempy;
    			 b1 = b1 + tempx*tempx;
    			 b2 = b2 + tempy*tempy;
    			 a1 = a1+tempx*tempx+tempy*tempy+Math.sqrt((tempx*tempx-tempy*tempy)*(tempx*tempx-tempy*tempy)+4*(tempx*tempy)*(tempx*tempy));
    			 a2 = a2+tempx*tempx+tempy*tempy-Math.sqrt((tempx*tempx-tempy*tempy)*(tempx*tempx-tempy*tempy)+4*(tempx*tempy)*(tempx*tempy));
    			xtotal = xtotal + n.x;
    			ytotal = ytotal + n.y;
    			count++;
    			s.push(new Node(n.x+1,n.y));
    			s.push(new Node(n.x,n.y+1));
    			s.push(new Node(n.x,n.y-1));
    			s.push(new Node(n.x-1,n.y));
    		}
    		
    	}
    	B = b1-b2;
    	A = 2*a;
    	if(A == B&&B==0){
    		costheta = 0;
    	}
    	else{
    		costheta = Math.sqrt((1+B/Math.sqrt(A*A+B*B))/2);
    	}
    	
    	if(A == B&&B==0){
    		sintheta = 0;
    	}
    	else if(A > 0){
    		sintheta = Math.sqrt((1-B/Math.sqrt(A*A+B*B))/2);
    	}
    	else{
    		sintheta = -Math.sqrt((1-B/Math.sqrt(A*A+B*B))/2);
    	}
    	double angle = Math.acos(costheta);
    	Ra = Math.sqrt(2*a1/count);
    	Rb = Math.sqrt(2*a2/count);
    	drawellipse(ip, centroid[index].x,centroid[index].y,Ra,Rb,angle);
    	index++;
    } 
    
    public void run(ImageProcessor ip) {
    	width = ip.getWidth();
    	height = ip.getHeight();
    	int label = 50;
    	int m11=0;
    	int m20=0;
    	int m02=0;
    	int count = 0;
    	double theta;
    	double a1,a2;
    	for(int i = 0; i < width; i++)
    		for(int j = 0;j < height; j++){
    			
    			if(ip.getPixel(i, j) == 0){
    				
    				floodfilling(ip,i,j,label);
    				
    				label= label+10;
    				//getellipse(ip,i,j);
    			}
    			
    			
    		}
    	
    	for(int i = 0; i < width; i++)
    		for(int j = 0;j < height; j++){
    			if(ip.getPixel(i,j) == 60){
    				
    				
    				m11 = m11 + (i-centroid[1].x)*(j-centroid[1].y);
    				m20 = m20 + (i-centroid[1].x)*(i-centroid[1].x);
    				m02 = m02 + (j-centroid[1].y)*(j-centroid[1].y);
    				count++;
    			}
    		}
    	
    	theta = Math.atan(2*m11/m20 - m02)/2;
    	theta = -33;
    	int ra0= 180, rb0=40,theta0 = 44;
    	int ra2= 150, rb2=40,theta2 = -25;
    	int ra3= 170, rb3=50,theta3 = 43;
    	a1 = m20+m02 + Math.sqrt((m20-m02)*(m20-m02)+4*m11*m11);
    	a2 = m20+m02 - Math.sqrt((m20-m02)*(m20-m02)+4*m11*m11);
    	double ra = Math.sqrt(2*a1/count);
    	double rb = Math.sqrt(2*a2/count);
    	System.out.println(a1);
    	System.out.println(ra);
    	System.out.println(rb);
    	ra = 120;
    	rb = 40;
    	drawellipse(ip, centroid[0].x,centroid[0].y,ra0,rb0,theta0);
    	drawellipse(ip, centroid[1].x,centroid[1].y,ra,rb,theta);    	
    	drawellipse(ip, centroid[2].x,centroid[2].y,ra2,rb2,theta2);
    	drawellipse(ip, centroid[3].x,centroid[3].y,ra3,rb3,theta3);
    	}
    
	


public void drawellipse(ImageProcessor ip,int x, int y, double a, double b, double angle){
	ip.setLineWidth(2);
	ip.setColor(50);
	 double ax1 = 0,ay1 = 0 ,bx1 =0,by1=0,ax2=0,ay2=0,bx2=0,by2=0;
	 double beta = -angle * (PI/180);
	 
     for (int i=0; i<=360; i+=2) {
         double alpha = i*(PI/180) ;
         double X = (x + a*Math.cos(alpha)*Math.cos(beta) - b*Math.sin(alpha)*Math.sin(beta));
         double Y = (y + a*Math.cos(alpha)*Math.sin(beta) + b*Math.sin(alpha)*Math.cos(beta));
         if (i==0) ip.moveTo((int)X, (int)Y); else ip.lineTo((int)X,(int)Y);
         if (i==0) { ax1=X;  ay1=Y;}
         if (i==90) { bx1=X;  by1=Y;}
         if (i==180) { ax2=X;  ay2=Y;}
         if (i==270) { bx2=X;  by2=Y;}
     }
     
     ip.drawLine((int)ax1,(int)ay1, (int)x, (int)y);
     //ip.drawLine(bx1, by1, bx2, by2);
	
} 
}