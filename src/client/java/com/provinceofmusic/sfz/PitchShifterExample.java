package com.provinceofmusic.sfz;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.data.FloatSample;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.VariableRateStereoReader;
import com.jsyn.util.SampleLoader;

import java.io.File;

public class PitchShifterExample {
    public static void main(String[] args) throws Exception {
        Synthesizer synth = JSyn.createSynthesizer();
        LineOut lineOut = new LineOut();
        VariableRateStereoReader reader = new VariableRateStereoReader();

        // Add the lineOut and reader to the synthesizer
        synth.add(lineOut);
        synth.add(reader);

        // Load the WAV file into a Sample
        FloatSample sample = SampleLoader.loadFloatSample(new File("music.wav"));

        // Connect the reader to the line out
        reader.output.connect(0, lineOut.input, 0);
        reader.output.connect(1, lineOut.input, 1);

        // Start the synthesizer and the line out
        synth.start();
        lineOut.start();

        // Set the rate of the reader (2.0 will shift the pitch up by one octave)
        reader.rate.set(sample.getFrameRate() * 2.0);

        // Start the reader
        reader.dataQueue.queue(sample);

        // Sleep for a while to allow the sound to play
        try {
            Thread.sleep(500000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Stop everything
        synth.stop();
    }
}



