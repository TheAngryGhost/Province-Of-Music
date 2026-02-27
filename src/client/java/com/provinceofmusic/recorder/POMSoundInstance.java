package com.provinceofmusic.recorder;

import net.minecraft.client.sound.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class POMSoundInstance extends AbstractSoundInstance {

    public POMSoundInstance(String ID, float pitch, float volume) {
        super(SoundEvent.of(Identifier.of(ID)), SoundCategory.MASTER, SoundInstance.createRandom());
        super.pitch = pitch;
        super.volume = volume;
    }
}
