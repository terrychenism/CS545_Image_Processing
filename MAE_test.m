A1=imread('frame_1.jpg');
A=double(A1);
B1=imread('frame_2.jpg');
B=double(B1);
topLine = 65;
bottomLine = 96;
leftColumn = 81;
rightColumn = 112;
width = bottomLine - topLine;
height = rightColumn - leftColumn;
I2 = imcrop(B, [leftColumn topLine width height]); 
[M N]=size(I2);
for i=1:size(A,1)-31
for j=1:size(A,2)-31
I1=A(i:i+31,j:j+31);
% T=abs(I2-I1);
% MAE(i,j)=sum(T(:))/(M*N);
MAE(i,j)= mean2(abs(I1-I2));
end
end
low=MAE(1,1);
for i=1:size(A,1)-31
for j=1:size(A,2)-31
    if (MAE(i,j)<low)
        low=MAE(i,j);
            m=i;
            n=j;
        end
    end
end
disp(low);
 disp(m);
 disp(n);
