package com.provinceofmusic.jukebox;

import com.provinceofmusic.ProvinceOfMusicClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;

import javax.sound.midi.*;

import java.io.IOException;
import java.util.Stack;
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

    public long samplerReceiverLastPlayTime = Long.MAX_VALUE / 2;

    public static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);

    public static Stack<SamplerReceiver> samplerReceiverPool = new Stack<>();

    public boolean ready = true;

    public SamplerReceiver(){

    }

    public int tryGetFreeChannel(){
        for (int i = 0; i < 16; i++){
            if(channelsLastPlayTime[i] < System.currentTimeMillis() - elapsedTimeTillFree){
                channelsLastPlayTime[i] = System.currentTimeMillis();
                samplerReceiverLastPlayTime = System.currentTimeMillis();
                return i;
            }
        }
        return -1;
    }

    public boolean playNote(float inPitch, int inVelocity, InstrumentDef instrumentDef, boolean override){
        if(!ready){
            return false;
        }

        //find open channel
        int freeChannel = tryGetFreeChannel();
        if(freeChannel == -1){
            if(!override) {
                return false;
            }
            freeChannel = (int)(Math.random() * 16);
        }
        final int channel = freeChannel;

        //calculate note velocity.
        float clampedVelocity = Math.max(0, Math.min(inVelocity, 100));
        float weightedVelocity = (float) (-1*(1/(100+2*clampedVelocity)*Math.pow(clampedVelocity-100,2))+100);
        final int velocity = (int) (Math.round(weightedVelocity) * instrumentDef.volume);

        //calculate a pitch bend for greater precision, better than integer precision
        float newPitchBend = inPitch - (int) inPitch;
        int pitchBendValue = (int)(8192 + 4096 * newPitchBend); // Center (8192) + One semitone (4096)
        int lsb = pitchBendValue & 0x7F; // Least significant 7 bits
        int msb = (pitchBendValue >> 7) & 0x7F; // Most significant 7 bits

        //calculate pitch
        int pitch;
        if (instrumentDef.singlePitch) {
            pitch = 60 + instrumentDef.transpose;
        }
        else{
            pitch = (int) inPitch + instrumentDef.transpose;
        }

        //tests to make sure your game won't crash when this note plays
        if(pitch < 0 || pitch > 127){
            ProvinceOfMusicClient.LOGGER.error("Note Pitch out of Range (Your transpose value is too extreme of a value. If not using single inPitch : originalPitch + transpose is > 127 or < 0. If using single inPitch : 60 + transpose is > 127 or < 0) Value: " + instrumentDef.transpose + "Note Type: " + instrumentDef.noteType);
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

        //calculate volume
        float volume = MinecraftClient.getInstance().options.getSoundVolume(SoundCategory.RECORDS) * MinecraftClient.getInstance().options.getSoundVolume(SoundCategory.MASTER);

        try {
            ShortMessage noteOn = new ShortMessage();
            ShortMessage pitchBend = new ShortMessage();
            ShortMessage volumeChange = new ShortMessage();
            ShortMessage reverb = new ShortMessage();
            pitchBend.setMessage(ShortMessage.PITCH_BEND, 0, lsb, msb);
            noteOn.setMessage(ShortMessage.NOTE_ON, channel, pitch, velocity);
            volumeChange.setMessage(ShortMessage.CONTROL_CHANGE, channel, 7, (int) (volume * 127));
            reverb.setMessage(ShortMessage.CONTROL_CHANGE, channel, 91, 50);
            receiver.send(noteOn, -1);
            receiver.send(pitchBend, -1);
            receiver.send(volumeChange, -1);
            receiver.send(reverb, -1);
            scheduler.schedule(() -> {
                try {
                    ShortMessage off = new ShortMessage();
                    off.setMessage(ShortMessage.NOTE_OFF, channel, pitch, 0);
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

    public void startup(Sampler inSampler) {
        ready = false;
        sampler = inSampler;

        try {
            synth = MidiSystem.getSynthesizer();
            synth.open();
            synth.unloadAllInstruments(synth.getDefaultSoundbank());

            // Load the custom soundbank
            soundbank = MidiSystem.getSoundbank(sampler.sample);
            synth.loadAllInstruments(soundbank);

            receiver = synth.getReceiver();
            ready = true;
        } catch (InvalidMidiDataException | IOException | MidiUnavailableException e) {
                throw new RuntimeException(e);
        }
    }

    public void shutdown(){
        shutdown(true);
    }

    public void shutdown(boolean instant){
        sampler = null;
        synth.unloadAllInstruments(soundbank);
        if(!instant){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        soundbank = null;
        receiver.close();
        if(!instant){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        synth.close();
        if(!instant){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        synth = null;
        receiver = null;
        samplerReceiverPool.add(this);
        System.gc();
    }

    public static SamplerReceiver retrieveSamplerReceiver(Sampler inSampler){
        SamplerReceiver out;
        if(samplerReceiverPool.isEmpty()){
            out = new SamplerReceiver();
        }
        else{
            out = samplerReceiverPool.pop();
        }
        out.startup(inSampler);
        return out;
    }
}
