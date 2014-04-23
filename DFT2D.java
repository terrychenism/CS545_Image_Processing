import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

public class DFT2D implements PlugInFilter {

	public int setup(String arg, ImagePlus img) {
		return DOES_ALL; 
	}
	int width;
	int height;
	float[] Real;	//original image data
	float[] Imag;
	float[] Power;
	float PowerMax;
	boolean swapQu = true;
	int scaleValue = 255;
	boolean forward = true;
	
	public void run(ImageProcessor ip){
		width = ip.getWidth();
		height = ip.getHeight();
		Real = (float[]) ip.getPixels();
		Imag = new float[width*height];  // values are zero
		doDft2d();
		//makePowerSpectrum();
	}
//	public DFT2D(ImageProcessor ip, boolean center){
//		//this(ip);
//		swapQu = center;
//	}
	//------------------------------------------------
	public void setForward(){
	forward = true;
	}
	public void setInverse(){
	forward = false;
	}
	public float[] getReal(){
	return Real;
	}
	public float[] getImag(){
	return Imag;
	}
	public float[] getPower(){
	return Power;
	}
	//------------------------------------------------
	public void doDft2d () { // in-place 2D Dft
	// do the rows:
		Complex[] row = Complex.makeComplexVector(width);
		Dft1d dftR = new Dft1d(width);
		for (int v=0; v<height; v++){
			getRow(v,row);
			Complex[] rowDft = dftR.DFT(row,forward);
			putRow(v,rowDft);
	}
	// do the columns:
	Complex[] col = Complex.makeComplexVector(height);
		Dft1d dftC = new Dft1d(height);
		for (int u=0; u<width; u++){
			getCol(u,col);
			Complex[] colDft = dftC.DFT(col,forward);
			putCol(u,colDft);
		}
	}
	void getRow(int v, Complex[] rowC){
		int i = v*width; //start index of row v
		for (int u=0; u<width; u++){
			rowC[u].real = Real[i+u];
			rowC[u].imag = Imag[i+u];
		}
	}
	void putRow(int v, Complex[] rowC){
		int i = v*width; //start index of row v
		for (int u=0; u<width; u++){
			Real[i+u] = (float) rowC[u].real;
			Imag[i+u] = (float) rowC[u].imag;
		}
	}
	void getCol(int u, Complex[] rowC){
		for (int v=0; v<height; v++){
			rowC[v].real = Real[v*width+u];
			rowC[v].imag = Imag[v*width+u];
		}
	}
	void putCol(int u, Complex[] rowC){
	for (int v=0; v<height; v++){
		Real[v*width+u] = (float) rowC[v].real;
		Imag[v*width+u] = (float) rowC[v].imag;
	}
}

	
}
