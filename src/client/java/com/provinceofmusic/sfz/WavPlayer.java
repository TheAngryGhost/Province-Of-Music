package com.provinceofmusic.sfz;


import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.PitchShifter;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.io.jvm.AudioPlayer;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;
import java.io.*;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;

public class WavPlayer {
    public static void main(String[] args) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        // Define the audio format
        //float tempoFactor = 0.8f;
        //float pitchFactor = 1.0f;
//
        //AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("music.wav"));
        //AudioFormat audioFormat = audioInputStream.getFormat();
        //AudioFormat modAudioFormat = getOutFormat(audioFormat);
//
//
//
        //final AudioInputStream modifiedAudio = AudioSystem.getAudioInputStream(modAudioFormat, audioInputStream);
//
//
//
        //double sampleRate = audioFormat.getSampleRate();
        //int bufferSize = audioFormat.getFrameSize();
        //int bufferOverlap = 0; // This is typically half of the bufferSize
        ////int channels = audioFormat.getChannels();
//
        ////Clip clip = AudioSystem.getClip();
        ////clip.open(modifiedAudio);
//
//
        //AudioSystem.write(modifiedAudio, AudioFileFormat.Type.WAVE, new File("music2.wav"));
//
//
//
        ////AudioDispatcher adp =  AudioDispatcherFactory.fromPipe("music.mp3", 44100, 4096, 0);
        ////AudioDispatcher adp =  AudioDispatcherFactory.fromPipe("music.wav", (int) sampleRate, bufferSize, 0);
        ////var format = adp.getFormat();
        //////PitchShifter pitchShifter = new PitchShifter(pitchFactor, (float) sampleRate, bufferSize, bufferOverlap);
        //////rbs = new RubberBandAudioProcessor(44100, tempoFactor, pitchFactor);
        //////adp.addAudioProcessor(rbs);
        ////adp.addAudioProcessor(new AudioPlayer(JVMAudioInputStream.toAudioFormat(format)));
        ////new Thread(adp).start();

        // Read the WAV file
        File file = new File("music.wav");
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
        AudioFormat format = audioStream.getFormat();
        int numSamples = (int) audioStream.getFrameLength() * format.getFrameSize();
        byte[] audioBytes = new byte[numSamples];
        double[] audioData = new double[numSamples];
        audioStream.read(audioBytes);

        //// Convert bytes to samples
        //for (int i = 0; i < numSamples; i++) {
        //    audioData[i] = audioBytes[i] / 128.0;
        //}
//
        //// Process with phase vocoder
        //PhaseVocoder vocoder = new PhaseVocoder(2.0, 0.0); // Change these parameters as needed
        //double[] paddedIn = vocoder.padToNextPowerOfTwo(audioData);
        //double[] processedData = vocoder.process(paddedIn);
//
        //// Convert samples to bytes
        //for (int i = 0; i < numSamples; i++) {
        //    audioBytes[i] = (byte) (processedData[i] * 128.0);
        //}

        // Process with OLA pitch shifter
        OlaPitchShifter pitchShifter = new OlaPitchShifter(2.0, 1024, 512); // Change these parameters as needed
        short[] processedShorts = pitchShifter.process(audioBytes);

        // Convert shorts back to bytes
        byte[] processedBytes = new byte[processedShorts.length * 2];

        // Write the processed data to a new WAV file
        File outputFile = new File("music2.wav");
        //ByteArrayInputStream byteStream = new ByteArrayInputStream(audioBytes);
        ByteArrayInputStream byteStream = new ByteArrayInputStream(processedBytes);
        AudioInputStream outputAudioStream = new AudioInputStream(byteStream, format, numSamples / format.getFrameSize());
        AudioSystem.write(outputAudioStream, AudioFileFormat.Type.WAVE, outputFile);

    }


    //private static AudioFormat getOutFormat(AudioFormat inFormat) {
    //    int ch = inFormat.getChannels();
    //    float rate = inFormat.getSampleRate();
    //    return new AudioFormat(PCM_SIGNED, 72000, 16, ch, ch * 3, rate / 2,
    //            inFormat.isBigEndian());
    //}
}
