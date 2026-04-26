package com.provinceofmusic.ui;


import com.provinceofmusic.jukebox.InstrumentDef;
import com.provinceofmusic.screen.NoteTypePickerScreen;
import com.provinceofmusic.screen.SF2FilePickerScreen;
import com.provinceofmusic.screen.SamplePackEditor;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.widget.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class InstrumentWidget extends WPlainPanel {

    public WTextField instrumentDirectory;
    public POMIntegerInputWidget transpose;
    public WTextField noteType;
    public POMFloatInputWidget volume;

    public WToggleButton singlePitchToggle;

    public SamplePackEditor screen;

    public InstrumentDef instrument;

    public int index;
    public InstrumentWidget(){
        this.setSize(7, 20);

        this.setBackgroundPainter(BackgroundPainter.createColorful(0x0FF000));



        WLabel directoryLabel = new WLabel(Text.literal("sf2 file"));
        this.add(directoryLabel, 7, 3, 10, 10);
        instrumentDirectory = new WTextField();
        this.add(instrumentDirectory, 7,13, 80, 5);
        instrumentDirectory.setMaxLength(150);

        WButton directorySwitchButton = new WButton(Text.literal("⟳"));
        this.add(directorySwitchButton, 80, 7, 10, 10);
        Runnable directorySwitchButtonRunnable = () -> {
            SF2FilePickerScreen newScreen = new SF2FilePickerScreen(screen);
            newScreen.widget = this;
            MinecraftClient.getInstance().setScreen(new CottonClientScreen(newScreen));
        };
        directorySwitchButton.setOnClick(directorySwitchButtonRunnable);

        WLabel transposeLabel = new WLabel(Text.literal("transpose"));
        this.add(transposeLabel, 92, 3, 10, 10);
        transpose = new POMIntegerInputWidget();
        this.add(transpose, 92,13, 50, 5);
        transpose.setMaxLength(150);

        WLabel noteTypeLabel = new WLabel(Text.literal("note type"));
        this.add(noteTypeLabel, 7, 35, 10, 10);
        noteType = new WTextField();
        this.add(noteType, 7,45, 80, 5);
        noteType.setMaxLength(150);

        WButton noteTypeSwitch = new WButton(Text.literal("⟳"));
        this.add(noteTypeSwitch, 80, 40, 10, 10);
        Runnable noteTypeSwitchRunnable = () -> {
            NoteTypePickerScreen newScreen = new NoteTypePickerScreen();
            newScreen.prevScreen = screen;
            newScreen.widget = this;
            MinecraftClient.getInstance().setScreen(new CottonClientScreen(newScreen));
        };
        noteTypeSwitch.setOnClick(noteTypeSwitchRunnable);

        WLabel singlePitchLabel = new WLabel(Text.literal("single pitch"));
        this.add(singlePitchLabel, 7, 68, 10, 10);

        singlePitchToggle = new WToggleButton();
        this.add(singlePitchToggle, 7, 73, 50, 10);

        WLabel volumeLabel = new WLabel(Text.literal("volume"));
        this.add(volumeLabel, 92, 35, 10, 10);
        volume = new POMFloatInputWidget();
        this.add(volume, 92,45, 50, 5);
        volume.setMaxLength(150);

        WButton deleteInstrumentButton = new WButton(Text.literal("Delete").withColor(0xFFFF0000));
        Runnable deleteInstrumentButtonRunnable = () -> {
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
        deleteInstrumentButton.setOnClick(deleteInstrumentButtonRunnable);
        this.add(deleteInstrumentButton, 92, 73, 50, 13);
    }
}
