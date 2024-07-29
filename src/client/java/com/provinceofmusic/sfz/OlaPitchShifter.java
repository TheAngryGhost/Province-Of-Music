package com.provinceofmusic.sfz;

import javax.sound.sampled.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
public class OlaPitchShifter {
    private double pitchShiftFactor;
    private int windowSize;
    private int hopSize;
    private float[] windowFunction;

    public OlaPitchShifter(double pitchShiftFactor, int windowSize, int hopSize) {
        this.pitchShiftFactor = pitchShiftFactor;
        this.windowSize = windowSize;
        this.hopSize = hopSize;
        this.windowFunction = new float[windowSize];
        for (int i = 0; i < windowSize; i++) {
            windowFunction[i] = (float) (0.5 * (1 - Math.cos(2 * Math.PI * i / windowSize)));
        }
    }

    public short[] process(byte[] audioBytes) {
        int N = audioBytes.length / 2; // 2 bytes per sample for 16 bit audio
        short[] audioShorts = new short[N];
        ByteBuffer.wrap(audioBytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(audioShorts);

        int stepSize = (int) (N / pitchShiftFactor);
        short[] output = new short[N];
        Arrays.fill(output, (short) 0);

        for (int i = 0; i < N - windowSize; i += stepSize) {
            for (int j = 0; j < windowSize; j++) {
                float value = output[i + j] + audioShorts[i + j] * windowFunction[j];
                // Clip the value to the valid range for a 16-bit signed integer
                if (value > Short.MAX_VALUE) {
                    value = Short.MAX_VALUE;
                } else if (value < Short.MIN_VALUE) {
                    value = Short.MIN_VALUE;
                }
                output[i + j] = (short) value;
            }
        }

        return output;
    }

}
