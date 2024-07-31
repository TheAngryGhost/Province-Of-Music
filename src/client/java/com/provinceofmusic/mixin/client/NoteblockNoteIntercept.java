package com.provinceofmusic.mixin.client;

import com.provinceofmusic.listeners.NoteListener;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ClickType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Mixin(SoundManager.class)
public class NoteblockNoteIntercept {
    private List<NoteListener> listeners = new ArrayList<NoteListener>();

    Instant instant;
    long lastNoteMillis = -1;

    int ellapsedTicks = 0;


    private static ArrayList<String> instruments = new ArrayList<>();
    private static ArrayList<Integer> instrumentChannels = new ArrayList<>();

    private static ArrayList<Integer> instrumentPrograms = new ArrayList<>();

    private static ArrayList<Integer> instrumentPitchesToShift = new ArrayList<>();
    @Inject(at = @At("HEAD"), method = "play(Lnet/minecraft/client/sound/SoundInstance;)V", cancellable = true)
    private void run(SoundInstance sound, CallbackInfo ci) {
        // This code is injected into the start of MinecraftClient.run()V
        System.out.println(sound.getCategory());
        if(sound.getCategory() == SoundCategory.RECORDS){
            //System.out.println("stopped");
            //ci.cancel();
            notePlayed();

            //return;
        }
        //System.out.println("sfx");

    }

    @Inject(at = @At("HEAD"), method = "play(Lnet/minecraft/client/sound/SoundInstance;I)V", cancellable = true)
    private void run2(SoundInstance sound, int delay, CallbackInfo ci) {
        // This code is injected into the start of MinecraftClient.run()V
        if(sound.getCategory() == SoundCategory.RECORDS){
            //System.out.println("stopped");
            notePlayed();
            ci.cancel();
            //return;
        }
        //System.out.println("sfx2");

    }

    void notePlayed(SoundInstance sound, int delay){

        if(lastNoteMillis == -1){
            long lastNoteMillis = instant.toEpochMilli();
        }
        else{
            instant = Instant.now();
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
            System.out.println("Error");
        }

        int insertNotePitch = (Math.round((log2(pitch) * 12) + 66.5f) - 1) + instrumentPitchesToShift.get(noteInstrument);
        int insertNoteVelocity =  (int) (volume * 100f);
        //int insertNoteTick = currentTick * (240 / 6);

        for (NoteListener hl : listeners)
            hl.onNotePlayed(name, ellapsedTicks, insertNotePitch, insertNoteVelocity);
    }

    public void addListener(NoteListener toAdd) {
        listeners.add(toAdd);
    }

    private static float log2(float N)
    {

        // calculate log2 N indirectly
        // using log() method
        float result = (float) (Math.log(N) / Math.log(2));

        return result;
    }

    private static void RunSetup(){
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
