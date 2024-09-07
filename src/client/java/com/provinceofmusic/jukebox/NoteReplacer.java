package com.provinceofmusic.jukebox;

import com.provinceofmusic.ProvinceOfMusicClient;
import com.provinceofmusic.listeners.NoteListener;
import com.provinceofmusic.listeners.NoteListenerHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class NoteReplacer implements NoteListener {

    public static KeyBinding replaceNoteBinding;
    
    
    public static ArrayList<Instrument> instruments = new ArrayList<>();
    public static boolean replaceMusic = true;

    public static float musicVolume = 0.5f;

    public static boolean interupt = false;
    
    @Override
    public void onNotePlayed(InstrumentSound instrument, int ticksPassed, float pitch, int volume) {
        if (!replaceMusic) return;

        playMusicFrame(instrument, pitch, volume);

    }

    public static void playMusicFrame(InstrumentSound instrumentSound, float pitch, int volume){
        //this function is written like this to produce the least amount of TimerTasks possible

        ArrayList<Instrument> instrumentCache = new ArrayList<>();
        ArrayList<Integer> channelCache = new ArrayList<>();

        if(!interupt){
            for (Instrument tempInstrument : instruments) {
                if (instrumentSound.registeredName.equals(tempInstrument.noteType)) {
                    instrumentCache.add(tempInstrument);

                    final int newVolume = Math.max(0, Math.min(volume, 100));

                    float newPitchBend = pitch - (int)pitch;

                    int channel = tempInstrument.channel;
                    channelCache.add(channel);

                    int pitchBendValue = (int)(8192 + 4096 * newPitchBend); // Center (8192) + One semitone (4096)
                    int lsb = pitchBendValue & 0x7F; // Least significant 7 bits
                    int msb = (pitchBendValue >> 7) & 0x7F; // Most significant 7 bits

                    int pitchAfterSinglePitch;
                    if (tempInstrument.singlePitch) {
                        pitchAfterSinglePitch = 60 + tempInstrument.transpose;
                    }
                    else{
                        pitchAfterSinglePitch = (int)pitch + tempInstrument.transpose;
                    }
                    if(pitchAfterSinglePitch < 0 || pitchAfterSinglePitch > 127){
                        ProvinceOfMusicClient.LOGGER.error("Note Pitch out of Range (Your transpose value is too extreme of a value. If not using single pitch : originalPitch + transpose is > 127 or < 0. If using single pitch : 60 + transpose is > 127 or < 0) Value: " + tempInstrument.transpose + "Note Type: " + tempInstrument.noteType);
                        return;
                    }
                    if(tempInstrument.volume < 0 || tempInstrument.volume > 1){
                        ProvinceOfMusicClient.LOGGER.error("Note Volume out of Range (Your Volume value is too extreme of a value. Keep it between 0 and 1) Value: " + tempInstrument.volume + "Note Type: " + tempInstrument.noteType);
                        return;
                    }


                    try {
                        if(!interupt){
                            ShortMessage noteOn = new ShortMessage();
                            // Pitch bend value for one semitone up
                            ShortMessage pitchBend = new ShortMessage();
                            pitchBend.setMessage(ShortMessage.PITCH_BEND, 0, lsb, msb);
                            noteOn.setMessage(ShortMessage.NOTE_ON, channel, pitchAfterSinglePitch, (int) ((float) (newVolume) * ((Math.log10(0.9 * musicVolume + 0.1)) + 1) * tempInstrument.volume));
                            tempInstrument.receiver.send(noteOn, -1);
                            tempInstrument.receiver.send(pitchBend, -1);
                        }
                    } catch (InvalidMidiDataException e) {
                        throw new RuntimeException(e);
                    }
                    tempInstrument.incrementChannel();
                }
            }


            Timer timer = new Timer(true);
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    try {
                        for (int i = 0; i < instrumentCache.size(); i++) {
                            Instrument instrument = instrumentCache.get(i);
                            int channel = channelCache.get(i);
                            int pitchAfterSinglePitch;
                            if (instrument.singlePitch) {
                                pitchAfterSinglePitch = 60 + instrument.transpose;
                            } else {
                                pitchAfterSinglePitch = (int) pitch + instrument.transpose;
                            }
                            ShortMessage noteOff = new ShortMessage();
                            noteOff.setMessage(ShortMessage.NOTE_OFF, channel, pitchAfterSinglePitch, 0);
                            instrument.receiver.send(noteOff, -1);
                        }

                    } catch (InvalidMidiDataException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
            timer.schedule(task, 200);

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
                    ProvinceOfMusicClient.LOGGER.info("Playing better music");
                    client.player.sendMessage(Text.of("Playing better music"), false);
                } else {
                    ProvinceOfMusicClient.LOGGER.info("Playing original music");
                    client.player.sendMessage(Text.of("Playing original music"), false);

                }
            }
        });
    }

    public void RunSetup(){

        if(ProvinceOfMusicClient.configSettings.activeSamplePack != null){
            ProvinceOfMusicClient.LOGGER.debug("Trying to load SamplePack");
            instruments = null;
            if(ProvinceOfMusicClient.configSettings.activeSamplePack != null && SamplePack.getFile(ProvinceOfMusicClient.configSettings.activeSamplePack).exists()) {
                SamplePack pack = SamplePack.getSamplePack(SamplePack.getFile(ProvinceOfMusicClient.configSettings.activeSamplePack));
                instruments = pack.getInstruments(instruments);
                ProvinceOfMusicClient.LOGGER.debug("Loaded SamplePack successfully");
            }
            else{
                ProvinceOfMusicClient.configSettings.activeSamplePack = null;
                ProvinceOfMusicClient.saveConfigSettings();
                ProvinceOfMusicClient.LOGGER.warn("SamplePack load failed (Replacing active SamplePack with no SamplePack)");
            }


        }

        musicVolume = ProvinceOfMusicClient.configSettings.volume;

        NoteListenerHelper.addListener(this);
    }
}
