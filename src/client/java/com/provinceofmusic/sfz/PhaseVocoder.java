package com.provinceofmusic.sfz;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

public class PhaseVocoder {
    private FastFourierTransformer fft;
    private double factor;
    private double shift;

    public PhaseVocoder(double factor, double shift) {
        this.fft = new FastFourierTransformer(DftNormalization.STANDARD);
        this.factor = factor;
        this.shift = shift;
    }

    private double interpolate(double a, double b, double weight) {
        return a * (1 - weight) + b * weight;
    }

    public double[] process(double[] in) {
        Complex[] freq = fft.transform(in, TransformType.FORWARD);
        Complex[] freqn = new Complex[freq.length];
        freqn[0] = Complex.valueOf(freq[0].getReal(), freq[0].getImaginary());

        for (int i = 1; i <= freq.length / 2; i++) {
            double fOrig = i / factor + shift;
            int left = (int) Math.floor(fOrig);
            int right = (int) Math.ceil(fOrig);
            double weighting = fOrig - left;
            double new_Re = 0, new_Im = 0;

            if (left > 0 && left < freq.length / 2 && right > 0 && right < freq.length / 2) {
                new_Re = interpolate(freq[left].getReal(), freq[right].getReal(), weighting);
                new_Im = interpolate(freq[left].getImaginary(), freq[right].getImaginary(), weighting);
            }

            freqn[i] = Complex.valueOf(new_Re, new_Im);
            freqn[freq.length - i] = Complex.valueOf(new_Re, new_Im);
        }

        Complex[] inverse = fft.transform(freqn, TransformType.INVERSE);
        double[] out = new double[inverse.length];
        for (int i = 0; i < inverse.length; i++) {
            out[i] = inverse[i].getReal();
        }

        return out;
    }

    public double[] padToNextPowerOfTwo(double[] original) {
        int originalLength = original.length;
        int nextPowerOfTwo = (int) Math.pow(2, Math.ceil(Math.log(originalLength) / Math.log(2)));
        double[] padded = new double[nextPowerOfTwo];
        System.arraycopy(original, 0, padded, 0, originalLength);
        return padded;
    }
}