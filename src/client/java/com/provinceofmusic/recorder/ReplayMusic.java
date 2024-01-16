package com.provinceofmusic.recorder;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.io.*;
import java.util.ArrayList;

public class ReplayMusic {

    //static int ticksSinceReset = 0;

    //static int currentline = 0;
    //int currenttick = 0;

    static Thread playThread;
    static boolean endMusic = false;
    //public static void run(){
//
    //    if(playThread == null){
    //        PlayMusic(new File("recorded-music/" + "2023-11-04_15-51-46.csv"));
    //    }
    //    else{
    //        System.out.println("isn't null");
    //        if(playThread.isAlive()){
    //            //playThread.stop();
    //            System.out.println("tried to stop");
    //        }
    //        else{
    //            PlayMusic(new File("recorded-music/" + "2023-11-04_15-51-46.csv"));
    //        }
    //    }
//
//
    //}

    public static void StopMusic(){
        if(playThread != null){
            if(!playThread.isAlive()){
                endMusic = false;
            }
            else {
                endMusic = true;
            }
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
                //int ticksSinceReset = 0;

                int currentline = 0;
                //int currenttick = 0;

                public void run() {

                    //File f = new File("recorded-music/" + "2023-10-29_14-13-47.csv");
                    //File f = new File("recorded-music/" + "2023-11-04_15-51-46.csv");
                    File f = new File(inputFile);

                    ArrayList<String> type = new ArrayList<>();
                    ArrayList<String> ticks = new ArrayList<>();
                    ArrayList<String> pitch = new ArrayList<>();
                    ArrayList<String> volume = new ArrayList<>();

                    if (f.exists()) {
                        //System.out.println("File exists.");
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
                        catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                        catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else {
                        //System.out.println("File does not exist.");
                    }

                    //f = new File("");


                    //if(ticksSinceReset != Integer.parseInt(ticks.get(currentline))){
                    //    ticksSinceReset++;
                    //    System.out.println(ticksSinceReset);
                    //}
                    //else{
                    //    ticksSinceReset = 0;


                    for(int i = 0; currentline < type.size(); i++){


                        //System.out.println(type.get(currentline) + " " + ticks.get(currentline) + " " + pitch.get(currentline) + " " + volume.get(currentline));
                        Identifier NOTE_BLOCK_HARP_SOUND_ID = new Identifier(type.get(currentline));
                        SoundEvent NOTE_BLOCK_HARP_SOUND_EVENT = SoundEvent.of(NOTE_BLOCK_HARP_SOUND_ID);
                        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(NOTE_BLOCK_HARP_SOUND_EVENT, Float.parseFloat(pitch.get(currentline))));



                        //currentline++;
                        //}

                        //if(currentline == type.size()){
                        //    System.out.println("Uh oh");
                        //}

                        currentline++;


                        if(currentline != type.size()){
                            try {
                                Thread.sleep((long)(((float)(Integer.parseInt(ticks.get(currentline)))) * 50));
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        if(endMusic){
                            currentline = type.size();
                        }
                        //}
                        //}

                        //i++;

                    }
                    endMusic = false;
                }
            };
            playThread.start();

        }
    }
}
