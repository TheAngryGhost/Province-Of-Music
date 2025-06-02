package com.provinceofmusic.mixin.client;

import com.provinceofmusic.ProvinceOfMusicClient;
import com.provinceofmusic.jukebox.InstrumentRemap;
import com.provinceofmusic.jukebox.InstrumentSound;
import com.provinceofmusic.jukebox.NoteReplacer;
import com.provinceofmusic.listeners.NoteListenerHelper;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(SoundSystem.class)
public class SoundRedirectMixin{
    @Inject(method = "play", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundInstance;getVolume()F"), cancellable = true)
    public void onPlaySound(SoundInstance sound, CallbackInfo info) {
        for(InstrumentSound tempSound : NoteListenerHelper.instrumentSounds){
            if(tempSound.registeredName.equals(sound.getId().toString())){
                ProvinceOfMusicClient.noteListenerHelper.onSoundPlayed(sound.getPitch(), sound.getVolume(), sound.getId().toString());
                if(NoteReplacer.replaceMusic && ProvinceOfMusicClient.configSettings.activeSamplePack != null) {
                    info.cancel();
                }
            }
            else {
                for (InstrumentRemap remap : tempSound.remaps) {
                    if(remap.remapSoundName.equals(sound.getId().toString())){
                        ProvinceOfMusicClient.noteListenerHelper.onSoundPlayed(sound.getPitch(), sound.getVolume(), sound.getId().toString());
                        if(NoteReplacer.replaceMusic && ProvinceOfMusicClient.configSettings.activeSamplePack != null) {
                            info.cancel();
                        }
                    }
                }
            }
        }
    }
}
