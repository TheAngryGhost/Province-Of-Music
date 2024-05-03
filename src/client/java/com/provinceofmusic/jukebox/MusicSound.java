package com.provinceofmusic.jukebox;//import org.jaudiotagger.audio.AudioFile;
//import org.jaudiotagger.audio.AudioFileIO;
//import org.jaudiotagger.audio.mp3.MP3File;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;


public class MusicSound {
    public String name;
    public String path;
    public String filetype;

    //public int startLoopMin = -1;
    //public int startLoopSec = -1;
    //public int startLoopMilliSec = -1;
//
    //public int endLoopMin = -1;
    //public int endLoopSec = -1;
    //public int endLoopMilliSec = -1;

    public long startMillisecond = 0;

    public long endMillisecond = -1;

    //boolean fadeOutOnLoop = false;
    boolean fastSwitch = false;

    boolean loop = true;

    public MusicSound(String name, String filetype, String path){
        this.name = name;
        this.path = path;
        this.filetype = filetype;
    }



    public MusicSound(String fullpath){

        String[] parts = fullpath.split("[/.]");
        System.out.println(parts.length);
        if(parts.length < 2){
            this.name = parts[0];
            this.path = "";
            this.filetype = parts[1];
        }
        else{
            String combinedpath = "";
            for(int i = 0; i < parts.length - 2; i++){
                combinedpath = combinedpath + parts[i] + "/";
            }
            this.path = combinedpath;
            this.name = parts[parts.length - 2];
            this.filetype = parts[parts.length - 1];
        }
    }

    public static File toFile(MusicSound ms){
        return new File(ms.path + ms.name + "." + ms.filetype);
    }

    public long getEndPointOg() throws UnsupportedAudioFileException, IOException {
        AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(MusicSound.toFile(this));

        long durationMicroseconds = (long) fileFormat.properties().get("duration");
        long durationMilliSeconds = durationMicroseconds / 1_000; // Convert microseconds to seconds
        //System.out.println("Duration: " + durationSeconds + " seconds");
        return durationMilliSeconds;
    }

    public long GetStartOffset(){
        if(startMillisecond == -1){
            return 0;
        }
        else{
            return startMillisecond;
        }
    }

    public long GetEndOffset() {
        if(endMillisecond == -1){
            //try{
            //return getEndPointOg();
//
            //}catch (UnsupportedAudioFileException | IOException e){
            //    e.printStackTrace();
            return -1;
            //}
        }
        else{
            return endMillisecond;
        }
    }

    //public long getBytesFromMillisecond(long Milliseconds) throws UnsupportedAudioFileException, IOException {
    //    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(MusicSound.toFile(this));
    //    long bytesPerMillisecond = audioInputStream.getFormat().getFrameSize() * Math.round(audioInputStream.getFormat().getFrameRate() / 1000);
    //    System.out.println("    "+audioInputStream.getFormat().getFrameRate());
    //    return bytesPerMillisecond * Milliseconds;
    //}

    //Made by ChatGPT
    //public long getBytesFromMillisecond(long milliseconds) {
    //    try {
    //        File audioFile = MusicSound.toFile(this);
    //        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
    //        int frameSize = audioInputStream.getFormat().getFrameSize();
    //        float frameRate = audioInputStream.getFormat().getFrameRate();
    //        long bytesPerMillisecond = frameSize != AudioSystem.NOT_SPECIFIED && frameRate != AudioSystem.NOT_SPECIFIED && frameRate != 0 ?
    //                (long) (frameSize * (frameRate / 1000)) : 0;
//
    //        return bytesPerMillisecond != 0 ? milliseconds * bytesPerMillisecond : 0;
    //    } catch (Exception e) {
    //        e.printStackTrace();
    //        return 0;
    //    }
    //}


    //public long getBytesFromMillisecond(long milliseconds) {
    //    try {
    //        File audioFile = MusicSound.toFile(this);
    //        AudioFile audioFile2 = AudioFileIO.read(audioFile);
    //        long bitrate = ((MP3File) audioFile2).getMP3AudioHeader().getBitRateAsNumber();
    //        System.out.println(bitrate + "bitrate");
//
    //        return (long) ((bitrate * 0.125f) * milliseconds);
    //    } catch (Exception e) {
    //        e.printStackTrace();
    //        //return 0;
    //        return 0;
    //    }
    //}
//
    //public long getMillisecondsFromBytes(long bytes) {
    //    try {
    //        File audioFile = MusicSound.toFile(this);
    //        AudioFile audioFile2 = AudioFileIO.read(audioFile);
    //        long bitrate = ((MP3File) audioFile2).getMP3AudioHeader().getBitRateAsNumber();
    //        System.out.println(bitrate + "bitrate");
//
    //        return (long) ((bytes / (bitrate * 0.125f)));
    //    } catch (Exception e) {
    //        e.printStackTrace();
    //        return 0;
    //    }
    //}

}
