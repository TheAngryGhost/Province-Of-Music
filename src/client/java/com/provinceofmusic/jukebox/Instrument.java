package com.provinceofmusic.jukebox;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;

public class Instrument {
    public Synthesizer synth;
    public Receiver receiver = null;

    public String noteType;

    public int transpose = 0;

    public float volume = 1f;

    public Instrument(File insFile, String noteType) {
        try{
            this.noteType = noteType;
            synth = MidiSystem.getSynthesizer();
            synth.open();
            synth.unloadAllInstruments(synth.getDefaultSoundbank());

            // Load the custom soundbank
            Soundbank soundbank = MidiSystem.getSoundbank(insFile);
            synth.loadAllInstruments(soundbank);

            receiver = synth.getReceiver();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        } catch (InvalidMidiDataException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Instrument(File insFile, String noteType, int transpose) {
        try{
            this.noteType = noteType;
            this.transpose = transpose;
            synth = MidiSystem.getSynthesizer();
            synth.open();
            synth.unloadAllInstruments(synth.getDefaultSoundbank());

            // Load the custom soundbank
            Soundbank soundbank = MidiSystem.getSoundbank(insFile);
            synth.loadAllInstruments(soundbank);

            receiver = synth.getReceiver();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        } catch (InvalidMidiDataException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Instrument(File insFile, String noteType, int transpose, float volume) {
        try{
            this.noteType = noteType;
            this.transpose = transpose;
            this.volume = volume;
            synth = MidiSystem.getSynthesizer();
            synth.open();
            synth.unloadAllInstruments(synth.getDefaultSoundbank());

            // Load the custom soundbank
            Soundbank soundbank = MidiSystem.getSoundbank(insFile);
            synth.loadAllInstruments(soundbank);

            receiver = synth.getReceiver();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        } catch (InvalidMidiDataException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
