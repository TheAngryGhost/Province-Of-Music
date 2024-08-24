package com.provinceofmusic.recorder;

import com.provinceofmusic.jukebox.InstrumentSound;
import com.provinceofmusic.listeners.NoteListenerHelper;
import com.provinceofmusic.midi.MidiFile;
import com.provinceofmusic.midi.MidiTrack;
import com.provinceofmusic.midi.event.meta.Tempo;
import com.provinceofmusic.midi.event.meta.TimeSignature;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ConvertToMidi {

    public static void convert(File inputFile, String outputPath){
        File f = inputFile;

        ArrayList<String> types = new ArrayList<>();
        ArrayList<String> ticks = new ArrayList<>();
        ArrayList<String> pitches = new ArrayList<>();
        ArrayList<String> volumes = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(f));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                types.add(values[0]);
                ticks.add(values[1]);
                pitches.add(values[2]);
                volumes.add(values[3]);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        MidiTrack tempoTrack = new MidiTrack();
        MidiTrack noteTrack = new MidiTrack();

        TimeSignature ts = new TimeSignature();
        ts.setTimeSignature(4, 4, TimeSignature.DEFAULT_METER, TimeSignature.DEFAULT_DIVISION);

        Tempo tempo = new Tempo();
        tempo.setBpm(100);

        tempoTrack.insertEvent(ts);
        tempoTrack.insertEvent(tempo);

        int currentTick = 0;
        for(int i = 0; i < types.size(); i++){
            String noteType = types.get(i);
            int noteTick = Integer.valueOf(ticks.get(i));
            float notePitch = Float.valueOf(pitches.get(i));
            float noteVolume = Float.valueOf(volumes.get(i));


            int instrumentSoundIndex = -1;
            InstrumentSound instrumentSound = null;
            for(int j = 0; j < NoteListenerHelper.instrumentSounds.size(); j++){
                InstrumentSound tempSound = NoteListenerHelper.instrumentSounds.get(j);
                if(tempSound.registeredName.equals(noteType)){
                    instrumentSound = tempSound;
                    instrumentSoundIndex = j;
                }
                else {
                    for (String tempSound2 : tempSound.remaps) {
                        if(tempSound2.equals(noteType)){
                            instrumentSound = tempSound;
                            instrumentSoundIndex = j;
                        }
                    }
                }
            }
            if(instrumentSound == null){
                return;
            }
            else{
            }

            currentTick += noteTick;

            int insertNoteChannel = instrumentSoundIndex;
            int insertNotePitch = (int) (((((log2(notePitch) * 12f) + 66.5f) - 1) + 0.5f) + instrumentSound.transpose);
            int insertNoteVelocity =  (int) (noteVolume * 100f);
            int insertNoteTick = currentTick * (240 / 6);
            int insertNoteDuration = 120;

            noteTrack.insertNote(insertNoteChannel, insertNotePitch, insertNoteVelocity, insertNoteTick, insertNoteDuration);
        }

        List<MidiTrack> tracks = new ArrayList<MidiTrack>();
        tracks.add(tempoTrack);
        tracks.add(noteTrack);

        MidiFile midi = new MidiFile(MidiFile.DEFAULT_RESOLUTION, tracks);

        File output = new File(outputPath + ".mid");
        try
        {
            midi.writeToFile(output);
        }
        catch(IOException e)
        {
            System.err.println(e);
        }
    }

    public static float log2(float N)
    {

        // calculate log2 N indirectly
        // using log() method
        float result = (float) (Math.log(N) / Math.log(2));

        return result;
    }
}
