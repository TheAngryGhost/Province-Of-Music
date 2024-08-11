package com.provinceofmusic.mixin.client;

import com.provinceofmusic.jukebox.NoteReplacer;
import com.provinceofmusic.listeners.NoteListener;
import com.provinceofmusic.listeners.NoteListenerHelper;
import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ClickType;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Mixin(SoundSystem.class)
public class NoteblockNoteIntercept implements NoteListener{

    @Inject(at = @At("RETURN"), method = "play(Lnet/minecraft/client/sound/SoundInstance;)V", cancellable = true)
    private void run(SoundInstance sound, CallbackInfo ci) {
        // This code is injected into the start of MinecraftClient.run()V
        //System.out.println(sound.getCategory());
        if(sound.getCategory() == SoundCategory.RECORDS){

            if (sound.canPlay()) {
                Sound sound2 = sound.getSound();
                if (sound2 != SoundManager.INTENTIONALLY_EMPTY_SOUND) {
                    if (sound2 == SoundManager.MISSING_SOUND) {

                    }
                    else{
                        //float pitch = sound.getPitch();
                        //System.out.println("Pitch " + pitch);
                        //float musicVolume = sound.getVolume();
                        //System.out.println("Volume " + musicVolume);
                        NoteListenerHelper.notePlayed(sound, 0);
                    }
                }
                else{
                    //System.out.println("Empty");
                }
            }


            //if(NoteReplacer.replaceMusic){
            //    ci.cancel();
            //}

            //System.out.println("stopped");
            //ci.cancel();
            //sound.getSound().getPitch()
            //float pitch = sound.getSound().getPitch().get(Random.create());
            //float musicVolume = sound.getSound().getVolume().get(Random.create());
            //NoteListenerHelper.notePlayed(sound, pitch, musicVolume, 0);
            //ci.cancel();
            //return;
        }
        //System.out.println("sfx");

    }

    //@Inject(at = @At("HEAD"), method = "play(Lnet/minecraft/client/sound/SoundInstance;I)V", cancellable = true)
    //private void run2(SoundInstance sound, int delay, CallbackInfo ci) {
    //    // This code is injected into the start of MinecraftClient.run()V
    //    if(sound.getCategory() == SoundCategory.RECORDS){
    //        //System.out.println("stopped");
    //        float pitch = sound.getPitch();
    //        float musicVolume = sound.getVolume();
    //        NoteListenerHelper.notePlayed(sound, pitch, musicVolume, 0);
    //        ci.cancel();
    //        //return;
    //    }
    //    //System.out.println("sfx2");
//
    //}


}
