package com.provinceofmusic.listeners;
public interface NoteListener{
    default void onNotePlayed(String instrument, int ticksPassed, float pitch, float volume){

    }

    public static void notePlayed(){

    }

}
