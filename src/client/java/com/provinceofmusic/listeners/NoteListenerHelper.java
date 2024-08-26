package com.provinceofmusic.listeners;

import com.provinceofmusic.jukebox.InstrumentSound;

import java.util.ArrayList;
import java.util.List;

public class NoteListenerHelper {

    private static List<NoteListener> listeners = new ArrayList<>();
    private static int ellapsedTicks = 0;

    public static ArrayList<InstrumentSound> instrumentSounds = new ArrayList<>();

    public static void addListener(NoteListener toAdd) {
        listeners.add(toAdd);
    }

    private static float log2(float N)
    {
        return (float) (Math.log(N) / Math.log(2));
    }

    public static void tick(){
        ellapsedTicks++;
    }

    public NoteListenerHelper(){

        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.harp", new String[]{"minecraft:block.note_block.harp", "minecraft:note.harp"}, 0 , 1));
        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.bass", new String[]{"minecraft:block.note_block.bass", "minecraft:note.bassattack"}, -24 , 2));
        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.snare", new String[]{"minecraft:block.note_block.snare", "minecraft:note.snare"}, 0 , 3));
        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.hat", new String[]{"minecraft:block.note_block.hat", "minecraft:note.hat"}, 0 , 4));
        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.drum", new String[]{"minecraft:block.note_block.basedrum", "minecraft:note.bd"}, 0 , 5));

        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.bell", new String[]{"minecraft:block.note_block.bell"}, 24 , 6));
        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.flute", new String[]{"minecraft:block.note_block.flute"}, 12 , 7));
        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.guitar", new String[]{"minecraft:block.note_block.guitar"}, -12 , 8));

        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.brass", 0 , 9));
        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.kora", -12 , 10));
        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.marimba", 0 , 11));
        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.piano", 0 , 12));
        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.pizz", -12 , 13));
        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.tamborine", 0 , 14));
        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.tom", 0 , 15));
        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.vibraphone", 0 , 16));
        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.woodblock", 0 , 17));

        //legacy
        instrumentSounds.add(new InstrumentSound("minecraft:block.note_block.chime", 24 , 18));
        instrumentSounds.add(new InstrumentSound("minecraft:block.note_block.xylophone", -12 , 19));
        instrumentSounds.add(new InstrumentSound("minecraft:block.note_block.iron_xylophone", 24 , 20));
        instrumentSounds.add(new InstrumentSound("minecraft:block.note_block.cow_bell", 12 , 21));
        instrumentSounds.add(new InstrumentSound("minecraft:block.note_block.didgeridoo", -24 , 22));
        instrumentSounds.add(new InstrumentSound("minecraft:block.note_block.bit", 0 , 23));
        instrumentSounds.add(new InstrumentSound("minecraft:block.note_block.banjo", 0 , 24));
        instrumentSounds.add(new InstrumentSound("minecraft:block.note_block.pling", 0 , 25));
    }

    public void onSoundPlayed(float pitch, float volume, String id) {

        InstrumentSound instrumentSound = SoundIdToInstrumentSound(id);

        //float insertNotePitch = ((((log2(pitch) * 12f) + 66.5f) - 1) + 0.5f) + instrumentSound.transpose;
        assert instrumentSound != null;
        float insertNotePitch = convertPitchMinecraftToMidi(pitch, instrumentSound);
        int insertNoteVelocity =  (int) (volume * 100f);

        for (NoteListener hl : listeners){
            hl.onNotePlayed(instrumentSound, ellapsedTicks, insertNotePitch, insertNoteVelocity);
        }
        ellapsedTicks = 0;
    }

    public static InstrumentSound SoundIdToInstrumentSound(String id){
        InstrumentSound instrumentSound;
        for(InstrumentSound tempSound : instrumentSounds){
            if(tempSound.registeredName.equals(id)){
                instrumentSound = tempSound;
                return instrumentSound;
            }
            else {
                for (String tempSound2 : tempSound.remaps) {
                    if(tempSound2.equals(id)){
                        instrumentSound = tempSound;
                        return instrumentSound;
                    }
                }
            }
        }
        return null;
    }

    public static float convertPitchMinecraftToMidi(float pitch, InstrumentSound instrument){
        return (((((log2(pitch) * 12f) + 66.5f) - 1) + 0.5f) + instrument.transpose);
    }

    public static float convertPitchMidiToMinecraft(float pitch, InstrumentSound instrument){
        return (float) Math.pow(2, (((((pitch - instrument.transpose) - 0.5f) + 1) - 66.5f) / 12f));
    }
}
