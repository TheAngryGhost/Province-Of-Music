package com.provinceofmusic.listeners;

import com.provinceofmusic.jukebox.InstrumentRemap;
import com.provinceofmusic.jukebox.Instrument;
import com.provinceofmusic.jukebox.NoteSoundMinecraft;

import java.util.ArrayList;
import java.util.List;

public class NoteListenerHelper {

    private static List<NoteListener> listeners = new ArrayList<>();
    private static int ellapsedTicks = 0;

    public static ArrayList<Instrument> instruments = new ArrayList<>();

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

        instruments.add(new Instrument("minecraft:wynn.instrument.harp", new InstrumentRemap[]{
                new InstrumentRemap("minecraft:block.note_block.harp"), //legacy
                new InstrumentRemap("minecraft:note.harp"), //legacy
                new InstrumentRemap("minecraft:wynn.instrument.harp_lower", -24) // lower
        }, 0 , 1));
        instruments.add(new Instrument("minecraft:wynn.instrument.bass", new InstrumentRemap[]{
                new InstrumentRemap("minecraft:block.note_block.bass"), //legacy
                new InstrumentRemap("minecraft:note.bassattack"), //legacy
                new InstrumentRemap("minecraft:wynn.instrument.bass_lower", -24) // lower
        }, -24 , 2));
        instruments.add(new Instrument("minecraft:wynn.instrument.snare", new InstrumentRemap[]{
                new InstrumentRemap("minecraft:block.note_block.snare"), //legacy
                new InstrumentRemap("minecraft:note.snare"), //legacy
                new InstrumentRemap("minecraft:wynn.instrument.snare_lower", -24) // lower
        }, 0 , 3));
        instruments.add(new Instrument("minecraft:wynn.instrument.hat", new InstrumentRemap[]{
                new InstrumentRemap("minecraft:block.note_block.hat"), //legacy
                new InstrumentRemap("minecraft:note.hat"), //legacy
                new InstrumentRemap("minecraft:wynn.instrument.hat_lower", -24) // lower
        }, 0 , 4));
        instruments.add(new Instrument("minecraft:wynn.instrument.drum", new InstrumentRemap[]{
                new InstrumentRemap("minecraft:block.note_block.basedrum"), //legacy
                new InstrumentRemap("minecraft:note.bd"), //legacy
                new InstrumentRemap("minecraft:wynn.instrument.drum_lower", -24) // lower
        }, 0 , 5));

        instruments.add(new Instrument("minecraft:wynn.instrument.bell", new InstrumentRemap[]{
                new InstrumentRemap("minecraft:block.note_block.bell"), //legacy
                new InstrumentRemap("minecraft:wynn.instrument.bell_lower", -24) // lower
        }, 24 , 6));
        instruments.add(new Instrument("minecraft:wynn.instrument.flute", new InstrumentRemap[]{
                new InstrumentRemap("minecraft:block.note_block.flute"), //legacy
                new InstrumentRemap("minecraft:wynn.instrument.flute_lower", -24) // lower
        }, 12 , 7));
        instruments.add(new Instrument("minecraft:wynn.instrument.guitar", new InstrumentRemap[]{
                new InstrumentRemap("minecraft:block.note_block.guitar"), //legacy
                new InstrumentRemap("minecraft:wynn.instrument.guitar_lower", -24) // lower
        }, -12 , 8));
        instruments.add(new Instrument("minecraft:wynn.instrument.brass", new InstrumentRemap[]{
                new InstrumentRemap("minecraft:wynn.instrument.brass_lower", -24) // lower
        }, 0 , 9));
        instruments.add(new Instrument("minecraft:wynn.instrument.kora", new InstrumentRemap[]{
                new InstrumentRemap("minecraft:wynn.instrument.kora_lower", -24) // lower
        }, -12 , 10));
        instruments.add(new Instrument("minecraft:wynn.instrument.marimba", new InstrumentRemap[]{
                new InstrumentRemap("minecraft:wynn.instrument.marimba_lower", -24) // lower
        }, 0 , 11));
        instruments.add(new Instrument("minecraft:wynn.instrument.piano", new InstrumentRemap[]{
                new InstrumentRemap("minecraft:wynn.instrument.piano_lower", -24) // lower
        }, 0 , 12));
        instruments.add(new Instrument("minecraft:wynn.instrument.pizz", new InstrumentRemap[]{
                new InstrumentRemap("minecraft:wynn.instrument.pizz_lower", -24) // lower
        }, -12 , 13));
        instruments.add(new Instrument("minecraft:wynn.instrument.tamborine", new InstrumentRemap[]{
                new InstrumentRemap("minecraft:wynn.instrument.tamborine_lower", -24) // lower
        }, 0 , 14));
        instruments.add(new Instrument("minecraft:wynn.instrument.tom", new InstrumentRemap[]{
                new InstrumentRemap("minecraft:wynn.instrument.tom_lower", -24) // lower
        }, 0 , 15));
        instruments.add(new Instrument("minecraft:wynn.instrument.vibraphone", new InstrumentRemap[]{
                new InstrumentRemap("minecraft:wynn.instrument.vibraphone_lower", -24) // lower
        }, 0 , 16));
        instruments.add(new Instrument("minecraft:wynn.instrument.woodblock", new InstrumentRemap[]{
                new InstrumentRemap("minecraft:wynn.instrument.woodblock_lower", -24) // lower
        }, 0 , 17));

        //legacy
        instruments.add(new Instrument("minecraft:block.note_block.chime", 24 , 18));
        instruments.add(new Instrument("minecraft:block.note_block.xylophone", -12 , 19));
        instruments.add(new Instrument("minecraft:block.note_block.iron_xylophone", 24 , 20));
        instruments.add(new Instrument("minecraft:block.note_block.cow_bell", 12 , 21));
        instruments.add(new Instrument("minecraft:block.note_block.didgeridoo", -24 , 22));
        instruments.add(new Instrument("minecraft:block.note_block.bit", 0 , 23));
        instruments.add(new Instrument("minecraft:block.note_block.banjo", 0 , 24));
        instruments.add(new Instrument("minecraft:block.note_block.pling", 0 , 25));
    }

    public void onSoundPlayed(float pitch, float volume, String id) {
        for (NoteListener hl : listeners){
            hl.onNotePlayed(new NoteSoundMinecraft(id, ellapsedTicks, pitch, volume));
        }
        ellapsedTicks = 0;
    }

    public static Instrument soundIdToInstrument(String id){
        Instrument instrument;
        for(Instrument tempSound : instruments){
            if(tempSound.registeredName.equals(id)){
                instrument = tempSound;
                return instrument;
            }
            else {
                for (InstrumentRemap remap : tempSound.remaps) {
                    if(remap.remapSoundName.equals(id)){
                        instrument = tempSound;
                        return instrument;
                    }
                }
            }
        }
        return null;
    }

    public static int soundIdToAdditionalTranspose(String id){
        Instrument instrument = soundIdToInstrument(id);
        for (InstrumentRemap remap : instrument.remaps) {
            if(remap.remapSoundName.equals(id)){
                return remap.additionalTranspose;
            }
        }
        return 0;
    }

    public static float convertPitchMinecraftToMidi(float pitch, String id){
        Instrument instrument = soundIdToInstrument(id);
        assert instrument != null;
        return (((((log2(pitch) * 12f) + 66.5f) - 1) + 0.5f) + instrument.transpose + soundIdToAdditionalTranspose(id));
    }
}
