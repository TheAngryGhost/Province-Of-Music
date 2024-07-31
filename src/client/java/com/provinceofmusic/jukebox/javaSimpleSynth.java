package com.provinceofmusic.jukebox;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;


public class javaSimpleSynth {
    public Synthesizer synth;

    public javaSimpleSynth(){
        try {
            synth = MidiSystem.getSynthesizer();
            synth.open();
            synth.unloadAllInstruments(synth.getDefaultSoundbank());

            // Load the custom soundbank
            Soundbank soundbank = MidiSystem.getSoundbank(new File("1st-violin-SEC-accent.sf2"));
            synth.loadAllInstruments(soundbank);
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        } catch (InvalidMidiDataException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //synth.open();

    //public static void main(String[] args) {
    //    try {
    //        // Open the custom soundfont synthesizer
    //        Synthesizer synth = MidiSystem.getSynthesizer();
    //        synth.open();
//
    //        // Disable the default soundbank
    //        synth.unloadAllInstruments(synth.getDefaultSoundbank());
//
    //        // Load the custom soundbank
    //        Soundbank soundbank = MidiSystem.getSoundbank(new File("1st-violin-SEC-accent.sf2"));
    //        synth.loadAllInstruments(soundbank);
//
    //        //// Create a sequencer with a disconnected sequence
    //        //Sequencer sequencer = MidiSystem.getSequencer(false);
    //        //sequencer.open();
////
    //        //// Connect the sequencer to the custom synthesizer
    //        //Transmitter transmitter = sequencer.getTransmitter();
    //        //Receiver receiver = synth.getReceiver();
    //        //transmitter.setReceiver(receiver);
////
    //        //receiver.send(new ShortMessage(ShortMessage.NOTE_ON, 5, 60, 100), 0);
////
    //        //// Load and play the MIDI sequence
    //        Sequence sequence = MidiSystem.getSequence(new File("gavelgate.mid"));
    //        //sequencer.setSequence(sequence);
    //        //sequencer.start();
////
    //        //// Wait for the sequence to finish
////
    //        ////new ShortMessage(ShortMessage.NOTE_ON, 5, 90, 100), 0
    //        ////command, channel, note, velocity, timestamp
////
////
    //        //while (true) {
    //        //    Thread.sleep(100);
    //        //    //receiver.send(new ShortMessage(ShortMessage.NOTE_ON, 5, 90, 100), 0);
    //        //}
////
    //        //// Stop and close the sequencer and synthesizer
    //        ////sequencer.stop();
    //        ////sequencer.close();
    //        ////synth.close();
//
    //        // Create a sequence and a track
    //        //Sequence sequence = new Sequence(Sequence.PPQ, 4); // 4 ticks per beat
    //        Track track = sequence.getTracks()[1];
//
    //        // Add NOTE_ON and NOTE_OFF events to the track
    //        int channel = 5;
    //        int velocity = 100;
    //        long timestamp = 0;
    //        //int[] notes = {60, 62, 64, 65, 67, 69, 71}; // C, D, E, F, G, A, B
//
    //        for(int i = 0; i < track.size(); i++){
    //            System.out.println(track.get(i).getMessage());
    //        }
//
    //        //FastShortMessage()
//
    //        //ShortMessage.NOTE_ON
//
//
    //        //for (int note : notes) {
    //            //track.add(new MidiEvent(new ShortMessage(ShortMessage.NOTE_ON, channel, note, velocity), timestamp));
    //            //timestamp += 100; // increase the timestamp for each note
    //            //track.add(new MidiEvent(new ShortMessage(ShortMessage.NOTE_OFF, channel, note, velocity), timestamp));
    //            //timestamp += 100; // increase the timestamp for each note off
    //        //}
//
    //        // Create a sequencer and set the sequence
    //        Sequencer sequencer = MidiSystem.getSequencer(false);
    //        sequencer.open();
    //        sequencer.setSequence(sequence);
//
    //        // Connect the sequencer to the custom synthesizer
    //        Transmitter transmitter = sequencer.getTransmitter();
    //        Receiver receiver = synth.getReceiver();
    //        transmitter.setReceiver(receiver);
//
    //        // Start the sequencer
    //        sequencer.start();
//
    //        // Wait for the sequence to finish
    //        while (sequencer.isRunning()) {
    //            Thread.sleep(100);
    //        }
//
//
    //    } catch (Exception e) {
    //        e.printStackTrace();
    //    }
    //}

    public void PlayNote(int insertNoteChannel, int insertNotePitch, int insertNoteVelocity) throws MidiUnavailableException, InvalidMidiDataException, IOException, InterruptedException {


        //Synthesizer synth = MidiSystem.getSynthesizer();
        //synth.open();
        //// Disable the default soundbank
        //synth.unloadAllInstruments(synth.getDefaultSoundbank());
//
        //// Load the custom soundbank
        //Soundbank soundbank = MidiSystem.getSoundbank(new File("1st-violin-SEC-accent.sf2"));
        //synth.loadAllInstruments(soundbank);
        ////Track track = sequence.getTracks()[1];
//
//
        ////Transmitter transmitter = sequencer.getTransmitter();
        ////Receiver receiver = synth.getReceiver();
       //// transmitter.setReceiver(receiver);
//
//
        //// Create a Sequence
        //Sequence sequence = new Sequence(Sequence.PPQ, 480);
//
// Get t//he track
        //Track track = sequence.createTrack();
//
// Add a// C4 note (note-on and note-off)
        //int channel = insertNoteChannel; // MIDI channel
        //int pitch = insertNotePitch; // C4
        //int velocity = insertNoteVelocity; // Medium velocity
        //int durationTicks = 120; // Quarter note duration (adjust as needed)
//

        Thread thread = new Thread(() -> {
            ShortMessage noteOn = new ShortMessage();
            try {
                noteOn.setMessage(ShortMessage.NOTE_ON, insertNoteChannel, insertNotePitch, insertNoteVelocity);
            } catch (InvalidMidiDataException e) {
                throw new RuntimeException(e);
            }
            //track.add(new MidiEvent(noteOn, 0));
//
// Note-//off event (after the desired duration)
            ShortMessage noteOff = new ShortMessage();
            try {
                noteOff.setMessage(ShortMessage.NOTE_OFF, insertNoteChannel, insertNotePitch, 0);
            } catch (InvalidMidiDataException e) {
                throw new RuntimeException(e);
            }

            Receiver receiver = null;
            try {
                receiver = synth.getReceiver();
            } catch (MidiUnavailableException e) {
                throw new RuntimeException(e);
            }

            receiver.send(noteOn, -1);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            receiver.send(noteOff, -1);
        });
        thread.start();


// Note-//on event
        //ShortMessage noteOn = new ShortMessage();
        //noteOn.setMessage(ShortMessage.NOTE_ON, insertNoteChannel, insertNotePitch, insertNoteVelocity);
        //track.add(new MidiEvent(noteOn, 0));
//
// Note-//off event (after the desired duration)
        //ShortMessage noteOff = new ShortMessage();
        //noteOff.setMessage(ShortMessage.NOTE_OFF, insertNoteChannel, insertNotePitch, 0);

        //Receiver receiver = synth.getReceiver();

        //receiver.send(noteOn, 100);
        //receiver.send(noteOff, -1);


        //track.add(new MidiEvent(noteOff, durationTicks));
//
// Now p//lay the sequence using the sequencer
//
//
        ////track.add(new MidiEvent(new ShortMessage(ShortMessage.NOTE_ON, channel, 60, velocity), timestamp));
        ////timestamp += 100; // increase the timestamp for each note
        ////track.add(new MidiEvent(new ShortMessage(ShortMessage.NOTE_OFF, channel, 60, velocity), timestamp));
        ////timestamp += 100; // increase the timestamp for each note off
//
//
//
        //Sequencer sequencer = MidiSystem.getSequencer(false);
        //sequencer.open();
//
//
        //sequencer.setSequence(sequence);
        //sequencer.start();
    }
}