package com.provinceofmusic.jukebox;

import com.provinceofmusic.ProvinceOfMusicClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;

import javax.sound.midi.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.provinceofmusic.jukebox.NoteReplacer.interupt;

public class SamplerReceiver {
    public Sampler sampler = null;
    public Receiver receiver = null;
    public int elapsedTimeTillFree = 300;
    public long channel0LastPlayTime = 0;
    public long channel1LastPlayTime = 0;
    public long channel2LastPlayTime = 0;
    public long channel3LastPlayTime = 0;
    public long channel4LastPlayTime = 0;
    public long channel5LastPlayTime = 0;
    public long channel6LastPlayTime = 0;
    public long channel7LastPlayTime = 0;

    public long channel8LastPlayTime = 0;
    public long channel9LastPlayTime = 0;
    public long channel10LastPlayTime = 0;
    public long channel11LastPlayTime = 0;
    public long channel12LastPlayTime = 0;
    public long channel13LastPlayTime = 0;
    public long channel14LastPlayTime = 0;
    public long channel15LastPlayTime = 0;

    //TODO remove these 4
    public static ArrayList<SamplerReceiver> receiverQueue = new ArrayList<>();
    public static ArrayList<Integer> channelCancelQueue = new ArrayList<>();
    public static ArrayList<Integer> pitchCancelQueue = new ArrayList<>();
    public static ArrayList<Long> playTimeQueue = new ArrayList<>();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    //TODO remove this
    public static Thread cleanerThread = new Thread(){
        @Override
        public void run() {
            super.run();
            while(!disable){
                for(int i = 0; i < playTimeQueue.size(); i++){
                    if(!playTimeQueue.isEmpty() && System.currentTimeMillis() - playTimeQueue.get(i) > 200){
                        try {
                            ShortMessage noteOff = new ShortMessage();
                            noteOff.setMessage(ShortMessage.NOTE_OFF, channelCancelQueue.get(i), pitchCancelQueue.get(i), 0);
                            receiverQueue.get(i).receiver.send(noteOff, -1);
                            channelCancelQueue.remove(i);
                            pitchCancelQueue.remove(i);
                            playTimeQueue.remove(i);
                            receiverQueue.remove(i);
                            i--;
                        } catch (InvalidMidiDataException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    };

    public static boolean disable = false;

    public SamplerReceiver(Sampler inSampler){
        sampler = inSampler;
        try{
            //insFileName = insFile.getName();
            Synthesizer synth;
            //this.noteType = noteType;
            //this.transpose = transpose;
            //this.volume = volume;
            //this.singlePitch = singlePitch;
            synth = MidiSystem.getSynthesizer();
            synth.open();
            synth.unloadAllInstruments(synth.getDefaultSoundbank());

            // Load the custom soundbank
            Soundbank soundbank = MidiSystem.getSoundbank(sampler.sample);
            synth.loadAllInstruments(soundbank);

            receiver = synth.getReceiver();
            //System.out.println("receiver" + receiver + (receiver == null));


        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        } catch (InvalidMidiDataException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int tryGetFreeChannel(){
        //System.out.println("0" + channel0LastPlayTime + " " + (System.currentTimeMillis() - elapsedTimeTillFree) + " " + (channel0LastPlayTime < System.currentTimeMillis() - elapsedTimeTillFree));
        //System.out.println("1" + channel1LastPlayTime + " " + (System.currentTimeMillis() - elapsedTimeTillFree) + " " + (channel1LastPlayTime < System.currentTimeMillis() - elapsedTimeTillFree));
        //System.out.println("2" + channel2LastPlayTime + " " + (System.currentTimeMillis() - elapsedTimeTillFree) + " " + (channel2LastPlayTime < System.currentTimeMillis() - elapsedTimeTillFree));
        //System.out.println("3" + channel3LastPlayTime + " " + (System.currentTimeMillis() - elapsedTimeTillFree) + " " + (channel3LastPlayTime < System.currentTimeMillis() - elapsedTimeTillFree));
        //System.out.println("4" + channel4LastPlayTime + " " + (System.currentTimeMillis() - elapsedTimeTillFree) + " " + (channel4LastPlayTime < System.currentTimeMillis() - elapsedTimeTillFree));
        //System.out.println("5" + channel5LastPlayTime + " " + (System.currentTimeMillis() - elapsedTimeTillFree) + " " + (channel5LastPlayTime < System.currentTimeMillis() - elapsedTimeTillFree));
        //System.out.println("6" + channel6LastPlayTime + " " + (System.currentTimeMillis() - elapsedTimeTillFree) + " " + (channel6LastPlayTime < System.currentTimeMillis() - elapsedTimeTillFree));
        //System.out.println("7" + channel7LastPlayTime + " " + (System.currentTimeMillis() - elapsedTimeTillFree) + " " + (channel7LastPlayTime < System.currentTimeMillis() - elapsedTimeTillFree));
        //System.out.println(System.currentTimeMillis() - elapsedTimeTillFree);
        if(channel0LastPlayTime < System.currentTimeMillis() - elapsedTimeTillFree){
            //System.out.println("using channel 0");
            channel0LastPlayTime = System.currentTimeMillis();
            return 0;
        }
        if(channel1LastPlayTime < System.currentTimeMillis() - elapsedTimeTillFree){
            //System.out.println("using channel 1");
            channel1LastPlayTime = System.currentTimeMillis();
            return 1;
        }
        if(channel2LastPlayTime < System.currentTimeMillis() - elapsedTimeTillFree){
            //System.out.println("using channel 2");
            channel2LastPlayTime = System.currentTimeMillis();
            return 2;
        }
        if(channel3LastPlayTime < System.currentTimeMillis() - elapsedTimeTillFree){
            //System.out.println("using channel 3");
            channel3LastPlayTime = System.currentTimeMillis();
            return 3;
        }
        if(channel4LastPlayTime < System.currentTimeMillis() - elapsedTimeTillFree){
            //System.out.println("using channel 4");
            channel4LastPlayTime = System.currentTimeMillis();
            return 4;
        }
        if(channel5LastPlayTime < System.currentTimeMillis() - elapsedTimeTillFree){
            //System.out.println("using channel 5");
            channel5LastPlayTime = System.currentTimeMillis();
            return 5;
        }
        if(channel6LastPlayTime < System.currentTimeMillis() - elapsedTimeTillFree){
            //System.out.println("using channel 6");
            channel6LastPlayTime = System.currentTimeMillis();
            return 6;
        }
        if(channel7LastPlayTime < System.currentTimeMillis() - elapsedTimeTillFree){
            //System.out.println("using channel 7");
            channel7LastPlayTime = System.currentTimeMillis();
            return 7;
        }
        //ExtraChannels?
        if(channel8LastPlayTime < System.currentTimeMillis() - elapsedTimeTillFree){
            //System.out.println("using channel 7");
            channel8LastPlayTime = System.currentTimeMillis();
            return 8;
        }
        if(channel9LastPlayTime < System.currentTimeMillis() - elapsedTimeTillFree){
            //System.out.println("using channel 7");
            channel9LastPlayTime = System.currentTimeMillis();
            return 9;
        }
        if(channel10LastPlayTime < System.currentTimeMillis() - elapsedTimeTillFree){
            //System.out.println("using channel 7");
            channel10LastPlayTime = System.currentTimeMillis();
            return 10;
        }
        if(channel11LastPlayTime < System.currentTimeMillis() - elapsedTimeTillFree){
            //System.out.println("using channel 7");
            channel11LastPlayTime = System.currentTimeMillis();
            return 11;
        }
        if(channel12LastPlayTime < System.currentTimeMillis() - elapsedTimeTillFree){
            //System.out.println("using channel 7");
            channel12LastPlayTime = System.currentTimeMillis();
            return 8;
        }
        if(channel13LastPlayTime < System.currentTimeMillis() - elapsedTimeTillFree){
            //System.out.println("using channel 7");
            channel13LastPlayTime = System.currentTimeMillis();
            return 13;
        }
        if(channel14LastPlayTime < System.currentTimeMillis() - elapsedTimeTillFree){
            //System.out.println("using channel 7");
            channel14LastPlayTime = System.currentTimeMillis();
            return 14;
        }
        if(channel15LastPlayTime < System.currentTimeMillis() - elapsedTimeTillFree){
            //System.out.println("using channel 7");
            channel15LastPlayTime = System.currentTimeMillis();
            return 15;
        }


        //System.out.println("using channel -1");
        return -1;
    }

    public boolean playNote(float pitch, int volume, InstrumentDef instrumentDef, boolean override){
        int channel = tryGetFreeChannel();

        if(channel == -1){
            if(!override) {
                return false;
            }
            channel = (int)(Math.random() * 16);
        }

        //System.out.println("channel " + channel);

        float musicVolume = MinecraftClient.getInstance().options.getSoundVolume(SoundCategory.RECORDS) * MinecraftClient.getInstance().options.getSoundVolume(SoundCategory.MASTER);

        float clampedVolume = Math.max(0, Math.min(volume, 100));
        float weightedVolume = (float) (-1*(1/(100+2*clampedVolume)*Math.pow(clampedVolume-100,2))+100);

        final int newVolume = Math.round(weightedVolume);

        float newPitchBend = pitch - (int)pitch;
        //TODO might want to move this to the end so the time is more accurate
        /*
        switch(channel){
            case 0:
                channel0LastPlayTime = System.currentTimeMillis();
            case 1:
                channel1LastPlayTime = System.currentTimeMillis();
            case 2:
                channel2LastPlayTime = System.currentTimeMillis();
            case 3:
                channel3LastPlayTime = System.currentTimeMillis();
            case 4:
                channel4LastPlayTime = System.currentTimeMillis();
            case 5:
                channel5LastPlayTime = System.currentTimeMillis();
            case 6:
                channel6LastPlayTime = System.currentTimeMillis();
            case 7:
                channel7LastPlayTime = System.currentTimeMillis();
        }
        */
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
            return true; //TODO might want to change this
        }
        if(instrumentDef.volume < 0 || instrumentDef.volume > 1){
            ProvinceOfMusicClient.LOGGER.error("Note Volume out of Range (Your Volume value is too extreme of a value. Keep it between 0 and 1) Value: " + instrumentDef.volume + "Note Type: " + instrumentDef.noteType);
            return true;
        }
        //TODO testing
        if(receiver == null){
            ProvinceOfMusicClient.LOGGER.error("Could not use Instrument. File could be missing or corrupt. File: " + sampler.insFileName + "Note Type: " + instrumentDef.noteType);
            return true;
        }

        try {
            if(!interupt){ //TODO try and reremember what interupt does
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

                /*
                channelCancelQueue.add(channel);
                pitchCancelQueue.add(pitchAfterSinglePitch);
                playTimeQueue.add(System.currentTimeMillis());
                receiverQueue.add(this);
                 */
                int finalChannel = channel;
                scheduler.schedule(() -> {
                    try {
                        ShortMessage off = new ShortMessage();
                        off.setMessage(ShortMessage.NOTE_OFF, finalChannel, pitchAfterSinglePitch, 0);
                        receiver.send(off, -1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, 200, TimeUnit.MILLISECONDS);
            }
        } catch (InvalidMidiDataException e) {
            throw new RuntimeException(e);
        }

        return true;
    }
}
