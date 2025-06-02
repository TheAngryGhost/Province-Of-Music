package com.provinceofmusic.listeners;

import com.provinceofmusic.jukebox.InstrumentRemap;
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

        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.harp", new InstrumentRemap[]{
                new InstrumentRemap("minecraft:block.note_block.harp"), //legacy
                new InstrumentRemap("minecraft:note.harp"), //legacy
                new InstrumentRemap("minecraft:wynn.instrument.harp_lower", -12) // lower
        }, 0 , 1));
        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.bass", new InstrumentRemap[]{
                new InstrumentRemap("minecraft:block.note_block.bass"), //legacy
                new InstrumentRemap("minecraft:note.bassattack"), //legacy
                new InstrumentRemap("minecraft:wynn.instrument.bass_lower", -12) // lower
        }, -24 , 2));
        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.snare", new InstrumentRemap[]{
                new InstrumentRemap("minecraft:block.note_block.snare"), //legacy
                new InstrumentRemap("minecraft:note.snare"), //legacy
                new InstrumentRemap("minecraft:wynn.instrument.snare_lower", -12) // lower
        }, 0 , 3));
        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.hat", new InstrumentRemap[]{
                new InstrumentRemap("minecraft:block.note_block.hat"), //legacy
                new InstrumentRemap("minecraft:note.hat"), //legacy
                new InstrumentRemap("minecraft:wynn.instrument.hat_lower", -12) // lower
        }, 0 , 4));
        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.drum", new InstrumentRemap[]{
                new InstrumentRemap("minecraft:block.note_block.basedrum"), //legacy
                new InstrumentRemap("minecraft:note.bd"), //legacy
                new InstrumentRemap("minecraft:wynn.instrument.drum_lower", -12) // lower
        }, 0 , 5));

        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.bell", new InstrumentRemap[]{
                new InstrumentRemap("minecraft:block.note_block.bell"), //legacy
                new InstrumentRemap("minecraft:wynn.instrument.bell_lower", -12) // lower
        }, 24 , 6));
        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.flute", new InstrumentRemap[]{
                new InstrumentRemap("minecraft:block.note_block.flute"), //legacy
                new InstrumentRemap("minecraft:wynn.instrument.flute_lower", -12) // lower
        }, 12 , 7));
        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.guitar", new InstrumentRemap[]{
                new InstrumentRemap("minecraft:block.note_block.guitar"), //legacy
                new InstrumentRemap("minecraft:wynn.instrument.guitar_lower", -12) // lower
        }, -12 , 8));
        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.brass", new InstrumentRemap[]{
                new InstrumentRemap("minecraft:wynn.instrument.brass_lower", -12) // lower
        }, 0 , 9));
        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.kora", new InstrumentRemap[]{
                new InstrumentRemap("minecraft:wynn.instrument.kora_lower", -12) // lower
        }, -12 , 10));
        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.marimba", new InstrumentRemap[]{
                new InstrumentRemap("minecraft:wynn.instrument.marimba_lower", -12) // lower
        }, 0 , 11));
        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.piano", new InstrumentRemap[]{
                new InstrumentRemap("minecraft:wynn.instrument.piano_lower", -12) // lower
        }, 0 , 12));
        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.pizz", new InstrumentRemap[]{
                new InstrumentRemap("minecraft:wynn.instrument.pizz_lower", -12) // lower
        }, -12 , 13));
        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.tamborine", new InstrumentRemap[]{
                new InstrumentRemap("minecraft:wynn.instrument.tamborine_lower", -12) // lower
        }, 0 , 14));
        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.tom", new InstrumentRemap[]{
                new InstrumentRemap("minecraft:wynn.instrument.tom_lower", -12) // lower
        }, 0 , 15));
        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.vibraphone", new InstrumentRemap[]{
                new InstrumentRemap("minecraft:wynn.instrument.vibraphone_lower", -12) // lower
        }, 0 , 16));
        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.woodblock", new InstrumentRemap[]{
                new InstrumentRemap("minecraft:wynn.instrument.woodblock_lower", -12) // lower
        }, 0 , 17));

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
        float insertNotePitch = convertPitchMinecraftToMidi(pitch, id);
        int insertNoteVelocity =  (int) (volume * 100f);

        for (NoteListener hl : listeners){
            hl.onNotePlayed(id, ellapsedTicks, insertNotePitch, insertNoteVelocity);
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
                for (InstrumentRemap remap : tempSound.remaps) {
                    if(remap.remapSoundName.equals(id)){
                        instrumentSound = tempSound;
                        return instrumentSound;
                    }
                }
            }
        }
        return null;
    }

    public static int SoundIdToAdditionalTranspose(String id){
        InstrumentSound instrumentSound = SoundIdToInstrumentSound(id);
        for (InstrumentRemap remap : instrumentSound.remaps) {
            if(remap.remapSoundName.equals(id)){
                return remap.additionalTranspose;
            }
        }
        return 0;
    }

    public static float convertPitchMinecraftToMidi(float pitch, String id){
        InstrumentSound instrument = SoundIdToInstrumentSound(id);
        assert instrument != null;
        return (((((log2(pitch) * 12f) + 66.5f) - 1) + 0.5f) + instrument.transpose + SoundIdToAdditionalTranspose(id));
    }

    public static float convertPitchMidiToMinecraft(float pitch, String id){
        InstrumentSound instrument = SoundIdToInstrumentSound(id);
        assert instrument != null;
        return (float) Math.pow(2, (((((pitch - instrument.transpose - SoundIdToAdditionalTranspose(id)) - 0.5f) + 1) - 66.5f) / 12f));
    }

    //public static String verifyLower(){
//
   //  }

}
