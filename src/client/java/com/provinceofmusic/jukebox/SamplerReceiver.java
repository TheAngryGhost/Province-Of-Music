package com.provinceofmusic.jukebox;

import com.provinceofmusic.ProvinceOfMusicClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;

import javax.sound.midi.*;
import javax.sound.sampled.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SamplerReceiver {
    public Sampler sampler = null;
    public Receiver receiver = null;

    public Synthesizer synth = null;
    public Soundbank soundbank = null;
    public int elapsedTimeTillFree = 300;

    public long[] channelsLastPlayTime = new long[16];

    public static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);

    public SamplerReceiver(Sampler inSampler){
        if(scheduler == null){
            scheduler = Executors.newScheduledThreadPool(3);
        }
        sampler = inSampler;
        try{
            synth = MidiSystem.getSynthesizer();
            synth.open();
            synth.unloadAllInstruments(synth.getDefaultSoundbank());

            // Load the custom soundbank
            soundbank = MidiSystem.getSoundbank(sampler.sample);
            synth.loadAllInstruments(soundbank);

            receiver = synth.getReceiver();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        } catch (InvalidMidiDataException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int tryGetFreeChannel(){
        for (int i = 0; i < 16; i++){
            if(channelsLastPlayTime[i] < System.currentTimeMillis() - elapsedTimeTillFree){
                channelsLastPlayTime[i] = System.currentTimeMillis();
                return i;
            }
        }
        return -1;
    }

    public boolean playNote(float pitch, int volume, InstrumentDef instrumentDef, boolean override){
        int freeChannel = tryGetFreeChannel();

        if(freeChannel == -1){
            if(!override) {
                return false;
            }
            freeChannel = (int)(Math.random() * 16);
        }

        final int channel = freeChannel;

        float musicVolume = MinecraftClient.getInstance().options.getSoundVolume(SoundCategory.RECORDS) * MinecraftClient.getInstance().options.getSoundVolume(SoundCategory.MASTER);

        float clampedVolume = Math.max(0, Math.min(volume, 100));
        float weightedVolume = (float) (-1*(1/(100+2*clampedVolume)*Math.pow(clampedVolume-100,2))+100);

        final int newVolume = Math.round(weightedVolume);

        float newPitchBend = pitch - (int)pitch;

        int pitchBendValue = (int)(8192 + 4096 * newPitchBend); // Center (8192) + One semitone (4096)
        int lsb = pitchBendValue & 0x7F; // Least significant 7 bits
        int msb = (pitchBendValue >> 7) & 0x7F; // Most significant 7 bits

        int pitchAfterSinglePitch;
        if (instrumentDef.singlePitch) {
            pitchAfterSinglePitch = 60 + instrumentDef.transpose;
        }
        else{
            pitchAfterSinglePitch = (int)pitch + instrumentDef.transpose;
        }
        if(pitchAfterSinglePitch < 0 || pitchAfterSinglePitch > 127){
            ProvinceOfMusicClient.LOGGER.error("Note Pitch out of Range (Your transpose value is too extreme of a value. If not using single pitch : originalPitch + transpose is > 127 or < 0. If using single pitch : 60 + transpose is > 127 or < 0) Value: " + instrumentDef.transpose + "Note Type: " + instrumentDef.noteType);
            return true;
        }
        if(instrumentDef.volume < 0 || instrumentDef.volume > 1){
            ProvinceOfMusicClient.LOGGER.error("Note Volume out of Range (Your Volume value is too extreme of a value. Keep it between 0 and 1) Value: " + instrumentDef.volume + "Note Type: " + instrumentDef.noteType);
            return true;
        }
        if(receiver == null){
            ProvinceOfMusicClient.LOGGER.error("Could not use Instrument. File could be missing or corrupt. File: " + sampler.insFileName + "Note Type: " + instrumentDef.noteType);
            return true;
        }

        try {
            ShortMessage noteOn = new ShortMessage();
            // Pitch bend value for one semitone up
            ShortMessage pitchBend = new ShortMessage();
            ShortMessage volumeChange = new ShortMessage();
            ShortMessage reverb = new ShortMessage();
            pitchBend.setMessage(ShortMessage.PITCH_BEND, 0, lsb, msb);
            noteOn.setMessage(ShortMessage.NOTE_ON, channel, pitchAfterSinglePitch, (int) ((float) (newVolume) * instrumentDef.volume));
            volumeChange.setMessage(ShortMessage.CONTROL_CHANGE, channel, 7, (int) (musicVolume * 127));
            reverb.setMessage(ShortMessage.CONTROL_CHANGE, channel, 91, 50);
            receiver.send(noteOn, -1);
            receiver.send(pitchBend, -1);
            receiver.send(volumeChange, -1);
            receiver.send(reverb, -1);
            scheduler.schedule(() -> {
                try {
                    ShortMessage off = new ShortMessage();
                    off.setMessage(ShortMessage.NOTE_OFF, channel, pitchAfterSinglePitch, 0);
                    receiver.send(off, -1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, 200, TimeUnit.MILLISECONDS);
        } catch (InvalidMidiDataException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    //https://github.com/ModistAndrew/RomanticTp/blob/master/src/main/java/modist/romantictp/client/sound/midi/MidiFilter.java
    public void stopAll() {
        try {
            for (int ch = 0; ch < 16; ch++) {
                for (int i = 0; i < 128; i++) {
                    ShortMessage off = new ShortMessage();
                    off.setMessage(ShortMessage.NOTE_OFF, ch, i, 0);
                    receiver.send(off, -1);
                }
                ShortMessage controlChange1 = new ShortMessage();
                controlChange1.setMessage(ShortMessage.NOTE_OFF, ch, 123, 0);
                ShortMessage controlChange2 = new ShortMessage();
                controlChange2.setMessage(ShortMessage.NOTE_OFF, ch, 64, 0);
                receiver.send(controlChange1, -1);
                receiver.send(controlChange2, -1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
