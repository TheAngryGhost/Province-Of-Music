package com.provinceofmusic.mixin.client;

import com.provinceofmusic.ProvinceOfMusicClient;
import com.provinceofmusic.jukebox.InstrumentRemap;
import com.provinceofmusic.jukebox.Instrument;
import com.provinceofmusic.jukebox.NoteReplacer;
import com.provinceofmusic.listeners.NoteListenerHelper;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(SoundSystem.class)
public class SoundRedirectMixin{
    @Inject(method = "play", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundInstance;getVolume()F"), cancellable = true)
    private void onPlaySound(
            SoundInstance sound,
            CallbackInfoReturnable<SoundSystem.PlayResult> cir
    ) {
        if (sound == null) {
            return;
        }
        if (sound.getId() == null) {
            return;
        }
        if (sound.getSound() == null) {
            return;
        }

        String soundId = sound.getId().toString();

        for (Instrument tempSound : NoteListenerHelper.instruments) {

            boolean matched = false;

            if (tempSound.registeredName.equals(soundId)) {
                matched = true;
            } else {
                for (InstrumentRemap remap : tempSound.remaps) {
                    if (remap.remapSoundName.equals(soundId)) {
                        matched = true;
                        break;
                    }
                }
            }

            if (matched) {
                ProvinceOfMusicClient.noteListenerHelper.onSoundPlayed(sound.getPitch(), sound.getVolume(), soundId);
                if (ProvinceOfMusicClient.configSettings.samplePackMusicReplacement && ProvinceOfMusicClient.configSettings.activeSamplePack != null) {
                    cir.setReturnValue(SoundSystem.PlayResult.NOT_STARTED);
                    cir.cancel();
                    return;
                }
            }
        }
    }
}
