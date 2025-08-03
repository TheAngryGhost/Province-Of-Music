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


                Thread reload = new Thread(){
                    @Override
                    public void run(){
                        interrupt = true;

                        if(samplers != null && !samplers.isEmpty()) {
                            for (Sampler s : samplers) {
                                for (SamplerReceiver s2 : s.samplerReceivers) {
                                    s2.stopAll();
                                }
                            }
                        }

                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }


                        if(samplers != null && !samplers.isEmpty()){
                            for(Sampler s: samplers) {
                                s.storeSampleReceivers();
                            }
                            samplers.clear();
                        }

                        System.gc();

                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                        pack = SamplePack.getSamplePack(SamplePack.getFile(ProvinceOfMusicClient.configSettings.activeSamplePack));
                        if((samplers == null || samplers.isEmpty())){//&& !debugTest

                            for (InstrumentDef instrumentDef : pack.instrumentDefs) {
                                File file = new File(ProvinceOfMusicClient.samplepacksdir + "/" + pack.name + "/" + "instrumentfiles" + "/" + instrumentDef.dir);
                                if(file.exists()){
                                    System.out.println((Runtime.getRuntime().freeMemory()/ 1024.0 / 1024.0) + " MB");
                                    System.out.println(file.getName());
                                    samplers.add(new Sampler(file));

                                }
                                else{
                                    ProvinceOfMusicClient.LOGGER.warn("sf2 file not found File: " + instrumentDef.dir + " Ignoring this instrument");
                                }
                            }
                        }

                        System.gc();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                        interrupt = false;
                    }
                };

                reload.start();

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
