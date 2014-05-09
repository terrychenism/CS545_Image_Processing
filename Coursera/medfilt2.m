J= double(imread('digital.jpg'));
raw = double(imread('raw.jpg'));
K = medfilt2(J);
H = medfilt2(K);
imshow(J,[]), figure, imshow(K,[]),figure, imshow(H,[])
[h, w]=size(J);

MSE = sum( sum((raw-J).^2)) /(h*w);
PSNR= 10*log10(255^2/MSE);
disp(PSNR)

MSE2 = sum( sum((raw-K).^2)) /(h*w);
PSNR2= 10*log10(255^2/MSE2);
disp(PSNR2)

MSE3 = sum( sum((raw-H).^2)) /(h*w);
PSNR3= 10*log10(255^2/MSE3);
disp(PSNR3)
