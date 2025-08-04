package com.provinceofmusic.background;

import com.provinceofmusic.ProvinceOfMusicClient;
import com.provinceofmusic.jukebox.SamplePack;
import com.provinceofmusic.screen.SamplePackConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class PackUpgrader {
    public static void main(){
        ArrayList<File> samplePackFiles = SamplePack.FetchSamplePackFiles();
        for(File samplePackFile : samplePackFiles){
            for(File innerFile: Objects.requireNonNull(samplePackFile.listFiles())){
                if(innerFile.toString().equals(samplePackFile.toPath() + "\\" + samplePackFile.getName() + ".json")){
                    ProvinceOfMusicClient.LOGGER.debug("Auto upgrading samplepacks to new system: " + samplePackFile.getName());
                    innerFile.renameTo(new File(samplePackFile.toPath() + "\\pack.json"));
                }
                if(innerFile.toString().equals(samplePackFile.toPath() + "\\instrumentfiles")){
                    ProvinceOfMusicClient.LOGGER.debug("Auto upgrading samplepacks to new system: " + samplePackFile.getName());
                    innerFile.renameTo(new File(samplePackFile.toPath() + "\\samples"));
                }


                //System.out.println(innerFile);
                //System.out.println(samplePackFile.toPath() + "\\" + samplePackFile.getName() + ".json");
            }
        }
    }
}
