package com.provinceofmusic.jukebox;

import com.provinceofmusic.ProvinceOfMusicClient;
import com.provinceofmusic.listeners.NoteListener;
import com.provinceofmusic.listeners.NoteListenerHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;
import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

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
                            sampler.playNoteVariant(pitch,volume,instrumentDef);
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

    public void RunSetup(){

        if(ProvinceOfMusicClient.configSettings.activeSamplePack != null){
            ProvinceOfMusicClient.LOGGER.debug("Trying to load SamplePack");
            //samplers = null;
            if(ProvinceOfMusicClient.configSettings.activeSamplePack != null && SamplePack.getFile(ProvinceOfMusicClient.configSettings.activeSamplePack).exists()) {
                pack = SamplePack.getSamplePack(SamplePack.getFile(ProvinceOfMusicClient.configSettings.activeSamplePack));

                //creating Samplers
                ArrayList<File> sampleFiles = pack.getInstrumentFiles();
                for (File file : sampleFiles){
                    if (file.exists()){
                        Sampler temp = new Sampler(file);
                        samplers.add(temp);
                    }
                    else{
                        ProvinceOfMusicClient.LOGGER.warn("sf2 file not found File: " + file + " Ignoring this instrument");
                    }
                }
                //setting variants //TODO remove dead code of variants so delete variants
                for (InstrumentDef instrumentDef : pack.instrumentDefs) {
                    String path = ProvinceOfMusicClient.samplepacksdir + "\\" + pack.name + "\\" + "instrumentfiles" + "\\" + instrumentDef.dir;
                    for (Sampler sampler : samplers) {
                        if(sampler.sample.toPath().toString().equals(path)){
                            //sampler.variants.add(instrumentDef);
                            sampler.createNewReciever();
                            break;
                        }
                    }
                }

                //TODO add extra recievers for instruments of high frequency like all the kick snare hi hat harp piano bass

                //samplers = pack.getInstruments(samplers);
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
