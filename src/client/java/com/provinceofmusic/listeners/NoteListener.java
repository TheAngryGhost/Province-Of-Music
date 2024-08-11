package com.provinceofmusic.listeners;
public interface NoteListener{
    default void onNotePlayed(String instrument, int ticksPassed, int pitch, int volume){

    }

}
