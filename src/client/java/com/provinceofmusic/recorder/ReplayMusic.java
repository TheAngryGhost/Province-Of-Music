package com.provinceofmusic.recorder;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.io.*;
import java.util.ArrayList;

public class ReplayMusic {
    static Thread playThread;
    static boolean endMusic = false;

    public static void StopMusic(){
        if(playThread != null){
            endMusic = playThread.isAlive();
        }
    }

    public static void PlayMusic(String inputFile){

        if(playThread != null){
            if(playThread.isAlive()){
                StopMusic();
            }
        }


        if(!endMusic){
        playThread = new Thread(){

                int currentline = 0;

                public void run() {

                    File f = new File(inputFile);

                    ArrayList<String> type = new ArrayList<>();
                    ArrayList<String> ticks = new ArrayList<>();
                    ArrayList<String> pitch = new ArrayList<>();
                    ArrayList<String> volume = new ArrayList<>();

                    if (f.exists()) {
                        try {
                            BufferedReader reader = new BufferedReader(new FileReader(f));
                            String line;
                            while ((line = reader.readLine()) != null) {
                                String[] values = line.split(",");

                                type.add(values[0]);
                                ticks.add(values[1]);
                                pitch.add(values[2]);
                                volume.add(values[3]);
                            }
                            reader.close();
                        }
                        catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }


                    for(int i = 0; currentline < type.size(); i++){

                        Identifier NOTE_BLOCK_HARP_SOUND_ID = Identifier.of(type.get(currentline));
                        SoundEvent NOTE_BLOCK_HARP_SOUND_EVENT = SoundEvent.of(NOTE_BLOCK_HARP_SOUND_ID);
                        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(NOTE_BLOCK_HARP_SOUND_EVENT, Float.parseFloat(pitch.get(currentline)), Float.parseFloat(volume.get(currentline))));

                        currentline++;


                        if(currentline != type.size()){
                            if((Integer.parseInt(ticks.get(currentline))) != 0){
                                try {
                                    Thread.sleep((long)(((float)(Integer.parseInt(ticks.get(currentline)))) * 50));
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }

                        if(endMusic){
                            currentline = type.size();
                        }
                    }
                    endMusic = false;
                }
            };
            playThread.start();

        }
    }
}
