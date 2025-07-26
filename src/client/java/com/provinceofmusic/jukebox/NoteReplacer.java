package com.provinceofmusic.jukebox;

import com.provinceofmusic.ProvinceOfMusicClient;
import com.provinceofmusic.listeners.NoteListener;
import com.provinceofmusic.listeners.NoteListenerHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;

import java.io.File;
import java.util.ArrayList;

public class NoteReplacer implements NoteListener {

    public static KeyBinding replaceNoteBinding;
    
    
    public static ArrayList<Sampler> samplers = new ArrayList<>();
    public static boolean replaceMusic = true;

    public static float musicVolume = 0;

    public static boolean interupt = false;

    public static SamplePack pack;
    
    @Override
    public void onNotePlayed(NoteSoundMinecraft note) {
        if (!replaceMusic) return;
        NoteSoundMidi noteSoundMidi = new NoteSoundMidi(note);
        playMusicFrame(noteSoundMidi.instrument, noteSoundMidi.pitch, noteSoundMidi.volume);
        //playMusicFrame(NoteListenerHelper.SoundIdToInstrumentSound(instrument), pitch, volume);

    }

    public static void playMusicFrame(Instrument instrument, float pitch, int volume){
        //this function is written like this to produce the least amount of TimerTasks possible //TODO remove this

        ArrayList<Sampler> samplerCache = new ArrayList<>(); //TODO remove this
        ArrayList<Integer> channelCache = new ArrayList<>(); //TODO remove this
        musicVolume = MinecraftClient.getInstance().options.getSoundVolume(SoundCategory.RECORDS) * MinecraftClient.getInstance().options.getSoundVolume(SoundCategory.MASTER); //TODO remove this
        //TODO remove musicVolume
        if(!interupt){



            for (InstrumentDef instrumentDef : pack.instrumentDefs) {
                if(instrumentDef.noteType.equals(instrument.registeredName)){
                    String path = ProvinceOfMusicClient.samplepacksdir + "\\" + pack.name + "\\" + "instrumentfiles" + "\\" + instrumentDef.dir;
                    for (Sampler sampler : samplers) {
                        if(sampler.sample.toPath().toString().equals(path)){
                            //sampler.variants.add(instrumentDef);
                            //System.out.println(sampler.sample.toPath());
                            //System.out.println(path);
                            sampler.playNote(pitch,volume,instrumentDef);
                            break;
                        }
                    }
                }

            }



            /*
            for (Sampler tempSampler : samplers) {
                if (instrument.registeredName.equals(tempSampler.noteType)) {
                    tempSampler.playNote(pitch,volume);
                }
            }
            */

        }

    }

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
                pack = SamplePack.getSamplePack(SamplePack.getFile(ProvinceOfMusicClient.configSettings.activeSamplePack));
                samplers = pack.getInstruments(NoteReplacer.samplers);

                ProvinceOfMusicClient.LOGGER.debug("Loaded SamplePack successfully");
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
