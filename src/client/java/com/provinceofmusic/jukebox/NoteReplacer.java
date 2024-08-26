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

    public static void playMusicFrame(InstrumentSound instrument, float pitch, int volume){

        //System.out.println("Note Played " + "Ins: " + instrument.registeredName + " Pitch: " + pitch + " Volume: " + volume + " Time: " + Instant.now());

        final int newVolume = Math.min(volume, 100);


        int newPitch = (int)pitch;
        float newPitchBend = pitch - newPitch;

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    if(!interupt){
                        ShortMessage noteOn = new ShortMessage();
                        noteOn.setMessage(ShortMessage.NOTE_ON, 0, newPitch, newVolume);

                        // Pitch bend value for one semitone up
                        int pitchBendValue = (int)(8192 + 4096 * newPitchBend); // Center (8192) + One semitone (4096)
                        int lsb = pitchBendValue & 0x7F; // Least significant 7 bits
                        int msb = (pitchBendValue >> 7) & 0x7F; // Most significant 7 bits

                        ShortMessage pitchBend = new ShortMessage();
                        pitchBend.setMessage(ShortMessage.PITCH_BEND, 0, lsb, msb);
                        for (Instrument tempInstrument : instruments) {
                            if (instrument.registeredName.equals(tempInstrument.noteType)) {
                                if (tempInstrument.singlePitch) {
                                    noteOn.setMessage(ShortMessage.NOTE_ON, 0, 60 + tempInstrument.transpose, (int) ((float) (newVolume) * ((Math.log10(0.9 * musicVolume + 0.1)) + 1) * tempInstrument.volume));
                                } else {
                                    noteOn.setMessage(ShortMessage.NOTE_ON, 0, newPitch + tempInstrument.transpose, (int) ((float) (newVolume) * ((Math.log10(0.9 * musicVolume + 0.1)) + 1) * tempInstrument.volume));
                                }
                                tempInstrument.receiver.send(noteOn, -1);
                                tempInstrument.receiver.send(pitchBend, -1);
                            }
                        }
                    }

                } catch (InvalidMidiDataException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Timer timer = new Timer(true);
        timer.schedule(task, 0);


        TimerTask task2 = new TimerTask() {
            @Override
            public void run() {
                try {
                    ShortMessage noteOff = new ShortMessage();
                    noteOff.setMessage(ShortMessage.NOTE_OFF, 0, newPitch, 0);
                    for (Instrument tempInstrument : instruments) {
                        if (tempInstrument.singlePitch) {
                            if (instrument.registeredName.equals(tempInstrument.noteType)) {
                                noteOff.setMessage(ShortMessage.NOTE_ON, 0, 60 + tempInstrument.transpose, 0);
                            }
                        } else {
                            if (instrument.registeredName.equals(tempInstrument.noteType)) {
                                noteOff.setMessage(ShortMessage.NOTE_ON, 0, newPitch + tempInstrument.transpose, 0);
                            }
                        }
                        tempInstrument.receiver.send(noteOff, -1);
                    }

                } catch (InvalidMidiDataException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        timer.schedule(task2, 200);
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
            System.out.println("Trying to load SamplePack");
            instruments = null;
            SamplePack pack = SamplePack.getSamplePack(SamplePack.getFile(ProvinceOfMusicClient.configSettings.activeSamplePack));
            instruments = pack.getInstruments(instruments);
            System.out.println("Loaded Successfully");

        }

        musicVolume = ProvinceOfMusicClient.configSettings.volume;

        NoteListenerHelper.addListener(this);
    }
}
