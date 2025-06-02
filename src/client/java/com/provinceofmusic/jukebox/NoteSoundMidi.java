package com.provinceofmusic.jukebox;

import com.provinceofmusic.listeners.NoteListenerHelper;

public class NoteSoundMidi {
    public InstrumentSound instrument;
    public int ticksPassed;
    public float pitch;
    public int volume;

    public NoteSoundMidi(InstrumentSound instrument, int ticksPassed, float pitch, int volume){
        this.instrument = instrument;
        this.ticksPassed = ticksPassed;
        this.pitch = pitch;
        this.volume = volume;
    }

    public NoteSoundMidi(NoteSoundMinecraft input){
        ticksPassed = input.ticksPassed;
        volume = (int) (input.volume * 100f);
        instrument = NoteListenerHelper.SoundIdToInstrumentSound(input.instrument);
        pitch = NoteListenerHelper.convertPitchMinecraftToMidi(input.pitch, input.instrument);
    }
}
