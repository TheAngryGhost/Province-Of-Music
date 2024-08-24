package com.provinceofmusic.jukebox;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.provinceofmusic.ProvinceOfMusicClient;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.util.ArrayList;

public class SamplePack {
    public String name = "Unnamed";
    public String author = "None Listed";
    public ArrayList<InstrumentDef> instrumentDefs = new ArrayList<>();

    public ArrayList<Instrument> getInstruments(){
        ArrayList<Instrument> out = new ArrayList<>();
        for(int i = 0; i < instrumentDefs.size(); i++){
            Instrument temp = new Instrument(new File(ProvinceOfMusicClient.samplepacksdir + "/" + name + "/" + "instrumentfiles" + "/" + instrumentDefs.get(i).dir), instrumentDefs.get(i).noteType, instrumentDefs.get(i).transpose, instrumentDefs.get(i).volume, instrumentDefs.get(i).singlePitch);
            out.add(temp);
        }
        return out;
    }

    public ArrayList<Instrument> getInstruments(ArrayList<Instrument> out){
        if(out == null){
            out = new ArrayList<>();
        }
        else{
            out.clear();
        }
        for(int i = 0; i < instrumentDefs.size(); i++){
            Instrument temp = new Instrument(new File(ProvinceOfMusicClient.samplepacksdir + "/" + name + "/" + "instrumentfiles" + "/" + instrumentDefs.get(i).dir), instrumentDefs.get(i).noteType, instrumentDefs.get(i).transpose, instrumentDefs.get(i).volume, instrumentDefs.get(i).singlePitch);
            out.add(temp);
        }
        return out;
    }



    public void WriteSamplePack(){

        File folderTemp = new File(ProvinceOfMusicClient.samplepacksdir + "/" + name + "/");
        folderTemp.mkdirs();
        File folderTemp2 = new File(ProvinceOfMusicClient.samplepacksdir + "/" + name + "/" + "instrumentfiles");
        folderTemp2.mkdirs();
        File outputFile = new File(ProvinceOfMusicClient.samplepacksdir + "/" + name + "/" + name +".json");
        try {
            FileWriter fileWriter = new FileWriter(outputFile);
            fileWriter.write("null");
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }














        File jsonTemp = new File(ProvinceOfMusicClient.samplepacksdir + "/" + name + "/" + name +".json");
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
        File jsonTemp = new File(in.getPath() + "/" + in.getName() + ".json");
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting().serializeNulls();
        Gson gson = builder.create();
        SamplePack out = null;
        try {
            out = gson.fromJson(Files.readString(jsonTemp.toPath(), Charset.defaultCharset()) + "", SamplePack.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Imported Successfully");
        return out;
    }

    public static File getFile(String name){
        return new File(ProvinceOfMusicClient.samplepacksdir + "/" + name);
    }

    public static void DeletePack(String name){

        File file = getFile(name);
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

        File og = SamplePack.getFile(in.name);

        File newFile = new File(og.getParent(), name);
        try {
            Files.move(og.toPath(), newFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        File og2 = new File(ProvinceOfMusicClient.samplepacksdir + "/" + name + "/" + in.name + ".json");

        File newFile2 = new File(og2.getParent(), name + ".json");
        try {
            Files.move(og2.toPath(), newFile2.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<File> getInstrumentFiles(){
        File folderTemp2 = new File(ProvinceOfMusicClient.samplepacksdir + "/" + name + "/" + "instrumentfiles");
        ArrayList<File> tempFiles = new ArrayList<>();
        int fileCount = folderTemp2.listFiles().length;
        for(int i = 0; i < fileCount; i++){
            tempFiles.add(folderTemp2.listFiles()[i]);
        }
        return tempFiles;
    }
}
