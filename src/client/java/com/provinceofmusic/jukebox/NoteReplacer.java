package com.provinceofmusic.jukebox;

import com.provinceofmusic.ProvinceOfMusicClient;
import com.provinceofmusic.listeners.NoteListener;
import com.provinceofmusic.listeners.NoteListenerHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class NoteReplacer implements NoteListener {

    private static int time_passed = 0;
    //private static boolean is_writing_to_file = false;
    //private static String file_to_write;

    public static KeyBinding replaceNoteBinding;

    public static ArrayList<String> NoteTypes = new ArrayList<>();
    public static ArrayList<Integer> instrumentChannels = new ArrayList<>();
    public static ArrayList<Integer> instrumentPrograms = new ArrayList<>();
    public static ArrayList<Integer> instrumentPitchesToShift = new ArrayList<>();

    //public static Synthesizer synth;
    //public static Receiver receiver = null;
    
    
    public static ArrayList<Instrument> instruments = new ArrayList<>();
    
    
    

    //public static ArrayList<String> NoteChannelsQueue = new ArrayList<>();
    //public static ArrayList<Integer> NotePitchsQueue = new ArrayList<>();
    //public static ArrayList<Integer> NoteVelocitiesQueue = new ArrayList<>();


    public static boolean replaceMusic = false;

    public static float musicVolume = 0.5f;
    
    @Override
    public void onNotePlayed(String instrument, int ticksPassed, int pitch, int volume) {

        if (!replaceMusic) return;

        String noteType = instrument;
        float notePitch = pitch;
        float noteVolume = volume;

        int noteInstrument = -1;
        for(int j = 0; j < NoteTypes.size(); j++){
            if(NoteTypes.get(j).equals(noteType)){
                noteInstrument = j;
            }
        }
        if(noteInstrument == -1){
            System.out.println("Error");
            return;
        }


        //int insertNoteChannel = instrumentChannels.get(noteInstrument);

        //if(instrument.equals("minecraft:block.note_block.xylophone")){
        //    System.err.println("Match");
        //}
        //System.err.println(instrument);

        //NoteChannelsQueue.add(instrument);
        //NotePitchsQueue.add(pitch);
        //NoteVelocitiesQueue.add(volume);

        //if(time_passed != 0){
            playMusicFrame(instrument, pitch, volume);
        //}

        time_passed = 0;
    }

    public static void playMusicFrame(String instrument, int pitch, int volume){
        //ArrayList<String> channelsCopy = (ArrayList) instrument.clone();
        //ArrayList<Integer> pitchesCopy = (ArrayList) pitch.clone();
        //ArrayList<Integer> volumesCopy = (ArrayList) volume.clone();
        //NoteChannelsQueue.clear();
        //NotePitchsQueue.clear();
        //NoteVelocitiesQueue.clear();


        System.out.println("Note Played " + "Ins: " + instrument + " Pitch: " + pitch + " Volume: " + volume + " Time: " + Instant.now());

        final int newVolume;

        if(volume > 100){
            newVolume = 100;
        }
        else{
            newVolume = volume;
        }

        //Thread playThread = new Thread(() -> {
        //    try {
        //        ShortMessage noteOn = new ShortMessage();
        //        noteOn.setMessage(ShortMessage.NOTE_ON, 0, pitch, volume);
        //        for(int j = 0; j < instruments.size(); j++){
        //            if(instrument.equals(instruments.get(j).noteType)){
        //                noteOn.setMessage(ShortMessage.NOTE_ON, 0, pitch + instruments.get(j).transpose, (int) ((float)(volume) * musicVolume * instruments.get(j).volume));
        //                instruments.get(j).receiver.send(noteOn, -1);
        //            }
        //        }
        //        try {
        //            Thread.sleep(120);
        //        } catch (InterruptedException e) {
        //            throw new RuntimeException(e);
        //        }
        //        ShortMessage noteOff = new ShortMessage();
        //        noteOff.setMessage(ShortMessage.NOTE_OFF, 0, pitch, 0);
        //        for(int j = 0; j < instruments.size(); j++){
        //            if(instrument.equals(instruments.get(j).noteType)){
        //                noteOff.setMessage(ShortMessage.NOTE_ON, 0, pitch + instruments.get(j).transpose, 0);
        //                instruments.get(j).receiver.send(noteOff, -1);
        //            }
        //        }
//
        //    } catch (InvalidMidiDataException e) {
        //        throw new RuntimeException(e);
        //    }
        //});
        //playThread.start();


        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    ShortMessage noteOn = new ShortMessage();
                    noteOn.setMessage(ShortMessage.NOTE_ON, 0, pitch, newVolume);
                    for(int j = 0; j < instruments.size(); j++){
                        if(instrument.equals(instruments.get(j).noteType)){
                            noteOn.setMessage(ShortMessage.NOTE_ON, 0, pitch + instruments.get(j).transpose, (int) ((float)(newVolume) * musicVolume * instruments.get(j).volume));
                            instruments.get(j).receiver.send(noteOn, -1);
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
                    noteOff.setMessage(ShortMessage.NOTE_OFF, 0, pitch, 0);
                    for(int j = 0; j < instruments.size(); j++){
                        if(instrument.equals(instruments.get(j).noteType)){
                            noteOff.setMessage(ShortMessage.NOTE_ON, 0, pitch + instruments.get(j).transpose, 0);
                            instruments.get(j).receiver.send(noteOff, -1);
                        }
                    }

                } catch (InvalidMidiDataException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        //Timer timer = new Timer(true);
        timer.schedule(task2, 120);
    }

    public void PassTime(){
        if(replaceMusic){
            time_passed++;
        }
    }

    public static void main(){


        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (replaceNoteBinding.wasPressed()) {

                replaceMusic = !replaceMusic;
                if (replaceMusic) {
                    ProvinceOfMusicClient.LOGGER.info("Playing better music");
                    client.player.sendMessage(Text.of("Playing better music"), false);
                } else {
                    time_passed = 0;
                    ProvinceOfMusicClient.LOGGER.info("Playing original music");
                    client.player.sendMessage(Text.of("Playing original music"), false);

                }
            }
        });
    }

    public static float log2(float N)
    {

        // calculate log2 N indirectly
        // using log() method
        float result = (float) (Math.log(N) / Math.log(2));

        return result;
    }

    public void RunSetup(){
        //ArrayList<String> NoteTypes = new ArrayList<>();
        NoteTypes.add("minecraft:block.note_block.harp");
        NoteTypes.add("minecraft:block.note_block.bass");
        NoteTypes.add("minecraft:block.note_block.snare");
        NoteTypes.add("minecraft:block.note_block.hat");
        NoteTypes.add("minecraft:block.note_block.basedrum");
        NoteTypes.add("minecraft:block.note_block.bell");
        NoteTypes.add("minecraft:block.note_block.flute");
        NoteTypes.add("minecraft:block.note_block.chime");
        NoteTypes.add("minecra ft:block.note_block.guitar");
        NoteTypes.add("minecraft:block.note_block.xylophone");
        NoteTypes.add("minecraft:block.note_block.iron_xylophone");
        NoteTypes.add("minecraft:block.note_block.cow_bell");
        NoteTypes.add("minecraft:block.note_block.didgeridoo");
        NoteTypes.add("minecraft:block.note_block.bit");
        NoteTypes.add("minecraft:block.note_block.banjo");
        NoteTypes.add("minecraft:block.note_block.pling");

        //ArrayList<Integer> instrumentChannels = new ArrayList<>();
        instrumentChannels.add(0);
        instrumentChannels.add(2);
        instrumentChannels.add(9);
        instrumentChannels.add(9);
        instrumentChannels.add(9);
        instrumentChannels.add(3);
        instrumentChannels.add(4);
        instrumentChannels.add(5);
        instrumentChannels.add(6);
        instrumentChannels.add(7);
        instrumentChannels.add(8);
        instrumentChannels.add(11);
        instrumentChannels.add(12);
        instrumentChannels.add(13);
        instrumentChannels.add(14);
        instrumentChannels.add(15);

        //ArrayList<Integer> instrumentPrograms = new ArrayList<>();

        instrumentPrograms.add(6);
        instrumentPrograms.add(32);
        instrumentPrograms.add(38);
        instrumentPrograms.add(42);
        instrumentPrograms.add(35);
        instrumentPrograms.add(14);
        instrumentPrograms.add(73);
        instrumentPrograms.add(112);
        instrumentPrograms.add(24);
        instrumentPrograms.add(12);
        instrumentPrograms.add(11);
        instrumentPrograms.add(113);
        instrumentPrograms.add(111);
        instrumentPrograms.add(80);
        instrumentPrograms.add(105);
        instrumentPrograms.add(4);

        //ArrayList<Integer> instrumentPitchesToShift = new ArrayList<>();

        instrumentPitchesToShift.add(0);
        instrumentPitchesToShift.add(-24);
        instrumentPitchesToShift.add(0);
        instrumentPitchesToShift.add(0);
        instrumentPitchesToShift.add(0);
        instrumentPitchesToShift.add(24);
        instrumentPitchesToShift.add(12);
        instrumentPitchesToShift.add(24);
        instrumentPitchesToShift.add(-12);
        instrumentPitchesToShift.add(24);
        instrumentPitchesToShift.add(0);
        instrumentPitchesToShift.add(12);
        instrumentPitchesToShift.add(-24);
        instrumentPitchesToShift.add(0);
        instrumentPitchesToShift.add(0);
        instrumentPitchesToShift.add(0);


        //"harp": [0, 6, 0],
        //"bass": [2, 32, -24], #MIDI visualizer matches channel 9 to channel 1 and I need bass to be separate from percussion
        //"snare": [9, 38 if not use_visual_percussion else 26, 0], #sadly we can't convert pitch for percussion (channel 9) NoteTypes,
        //"hat": [9, 42 if not use_visual_percussion else 28, 0], #because it is needed for different percussion NoteTypes
        //"basedrum": [9, 35 if not use_visual_percussion else 24, 0],
        //#        "bell": [3, 14, 24], #uncomment these if you want to record music with these NoteTypes too (Wynncraft doesn't have these)
        //#        "flute": [4, 73, 12],
        //#        "chime": [5, 112, 24],
        //#        "guitar": [6, 24, -12],
        //#        "xylophone": [7, 12, 24],
        //#        "iron_xylophone": [8, 11, 0],
        //#        "cow_bell": [11, 113, 12], #mapped to agogo because cowbell is a percussion instrument
        //#        "didgeridoo": [12, 111, -24], #mapped to shehnai because there's no didgeridoo
        //#        "bit": [13, 80, 0],
        //#        "banjo": [14, 105, 0],
        //#        "pling": [15, 4, 0],









        //instruments.add(new Instrument(new File("1st-violin-SEC-staccato.sf2"), "minecraft:block.note_block.harp", 0, 0.75f));
        //instruments.add(new Instrument(new File("2nd-violin-SEC-staccato.sf2"), "minecraft:block.note_block.harp", 0, 0.75f));
        //instruments.add(new Instrument(new File("cello-SEC-accent.sf2"), "minecraft:block.note_block.harp", 0 , 0.75f));
        //instruments.add(new Instrument(new File("cello-SEC-staccato.sf2"), "minecraft:block.note_block.harp"));
        //instruments.add(new Instrument(new File("viola-SEC-staccato.sf2"), "minecraft:block.note_block.harp", 0, 0.75f));
        //instruments.add(new Instrument(new File("BRIGHT GRAND.sf2"), "minecraft:block.note_block.harp"));
        //instruments.add(new Instrument(new File("BRIGHT GRAND.sf2"), "minecraft:block.note_block.harp", 0, 0.75f));
        //instruments.add(new Instrument(new File("BRIGHT GRAND.sf2"), "minecraft:block.note_block.harp", 0, 0.75f));
        //instruments.add(new Instrument(new File("BRIGHT GRAND.sf2"), "minecraft:block.note_block.harp", -12));
        //instruments.add(new Instrument(new File("BRIGHT GRAND.sf2"), "minecraft:block.note_block.harp", 12, 0.75f));
        //instruments.add(new Instrument(new File("BRIGHT GRAND.sf2"), "minecraft:block.note_block.harp", -12, 0.75f));

        //instruments.add(new Instrument(new File("firelyre.sf2"), "minecraft:block.note_block.harp"));
        //instruments.add(new Instrument(new File("SuperHarp.sf2"), "minecraft:block.note_block.harp"));


        //instruments.add(new Instrument(new File("bass-SEC-staccato.sf2"), "minecraft:block.note_block.bass"));
        //instruments.add(new Instrument(new File("bass-SEC-staccato.sf2"), "minecraft:block.note_block.bass"));
        //instruments.add(new Instrument(new File("bass-SEC-staccato.sf2"), "minecraft:block.note_block.bass"));
        //instruments.add(new Instrument(new File("bass-SEC-staccato.sf2"), "minecraft:block.note_block.bass"));
        //instruments.add(new Instrument(new File("bass-SEC-staccato.sf2"), "minecraft:block.note_block.bass"));
        //instruments.add(new Instrument(new File("bass-SEC-staccato.sf2"), "minecraft:block.note_block.bass"));
        //instruments.add(new Instrument(new File("bass-SEC-staccato.sf2"), "minecraft:block.note_block.bass"));


        //instruments.add(new Instrument(new File("SnareDrum.sf2"), "minecraft:block.note_block.snare"));
        //instruments.add(new Instrument(new File("KickDrum.sf2"), "minecraft:block.note_block.basedrum"));
        //instruments.add(new Instrument(new File("SnareDrum.sf2"), "minecraft:block.note_block.snare"));
        //instruments.add(new Instrument(new File("KickDrum.sf2"), "minecraft:block.note_block.basedrum"));
        //instruments.add(new Instrument(new File("SnareDrum.sf2"), "minecraft:block.note_block.snare"));
        //instruments.add(new Instrument(new File("HatDrum.sf2"), "minecraft:block.note_block.hat"));
        //instruments.add(new Instrument(new File("KickDrum.sf2"), "minecraft:block.note_block.basedrum"));
        //instruments.add(new Instrument(new File("SnareDrum.sf2"), "minecraft:block.note_block.snare"));
        //instruments.add(new Instrument(new File("HatDrum.sf2"), "minecraft:block.note_block.hat"));
        //instruments.add(new Instrument(new File("KickDrum.sf2"), "minecraft:block.note_block.basedrum"));
        //instruments.add(new Instrument(new File("SnareDrum.sf2"), "minecraft:block.note_block.snare"));
        //instruments.add(new Instrument(new File("HatDrum.sf2"), "minecraft:block.note_block.hat"));
        //instruments.add(new Instrument(new File("KickDrum.sf2"), "minecraft:block.note_block.basedrum"));
        //instruments.add(new Instrument(new File("SnareDrum.sf2"), "minecraft:block.note_block.snare"));
        //instruments.add(new Instrument(new File("HatDrum.sf2"), "minecraft:block.note_block.hat"));
        //instruments.add(new Instrument(new File("KickDrum.sf2"), "minecraft:block.note_block.basedrum"));
        //instruments.add(new Instrument(new File("KickDrum.sf2"), "minecraft:block.note_block.basedrum"));


        //SamplePack samplePackTest = new SamplePack();
        //samplePackTest.name = "testPack";
        //samplePackTest.author = "TheAngryGhost";
        //samplePackTest.instrumentDefs = new ArrayList<>();
        //samplePackTest.instrumentDefs.add(new InstrumentDef("1st-violin-SEC-staccato.sf2", "minecraft:block.note_block.harp", 0, 0.75f));
        //samplePackTest.WriteSamplePack();


        if(ProvinceOfMusicClient.configSettings.activeSamplePack != null){
            instruments = SamplePack.getSamplePack(SamplePack.getFile(ProvinceOfMusicClient.configSettings.activeSamplePack)).getInstruments();
        }


        //instruments.add(new Instrument(new File("SnareDrum.sf2"), "minecraft:block.note_block.snare"));
        //instruments.add(new Instrument(new File("HatDrum.sf2"), "minecraft:block.note_block.hat"));
        //instruments.add(new Instrument(new File("SnareDrum.sf2"), "minecraft:block.note_block.snare"));
        //instruments.add(new Instrument(new File("HatDrum.sf2"), "minecraft:block.note_block.hat"));
        //instruments.add(new Instrument(new File("SnareDrum.sf2"), "minecraft:block.note_block.snare"));
        //instruments.add(new Instrument(new File("HatDrum.sf2"), "minecraft:block.note_block.hat"));
        //instruments.add(new Instrument(new File("KickDrum.sf2"), "minecraft:block.note_block.basedrum"));
        //instruments.add(new Instrument(new File("SnareDrum.sf2"), "minecraft:block.note_block.snare"));
        //instruments.add(new Instrument(new File("SnareDrum.sf2"), "minecraft:block.note_block.snare"));
        //instruments.add(new Instrument(new File("SnareDrum.sf2"), "minecraft:block.note_block.snare"));
        //instruments.add(new Instrument(new File("SnareDrum.sf2"), "minecraft:block.note_block.snare"));
        //instruments.add(new Instrument(new File("SnareDrum.sf2"), "minecraft:block.note_block.snare"));
        //instruments.add(new Instrument(new File("KickDrum.sf2"), "minecraft:block.note_block.basedrum"));
        //instruments.add(new Instrument(new File("KickDrum.sf2"), "minecraft:block.note_block.basedrum"));
        //instruments.add(new Instrument(new File("KickDrum.sf2"), "minecraft:block.note_block.basedrum"));
//
//
        //instruments.add(new Instrument(new File("SnareDrum.sf2"), "minecraft:block.note_block.snare"));
        //instruments.add(new Instrument(new File("HatDrum.sf2"), "minecraft:block.note_block.hat"));
        //instruments.add(new Instrument(new File("KickDrum.sf2"), "minecraft:block.note_block.basedrum"));
        //instruments.add(new Instrument(new File("SnareDrum.sf2"), "minecraft:block.note_block.snare"));
        //instruments.add(new Instrument(new File("HatDrum.sf2"), "minecraft:block.note_block.hat"));
        //instruments.add(new Instrument(new File("KickDrum.sf2"), "minecraft:block.note_block.basedrum"));
        //instruments.add(new Instrument(new File("SnareDrum.sf2"), "minecraft:block.note_block.snare"));
        //instruments.add(new Instrument(new File("HatDrum.sf2"), "minecraft:block.note_block.hat"));
        //instruments.add(new Instrument(new File("KickDrum.sf2"), "minecraft:block.note_block.basedrum"));
        //instruments.add(new Instrument(new File("SnareDrum.sf2"), "minecraft:block.note_block.snare"));
        //instruments.add(new Instrument(new File("KickDrum.sf2"), "minecraft:block.note_block.basedrum"));
        //instruments.add(new Instrument(new File("KickDrum.sf2"), "minecraft:block.note_block.basedrum"));
        //instruments.add(new Instrument(new File("KickDrum.sf2"), "minecraft:block.note_block.basedrum"));



        //instruments.add(new Instrument(new File("HatDrum.sf2"), "minecraft:block.note_block.hat"));
        //instruments.add(new Instrument(new File("HatDrum.sf2"), "minecraft:block.note_block.hat"));
        //instruments.add(new Instrument(new File("HatDrum.sf2"), "minecraft:block.note_block.hat"));
        //instruments.add(new Instrument(new File("HatDrum.sf2"), "minecraft:block.note_block.hat"));
        //instruments.add(new Instrument(new File("HatDrum.sf2"), "minecraft:block.note_block.hat"));
        //instruments.add(new Instrument(new File("HatDrum.sf2"), "minecraft:block.note_block.hat"));
        //instruments.add(new Instrument(new File("HatDrum.sf2"), "minecraft:block.note_block.hat"));
        //instruments.add(new Instrument(new File("HatDrum.sf2"), "minecraft:block.note_block.hat"));
        //instruments.add(new Instrument(new File("HatDrum.sf2"), "minecraft:block.note_block.hat"));
//
//
//
//
//
//
        //instruments.add(new Instrument(new File("flute-SEC-staccato.sf2"), "minecraft:block.note_block.bell"));
        //instruments.add(new Instrument(new File("flute-SEC-staccato.sf2"), "minecraft:block.note_block.bell"));
        //instruments.add(new Instrument(new File("piccolo-SOLO-staccato.sf2"), "minecraft:block.note_block.bell"));
        //instruments.add(new Instrument(new File("piccolo-SOLO-staccato.sf2"), "minecraft:block.note_block.bell"));
        //instruments.add(new Instrument(new File("flute-SEC-staccato.sf2"), "minecraft:block.note_block.bell"));
        //instruments.add(new Instrument(new File("flute-SEC-staccato.sf2"), "minecraft:block.note_block.bell"));
        //instruments.add(new Instrument(new File("piccolo-SOLO-staccato.sf2"), "minecraft:block.note_block.bell"));
        //instruments.add(new Instrument(new File("piccolo-SOLO-staccato.sf2"), "minecraft:block.note_block.bell"));
        //instruments.add(new Instrument(new File("flute-SEC-staccato.sf2"), "minecraft:block.note_block.bell"));
        //instruments.add(new Instrument(new File("flute-SEC-staccato.sf2"), "minecraft:block.note_block.bell"));
        //instruments.add(new Instrument(new File("piccolo-SOLO-staccato.sf2"), "minecraft:block.note_block.bell"));
        //instruments.add(new Instrument(new File("piccolo-SOLO-staccato.sf2"), "minecraft:block.note_block.bell"));
        //instruments.add(new Instrument(new File("flute-SEC-staccato.sf2"), "minecraft:block.note_block.bell"));
        //instruments.add(new Instrument(new File("flute-SEC-staccato.sf2"), "minecraft:block.note_block.bell"));
//
        //instruments.add(new Instrument(new File("SpanishClassicalGuitar.sf2"), "minecraft:block.note_block.guitar", -12));
        //instruments.add(new Instrument(new File("SpanishClassicalGuitar.sf2"), "minecraft:block.note_block.guitar", -12));
        //instruments.add(new Instrument(new File("SpanishClassicalGuitar.sf2"), "minecraft:block.note_block.guitar", -12));
        //instruments.add(new Instrument(new File("SpanishClassicalGuitar.sf2"), "minecraft:block.note_block.guitar", -12));
        //instruments.add(new Instrument(new File("SpanishClassicalGuitar.sf2"), "minecraft:block.note_block.guitar", -12));
        //instruments.add(new Instrument(new File("SpanishClassicalGuitar.sf2"), "minecraft:block.note_block.guitar", -12));
        //instruments.add(new Instrument(new File("SpanishClassicalGuitar.sf2"), "minecraft:block.note_block.guitar", -12));
        //instruments.add(new Instrument(new File("SpanishClassicalGuitar.sf2"), "minecraft:block.note_block.guitar", -12));
        //instruments.add(new Instrument(new File("SpanishClassicalGuitar.sf2"), "minecraft:block.note_block.guitar", -12));
        //instruments.add(new Instrument(new File("SpanishClassicalGuitar.sf2"), "minecraft:block.note_block.guitar", -12));
        //instruments.add(new Instrument(new File("SpanishClassicalGuitar.sf2"), "minecraft:block.note_block.guitar", -12));
        //instruments.add(new Instrument(new File("SpanishClassicalGuitar.sf2"), "minecraft:block.note_block.guitar", -12));
//
//
        //instruments.add(new Instrument(new File("celesta.sf2"), "minecraft:block.note_block.chime"));
        //instruments.add(new Instrument(new File("Mallets_GM.sf2"), "minecraft:block.note_block.chime"));
        //instruments.add(new Instrument(new File("Mallets_GM.sf2"), "minecraft:block.note_block.chime"));
        //instruments.add(new Instrument(new File("BRIGHT GRAND.sf2"), "minecraft:block.note_block.chime"));
        //instruments.add(new Instrument(new File("celesta.sf2"), "minecraft:block.note_block.chime"));
        //instruments.add(new Instrument(new File("Mallets_GM.sf2"), "minecraft:block.note_block.chime"));
        //instruments.add(new Instrument(new File("Mallets_GM.sf2"), "minecraft:block.note_block.chime"));
        //instruments.add(new Instrument(new File("BRIGHT GRAND.sf2"), "minecraft:block.note_block.chime"));
        //instruments.add(new Instrument(new File("celesta.sf2"), "minecraft:block.note_block.chime"));
        //instruments.add(new Instrument(new File("Mallets_GM.sf2"), "minecraft:block.note_block.chime"));
        //instruments.add(new Instrument(new File("Mallets_GM.sf2"), "minecraft:block.note_block.chime"));
        //instruments.add(new Instrument(new File("BRIGHT GRAND.sf2"), "minecraft:block.note_block.chime"));
        //instruments.add(new Instrument(new File("celesta.sf2"), "minecraft:block.note_block.chime"));
        //instruments.add(new Instrument(new File("Mallets_GM.sf2"), "minecraft:block.note_block.chime"));
        //instruments.add(new Instrument(new File("Mallets_GM.sf2"), "minecraft:block.note_block.chime"));
        //instruments.add(new Instrument(new File("BRIGHT GRAND.sf2"), "minecraft:block.note_block.chime"));
        

        //try {
        //    synth = MidiSystem.getSynthesizer();
        //    synth.open();
        //    synth.unloadAllInstruments(synth.getDefaultSoundbank());
//
        //    // Load the custom soundbank
        //    Soundbank soundbank = MidiSystem.getSoundbank(new File("1st-violin-SEC-accent.sf2"));
        //    synth.loadAllInstruments(soundbank);
//
        //    receiver = synth.getReceiver();
        //} catch (MidiUnavailableException e) {
        //    e.printStackTrace();
        //} catch (InvalidMidiDataException e) {
        //    throw new RuntimeException(e);
        //} catch (IOException e) {
        //    throw new RuntimeException(e);
        //}

        NoteListenerHelper.addListener(this);

        //PlayNotesThread();
    }
}
