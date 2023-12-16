package com.provinceofmusic;

import com.provinceofmusic.midi.MidiFile;
import com.provinceofmusic.midi.MidiTrack;
import com.provinceofmusic.midi.event.meta.Tempo;
import com.provinceofmusic.midi.event.meta.TimeSignature;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ConvertToMidi {

    static ArrayList<String> instruments = new ArrayList<>();
    static ArrayList<Integer> instrumentChannels = new ArrayList<>();

    static ArrayList<Integer> instrumentPrograms = new ArrayList<>();

    static ArrayList<Integer> instrumentPitchesToShift = new ArrayList<>();
    public static void convert(File inputFile, String outputPath){
        RunSetup();

        File f = inputFile;
        File f2 = new File("exported-music/");
        if (!f2.exists()){
            f2.mkdirs();
        }

        ArrayList<String> types = new ArrayList<>();
        ArrayList<String> ticks = new ArrayList<>();
        ArrayList<String> pitches = new ArrayList<>();
        ArrayList<String> volumes = new ArrayList<>();



        if (f.exists()) {
            //System.out.println("File exists.");
            try {
                BufferedReader reader = new BufferedReader(new FileReader(f));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] values = line.split(",");
                    types.add(values[0]);
                    ticks.add(values[1]);
                    pitches.add(values[2]);
                    volumes.add(values[3]);
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("File does not exist.");
        }

        MidiTrack tempoTrack = new MidiTrack();
        MidiTrack noteTrack = new MidiTrack();

        TimeSignature ts = new TimeSignature();
        ts.setTimeSignature(4, 4, TimeSignature.DEFAULT_METER, TimeSignature.DEFAULT_DIVISION);

        Tempo tempo = new Tempo();
        tempo.setBpm(100);

        tempoTrack.insertEvent(ts);
        tempoTrack.insertEvent(tempo);

        int currentTick = 0;
        for(int i = 0; i < types.size(); i++){
            String noteType = types.get(i);
            int noteTick = Integer.valueOf(ticks.get(i));
            float notePitch = Float.valueOf(pitches.get(i));
            float noteVolume = Float.valueOf(volumes.get(i));


            int noteInstrument = -1;
            for(int j = 0; j < instruments.size(); j++){
                if(instruments.get(j).equals(noteType)){
                    noteInstrument = j;
                }
            }
            if(noteInstrument == -1){
                System.out.println("Error");
            }

            currentTick += noteTick;

            int insertNoteChannel = instrumentChannels.get(noteInstrument);
            int insertNotePitch = (Math.round((log2(notePitch) * 12) + 66.5f) - 1) + instrumentPitchesToShift.get(noteInstrument);
            int insertNoteVelocity = 100;
            int insertNoteTick = currentTick * (240 / 6);
            int insertNoteDuration = 120;

            //currentTick += noteTick;

            noteTrack.insertNote(insertNoteChannel, insertNotePitch, insertNoteVelocity, insertNoteTick, insertNoteDuration);
        }

        List<MidiTrack> tracks = new ArrayList<MidiTrack>();
        tracks.add(tempoTrack);
        tracks.add(noteTrack);

        MidiFile midi = new MidiFile(MidiFile.DEFAULT_RESOLUTION, tracks);

        File output = new File(outputPath + ".mid");
        try
        {
            midi.writeToFile(output);
        }
        catch(IOException e)
        {
            System.err.println(e);
        }
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
    }
}
