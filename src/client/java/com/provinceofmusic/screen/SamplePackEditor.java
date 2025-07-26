package com.provinceofmusic.screen;

import com.provinceofmusic.ProvinceOfMusicClient;
import com.provinceofmusic.jukebox.InstrumentDef;
import com.provinceofmusic.jukebox.NoteReplacer;
import com.provinceofmusic.jukebox.SamplePack;
import com.provinceofmusic.ui.InstrumentWidget;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.BiConsumer;

public class SamplePackEditor extends LightweightGuiDescription {

    public SamplePack thisPack;

    WListPanel instrumentList;

    public ArrayList<InstrumentWidget> instrumentWidgets = new ArrayList<>();

    WTextField nameField;

    WTextField authorField;
    public SamplePackEditor(SamplePack inPack){
        thisPack = inPack;

        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(356, 200 * (4 - ProvinceOfMusicClient.guiSize));
        root.setInsets(Insets.ROOT_PANEL);

        WLabel title = new WLabel(Text.literal("Sample Pack Editor"), 0x000000);
        root.add(title, 0, 0, 9, 3);

        WButton deletePackButton = new WButton(Text.literal("Delete Pack").withColor(0xFF0000).styled(style -> style.withBold(true)));
        Runnable deletePackButtonRunnable = () -> {
            SaveChanges();
            if(ProvinceOfMusicClient.configSettings.activeSamplePack != null){
                if(ProvinceOfMusicClient.configSettings.activeSamplePack.equals(thisPack.name)){
                    ProvinceOfMusicClient.configSettings.activeSamplePack = null;
                    ProvinceOfMusicClient.saveConfigSettings();
                }
            }
            SamplePack.DeletePack(thisPack.name);
            BackOutToPreviousScreen();
        };
        root.add(deletePackButton, 8, 0, 5,1);
        deletePackButton.setOnClick(deletePackButtonRunnable);

        WButton openFolderButton = new WButton(Text.literal("Open Folder \uD83D\uDDC1"));
        root.add(openFolderButton, 14, 1, 5, 1);
        Runnable openFolderButtonRunnable = () -> {
            try {
                Desktop.getDesktop().open(new File("provinceofmusic/samplepacks" + "/" + thisPack.name).getAbsoluteFile());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        openFolderButton.setOnClick(openFolderButtonRunnable);


        WButton backButton = new WButton(Text.literal("Back ↶"));
        Runnable backButtonRunnable = () -> {
            BackOutToPreviousScreen();
        };
        root.add(backButton, 1, 3, 3,1);
        backButton.setOnClick(backButtonRunnable);

        WButton changeImageButton = new WButton(Text.literal("Change Img"));
        root.add(changeImageButton, 4, 3, 4,1);
        Runnable changeImageButtonRunnable = () -> {

            FileDialog dialog = new FileDialog((Frame)null, "Select File to Open");
            dialog.setFile("*.png");
            dialog.setMode(FileDialog.LOAD);
            dialog.setVisible(true);

            if(dialog.getFiles()[0] != null){
                if(dialog.getFiles()[0].exists()){
                    File file = dialog.getFiles()[0];

                    try {
                        if(new File(SamplePack.getFile(thisPack.name).toPath() + "\\icon.png").exists()){
                            Files.delete(Path.of(SamplePack.getFile(thisPack.name).toPath() + "\\icon.png"));
                        }
                        Files.copy(file.toPath(), Path.of(SamplePack.getFile(thisPack.name).toPath() + "\\icon.png"));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    dialog.dispose();
                }
            }
        };
        changeImageButton.setOnClick(changeImageButtonRunnable);


        WLabel nameLabel = new WLabel(Text.literal("Name"), 0x000000);
        root.add(nameLabel, 1, 1, 5, 1);

        nameField = new WTextField();
        nameField.setMaxLength(1000);
        nameField.setText(thisPack.name);
        root.add(nameField, 1, 2, 5, 1);

        WLabel authorLabel = new WLabel(Text.literal("Author"), 0x000000);
        root.add(authorLabel, 7, 1, 5, 1);

        authorField = new WTextField();
        authorField.setMaxLength(1000);
        authorField.setText(thisPack.author);
        root.add(authorField, 7, 2, 5, 1);

        ArrayList<InstrumentDef> data = new ArrayList<>();
        for (int i = 0; i < thisPack.instrumentDefs.size(); i++){
            data.add(thisPack.instrumentDefs.get(i));
        }
        BiConsumer<InstrumentDef, InstrumentWidget> configurator = (InstrumentDef s, InstrumentWidget destination) -> {
            destination.instrumentDirectory.setText(s.dir);
            destination.volume.setText("" + s.volume);
            destination.noteType.setText(s.noteType);
            destination.transpose.setText("" + s.transpose);
            destination.screen = this;
            destination.instrument = s;
            destination.index = data.indexOf(s);
            destination.singlePitchToggle.setToggle(s.singlePitch);
            instrumentWidgets.add(destination);
        };
        instrumentList = new WListPanel(data, InstrumentWidget::new, configurator);
        instrumentList.setListItemHeight(66);

        if(ProvinceOfMusicClient.guiSize == 3){
            root.add(instrumentList, 0, 4, 19, 8);
        } else{
            root.add(instrumentList, 0, 5, 19, 17);
        }

        WButton saveChangesButton = new WButton(Text.literal("Save Changes \uD83D\uDDAB").styled(style -> style.withBold(true)));
        Runnable saveChangesButtonRunnable = () -> {
            SaveChanges();
            WLabel savedPopup = new WLabel(Text.literal("Changes Saved").styled(style -> style.withItalic(true)), 0x000000);
            root.add(savedPopup, 13, 2, 2, 1);
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
        saveChangesButton.setOnClick(saveChangesButtonRunnable);
        root.add(saveChangesButton, 8,3, 6, 1);

        WButton addNewButton = new WButton(Text.literal("+"));
        root.add(addNewButton, 0,3, 1, 1);
        Runnable addNewButtonRunnable = () -> {
            copyChangesToCache();
            InstrumentDef temp = new InstrumentDef("null", "null", 0, 1.0f, false);
            thisPack.instrumentDefs.add(temp);
            MinecraftClient.getInstance().setScreen(new CottonClientScreen(new SamplePackEditor(thisPack)));

        };
        addNewButton.setOnClick(addNewButtonRunnable);
    }

    public void copyChangesToCache() {
        for (InstrumentWidget ins: instrumentWidgets) {
            InstrumentDef temp = new InstrumentDef(ins.instrumentDirectory.getText(), ins.noteType.getText(), ins.transpose.getInt(), ins.volume.getFloat(), ins.singlePitchToggle.getToggle());
            thisPack.instrumentDefs.set(ins.index, temp);
        }

    }

    public void SaveChanges(){
        copyChangesToCache();
        String nameCache = thisPack.name;

        SamplePack.RenameSamplePack(thisPack, nameField.getText());
        thisPack.author = authorField.getText();
        thisPack.name = nameField.getText();
        thisPack.WriteSamplePack();

        if(ProvinceOfMusicClient.configSettings.activeSamplePack != null){
            if(ProvinceOfMusicClient.configSettings.activeSamplePack.equals(thisPack.name) || ProvinceOfMusicClient.configSettings.activeSamplePack.equals(nameCache)){
                ProvinceOfMusicClient.configSettings.activeSamplePack = thisPack.name;
                ProvinceOfMusicClient.saveConfigSettings();
            }
        }
    }

    public void BackOutToPreviousScreen(){
        MinecraftClient.getInstance().setScreen(new CottonClientScreen(new SamplePackConfig()));
    }
}
