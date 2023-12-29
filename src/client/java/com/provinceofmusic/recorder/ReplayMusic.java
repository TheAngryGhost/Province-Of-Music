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
    public static void run(){

        //File f = new File("recorded_music/" + "2023-10-29_14-13-47.csv");
//
        //ArrayList<String> type = new ArrayList<>();
        //ArrayList<String> ticks = new ArrayList<>();
        //ArrayList<String> pitch = new ArrayList<>();
        //ArrayList<String> volume = new ArrayList<>();
//
//
        //// Check if the file exists
        //if (f.exists()) {
        //    // You can now work with the 'file' object
        //    // For example, you can read or manipulate the file
        //    System.out.println("File exists.");
        //    try {
        //        BufferedReader reader = new BufferedReader(new FileReader(f));
        //        String line;
        //        while ((line = reader.readLine()) != null) {
        //            // Split the CSV line by a comma (you might need to adjust this based on your CSV format)
        //            String[] values = line.split(",");
//
        //            // Print each value
        //            //for (int i = 0; i < values.length - 1; i++) {
        //            //    System.out.print(values[i] + ", ");
        //            //}
        //            //System.out.print(values[values.length - 1]);
        //            //System.out.println(); // Move to the next line in the output
//
        //            type.add(values[0]);
        //            ticks.add(values[1]);
        //            pitch.add(values[2]);
        //            volume.add(values[3]);
//
        //            //System.out.println("type: " + values[0] + " ticks: " + values[1] + " pitch: " + values[2] + " volume: " + values[3]);
//
//
        //        }
        //    } catch (FileNotFoundException e) {
        //        throw new RuntimeException(e);
        //    } catch (IOException e) {
        //        throw new RuntimeException(e);
        //    }
        //} else {
        //    System.out.println("File does not exist.");
        //}
//
//
        //if(ticksSinceReset != Integer.parseInt(ticks.get(currentline))){
        //    ticksSinceReset++;
        //    System.out.println(ticksSinceReset);
        //}
        //else{
        //    ticksSinceReset = 0;
        //    //PlayerEntity player = MinecraftClient.getInstance().player;
        //    //String commandText = "playsound minecraft:block.note_block.harp master @p 0 0 0 " + volume.get(currentline) + " " + pitch.get(currentline);
        //    //ParseResults<ServerCommandSource> context;
        //    //context = MinecraftClient.getInstance().getServer().getCommandManager().getDispatcher().parse(new StringReader(commandText), MinecraftClient.getInstance().getServer().getCommandSource());
        //    //MinecraftClient.getInstance().getServer().getCommandManager().execute(context, commandText);
        //    ////MinecraftClient.getInstance().getSoundManager().play();
        //    //System.out.println(commandText);
//
//
        //    System.out.println(type.get(currentline) + " " + ticks.get(currentline) + " " + pitch.get(currentline) + " " + volume.get(currentline));
        //    //MinecraftClient.getInstance().getSoundManager().play();
        //    //Identifier NOTE_BLOCK_HARP_SOUND_ID = new Identifier("minecraft:block.note_block.harp");
        //    Identifier NOTE_BLOCK_HARP_SOUND_ID = new Identifier(type.get(currentline));
        //    //SoundEvent NOTE_BLOCK_HARP_SOUND_EVENT = Registry.SOUND_EVENT.get(NOTE_BLOCK_HARP_SOUND_ID);
        //    //SoundInstance si = new SoundInstance() ;
        //    //TickableSoundInstance
        //    //PositionedSoundInstance.master(CUSTOM_SOUND_ID, 1.0F)
        //    //SoundLoader sl = new SoundLoader();
        //    //sl;
        //    //SoundEvent NOTE_BLOCK_HARP_SOUND_EVENT = new SoundEvent(NOTE_BLOCK_HARP_SOUND_ID, 0, false);
        //    SoundEvent NOTE_BLOCK_HARP_SOUND_EVENT = SoundEvent.of(NOTE_BLOCK_HARP_SOUND_ID);
        //    MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(NOTE_BLOCK_HARP_SOUND_EVENT, Float.parseFloat(pitch.get(currentline))));
//
//
//
        //    currentline++;
        //}
//
        //if(currentline == type.size()){
        //    //dont run anymore
        //}


        //for(int i = 0; i < type.size(); i++){
        //    if(ticksSinceReset != Integer.parseInt(ticks)){
//
        //    }
        //}

        //PlayerEntity player = MinecraftClient.getInstance().player;
        //String commandText = "say helloworld";
        //ParseResults<ServerCommandSource> context;
        //context = MinecraftClient.getInstance().getServer().getCommandManager().getDispatcher().parse(new StringReader(commandText), MinecraftClient.getInstance().getServer().getCommandSource());
        //MinecraftClient.getInstance().getServer().getCommandManager().execute(context, commandText);

        PlayMusic(new File("recorded-music/" + "2023-11-04_15-51-46.csv"));
    }

    public static void PlayMusic(File inputFile){
        Thread playThread = new Thread(){
            //int ticksSinceReset = 0;

            int currentline = 0;
            //int currenttick = 0;

            public void run() {

                //File f = new File("recorded-music/" + "2023-10-29_14-13-47.csv");
                //File f = new File("recorded-music/" + "2023-11-04_15-51-46.csv");
                File f = inputFile;

                ArrayList<String> type = new ArrayList<>();
                ArrayList<String> ticks = new ArrayList<>();
                ArrayList<String> pitch = new ArrayList<>();
                ArrayList<String> volume = new ArrayList<>();

                if (f.exists()) {
                    System.out.println("File exists.");
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
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    System.out.println("File does not exist.");
                }


                //if(ticksSinceReset != Integer.parseInt(ticks.get(currentline))){
                //    ticksSinceReset++;
                //    System.out.println(ticksSinceReset);
                //}
                //else{
                //    ticksSinceReset = 0;


                for(int i = 0; currentline < type.size(); i++){


                    System.out.println(type.get(currentline) + " " + ticks.get(currentline) + " " + pitch.get(currentline) + " " + volume.get(currentline));
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
                    //}
                    //}

                    //i++;

                }
            }
        };

        playThread.start();
    }
}
