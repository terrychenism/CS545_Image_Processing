


import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;




public class DFT_2D implements PlugInFilter{
    
	static boolean center = true;
    
	public int setup(String arg, ImagePlus imp) {
		return DOES_8G+NO_CHANGES;
	}
    
	public void run(ImageProcessor ip) {
		FloatProcessor ip2 = (FloatProcessor) ip.convertToFloat();
		Dft2d dft = new Dft2d(ip2,center);
        
        
        
        Dft2d inversedft = (Dft2d)dft.clone();
        dft.doDft2d();
        dft.makePowerSpectrum();
		ImageProcessor ipP = dft.makePowerImage();
        dft.swapQuadrants(ipP);
		ImagePlus win = new ImagePlus("DFT_2d ",ipP);
		win.show();
        
        
        
       
        inversedft.inverse();
        inversedft.makePowerSpectrum();
        ImageProcessor ipP3 = inversedft.makePowerImage2();
		ImagePlus win2 = new ImagePlus("inverse DFT ",ipP3);
		win2.show();

	}
    
}