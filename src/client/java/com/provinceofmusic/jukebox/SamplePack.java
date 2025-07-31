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


    //TODO rewrite this code is complete garbage
    public ArrayList<Sampler> getInstruments(){
        /*
        if(out == null){
            out = new ArrayList<>();
        }
        else{
            out.clear();
        }
        */
        //some strange garbage collector thing
        //TODO maybe clear the reference to the sampler in the receiver could cause ram issues IDK

        /*
        if(out != null && !out.isEmpty()){
            for(Sampler s: out){
                for(SamplerReceiver s2: s.samplerReceivers){
                    //s2.receiver.close();
                    //s2.scheduler.close();
                    s2.receiver.close();
                    s2.receiver = null;
                    s2.sampler = null;
                    s2.scheduler.shutdownNow();
                    s2.scheduler.close();
                }
                s.samplerReceivers.clear();
                //s.scheduler.close();
                s.scheduler.shutdownNow();
                s.samplerReceivers = null;
                s.sample = null;
                //s.samplerReceivers = null;
            }
            NoteReplacer.samplers = null;
            //samplers = null;
            out.clear();
            //samplers = new ArrayList<>();
            return new ArrayList<>();


        }
        */
        ArrayList<Sampler> out = new ArrayList<>();

        for (InstrumentDef instrumentDef : instrumentDefs) {
            File file = new File(ProvinceOfMusicClient.samplepacksdir + "/" + name + "/" + "instrumentfiles" + "/" + instrumentDef.dir);
            if(file.exists()){
                Sampler temp = new Sampler(file);
                out.add(temp);
            }
            else{
                ProvinceOfMusicClient.LOGGER.warn("sf2 file not found File: " + instrumentDef.dir + " Ignoring this instrument");
            }
        }

        return out;
    }

    //TODO rewrite this code is complete garbage and this shouldn't exist because of the other one here ^^^^^ at the top
    public ArrayList<File> getInstrumentFiles(){
        File folderTemp2 = new File(ProvinceOfMusicClient.samplepacksdir + "/" + name + "/" + "instrumentfiles");
        ArrayList<File> tempFiles = new ArrayList<>();
        File[] files = folderTemp2.listFiles();
        assert files != null;
        for (File file : files) {
            tempFiles.add(file);
        }
        return tempFiles;
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
        SamplePack out;
        try {
            out = gson.fromJson(Files.readString(jsonTemp.toPath(), Charset.defaultCharset()) + "", SamplePack.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ProvinceOfMusicClient.LOGGER.info("Imported SamplePack Successfully");
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
}
