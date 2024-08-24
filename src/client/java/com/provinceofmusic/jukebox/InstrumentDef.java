package com.provinceofmusic.jukebox;

public class InstrumentDef {
    public String noteType;

    public int transpose = 0;

    public float volume = 1f;
    public String dir;

    public boolean singlePitch = false;

    public InstrumentDef(String dir, String noteType, int transpose, float volume, boolean singlePitch){
        this.dir = dir;
        this.volume = volume;
        this.transpose = transpose;
        this.noteType = noteType;
        this.singlePitch = singlePitch;
    }
}
