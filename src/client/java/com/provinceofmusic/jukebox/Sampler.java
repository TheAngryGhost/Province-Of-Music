package com.provinceofmusic.jukebox;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Sampler {
    //public Receiver receiver = null;

    //public String noteType;

    //public int transpose = 0;

    //public float volume = 1f;

    //public boolean singlePitch = false;

    //public int channel = 0;

    public String insFileName;

    public File sample;

    public ArrayList<SamplerReceiver> samplerReceivers = new ArrayList<>();

    //TODO add a bunch of InstrumentDefs here so that you know how many times and one which settings you have to play stuff

    public ArrayList<InstrumentDef> variants = new ArrayList<>();

    public float newThreads = 0;

    public static boolean disable = false;

    public static ArrayList<Sampler> reallocationQueue = new ArrayList<>();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    //TODO change this to an actual thread pool
    public static Thread sampleReceiverReallocator = new Thread(){
        @Override
        public void run() {
            super.run();
            while (!disable){
                while(!reallocationQueue.isEmpty()){
                    Sampler currentSampler = reallocationQueue.getFirst();
                    System.out.println("ran out of room for sample " + currentSampler.sample.getName());
                    currentSampler.createNewReciever();
                    reallocationQueue.removeFirst();
                    try {
                        sleep(150);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                try {
                    sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            //createNewReciever();
            //createNewReciever().playNote(pitch,volume,instrumentDef);
            //TODO make it play on this receiver once everything else works
        }
    };
    /*
    public Sampler(File insFile, String noteType) {
        try{
            insFileName = insFile.getName();
            Synthesizer synth;
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
        } catch (InvalidMidiDataException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    */
    /*
    public Sampler(File insFile, String noteType, int transpose) {
        try{
            insFileName = insFile.getName();
            Synthesizer synth;
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
        } catch (InvalidMidiDataException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    */

    public SamplerReceiver createNewReciever(){
        System.out.println("reciever created" + insFileName);
        SamplerReceiver temp = new SamplerReceiver(this);
        samplerReceivers.add(temp);
        return temp;
    }

    //TODO remove this
    public Sampler(File insFile, String noteType, int transpose, float volume, boolean singlePitch) {
        sample = insFile;
        insFileName = insFile.getName();
        //Synthesizer synth;
        //this.noteType = noteType;
        //this.transpose = transpose;
        //this.volume = volume;
        //this.singlePitch = singlePitch;

        createNewReciever();
        createNewReciever();

        //synth = MidiSystem.getSynthesizer();
        //synth.open();
        //synth.unloadAllInstruments(synth.getDefaultSoundbank());

        // Load the custom soundbank
        //Soundbank soundbank = MidiSystem.getSoundbank(insFile);
        //synth.loadAllInstruments(soundbank);

        //receiver = synth.getReceiver();


    }

    public Sampler(File insFile) {
        sample = insFile;
        insFileName = insFile.getName();
        //Synthesizer synth;

        createNewReciever();

        //synth = MidiSystem.getSynthesizer();
        //synth.open();
        //synth.unloadAllInstruments(synth.getDefaultSoundbank());

        // Load the custom soundbank
        //Soundbank soundbank = MidiSystem.getSoundbank(insFile);
        //synth.loadAllInstruments(soundbank);

        //receiver = synth.getReceiver();
    }

    public void playNote(float pitch, int volume){
        //variants
        for (InstrumentDef instrumentDef : variants){
            playNoteVariant(pitch,volume,instrumentDef);
        }
    }

    public void playNoteVariant(float pitch, int volume, InstrumentDef instrumentDef){
        Sampler me = this;
        for (SamplerReceiver receiver : samplerReceivers){
            //System.out.println(receiver.receiver.toString() + (receiver.receiver == null));
            if (receiver.playNote(pitch,volume,instrumentDef,false)) {
                //System.out.println("found channel playing note" + instrumentDef.noteType + insFileName);
                return;
            }
        }
        System.out.println("creating new");
        newThreads += (float) 1 /8;
        System.out.println(newThreads);
        samplerReceivers.get((int) (Math.random() * samplerReceivers.size())).playNote(pitch,volume,instrumentDef,true);

        if(newThreads > 0){
            newThreads--;
            scheduler.schedule(() -> {
                try {
                    System.out.println("ran out of room for sample " + sample.getName());
                    createNewReciever();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, 200, TimeUnit.MILLISECONDS);
            //reallocationQueue.add(this);
        }
    }

    /*
    public void incrementChannel(){
        channel++;
        if(channel > 8 - 1){
            channel = 0;
        }
    }
    */

}
