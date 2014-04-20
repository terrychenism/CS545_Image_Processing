clc;close all;clear;
myimage = imread('img.jpg'); 
myimagedouble = im2double(myimage);
filter = fspecial('average', [3,3]);
imagelp = imfilter(myimagedouble, filter, 'replicate');
imagelp(2:2:end,:)=[];
imagelp(:,2:2:end)=[];
myimagedoubleeven = imagelp;

upimage = zeros(359,479);
for i=1:180;
  for j=1:240;
    upimage(2*i-1,2*j-1) = myimagedoubleeven(i,j);
  end
end

att = [0.25,0.5,0.25;0.5,1,0.5;0.25,0.5,0.25];
final = imfilter(upimage,att);
figure;
imshow(final);
n=size(myimagedouble);
M=n(1);
N=n(2);
MSE = sum(sum((myimagedouble-final).^2))/(M*N);
PSNR = 10*log10(1*1/MSE)
