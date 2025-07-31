package com.provinceofmusic.jukebox;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Sampler {

    public String insFileName;

    public File sample;

    public ArrayList<SamplerReceiver> samplerReceivers = new ArrayList<>();

    public float newThreads = 0;

    public static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);

    public void createNewReceiver(){
        SamplerReceiver temp = new SamplerReceiver(this);
        samplerReceivers.add(temp);
    }

    public Sampler(File insFile) {
        if(scheduler == null){
            scheduler = Executors.newScheduledThreadPool(3);
        }
        sample = insFile;
        insFileName = insFile.getName();
        createNewReceiver();
    }

    public void playNote(float pitch, int volume, InstrumentDef instrumentDef){
        for (SamplerReceiver receiver : samplerReceivers){
            if (receiver.playNote(pitch,volume,instrumentDef,false)) {
                return;
            }
        }
        newThreads += (float) 1 /8;
        samplerReceivers.get((int) (Math.random() * samplerReceivers.size())).playNote(pitch,volume,instrumentDef,true);

        if(newThreads < -2){ //just in case lag builds up lots of repeated actions
            newThreads = -2;
        }

        if(newThreads > 0){
            newThreads--;
            scheduler.schedule(() -> {
                try {
                    System.out.println("new receiver created");
                    createNewReceiver();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, 200, TimeUnit.MILLISECONDS);
        }
    }
}
