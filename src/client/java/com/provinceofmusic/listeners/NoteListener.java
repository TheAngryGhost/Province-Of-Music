package com.provinceofmusic.listeners;

import com.provinceofmusic.jukebox.InstrumentSound;

public interface NoteListener{

    default void onNotePlayed(String instrument, int ticksPassed, float pitch, int volume){

    }

}
