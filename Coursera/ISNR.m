function value=ISNR(Ideal,Observed,Restored);
if size(Ideal)==size(Observed)&size(Restored)==size(Observed)
    value=10*log10(sum(sum(abs(Ideal-Observed).^2))/sum(sum(abs(Ideal-Restored).^2)));
end
