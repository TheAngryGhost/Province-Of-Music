package com.provinceofmusic.background;

import com.provinceofmusic.ProvinceOfMusicClient;
import com.provinceofmusic.jukebox.SamplePack;
import com.provinceofmusic.screen.SamplePackConfig;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class PackUpgrader {
    public static void main(){
        ArrayList<File> samplePackFiles = SamplePack.FetchSamplePackFiles();
        for(File samplePackFile : samplePackFiles){
            for(File innerFile: Objects.requireNonNull(samplePackFile.listFiles())){
                if(innerFile.toPath().equals(Path.of(samplePackFile.toPath() + "/" + samplePackFile.getName() + ".json"))){
                    ProvinceOfMusicClient.LOGGER.debug("Auto upgrading samplepacks to new system: " + samplePackFile.getName());
                    innerFile.renameTo(new File(Path.of(samplePackFile.toPath() + "/pack.json").toString()));
                }
                if(innerFile.toPath().equals(Path.of(samplePackFile.toPath() + "/instrumentfiles"))){
                    ProvinceOfMusicClient.LOGGER.debug("Auto upgrading samplepacks to new system: " + samplePackFile.getName());
                    innerFile.renameTo(new File(Path.of(samplePackFile.toPath() + "/samples").toString()));
                }


                //System.out.println(innerFile);
                //System.out.println(samplePackFile.toPath() + "\\" + samplePackFile.getName() + ".json");
            }
        }
    }
}
