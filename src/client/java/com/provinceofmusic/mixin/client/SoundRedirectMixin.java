package com.provinceofmusic.mixin.client;

import com.google.common.collect.Maps;
import com.provinceofmusic.ProvinceOfMusicClient;
import com.provinceofmusic.jukebox.InstrumentSound;
import com.provinceofmusic.jukebox.NoteReplacer;
import com.provinceofmusic.listeners.NoteListenerHelper;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.sound.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;


@Mixin(SoundSystem.class)
public class SoundRedirectMixin{
    @Inject(method = "play", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundInstance;getVolume()F"), cancellable = true)
    public void onPlaySound(SoundInstance sound, CallbackInfo info) {
        if(NoteReplacer.replaceMusic){
            for(InstrumentSound tempSound : NoteListenerHelper.instrumentSounds){
                if(tempSound.registeredName.equals(sound.getId().toString())){
                    ProvinceOfMusicClient.noteListenerHelper.onSoundPlayed(sound.getPitch(), sound.getVolume(), sound.getId().toString());
                    info.cancel();
                }
                else {
                    for (String tempSound2 : tempSound.remaps) {
                        if(tempSound2.equals(sound.getId().toString())){
                            ProvinceOfMusicClient.noteListenerHelper.onSoundPlayed(sound.getPitch(), sound.getVolume(), sound.getId().toString());
                            info.cancel();
                        }
                    }
                }
            }
        }
    }
}
