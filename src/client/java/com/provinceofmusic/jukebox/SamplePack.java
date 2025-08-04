package com.provinceofmusic.jukebox;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.provinceofmusic.ProvinceOfMusicClient;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;

public class SamplePack {
    public String name = "Unnamed";
    public String author = "None Listed";
    public ArrayList<InstrumentDef> instrumentDefs = new ArrayList<>();

    public ArrayList<File> getInstrumentFiles(){
        File folderTemp = new File(ProvinceOfMusicClient.samplepacksdir + "/" + name + "/" + "samples");
        ArrayList<File> tempFiles = new ArrayList<>();
        File[] files = folderTemp.listFiles();
        assert files != null;
        for (File file : files) {
            tempFiles.add(file);
        }
        return tempFiles;
    }

    public void WriteSamplePack(){

        File folderTemp = new File(ProvinceOfMusicClient.samplepacksdir + "/" + name + "/");
        folderTemp.mkdirs();
        File folderTemp2 = new File(ProvinceOfMusicClient.samplepacksdir + "/" + name + "/" + "samples");
        folderTemp2.mkdirs();
        //File outputFile = new File(ProvinceOfMusicClient.samplepacksdir + "/" + name + "/" + name +".json");
        File outputFile = new File(ProvinceOfMusicClient.samplepacksdir + "/" + name + "/pack.json");
        try {
            FileWriter fileWriter = new FileWriter(outputFile);
            fileWriter.write("null");
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        File jsonTemp = new File(ProvinceOfMusicClient.samplepacksdir + "/" + name + "/pack.json");
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting().serializeNulls();
        Gson gson = builder.create();

        try {
            FileWriter fileWriter = new FileWriter(jsonTemp);
            fileWriter.write(gson.toJson(this));
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static SamplePack getSamplePack(File in) {
        //File jsonTemp = new File(in.getPath() + "/" + in.getName() + ".json");
        File jsonTemp = new File(in.getPath() + "/pack.json");
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting().serializeNulls();
        Gson gson = builder.create();
        SamplePack out;
        try {
            out = gson.fromJson(Files.readString(jsonTemp.toPath(), Charset.defaultCharset()) + "", SamplePack.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        out.name = in.getName();
        ProvinceOfMusicClient.LOGGER.info("Imported SamplePack Successfully");
        return out;
    }

    public static File getSamplePackAsFile(String name){
        return new File(ProvinceOfMusicClient.samplepacksdir + "/" + name);
    }

    public static void DeletePack(String name){

        File file = getSamplePackAsFile(name);
        SamplePack samplePack = getSamplePack(file);

        samplePack.WriteSamplePack();
        try {
            ArrayList<File> filesToDelete = findAllFiles(file);
            for (File f : filesToDelete){
                Files.delete(f.toPath());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void CreateNewPack(){
        SamplePack out = new SamplePack();
        out.WriteSamplePack();
    }
    public static ArrayList<File> findAllFiles(File in){
        ArrayList<File> out = new ArrayList<>();

        if(in.isDirectory()){
            File[] insideFiles = in.listFiles();
            assert insideFiles != null;
            for(File f : insideFiles){
                ArrayList<File> temp = findAllFiles(f);
                out.addAll(temp);
            }
            out.add(in);
        }
        else{
            out.add(in);
        }
        return out;
    }

    public static void RenameSamplePack(SamplePack in, String name){

        File ogFolder = SamplePack.getSamplePackAsFile(in.name);
        //File og = SamplePack.getFile("pack");

        File newFileFolder = new File(ogFolder.getParent(), name);
        //File newFile = new File(og.getParent(), "pack");
        try {
            Files.move(ogFolder.toPath(), newFileFolder.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //File og2 = new File(ProvinceOfMusicClient.samplepacksdir + "/" + name + "/" + in.name + ".json");
        File og2 = new File(ProvinceOfMusicClient.samplepacksdir + "/" + name + "/pack.json");

        //File newFile2 = new File(og2.getParent(), name + ".json");
        File newFile2 = new File(og2.getParent(),"pack.json");
        try {
            Files.move(og2.toPath(), newFile2.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //TODO change this so that non sample packs in the sample packs folder are not read. zips and other stuff
    public static ArrayList<File> FetchSamplePackFiles(){
        ArrayList<File> tempFiles = new ArrayList<>();
        int fileCount = ProvinceOfMusicClient.samplepacksdir.listFiles().length;
        for(int i = 0; i < fileCount; i++){
            tempFiles.add(ProvinceOfMusicClient.samplepacksdir.listFiles()[i]);
        }
        return tempFiles;
    }
}
