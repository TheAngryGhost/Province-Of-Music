package com.provinceofmusic.jukebox;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.provinceofmusic.ProvinceOfMusicClient;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

public class PlayRuleSheet {
    public File sheet;
    public ArrayList<PlayRule> rules;
    public ArrayList<File> tracks;

    public ArrayList<String> tracksNames;

    public static PlayRuleSheet getSheetFromName(String nameIn) throws IOException {
        System.out.println(nameIn);
        PlayRuleSheet outputSheet = new PlayRuleSheet();
        outputSheet.sheet = new File(ProvinceOfMusicClient.playrulesheetsdir + "/" + nameIn);
        File jsonTemp = new File(ProvinceOfMusicClient.playrulesheetsdir + "/" + nameIn + "/" + nameIn +".json");
        outputSheet.rules = new ArrayList<>();
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting().serializeNulls();
        Gson gson = builder.create();
        //FileReader fileReader = new FileReader(jsonTemp);
        //outputSheet.rules = gson.fromJson(fileReader.toString(), PlayRule[].class);
        //Files.readString(jsonTemp.toPath(), Charset.defaultCharset());
        //PlayRule[] list = new PlayRule[2];
        //PlayRule[] testRule2 = gson.fromJson(gson.toJson(list), PlayRule[].class);
        PlayRule[] rulesArr = gson.fromJson(Files.readString(jsonTemp.toPath(), Charset.defaultCharset()) + "", PlayRule[].class);
        for(int i = 0; i < rulesArr.length; i++){
            outputSheet.rules.add(rulesArr[i]);
        }


        outputSheet.tracks = FetchTracksFromFile(outputSheet.sheet);

        outputSheet.tracksNames = new ArrayList<>();

        for (int i = 0; i < outputSheet.tracks.size(); i++){
            String name = outputSheet.tracks.get(i).getName();
            name = name.substring(0, outputSheet.tracks.get(i).getName().indexOf(".wav"));
            System.out.println(name);
            outputSheet.tracksNames.add(name);
        }


        //String jsonDir = fileIn.getParentFile().getPath() + "\\" + fileIn.getName() + ".json";
        //System.out.println(jsonDir);


        //PlayRule[] list = new PlayRule[2];
        //GsonBuilder builder = new GsonBuilder();
        //builder.setPrettyPrinting().serializeNulls();
        //Gson gson = builder.create();
        //System.out.println(gson.toJson(list));
        //PlayRule[] testRule2 = gson.fromJson(gson.toJson(list), PlayRule[].class);
        //System.out.println(testRule2[0].ruleName);
        return outputSheet;
    }

    public int getRuleIndexFromName(String nameIn){
        for(int i = 0; i < rules.size(); i++){
            if(rules.get(i).ruleName.equals(nameIn)){
                return i;
            }
        }
        return -1;
    }

    public static void createRuleSheet(String nameIn){
        File folderTemp = new File(ProvinceOfMusicClient.playrulesheetsdir + "/" + nameIn + "/" + "tracks");
        folderTemp.mkdirs();
        File outputFile = new File(ProvinceOfMusicClient.playrulesheetsdir + "/" + nameIn + "/" + nameIn +".json");
        try {
            FileWriter fileWriter = new FileWriter(outputFile);
            fileWriter.write("null");
            //fileWriter.write("empy");
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //PlayRuleSheet.getSheetFromFile(outputFile);
    }

    public static void writeRuleSheet(String nameIn, PlayRuleSheet sheetIn){
        //PlayRuleSheet outputSheet = new PlayRuleSheet();
        //outputSheet.sheet = new File(ProvinceOfMusicClient.playrulesheetsdir + "");
        File jsonTemp = new File(ProvinceOfMusicClient.playrulesheetsdir + "/" + nameIn + "/" + nameIn +".json");
        //outputSheet.rules = new ArrayList<>();
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting().serializeNulls();
        Gson gson = builder.create();
        //gson.toJson(sheetIn);



        PlayRule[] rulesArr = new PlayRule[sheetIn.rules.size()];
        for(int i = 0; i < rulesArr.length; i++){
            rulesArr[i] = sheetIn.rules.get(i);
        }

        try {
            FileWriter fileWriter = new FileWriter(jsonTemp);
            fileWriter.write(gson.toJson(rulesArr));
            //fileWriter.write("empy");
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        //FileReader fileReader = new FileReader(jsonTemp);
        //outputSheet.rules = gson.fromJson(fileReader.toString(), PlayRule[].class);
        //Files.readString(jsonTemp.toPath(), Charset.defaultCharset());
        //outputSheet.rules = gson.fromJson(Files.readString(jsonTemp.toPath(), Charset.defaultCharset()) + "", (Type) ArrayList.class);
    }

    //public static File fileFromPlayRuleSheet(PlayRuleSheet playRuleSheetIn){
    //    return new File();
    //}

    public static ArrayList<File> FetchTracksFromFile(File fileDir){
        ArrayList<File> tempFiles = new ArrayList<>();
        //System.out.println(fileDir.getPath() + "\\tracks");
        int fileCount = new File(fileDir.getPath() + "\\tracks").listFiles().length;
        //System.out.println(fileCount);
        for(int i = 0; i < fileCount; i++){
            File temp = new File(fileDir.getPath() + "\\tracks").listFiles()[i];
            if(temp.getPath().indexOf(".wav") != -1){
                tempFiles.add(temp);
            }
        }
        return tempFiles;
    }

    public String getName(){
        //System.out.println();
        //return sheet.getName().substring(0,sheet.getName().indexOf(".json"));
        return sheet.getName();
    }

}
