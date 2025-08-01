package com.provinceofmusic.jukebox;

import com.provinceofmusic.ProvinceOfMusicClient;
import com.provinceofmusic.listeners.NoteListener;
import com.provinceofmusic.listeners.NoteListenerHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;

import javax.sound.midi.MidiChannel;
import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;

public class NoteReplacer implements NoteListener {

    public static KeyBinding replaceNoteBinding;
    
    
    public static ArrayList<Sampler> samplers = new ArrayList<>();
    public static boolean replaceMusic = true;

    public static boolean interrupt = false;

    public static SamplePack pack;

    //TODO remove this
    public static boolean debugTest = false;
    
    @Override
    public void onNotePlayed(NoteSoundMinecraft note) {
        if (!replaceMusic) return;
        NoteSoundMidi noteSoundMidi = new NoteSoundMidi(note);
        playMusicFrame(noteSoundMidi.instrument, noteSoundMidi.pitch, noteSoundMidi.volume);
        //playMusicFrame(NoteListenerHelper.SoundIdToInstrumentSound(instrument), pitch, volume);
    }

    public static void playMusicFrame(Instrument instrument, float pitch, int volume){

        if(!interrupt){



            for (InstrumentDef instrumentDef : pack.instrumentDefs) {
                if(instrumentDef.noteType.equals(instrument.registeredName)){
                    String path = ProvinceOfMusicClient.samplepacksdir + "\\" + pack.name + "\\" + "instrumentfiles" + "\\" + instrumentDef.dir;
                    if(samplers != null){
                        for (Sampler sampler : samplers) {
                            if(sampler.sample.toPath().toString().equals(path)){
                                sampler.playNote(pitch,volume,instrumentDef);
                                break;
                            }
                        }
                    }

                }

            }
        }
    }

    //TODO remove this
    public void PassTime(){

    }

    public static void main(){


        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (replaceNoteBinding.wasPressed()) {

                replaceMusic = !replaceMusic;
                assert client.player != null;
                if (replaceMusic) {
                    ProvinceOfMusicClient.LOGGER.info("Playing Sample Pack music");
                    client.player.sendMessage(Text.of("Playing Sample Pack music"), false);
                } else {
                    ProvinceOfMusicClient.LOGGER.info("Playing Default music");
                    client.player.sendMessage(Text.of("Playing Default music"), false);

                }
            }
        });
    }

    //TODO make the save changes run this instead
    public void RunSetup(){

        if(ProvinceOfMusicClient.configSettings.activeSamplePack != null){
            ProvinceOfMusicClient.LOGGER.debug("Trying to load SamplePack");

            if(ProvinceOfMusicClient.configSettings.activeSamplePack != null && SamplePack.getFile(ProvinceOfMusicClient.configSettings.activeSamplePack).exists()) {

                interrupt = true;

                if(samplers != null && !samplers.isEmpty()) {
                    for (Sampler s : samplers) {
                        for (SamplerReceiver s2 : s.samplerReceivers) {
                            s2.stopAll();
                        }
                    }
                }

                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {

                        Runtime runtime = Runtime.getRuntime();

                        // Run garbage collector to get more accurate result (optional)
                        System.gc();

                        long totalMemory = runtime.totalMemory();     // Total memory currently available to JVM
                        long freeMemory = runtime.freeMemory();       // Free memory within the total
                        long usedMemory = totalMemory - freeMemory;   // Used memory

                        System.out.println("Used Memory: " + (usedMemory / 1024 / 1024) + " MB");
                        System.out.println("Free Memory: " + (freeMemory / 1024 / 1024) + " MB");
                        System.out.println("Total Memory: " + (totalMemory / 1024 / 1024) + " MB");
                        System.out.println("Max Memory: " + (runtime.maxMemory() / 1024 / 1024) + " MB");

                        ///*
                        int count = 0;

                        if(samplers != null && !samplers.isEmpty()){
                            for(Sampler s: samplers) {
                                for (SamplerReceiver s2 : s.samplerReceivers) {
                                    count++;
                                    //TODO if you eventually get this to work move this to a stop function in the reciever

                                    for (MidiChannel channel : s2.synth.getChannels()) {
                                        channel.allNotesOff();
                                        channel.allSoundOff();
                                    }

                                    s2.receiver.close();
                                    s2.receiver = null;
                                    s2.synth.unloadAllInstruments(s2.soundbank);
                                    s2.soundbank = null;
                                    s2.synth.close();
                                    //s2.scheduler.close();
                                    s2.sampler = null;
                                    s2.synth = null;
                                }
                                //s.scheduler.close();
                                s.sample = null;
                            }
                            samplers.clear();

                            Sampler.scheduler.shutdownNow();
                            SamplerReceiver.scheduler.shutdownNow();
                        }
                        //*/

                        System.out.println("COUNT: " + count);

                        Runtime runtime2 = Runtime.getRuntime();

                        // Run garbage collector to get more accurate result (optional)
                        System.gc();

                        long totalMemory2 = runtime2.totalMemory();     // Total memory currently available to JVM
                        long freeMemory2 = runtime2.freeMemory();       // Free memory within the total
                        long usedMemory2 = totalMemory2 - freeMemory2;   // Used memory

                        System.out.println("Used Memory: " + (usedMemory2 / 1024 / 1024) + " MB");
                        System.out.println("Free Memory: " + (freeMemory2 / 1024 / 1024) + " MB");
                        System.out.println("Total Memory: " + (totalMemory2 / 1024 / 1024) + " MB");
                        System.out.println("Max Memory: " + (runtime2.maxMemory() / 1024 / 1024) + " MB");


                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, 5000);



                TimerTask task2 = new TimerTask() {
                    @Override
                    public void run() {
                        Thread thread = new Thread(() -> {


                            Sampler.scheduler = null;
                            SamplerReceiver.scheduler = null;




                            Runtime runtime2 = Runtime.getRuntime();

                            // Run garbage collector to get more accurate result (optional)
                            System.gc();

                            long totalMemory2 = runtime2.totalMemory();     // Total memory currently available to JVM
                            long freeMemory2 = runtime2.freeMemory();       // Free memory within the total
                            long usedMemory2 = totalMemory2 - freeMemory2;   // Used memory

                            System.out.println("Used Memory: " + (usedMemory2 / 1024 / 1024) + " MB");
                            System.out.println("Free Memory: " + (freeMemory2 / 1024 / 1024) + " MB");
                            System.out.println("Total Memory: " + (totalMemory2 / 1024 / 1024) + " MB");
                            System.out.println("Max Memory: " + (runtime2.maxMemory() / 1024 / 1024) + " MB");

                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }

                            pack = SamplePack.getSamplePack(SamplePack.getFile(ProvinceOfMusicClient.configSettings.activeSamplePack));
                            if((samplers == null || samplers.isEmpty()) ){//&& !debugTest

                                for (InstrumentDef instrumentDef : pack.instrumentDefs) {
                                    File file = new File(ProvinceOfMusicClient.samplepacksdir + "/" + pack.name + "/" + "instrumentfiles" + "/" + instrumentDef.dir);
                                    if(file.exists()){
                                        System.out.println((Runtime.getRuntime().freeMemory()/ 1024.0 / 1024.0) + " MB");
                                        samplers.add(new Sampler(file));
                                    }
                                    else{
                                        ProvinceOfMusicClient.LOGGER.warn("sf2 file not found File: " + instrumentDef.dir + " Ignoring this instrument");
                                    }
                                }










                                System.out.println("snorkel");
                                debugTest = true;
                                //ArrayList<Sampler> samplerCache = pack.getInstruments();
                                //samplers = samplerCache;
                            }

                            System.out.println("Sampler Count: " + samplers.size());

                            System.gc();

                            //samplers = pack.getInstruments();
                            //pack.getInstruments();
                            //samplers = samplerCache;

                            ProvinceOfMusicClient.LOGGER.debug("Loaded SamplePack successfully");


                            if(Sampler.scheduler == null){
                                Sampler.scheduler = Executors.newScheduledThreadPool(3);
                                System.out.println(Sampler.scheduler.isShutdown());
                            }

                            if(SamplerReceiver.scheduler == null){
                                SamplerReceiver.scheduler = Executors.newScheduledThreadPool(3);
                                System.out.println(SamplerReceiver.scheduler.isShutdown());
                            }

                            System.gc();

                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }

                            interrupt = false;
                        });
                        thread.start();
                    }
                };

                if(samplers == null || samplers.isEmpty()){
                    //timer.schedule(task2, 5000);
                }

                timer.schedule(task2, 10000);




            }
            else{
                ProvinceOfMusicClient.configSettings.activeSamplePack = null;
                ProvinceOfMusicClient.saveConfigSettings();
                ProvinceOfMusicClient.LOGGER.warn("SamplePack load failed (Replacing active SamplePack with no SamplePack)");
            }


        }

        NoteListenerHelper.addListener(this);
    }
}
