package com.provinceofmusic.recorder;

import com.provinceofmusic.ProvinceOfMusicClient;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundInstanceListener;
import net.minecraft.client.sound.WeightedSoundSet;
import net.minecraft.text.Text;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MusicYoinker implements SoundInstanceListener {

    private static int time_passed = 0;
    private static boolean is_writing_to_file = false;
    private static String file_to_write;

    public static KeyBinding recordBinding;
    @Override
    public void onSoundPlayed(SoundInstance sound, WeightedSoundSet soundSet) {
        //System.out.println(sound.getId());

        //if(sound.getId().toString().equals("minecraft:block.note_block.harp")){
        //    System.out.println(sound.getPitch());
        //}
        //sound.getSound().getIdentifier()

        String sound_category = sound.getCategory().getName().toLowerCase();
        //LOGGER.info("Sound played with category " + sound_category + ".");
        // test if the sound is of record category and if we're recording
        if (!sound_category.equals("record") || !is_writing_to_file) return;
        String[] split_up_name = sound.getId().toString().split("[.]");
        //we don't want to have "block.note.harp", only "harp" for backwards compatibility and easier readability
        //but we need the whole "entity.silverfish.hurt" e.g. for the other, non-noteblock sounds, that are part of the piece

        //if(sound.getId().toString().equals("minecraft:block.note_block.harp")){
        //    ProvinceOfMusicClient.LOGGER.info("Match!!!!!");
        //}
        //else {
        //    ProvinceOfMusicClient.LOGGER.info(sound.getId().toString() + " = " + "minecraft:block.note_block.harp");
        //}

        String name = (split_up_name[0].equals("block") && split_up_name[1].equals("note")) ? split_up_name[2] : sound.getId().toString();
        float pitch = sound.getPitch();
        float volume = sound.getVolume();
        //LOGGER.info("Recording " + name + " at time " + time_passed + " with pitch " + pitch + " and volume " + volume + ".");
        try {
            // we're opening and closing the file every time we add a line to it, performance could be improved maybe
            FileWriter myWriter = new FileWriter(file_to_write, true);
            myWriter.append(name + "," + time_passed + "," + pitch + "," + volume + "\n");
            myWriter.close();
        } catch (IOException e) {
            ProvinceOfMusicClient.LOGGER.error("Error writing to file " + file_to_write + ".");
            e.printStackTrace();
        }
        time_passed = 0;
    }

    public void PassTime(){
        if(is_writing_to_file){
            time_passed++;
        }
    }

    public static void main(){


        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (recordBinding.wasPressed()) {
                //client.player.sendMessage(Text.of("Recording Started"), false);
                is_writing_to_file = !is_writing_to_file;
                if (is_writing_to_file) {
                    file_to_write = "recorded-music/" + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + ".csv";
                    // make a folder if there isn't one
                    File theDir = new File("recorded-music");
                    if (!theDir.exists()) theDir.mkdirs();
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
