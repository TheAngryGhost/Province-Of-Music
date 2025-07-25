package com.provinceofmusic.listeners;

import com.provinceofmusic.jukebox.NoteSoundMinecraft;

public interface NoteListener{

    default void onNotePlayed(NoteSoundMinecraft sound){

    }

}
