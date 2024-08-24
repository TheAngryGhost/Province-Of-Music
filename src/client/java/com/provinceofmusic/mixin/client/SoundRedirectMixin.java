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
    //play(Lnet/minecraft/client/sound/SoundInstance;)V
    @Inject(method = "play", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundInstance;getVolume()F"), cancellable = true)
    public void onPlaySound(SoundInstance sound, CallbackInfo info) {
        //System.out.println(sound.getId());
        //System.out.println(sound.getVolume());
        //System.out.println(sound.getPitch());


        //if(sound!= null) {
        //    System.out.println(sound.getPitch());
        //}
        if(NoteReplacer.replaceMusic){
            for(InstrumentSound tempSound : NoteListenerHelper.instrumentSounds){
                if(tempSound.registeredName.equals(sound.getId().toString())){
                    //instrumentSound = tempSound;
                    //System.err.println("sdfjklhjdsjkfghusdkhjfgjklsdlfghksdjflghjklsdfghsdjklfghjklsdflg");
                    ProvinceOfMusicClient.noteListenerHelper.onSoundPlayed(sound.getPitch(), sound.getVolume(), sound.getId().toString());
                    info.cancel();
                }
                else {
                    for (String tempSound2 : tempSound.remaps) {
                        if(tempSound2.equals(sound.getId().toString())){
                            //instrumentSound = tempSound;
                            ProvinceOfMusicClient.noteListenerHelper.onSoundPlayed(sound.getPitch(), sound.getVolume(), sound.getId().toString());
                            //System.err.println("sdfjklhjdsjkfghusdkhjfgjklsdlfghksdjflghjklsdfghsdjklfghjklsdflg");
                            info.cancel();
                        }
                    }
                }
            }
        }


        // Check if it's a noteblock sound
        //if (isNoteblockSound(sound)) {
            //ci.cancel(); // Cancel the sound playback
        //}
    }

    //@Inject(method = "play", at = @At(value = "FIELD", target = "Lnet/minecraft/client/sounds/SoundEngine;instanceBySource:Lcom/google/common/collect/Multimap;"), locals = LocalCapture.CAPTURE_FAILHARD)
    //private void play(SoundInstance sound, CallbackInfo ci, WeighedSoundEvents weightedSoundSet, ResourceLocation identifier, Sound sound2, float f, float g, SoundSource soundCategory) {
    //    //SoundPhysics.setLastSoundCategoryAndName(soundCategory, sound.getLocation().toString());
    //}

    //private final Map<Identifier, WeightedSoundSet> sounds = Maps.newHashMap();
    //@Inject(method = "play(Lnet/minecraft/client/sound/SoundInstance;)V", at = @At(value = "FIELD", target = "net/minecraft/client/sound/SoundSystem.sounds"), locals = LocalCapture.CAPTURE_FAILHARD)
    //private void play(SoundInstance sound, CallbackInfo ci){
////
    //}
//
    //private boolean isNoteblockSound(SoundInstance sound) {
    //    // Implement your logic to identify noteblock sounds
    //    return sound.getId().getPath().startsWith("block.note_block.");
    //}
}
