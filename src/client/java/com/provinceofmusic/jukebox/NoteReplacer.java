package com.provinceofmusic.jukebox;

import com.provinceofmusic.ProvinceOfMusicClient;
import com.provinceofmusic.listeners.NoteListener;
import com.provinceofmusic.listeners.NoteListenerHelper;
import com.provinceofmusic.mixin.client.NoteblockNoteIntercept;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundInstanceListener;
import net.minecraft.client.sound.WeightedSoundSet;
import net.minecraft.text.Text;

import javax.sound.midi.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class NoteReplacer implements SoundInstanceListener {

    private static int time_passed = 0;
    private static boolean is_writing_to_file = false;
    private static String file_to_write;

    public static KeyBinding replaceNoteBinding;

    static ArrayList<String> instruments = new ArrayList<>();
    static ArrayList<Integer> instrumentChannels = new ArrayList<>();

    static ArrayList<Integer> instrumentPrograms = new ArrayList<>();

    static ArrayList<Integer> instrumentPitchesToShift = new ArrayList<>();

    public static Synthesizer synth;
    public static Receiver receiver = null;

    public static ArrayList<Integer> NoteChannelsQueue = new ArrayList<>();
    public static ArrayList<Integer> NotePitchsQueue = new ArrayList<>();
    public static ArrayList<Integer> NoteVelocitiesQueue = new ArrayList<>();



    public static ArrayList<Integer> NoteChannelsStopQueue = new ArrayList<>();
    public static ArrayList<Integer> NotePitchsStopQueue = new ArrayList<>();
    public static ArrayList<Integer> NoteVelocitiesStopQueue = new ArrayList<>();


    public static boolean Clear = false;


    int prevSize = 0;
    @Override
    public void onSoundPlayed(SoundInstance sound, WeightedSoundSet soundSet) {
        //System.out.println(sound.getId());

        //if(sound.getId().toString().equals("minecraft:block.note_block.harp")){
        //    System.out.println(sound.getPitch());
        //}
        //sound.getSound().getIdentifier()

        //System.out.println("Working");

        String sound_category = sound.getCategory().getName().toLowerCase();
        //LOGGER.info("Sound played with category " + sound_category + ".");
        // test if the sound is of record category and if we're recording
        if (!sound_category.equals("record") || !is_writing_to_file) return;
        String[] split_up_name = sound.getId().toString().split("[.]");
        //we don't want to have "block.note.harp", only "harp" for backwards compatibility and easier readability
        //but we need the whole "entity.silverfish.hurt" e.g. for the other, non-noteblock sounds, that are part of the piece

        //if(sound.getId().toString().equals("minecraft:block.note_block.harp")){
        //    ProvinceOfMusicClient.LOGGER.info("Match!!!!!");
        //}
        //else {
        //    ProvinceOfMusicClient.LOGGER.info(sound.getId().toString() + " = " + "minecraft:block.note_block.harp");
        //}

        String name = (split_up_name[0].equals("block") && split_up_name[1].equals("note")) ? split_up_name[2] : sound.getId().toString();
        float pitch = sound.getPitch();
        float volume = sound.getVolume();


        String noteType = name;
        int noteTick = Integer.valueOf(time_passed);
        float notePitch = Float.valueOf(pitch);
        float noteVolume = Float.valueOf(volume);

        int noteInstrument = -1;
        for(int j = 0; j < instruments.size(); j++){
            if(instruments.get(j).equals(noteType)){
                noteInstrument = j;
            }
        }
        if(noteInstrument == -1){
            System.out.println("Error");
        }

        //currentTick += noteTick;

        int insertNoteChannel = instrumentChannels.get(noteInstrument);
        int insertNotePitch = (Math.round((log2(notePitch) * 12) + 66.5f) - 1) + instrumentPitchesToShift.get(noteInstrument);
        int insertNoteVelocity =  (int) (noteVolume * 100f);
        //int insertNoteTick = currentTick * (240 / 6);
        int insertNoteDuration = 120;

        NoteChannelsQueue.add(insertNoteChannel);
        NotePitchsQueue.add(insertNotePitch);
        NoteVelocitiesQueue.add(insertNoteVelocity);

        //currentTick += noteTick;

        //javaSimpleSynth synth = new javaSimpleSynth();

        //try {
        //    //synth.PlayNote(insertNoteChannel, insertNotePitch, insertNoteVelocity);
        //} catch (MidiUnavailableException e) {
        //    throw new RuntimeException(e);
        //} catch (InvalidMidiDataException e) {
        //    throw new RuntimeException(e);
        //} catch (IOException e) {
        //    throw new RuntimeException(e);
        //} catch (InterruptedException e) {
        //    throw new RuntimeException(e);
        //}


        //ShortMessage noteOn = new ShortMessage();
        //try {
        //    noteOn.setMessage(ShortMessage.NOTE_ON, insertNoteChannel, insertNotePitch, insertNoteVelocity);
        //} catch (InvalidMidiDataException e) {
        //    throw new RuntimeException(e);
        //}

        //track.add(new MidiEvent(noteOn, 0));
//
// Note-//off event (after the desired duration)
        //ShortMessage noteOff = new ShortMessage();
        //try {
        //    noteOff.setMessage(ShortMessage.NOTE_OFF, insertNoteChannel, insertNotePitch, 0);
        //} catch (InvalidMidiDataException e) {
        //    throw new RuntimeException(e);
        //}

        //Receiver receiver = null;
        //try {
       //     receiver = synth.getReceiver();
        //} catch (MidiUnavailableException e) {
       //     throw new RuntimeException(e);
        //}


        //receiver.send(noteOn, -1);
        //try {
        //    Thread.sleep(100);
        //} catch (InterruptedException e) {
        //    throw new RuntimeException(e);
        //}
        //receiver.send(noteOff, -1);


        //noteTrack.insertNote(insertNoteChannel, insertNotePitch, insertNoteVelocity, insertNoteTick, insertNoteDuration);








        //LOGGER.info("Recording " + name + " at time " + time_passed + " with pitch " + pitch + " and volume " + volume + ".");
        //try {
        //    // we're opening and closing the file every time we add a line to it, performance could be improved maybe
        //    FileWriter myWriter = new FileWriter(file_to_write, true);
        //    myWriter.append(name + "," + time_passed + "," + pitch + "," + volume + "\n");
        //    myWriter.close();
        //} catch (IOException e) {
        //    ProvinceOfMusicClient.LOGGER.error("Error writing to file " + file_to_write + ".");
        //    e.printStackTrace();
        //}
        time_passed = 0;
    }

    public static void PlayNotesThread(){
       Thread playThread = new Thread(() -> {
           while (true){
               while(NoteChannelsQueue.size() != 0){
                   ShortMessage noteOn = new ShortMessage();
                   try {
                       noteOn.setMessage(ShortMessage.NOTE_ON, NoteChannelsQueue.get(0), NotePitchsQueue.get(0), NoteVelocitiesQueue.get(0));
                   } catch (InvalidMidiDataException e) {
                       throw new RuntimeException(e);
                   }
                   //track.add(new MidiEvent(noteOn, 0));
//
// Note-//o    ff event (after the desired duration)
                   receiver.send(noteOn, -1);
                   //try {
                   //    Thread.sleep(100);
                   //} catch (InterruptedException e) {
                   //    throw new RuntimeException(e);
                   //}

                   NoteChannelsStopQueue.add(NoteChannelsQueue.get(0));
                   NotePitchsStopQueue.add(NotePitchsQueue.get(0));
                   NoteVelocitiesStopQueue.add(NoteVelocitiesQueue.get(0));

                   NoteChannelsQueue.remove(0);
                   NotePitchsQueue.remove(0);
                   NoteVelocitiesQueue.remove(0);


               }

               //if(prevSize != 0 && prevSize != NoteChannelsQueue.size())
               //prevSize = NoteChannelsQueue.size();
               //ShortMessage noteOn = new ShortMessage();
               //try {
               //    noteOn.setMessage(ShortMessage.NOTE_ON, insertNoteChannel, insertNotePitch, insertNoteVelocity);
               //} catch (InvalidMidiDataException e) {
               //    throw new RuntimeException(e);
               //}
               try {
                   Thread.sleep(100);
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }

               //if(prevSize != 0 && ){
               for(int i = 0; i < NoteChannelsStopQueue.size(); i++){
                   ShortMessage noteOff = new ShortMessage();
                   try {
                       noteOff.setMessage(ShortMessage.NOTE_OFF, NoteChannelsStopQueue.get(i), NotePitchsStopQueue.get(i), 0);
                   } catch (InvalidMidiDataException e) {
                       throw new RuntimeException(e);
                   }
                   receiver.send(noteOff, -1);
               }
               NoteChannelsStopQueue.clear();
               NotePitchsStopQueue.clear();
               NoteVelocitiesStopQueue.clear();

           }

       });
       playThread.start();
    }

    public void PassTime(){
        if(is_writing_to_file){
            Clear = false;
            time_passed++;
        }
    }

    public static void main(){


        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (replaceNoteBinding.wasPressed()) {
                //client.player.sendMessage(Text.of("Recording Started"), false);
                is_writing_to_file = !is_writing_to_file;
                if (is_writing_to_file) {
                    //file_to_write = "recorded-music/" + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + ".csv";
                    file_to_write = ProvinceOfMusicClient.recordedmusicdir + "/" + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + ".csv";
                    // make a folder if there isn't one
                    //File theDir = new File("recorded-music");
                    //if (!theDir.exists()) theDir.mkdirs();
                    ProvinceOfMusicClient.LOGGER.info("Started recording to file " + file_to_write + ".");
                    client.player.sendMessage(Text.of("Started recording to file " + file_to_write + "."), false);
                } else {
                    time_passed = 0;
                    ProvinceOfMusicClient.LOGGER.info("Stopped recording to file " + file_to_write + ".");
                    client.player.sendMessage(Text.of("Stopped recording to file " + file_to_write + "."), false);

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

    public static void RunSetup(){
        //ArrayList<String> instruments = new ArrayList<>();
        instruments.add("minecraft:block.note_block.harp");
        instruments.add("minecraft:block.note_block.bass");
        instruments.add("minecraft:block.note_block.snare");
        instruments.add("minecraft:block.note_block.hat");
        instruments.add("minecraft:block.note_block.basedrum");
        instruments.add("minecraft:block.note_block.bell");
        instruments.add("minecraft:block.note_block.flute");
        instruments.add("minecraft:block.note_block.chime");
        instruments.add("minecraft:block.note_block.guitar");
        instruments.add("minecraft:block.note_block.xylophone");
        instruments.add("minecraft:block.note_block.iron_xylophone");
        instruments.add("minecraft:block.note_block.cow_bell");
        instruments.add("minecraft:block.note_block.didgeridoo");
        instruments.add("minecraft:block.note_block.bit");
        instruments.add("minecraft:block.note_block.banjo");
        instruments.add("minecraft:block.note_block.pling");

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
        //"snare": [9, 38 if not use_visual_percussion else 26, 0], #sadly we can't convert pitch for percussion (channel 9) instruments,
        //"hat": [9, 42 if not use_visual_percussion else 28, 0], #because it is needed for different percussion instruments
        //"basedrum": [9, 35 if not use_visual_percussion else 24, 0],
        //#        "bell": [3, 14, 24], #uncomment these if you want to record music with these instruments too (Wynncraft doesn't have these)
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

        try {
            synth = MidiSystem.getSynthesizer();
            synth.open();
            synth.unloadAllInstruments(synth.getDefaultSoundbank());

            // Load the custom soundbank
            Soundbank soundbank = MidiSystem.getSoundbank(new File("1st-violin-SEC-accent.sf2"));
            synth.loadAllInstruments(soundbank);

            receiver = synth.getReceiver();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        } catch (InvalidMidiDataException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        NoteListenerHelper.addListener(this);

        PlayNotesThread();
    }
}
