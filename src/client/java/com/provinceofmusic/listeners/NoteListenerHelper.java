package com.provinceofmusic.listeners;

import net.minecraft.client.sound.SoundInstance;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class NoteListenerHelper {

    private static List<NoteListener> listeners = new ArrayList<NoteListener>();

    static Instant instant;
    static long lastNoteMillis = -1;

    static int ellapsedTicks = 0;


    private static ArrayList<String> instruments = new ArrayList<>();
    private static ArrayList<Integer> instrumentChannels = new ArrayList<>();

    private static ArrayList<Integer> instrumentPrograms = new ArrayList<>();

    private static ArrayList<Integer> instrumentPitchesToShift = new ArrayList<>();
    public static void notePlayed(SoundInstance sound, int delay){
        //System.err.println("is null1    " + sound);

        instant = Instant.now();
        if(lastNoteMillis == -1){
            lastNoteMillis = instant.toEpochMilli();
        }
        else{
            long ellapsedMillis = instant.toEpochMilli() - lastNoteMillis;
            ellapsedTicks = ((int) (ellapsedMillis / 50)) + delay;

        }

        String[] split_up_name = sound.getId().toString().split("[.]");
        String name = (split_up_name[0].equals("block") && split_up_name[1].equals("note")) ? split_up_name[2] : sound.getId().toString();
        float pitch = sound.getPitch();
        float volume = sound.getVolume();

        int noteInstrument = -1;
        for(int j = 0; j < instruments.size(); j++){
            if(instruments.get(j).equals(name)){
                noteInstrument = j;
            }
        }
        if(noteInstrument == -1){
            System.out.println("Error: " + sound.getId().toString());
            return;
        }

        int insertNotePitch = (Math.round((log2(pitch) * 12) + 66.5f) - 1) + instrumentPitchesToShift.get(noteInstrument);
        int insertNoteVelocity =  (int) (volume * 100f);
        //int insertNoteTick = currentTick * (240 / 6);

        for (NoteListener hl : listeners)
            hl.onNotePlayed(name, ellapsedTicks, insertNotePitch, insertNoteVelocity);
    }

    public static void addListener(NoteListener toAdd) {
        listeners.add(toAdd);
    }

    private static float log2(float N)
    {

        // calculate log2 N indirectly
        // using log() method
        float result = (float) (Math.log(N) / Math.log(2));

        return result;
    }

    public NoteListenerHelper(){
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
    }
}
