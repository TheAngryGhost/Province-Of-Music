package com.provinceofmusic.ui;

import com.provinceofmusic.ProvinceOfMusicClient;
import com.provinceofmusic.jukebox.InstrumentDef;
import com.provinceofmusic.jukebox.SamplePack;
import com.provinceofmusic.screen.NoteTypePickerScreen;
import com.provinceofmusic.screen.SF2FilePickerScreen;
import com.provinceofmusic.screen.SamplePackEditor;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.widget.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class InstrumentWidget extends WPlainPanel {

    public WTextField dir;
    public POMIntegerInputWidget transpose;
    public WTextField noteType;
    public POMFloatInputWidget volume;

    public WToggleButton toggleButton;

    public SamplePackEditor screen;

    public InstrumentDef instrument;

    public int index;
    public InstrumentWidget(){
        this.setSize(7, 20);

        this.setBackgroundPainter(BackgroundPainter.createColorful(0x000000));

        WLabel dirLabel = new WLabel(Text.literal("sf2 file"));
        this.add(dirLabel, 10-3, 3, 10, 10);
        dir = new WTextField();
        this.add(dir, 10-3,0 + 10 + 3, 150, 5);
        dir.setMaxLength(150);

        WButton dirSwitch = new WButton(Text.literal("⟳"));
        this.add(dirSwitch, 10 + 50 + 50 + 15 + 50-3 - 10 - 3, 0 + 10 + 3 + 5, 10, 10);

        Runnable runnable = () -> {
            SF2FilePickerScreen newScreen = new SF2FilePickerScreen(screen);
            newScreen.widget = this;
            MinecraftClient.getInstance().setScreen(new CottonClientScreen(newScreen));
        };
        dirSwitch.setOnClick(runnable);

        WLabel transposeLabel = new WLabel(Text.literal("transpose"));
        this.add(transposeLabel, 10 + 50 + 50 + 15 + 50-3, 3, 10, 10);
        transpose = new POMIntegerInputWidget();
        this.add(transpose, 10 + 50 + 50 + 15 + 50-3,0 + 10 + 3, 50, 5);
        transpose.setMaxLength(150);

        WLabel noteTypeLabel = new WLabel(Text.literal("note type"));
        this.add(noteTypeLabel, 10-3, 5 + 25 + 5, 10, 10);
        noteType = new WTextField();
        this.add(noteType, 10-3,25 + 10 + 10, 150, 5);
        noteType.setMaxLength(150);

        WButton noteTypeSwitch = new WButton(Text.literal("⟳"));
        this.add(noteTypeSwitch, 10 + 50 + 50 + 15 + 50-3 - 10 - 3, 5 + 25 + 5 + 5 + 5 + 5, 10, 10);
        Runnable runnable2 = () -> {
            NoteTypePickerScreen newScreen = new NoteTypePickerScreen();
            newScreen.prevScreen = screen;
            newScreen.widget = this;
            MinecraftClient.getInstance().setScreen(new CottonClientScreen(newScreen));
        };
        noteTypeSwitch.setOnClick(runnable2);


        WLabel singlePitchLabel = new WLabel(Text.literal("single pitch"));
        this.add(singlePitchLabel, 10 + 50 + 50 + 15 + 50-3 + 30 + 30, 5 + 25 + 5, 10, 10);


        toggleButton = new WToggleButton();
        this.add(toggleButton, 10 + 50 + 50 + 15 + 50-3 - 10 - 3 + 20 + 40 + 20, 5 + 25 + 5 + 5 + 5, 50, 10);

        WLabel volumeLabel = new WLabel(Text.literal("volume"));
        this.add(volumeLabel, 10 + 50 + 50 + 15 + 50-3, 5 + 25 + 5, 10, 10);
        volume = new POMFloatInputWidget();
        this.add(volume, 10 + 50 + 50 + 15 + 50-3,25 + 10 + 10, 30, 5);
        volume.setMaxLength(150);

        WButton deleteInstrument = new WButton(Text.literal("X"));
        Runnable runnable3 = () -> {

            screen.copyChangesToCache();
            screen.instrumentWidgets.remove(this);
            screen.thisPack.instrumentDefs.remove(index);

            for(InstrumentWidget ins : screen.instrumentWidgets){
                if(ins.index > index){
                    ins.index--;
                }
            }

            screen.copyChangesToCache();


            MinecraftClient.getInstance().setScreen(new CottonClientScreen(new SamplePackEditor(screen.thisPack)));



        };
        deleteInstrument.setOnClick(runnable3);
        this.add(deleteInstrument, 10 + 50 + 50 + 115 + 50 + 50 - 10, 0, 10, 10);


    }
}
