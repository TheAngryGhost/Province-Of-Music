package com.provinceofmusic.recorder;

import com.provinceofmusic.ProvinceOfMusicClient;
import com.provinceofmusic.jukebox.NoteSoundMidi;
import com.provinceofmusic.jukebox.NoteSoundMinecraft;
import com.provinceofmusic.listeners.NoteListener;
import com.provinceofmusic.listeners.NoteListenerHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.time.Instant;

public class DebugMode implements NoteListener {
    public static boolean isOn = false;

    public DebugMode(){
        NoteListenerHelper.addListener(this);
    }
    @Override
    public void onNotePlayed(NoteSoundMinecraft note) {
        NoteSoundMidi noteSoundMidi = new NoteSoundMidi(note);
        if(isOn){
            String message = "Note Played " + "Ins: " + (noteSoundMidi.instrument.registeredName) + " Pitch: " + noteSoundMidi.pitch + " Volume: " + noteSoundMidi.volume + " Time: " + Instant.now();
            ProvinceOfMusicClient.LOGGER.info("[DEBUG] " + message);
            assert MinecraftClient.getInstance().player != null;
            MinecraftClient.getInstance().player.sendMessage(Text.of("[DEBUG] " + message), false);
        }
    }
}
