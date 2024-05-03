package com.provinceofmusic.jukebox;


import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;

import static java.lang.Thread.sleep;


public class MusicSoundThread implements Runnable {


    private AudioInputStream ais;
    private static Clip clip;
    private FloatControl gain;

    private Thread musicthread;
    private Thread initthread;

    static boolean songSet = false;
    boolean loop = false;

    //public int runpos;

    //public boolean isRunning;


    //public static void main(String[] args) throws Exception {
    //    MusicSoundThread s = new MusicSoundThread(new MusicSound("wynnjukebox/Music/L.mp3"));
    //}


    public MusicSoundThread() {
        //if (musicthread == null || !musicthread.isAlive()) {
            musicthread = new Thread((Runnable) this);
            musicthread.start();
        //}
    }

    public float getVolume() {
        if (songSet) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            return (float) Math.pow(10f, gainControl.getValue() / 20f);
        }
        return -1f;
    }

    public void setVolume(float volume) {
        if (songSet) {
            if (volume < 0f || volume > 1f)
                throw new IllegalArgumentException("Volume not valid: " + volume);
            //FloatControl.Type.
            if(clip.isControlSupported(FloatControl.Type.MASTER_GAIN)){
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(20f * (float) Math.log10(volume));
            }
            else{
                System.out.println("control not supported");
            }
        }
    }


    public void reset() {
        if (songSet) {
            try {
                ais.reset();
            } catch (IOException e) {
                System.err.println("IO error when resetting");
            }

        }
    }

    public void play() throws LineUnavailableException, IOException {
        //clip.open(ais);
        if (songSet) {
            //if (musicthread == null || !musicthread.isAlive()) {
            //    musicthread = new Thread((Runnable) this);
            //    musicthread.start();
            //this.run();
            System.out.println("test");
            clip.start();
            //}
            //clip.start();
        }
    }

    @Override
    public void run() {
        while (true) {
            if (songSet) {
                //System.out.println("on");
                if (loop) {
                    clip.loop(Integer.MAX_VALUE);
                    //System.out.println("test2");
                } else {
                    clip.loop(0);
                }
            }
            else{
                try {
                    sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                //System.out.println("");
            }
                //    //musicthread = new Thread((Runnable) this);
                //    //musicthread.start();
                    //System.out.println("test");
                //    //clip.start();
                //}

                //if (true) {
                //    clip.loop(Integer.MAX_VALUE);
                //    System.out.println("test2");
                //} else {
                //    clip.loop(0);
                //}
            //}
        }
    }

    public static void stop() {
        if (songSet) {
            //clip.close();
            clip.flush();
            songSet = false;
        }
    }

    public int getRunpos() {
        if (songSet) {
            return clip.getFramePosition();
        }
        return -1;
    }

    public boolean isRunning() {


        //while(clip.getMicrosecondLength() != clip.getMicrosecondPosition())

        return clip.getMicrosecondLength() != clip.getMicrosecondPosition();
    }

    public void setLoop(boolean x) {
        //if (songSet){
            loop = x;
            //if (x) {
            //    clip.loop(Integer.MAX_VALUE);
            //} else {
            //    clip.loop(0);
            //}
        //}
    }

    public void SetSong(MusicSound soundFile) {
        File filefound = MusicSound.toFile(soundFile);
        System.out.println(filefound);

        //System.out.println(soundFile.filetype);
        //if (soundFile.filetype.equals("mp3")){
        //    Converter c = new Converter(new FileInputStream(MusicSound.toFile(soundFile)));
        //    c.to(new FileOutputStream(soundFile.path + soundFile.name + "." + "wav"));
        //    System.out.println("done");
//
        //    filefound = new File(soundFile.path + soundFile.name + "." + "wav");
        //}


        try {
            ais = AudioSystem.getAudioInputStream(filefound);
            clip = AudioSystem.getClip();
            clip.open(ais);
            //gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        } catch (UnsupportedAudioFileException e) {
            System.err.println("File format not accepted");
        } catch (IOException e) {
            System.err.println("IO error");
        } catch (LineUnavailableException e) {
            System.err.println("Line unavailable");
        }

        songSet = true;

        //if (!musicthread.isAlive()){
        //this.run();
        //}
    }

    public void SwitchSong(MusicSound soundFile) {
        Thread t = new Thread() {
            float v = 0f;

            public void run() {
                if(songSet){
                    v = 1f;
                    for (int i = 0; i < 100; i++) {
                        if (v - 0.01f > 0.1) {
                            try {
                                sleep(100);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            v = v - 0.01f;
                            //System.out.println(v);
                            setVolume(v);
                        } else {

                            setVolume(0);
                            v = 0;
                            i = 100;
                        }
                    }
                    MusicSoundThread.stop();
                }
                //setVolume(0);
                SetSong(soundFile);
                setVolume(0);
                setLoop(true);
                songSet = true;
                try {
                    play();
                } catch (LineUnavailableException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                for (int i = 0; i < 100; i++) {
                    if (v + 0.01f < 0.9) {
                        try {
                            sleep(100);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        v = v + 0.01f;
                        setVolume(v);
                    } else {

                        setVolume(1);
                        i = 100;
                    }
                }
            }
        };
//
        t.start();
    }
}
