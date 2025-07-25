package com.provinceofmusic.download;

import com.provinceofmusic.ProvinceOfMusicClient;
import com.provinceofmusic.jukebox.NoteReplacer;
import com.provinceofmusic.jukebox.SamplePack;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class WMRUpdater {

    public static String link = "https://www.dropbox.com/scl/fi/rfis4jx5yk9cztarchfkf/Wynn-Music-Remastered-1.2.0.zip?rlkey=iwxzvw9atj8f1z5rbre3z978g&st=gi0vujwc&dl=1";
    public static String currentVersion = "Wynn Music Remastered 1.2.0";

    public static void download() {
        // Your direct download link
        String fileUrl = link;
        String destination = "provinceofmusic/downloaded.zip/";

        try (BufferedInputStream in = new BufferedInputStream(new URL(fileUrl).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(destination)) {

            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
            ProvinceOfMusicClient.LOGGER.debug("Download completed: " + destination);
        } catch (IOException e) {
            ProvinceOfMusicClient.LOGGER.error("Download failed: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        String zipFilePath = destination; // The .zip file
        String destDirectory = "provinceofmusic/samplepacks";     // Folder to extract to

        try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry = zipIn.getNextEntry();

            while (entry != null) {
                String filePath = destDirectory + File.separator + entry.getName();

                if (!entry.isDirectory()) {
                    // If the entry is a file, extract it
                    File parentDir = new File(filePath).getParentFile();
                    if (parentDir != null && !parentDir.exists()) {
                        parentDir.mkdirs(); // create missing parent dirs
                    }

                    try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath))) {
                        byte[] bytesIn = new byte[4096];
                        int read;
                        while ((read = zipIn.read(bytesIn)) != -1) {
                            bos.write(bytesIn, 0, read);
                        }
                    }
                } else {
                    // If the entry is a directory, make the directory
                    File dir = new File(filePath);
                    dir.mkdirs();
                }

                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }

            ProvinceOfMusicClient.LOGGER.debug("Unzip complete.");
        } catch (IOException e) {
            ProvinceOfMusicClient.LOGGER.error("Unzip failed: " + e.getMessage());
            e.printStackTrace();
            try {
                Files.delete(Path.of(destination));
            } catch (IOException e2) {
                throw new RuntimeException(e2);
            }
            return;
        }

        ProvinceOfMusicClient.configSettings.activeSamplePack = currentVersion;
        ProvinceOfMusicClient.saveConfigSettings();

        if(ProvinceOfMusicClient.configSettings.activeSamplePack != null){
            NoteReplacer.interupt = true;
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    NoteReplacer.samplers = SamplePack.getSamplePack(SamplePack.getFile(ProvinceOfMusicClient.configSettings.activeSamplePack)).getInstruments(NoteReplacer.samplers);
                    NoteReplacer.interupt = false;
                }
            };

            Timer timer = new Timer(true);
            timer.schedule(task, 300);
        }

        try {
            Files.delete(Path.of(destination));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        File[] files = ProvinceOfMusicClient.samplepacksdir.listFiles();
        assert files != null;
        for(File file : files){
            if(file.getName().contains("Wynn Music Remastered") && !file.getName().equals(currentVersion)){
                try {
                    ArrayList<File> filesToDelete = SamplePack.findAllFiles(file);
                    for (File f : filesToDelete){
                        Files.delete(f.toPath());
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        NoteReplacer.replaceMusic = true;
    }
}
