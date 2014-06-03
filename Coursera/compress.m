%% ===== this is for dsp week 9 =========

A = imread('Cameraman256.bmp');
imf = double(A);
% imwrite(A,'output.jpg','quality',10);


image1= imread('output.jpg');
image2= double(image1);

MSE = sum( sum((image2 - imf).^2)) /(256*256);
PSNR= 10*log10(255^2/MSE);


