package com.provinceofmusic.recorder;

import com.provinceofmusic.ProvinceOfMusicClient;
import com.provinceofmusic.jukebox.InstrumentSound;
import com.provinceofmusic.listeners.NoteListener;
import com.provinceofmusic.listeners.NoteListenerHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.time.Instant;

public class DebugMode implements NoteListener {
    boolean isOn = false;

    public DebugMode(){
        NoteListenerHelper.addListener(this);
    }
    @Override
    public void onNotePlayed(InstrumentSound instrument, int ticksPassed, float pitch, int volume) {
        if(isOn){
            String message = "Note Played " + "Ins: " + instrument.registeredName + " Pitch: " + pitch + " Volume: " + volume + " Time: " + Instant.now();
            ProvinceOfMusicClient.LOGGER.info("[DEBUG] " + message);
            assert MinecraftClient.getInstance().player != null;
            MinecraftClient.getInstance().player.sendMessage(Text.of("[DEBUG] " + message), false);
        }
    }
}
