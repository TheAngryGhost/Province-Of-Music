package com.provinceofmusic.jukebox;

import java.util.ArrayList;
import java.util.Collections;

public class InstrumentSound {
    public String registeredName;
    public ArrayList<InstrumentRemap> remaps = new ArrayList<>();
    public int transpose;
    public int exportChannel;

    public InstrumentSound(){

    }

    public InstrumentSound(String registeredName, ArrayList<InstrumentRemap> remaps, int transpose, int exportChannel){
        this.registeredName = registeredName;
        this.remaps = remaps;
        this.transpose = transpose;
        this.exportChannel = exportChannel;
    }

    public InstrumentSound(String registeredName, InstrumentRemap[] remaps, int transpose, int exportChannel){
        this.registeredName = registeredName;

        ArrayList<InstrumentRemap> temp = new ArrayList<>();
        Collections.addAll(temp, remaps);
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
