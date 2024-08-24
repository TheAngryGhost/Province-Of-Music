package com.provinceofmusic.listeners;

import com.provinceofmusic.jukebox.InstrumentSound;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundInstanceListener;
import net.minecraft.client.sound.WeightedSoundSet;

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
        float result = (float) (Math.log(N) / Math.log(2));

        return result;
    }

    public static void tick(){
        ellapsedTicks++;
    }

    public NoteListenerHelper(){

        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.harp", new String[]{"minecraft:block.note_block.harp", "minecraft:note.harp"}, 0 , 0));
        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.bass", new String[]{"minecraft:block.note_block.bass", "minecraft:note.bassattack"}, -24 , 0));
        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.snare", new String[]{"minecraft:block.note_block.snare", "minecraft:note.snare"}, 0 , 0));
        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.hat", new String[]{"minecraft:block.note_block.hat", "minecraft:note.hat"}, 0 , 0));
        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.drum", new String[]{"minecraft:block.note_block.basedrum", "minecraft:note.bd"}, 0 , 0));

        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.bell", new String[]{"minecraft:block.note_block.bell"}, 24 , 0));
        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.flute", new String[]{"minecraft:block.note_block.flute"}, 12 , 0));
        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.guitar", new String[]{"minecraft:block.note_block.guitar"}, -12 , 0));

        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.brass", 0 , 0));
        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.kora", -12 , 0));
        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.marimba", 0 , 0));
        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.piano", 0 , 0));
        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.pizz", -12 , 0));
        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.tamborine", 0 , 0));
        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.tom", 0 , 0));
        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.vibraphone", 0 , 0));
        instrumentSounds.add(new InstrumentSound("minecraft:wynn.instrument.woodblock", 0 , 0));

        //legacy
        instrumentSounds.add(new InstrumentSound("minecraft:block.note_block.chime", 24 , 0));
        instrumentSounds.add(new InstrumentSound("minecraft:block.note_block.xylophone", -12 , 0));
        instrumentSounds.add(new InstrumentSound("minecraft:block.note_block.iron_xylophone", 24 , 0));
        instrumentSounds.add(new InstrumentSound("minecraft:block.note_block.cow_bell", 12 , 0));
        instrumentSounds.add(new InstrumentSound("minecraft:block.note_block.didgeridoo", -24 , 0));
        instrumentSounds.add(new InstrumentSound("minecraft:block.note_block.bit", 0 , 0));
        instrumentSounds.add(new InstrumentSound("minecraft:block.note_block.banjo", 0 , 0));
        instrumentSounds.add(new InstrumentSound("minecraft:block.note_block.pling", 0 , 0));
    }

    public void onSoundPlayed(float pitch, float volume, String id) {

        InstrumentSound instrumentSound = null;
        for(InstrumentSound tempSound : instrumentSounds){
            if(tempSound.registeredName.equals(id)){
                instrumentSound = tempSound;
            }
            else {
                for (String tempSound2 : tempSound.remaps) {
                    if(tempSound2.equals(id)){
                        instrumentSound = tempSound;
                    }
                }
            }
        }
        if(instrumentSound == null){
            return;
        }
        else{
        }

        float insertNotePitch = ((((log2(pitch) * 12f) + 66.5f) - 1) + 0.5f) + instrumentSound.transpose;
        int insertNoteVelocity =  (int) (volume * 100f);

        for (NoteListener hl : listeners){
            hl.onNotePlayed(instrumentSound, ellapsedTicks, insertNotePitch, insertNoteVelocity);
        }
        ellapsedTicks = 0;
    }
}
