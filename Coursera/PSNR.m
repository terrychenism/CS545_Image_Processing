image1= imread('lena.gif');
image2= double(image1);
LP = 1/9*ones(3,3);
imf= imfilter(image2,LP,'replicate');
MSE = sum( sum((image2 - imf).^2)) /(256*256);
PSNR= 10*log10(255^2/MSE);

LP = 1/25*ones(5,5);
imf2= imfilter(image2,LP,'replicate');
MSE = sum( sum((image2 - imf2).^2)) /(256*256);
PSNR= 10*log10(255^2/MSE);


