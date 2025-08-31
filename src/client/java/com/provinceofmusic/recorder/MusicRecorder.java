package com.provinceofmusic.recorder;

import com.provinceofmusic.ProvinceOfMusicClient;
import com.provinceofmusic.jukebox.NoteSoundMinecraft;
import com.provinceofmusic.listeners.NoteListener;
import com.provinceofmusic.listeners.NoteListenerHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MusicRecorder implements NoteListener {

    private static int time_passed = 0;
    private static boolean is_writing_to_file = false;
    private static Path file_to_write;

    public static KeyBinding recordBinding;
    @Override
    public void onNotePlayed(NoteSoundMinecraft note) {
        if (!is_writing_to_file) return;

        //float pitchValue = NoteListenerHelper.convertPitchMidiToMinecraft(pitch, instrument);

        try {
            FileWriter myWriter = new FileWriter(file_to_write.toString(), true);
            myWriter.append(note.instrument + "," + time_passed + "," + note.pitch + "," + note.volume + "\n");
            myWriter.close();
        } catch (IOException e) {
            ProvinceOfMusicClient.LOGGER.error("Error writing to file " + file_to_write + ".");
            throw new RuntimeException(e);
        }
        time_passed = 0;
    }

    public void PassTime(){
        if(is_writing_to_file){
            time_passed++;
        }
    }

    public void main(){
        NoteListenerHelper.addListener(this);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (recordBinding.wasPressed()) {
                is_writing_to_file = !is_writing_to_file;
                assert client.player != null;
                if (is_writing_to_file) {
                    file_to_write = Path.of(ProvinceOfMusicClient.recordedmusicdir + "/" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + ".csv");
                    ProvinceOfMusicClient.LOGGER.info("Started recording to file " + file_to_write + ".");
                    client.player.sendMessage(Text.of("Started recording to file " + file_to_write + "."), false);
                } else {
                    time_passed = 0;
                    ProvinceOfMusicClient.LOGGER.info("Stopped recording to file " + file_to_write + ".");
                    client.player.sendMessage(Text.of("Stopped recording to file " + file_to_write + "."), false);

                }
            }
        });
    }
}
