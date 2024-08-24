package com.provinceofmusic.jukebox;

import java.util.ArrayList;
import java.util.Arrays;

public class InstrumentSound {
    public String registeredName;
    public ArrayList<String> remaps = new ArrayList<>();
    public int transpose;
    public int exportChannel;

    public InstrumentSound(){

    }

    public InstrumentSound(String registeredName, ArrayList<String> remaps, int transpose, int exportChannel){
        this.registeredName = registeredName;
        this.remaps = remaps;
        this.transpose = transpose;
        this.exportChannel = exportChannel;
    }

    public InstrumentSound(String registeredName, String[] remaps, int transpose, int exportChannel){
        this.registeredName = registeredName;

        ArrayList<String> temp = new ArrayList<>();
        for (int i = 0; i < remaps.length; i++){
            temp.add(remaps[i]);
        }
        this.remaps = temp;
        this.transpose = transpose;
        this.exportChannel = exportChannel;
    }

    public InstrumentSound(String registeredName, int transpose, int exportChannel){
        this.registeredName = registeredName;
        this.transpose = transpose;
        this.exportChannel = exportChannel;
    }
}
