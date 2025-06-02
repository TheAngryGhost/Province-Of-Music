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

        ArrayList<String> types = new ArrayList<>();
        ArrayList<String> ticks = new ArrayList<>();
        ArrayList<String> pitches = new ArrayList<>();
        ArrayList<String> volumes = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                types.add(values[0]);
                ticks.add(values[1]);
                pitches.add(values[2]);
                volumes.add(values[3]);
            }
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
            int noteTick = Integer.parseInt(ticks.get(i));
            float notePitch = Float.parseFloat(pitches.get(i));
            float noteVolume = Float.parseFloat(volumes.get(i));

            InstrumentSound instrumentSound = NoteListenerHelper.SoundIdToInstrumentSound(noteType);

            currentTick += noteTick;

            assert instrumentSound != null;
            int insertNoteChannel = instrumentSound.exportChannel;
            int insertNotePitch = (int) NoteListenerHelper.convertPitchMinecraftToMidi(notePitch, noteType);
            int insertNoteVelocity =  (int) (noteVolume * 100f);
            int insertNoteTick = currentTick * (240 / 6);
            int insertNoteDuration = 120;

            noteTrack.insertNote(insertNoteChannel, insertNotePitch, insertNoteVelocity, insertNoteTick, insertNoteDuration);
        }

        List<MidiTrack> tracks = new ArrayList<>();
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
            throw new RuntimeException(e);
        }
    }
}
