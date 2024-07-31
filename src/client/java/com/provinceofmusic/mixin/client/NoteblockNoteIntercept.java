package com.provinceofmusic.mixin.client;

import com.provinceofmusic.listeners.NoteListener;
import com.provinceofmusic.listeners.NoteListenerHelper;
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
public class NoteblockNoteIntercept implements NoteListener{

    @Inject(at = @At("HEAD"), method = "play(Lnet/minecraft/client/sound/SoundInstance;)V", cancellable = true)
    private void run(SoundInstance sound, CallbackInfo ci) {
        // This code is injected into the start of MinecraftClient.run()V
        System.out.println(sound.getCategory());
        if(sound.getCategory() == SoundCategory.RECORDS){
            //System.out.println("stopped");
            //ci.cancel();
            NoteListenerHelper.notePlayed(sound, 0);
            ci.cancel();
            //return;
        }
        //System.out.println("sfx");

    }

    @Inject(at = @At("HEAD"), method = "play(Lnet/minecraft/client/sound/SoundInstance;I)V", cancellable = true)
    private void run2(SoundInstance sound, int delay, CallbackInfo ci) {
        // This code is injected into the start of MinecraftClient.run()V
        if(sound.getCategory() == SoundCategory.RECORDS){
            //System.out.println("stopped");
            NoteListenerHelper.notePlayed(sound, delay);
            ci.cancel();
            //return;
        }
        //System.out.println("sfx2");

    }


}
