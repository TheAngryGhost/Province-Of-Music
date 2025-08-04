package com.provinceofmusic.jukebox;

import javax.sound.midi.Receiver;
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


    public static Thread recycler = new Thread(() -> {
        while(true){
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            for (int i = 0; i < NoteReplacer.samplers.size(); i++) {
                if(NoteReplacer.interrupt){
                    i = 1000000;
                    continue;
                }
                NoteReplacer.samplers.get(i).recycleExtraSampleReceivers();
            }
        }
    });


    //TODO rename this
    public void createNewReceiver(){
        SamplerReceiver temp = SamplerReceiver.retrieveSamplerReceiver(this);
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
        newThreads += (float) 1/8;
        if(!samplerReceivers.isEmpty()){
            samplerReceivers.get((int) (Math.random() * samplerReceivers.size())).playNote(pitch,volume,instrumentDef,true);
        }


        if(newThreads < -2){ //just in case lag builds up lots of repeated actions
            newThreads = -2;
        }

        if(newThreads > 0){
            newThreads--;
            scheduler.schedule(() -> {
                try {
                    createNewReceiver();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, 200, TimeUnit.MILLISECONDS);
        }
    }

    public void storeSampleReceivers(){
        for (SamplerReceiver sr : samplerReceivers) {
            sr.shutdown();
        }
        samplerReceivers.clear();
    }

    public void recycleExtraSampleReceivers(){
        for (int i = 0; i < samplerReceivers.size()- 1; i++) {
            SamplerReceiver curSR = samplerReceivers.get(i);
            if(curSR.samplerReceiverLastPlayTime + 200000 < System.currentTimeMillis()){
                curSR.shutdown(false);
                samplerReceivers.remove(i);
                i--;
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if(NoteReplacer.interrupt){
                    i = 1000000;
                }
            }
        }
    }
}
