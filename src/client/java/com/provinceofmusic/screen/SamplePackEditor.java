package com.provinceofmusic.screen;

import com.provinceofmusic.ProvinceOfMusicClient;
import com.provinceofmusic.jukebox.InstrumentDef;
import com.provinceofmusic.jukebox.NoteReplacer;
import com.provinceofmusic.jukebox.SamplePack;
import com.provinceofmusic.ui.InstrumentWidget;
import com.provinceofmusic.ui.SamplePackWidget;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.BiConsumer;

public class SamplePackEditor extends LightweightGuiDescription {

    public SamplePack thisPack;

    WListPanel packList;

    public ArrayList<InstrumentWidget> instrumentWidgets = new ArrayList<>();

    WTextField nameField;

    WTextField authorField;
    public SamplePackEditor(SamplePack inPack){
        thisPack = inPack;


        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(256, 200 * (4 - ProvinceOfMusicClient.guiSize));
        root.setInsets(Insets.ROOT_PANEL);



        WLabel title = new WLabel(Text.literal("Sample Pack Editor"), 0x000000);
        root.add(title, 0, 0, 9, 3);

        WButton deletePackButton = new WButton(Text.literal("Delete Pack").withColor(0xFF0000).styled(style -> style.withBold(true)));
        Runnable runnable3 = () -> {
            SaveChanges();
            if(ProvinceOfMusicClient.configSettings.activeSamplePack != null){
                if(ProvinceOfMusicClient.configSettings.activeSamplePack.equals(thisPack.name)){
                    ProvinceOfMusicClient.configSettings.activeSamplePack = null;
                    ProvinceOfMusicClient.setConfigSettings();
                    NoteReplacer.instruments = new ArrayList<>();
                }
            }
            SamplePack.DeletePack(thisPack.name);
            BackOutToPreviousScreen();
        };
        root.add(deletePackButton, 8, 0, 5,1);
        deletePackButton.setOnClick(runnable3);


        WButton backButton = new WButton(Text.literal("Back"));
        Runnable runnable4 = () -> {
            BackOutToPreviousScreen();
        };
        root.add(backButton, 1, 3, 3,1);
        backButton.setOnClick(runnable4);

        WButton changeImageButton = new WButton(Text.literal("Change Img"));
        Runnable runnable5 = () -> {

            FileDialog dialog = new FileDialog((Frame)null, "Select File to Open");
            dialog.setFile("*.png");
            dialog.setMode(FileDialog.LOAD);
            dialog.setVisible(true);


            if(dialog.getFiles()[0] != null){
                if(dialog.getFiles()[0].exists()){
                    File file = dialog.getFiles()[0];

                    System.out.println(SamplePack.getFile(thisPack.name).toPath() + "\\icon.png");

                    try {
                        Files.delete(Path.of(SamplePack.getFile(thisPack.name).toPath() + "\\icon.png"));
                        Files.copy(file.toPath(), Path.of(SamplePack.getFile(thisPack.name).toPath() + "\\icon.png"));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    dialog.dispose();
                    //System.out.println(file + " chosen.");
                }
            }


        };
        root.add(changeImageButton, 4, 3, 4,1);
        changeImageButton.setOnClick(runnable5);


        WLabel nameLabel = new WLabel(Text.literal("Name"), 0x000000);
        root.add(nameLabel, 1, 1, 5, 1);

        nameField = new WTextField();
        nameField.setText(thisPack.name);
        root.add(nameField, 1, 2, 5, 1);

        WLabel authorLabel = new WLabel(Text.literal("Author"), 0x000000);
        root.add(authorLabel, 7, 1, 5, 1);

        authorField = new WTextField();
        authorField.setText(thisPack.author);
        root.add(authorField, 7, 2, 5, 1);





        ArrayList<InstrumentDef> data = new ArrayList<>();
        for (int i = 0; i < thisPack.instrumentDefs.size(); i++){
            data.add(thisPack.instrumentDefs.get(i));
        }
        BiConsumer<InstrumentDef, InstrumentWidget> configurator = (InstrumentDef s, InstrumentWidget destination) -> {
            destination.dir.setText(s.dir);
            destination.volume.setText("" + s.volume);
            destination.noteType.setText(s.noteType);
            destination.transpose.setText("" + s.transpose);
            destination.screen = this;
            destination.instrument = s;
            destination.index = data.indexOf(s);
            destination.toggleButton.setToggle(s.singlePitch);
            if(s.singlePitch){
                destination.toggleButton.setLabel(Text.of("☑"));
            }
            else{
                destination.toggleButton.setLabel(Text.of("☐"));
            }
            instrumentWidgets.add(destination);
        };
        packList = new WListPanel(data, InstrumentWidget::new, configurator);
        packList.setListItemHeight(72-6);



        if(MinecraftClient.getInstance().options.getGuiScale().getValue() == 3){
            root.add(packList, 0, 4, 14, 8);
        } else{
            root.add(packList, 0, 5, 14, 17);
        }




        WButton saveChangesButton = new WButton(Text.literal("Save Changes").styled(style -> style.withBold(true)));
        Runnable runnable = () -> {
            SaveChanges();
            //BackOutToPreviousScreen
            //MinecraftClient.getInstance().setScreen(new CottonClientScreen(new SamplePackConfig()));
            WLabel savedPopup = new WLabel(Text.literal("Changes Saved").styled(style -> style.withItalic(true)), 0x000000);
            root.add(savedPopup, 8, 4, 2, 1);
            Thread t = new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                root.remove(savedPopup);
            });
            t.start();
        };
        saveChangesButton.setOnClick(runnable);
        root.add(saveChangesButton, 8,3, 5, 1);





        WButton addNewButton = new WButton(Text.literal("+"));
        Runnable runnable2 = () -> {
            copyChangesToCache();
            InstrumentDef temp = new InstrumentDef("null", "null", 0, 1.0f, false);
            thisPack.instrumentDefs.add(temp);
            MinecraftClient.getInstance().setScreen(new CottonClientScreen(new SamplePackEditor(thisPack)));

        };
        addNewButton.setOnClick(runnable2);
        root.add(addNewButton, 0,3, 1, 1);

    }

    public void copyChangesToCache() {
        for(int i = 0; i < instrumentWidgets.size(); i++){
            InstrumentDef temp2 = new InstrumentDef(instrumentWidgets.get(i).dir.getText(), instrumentWidgets.get(i).noteType.getText(), Integer.parseInt(instrumentWidgets.get(i).transpose.getText()), Float.parseFloat(instrumentWidgets.get(i).volume.getText()), instrumentWidgets.get(i).toggleButton.getToggle());
            thisPack.instrumentDefs.set(instrumentWidgets.get(i).index, temp2);
        }
    }

    public void SaveChanges(){
        copyChangesToCache();
        String namecache = thisPack.name;
        //thisPack.name = nameField.getText();


        SamplePack.RenameSamplePack(thisPack, nameField.getText());
        thisPack.author = authorField.getText();
        thisPack.name = nameField.getText();
        thisPack.WriteSamplePack();










        //if(!namecache.equals(nameField.getText())){
        //    if(ProvinceOfMusicClient.configSettings.activeSamplePack.equals(namecache)){
        //        ProvinceOfMusicClient.configSettings.activeSamplePack = thisPack.name;
        //    }
        //    SamplePack.DeletePack(namecache);
        //}
        if(ProvinceOfMusicClient.configSettings.activeSamplePack != null){
            if(ProvinceOfMusicClient.configSettings.activeSamplePack.equals(thisPack.name) || ProvinceOfMusicClient.configSettings.activeSamplePack.equals(namecache)){
                NoteReplacer.interupt = true;
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        ProvinceOfMusicClient.configSettings.activeSamplePack = thisPack.name;
                        NoteReplacer.instruments = thisPack.getInstruments(NoteReplacer.instruments);
                        ProvinceOfMusicClient.setConfigSettings();
                        NoteReplacer.interupt = false;
                    }
                };
                Timer timer = new Timer(true);
                timer.schedule(task, 300);

            }
        }
    }

    public void BackOutToPreviousScreen(){
        MinecraftClient.getInstance().setScreen(new CottonClientScreen(new SamplePackConfig()));
    }
}
