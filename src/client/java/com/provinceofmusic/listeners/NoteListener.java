package com.provinceofmusic.listeners;

import com.provinceofmusic.jukebox.InstrumentSound;

public interface NoteListener{

    default void onNotePlayed(InstrumentSound instrument, int ticksPassed, float pitch, int volume){

    }

}
