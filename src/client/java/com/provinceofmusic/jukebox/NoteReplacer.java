package com.provinceofmusic.jukebox;

import com.provinceofmusic.ProvinceOfMusicClient;
import com.provinceofmusic.listeners.NoteListener;
import com.provinceofmusic.listeners.NoteListenerHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

public class NoteReplacer implements NoteListener {

    public static KeyBinding replaceNoteBinding;
    
    
    public static ArrayList<Sampler> samplers = new ArrayList<>();
    public static boolean replaceMusic = true;

    public static boolean interrupt = false;

    public static SamplePack pack;
    
    @Override
    public void onNotePlayed(NoteSoundMinecraft note) {
        if (!replaceMusic) return;
        NoteSoundMidi noteSoundMidi = new NoteSoundMidi(note);
        playMusicFrame(noteSoundMidi.instrument, noteSoundMidi.pitch, noteSoundMidi.volume);
    }

    public static void playMusicFrame(Instrument instrument, float pitch, int volume){

        if(!interrupt){
            for (InstrumentDef instrumentDef : pack.instrumentDefs) {
                if(instrumentDef.noteType.equals(instrument.registeredName)){
                    Path path = Path.of(ProvinceOfMusicClient.samplepacksdir + "/" + pack.name + "/" + "samples" + "/" + instrumentDef.dir);
                    System.out.println(path);
                    if(samplers != null){
                        for (Sampler sampler : samplers) {
                            if(sampler.sample.toPath().equals(path)){
                                sampler.playNote(pitch,volume,instrumentDef);
                                break;
                            }
                        }
                    }

                }

            }
        }
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

    public void RunSetup(){

        if(ProvinceOfMusicClient.configSettings.activeSamplePack != null){
            ProvinceOfMusicClient.LOGGER.debug("Trying to load SamplePack");

            if(ProvinceOfMusicClient.configSettings.activeSamplePack != null && SamplePack.getSamplePackAsFile(ProvinceOfMusicClient.configSettings.activeSamplePack).exists()) {


                Thread reload = new Thread(() -> {
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

                    pack = SamplePack.getSamplePack(SamplePack.getSamplePackAsFile(ProvinceOfMusicClient.configSettings.activeSamplePack));
                    if((samplers == null || samplers.isEmpty())){
                        for (InstrumentDef instrumentDef : pack.instrumentDefs) {
                            File file = new File(ProvinceOfMusicClient.samplepacksdir + "/" + pack.name + "/" + "samples" + "/" + instrumentDef.dir);
                            if(file.exists()){
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
                });

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
