package com.provinceofmusic.jukebox;

public class InstrumentRemap {
    public String remapSoundName;
    public int additionalTranspose;

    public InstrumentRemap(String remapSoundName){
        this.remapSoundName = remapSoundName;
    }

    public InstrumentRemap(String remapSoundName, int additionalTranspose){
        this.remapSoundName = remapSoundName;
        this.additionalTranspose = additionalTranspose;
    }
}
