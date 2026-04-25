package com.provinceofmusic.screen;

import com.provinceofmusic.ProvinceOfMusicClient;
import com.provinceofmusic.jukebox.InstrumentDef;
import com.provinceofmusic.jukebox.SamplePack;
import com.provinceofmusic.ui.InstrumentWidget;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WListPanel;
import io.github.cottonmc.cotton.gui.widget.WTextField;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.awt.Desktop;
import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.function.BiConsumer;

public class SamplePackEditor extends LightweightGuiDescription {

    public SamplePack thisPack;

    WListPanel instrumentList;

    public ArrayList<InstrumentWidget> instrumentWidgets = new ArrayList<>();

    WTextField nameField;
    WTextField authorField;

    public SamplePackEditor(SamplePack inPack) {
        thisPack = inPack;

        //idk if there is a better way to do this
        int cell = 9;
        int totalCellsX = (400 * (4 - ProvinceOfMusicClient.guiSize)) / cell;

        int margin = 2;
        int centerX = totalCellsX / 2;

        int leftX = margin;
        int leftW = centerX - margin - 2;

        int rightX = centerX;
        int rightW = totalCellsX - margin;

        WGridPanel root = new WGridPanel(cell);
        setRootPanel(root);
        root.setSize(400 * (4 - ProvinceOfMusicClient.guiSize), 220 * (4 - ProvinceOfMusicClient.guiSize));
        root.setInsets(Insets.ROOT_PANEL);
        root.setGaps(1, 3);

        this.setUseDefaultRootBackground(false);
        root.setBackgroundPainter(BackgroundPainter.createGuiSprite(Identifier.of("provinceofmusic", "gui_overlay")));

        WLabel title = new WLabel(Text.literal("Sample Pack Editor"), 0xFF000000);
        title.setHorizontalAlignment(HorizontalAlignment.CENTER);
        root.add(title, leftX, margin, leftW, 1);

        WLabel nameLabel = new WLabel(Text.literal("Name"), 0xFF000000);
        root.add(nameLabel, leftX, 3, leftW, 1);

        nameField = new WTextField();
        nameField.setMaxLength(1000);
        nameField.setText(thisPack.name);
        root.add(nameField, leftX, 4, leftW, 2);

        WLabel authorLabel = new WLabel(Text.literal("Author"), 0xFF000000);
        root.add(authorLabel, leftX, 6, leftW, 1);

        authorField = new WTextField();
        authorField.setMaxLength(1000);
        authorField.setText(thisPack.author);
        root.add(authorField, leftX, 7, leftW, 2);

        WButton backButton = new WButton(Text.literal("Back ↶"));
        backButton.setAlignment(HorizontalAlignment.CENTER);
        root.add(backButton, leftX, 9, (leftW / 2) - 1, 2);
        backButton.setOnClick(this::BackOutToPreviousScreen);

        WButton openFolderButton = new WButton(Text.literal("Open Folder \uD83D\uDDC1"));
        openFolderButton.setAlignment(HorizontalAlignment.CENTER);
        root.add(openFolderButton, leftX + (leftW / 2) + 1, 9, (leftW / 2) -1, 2);
        openFolderButton.setOnClick(() -> {
            try {
                Desktop.getDesktop().open(new File(Path.of(ProvinceOfMusicClient.samplepacksdir + "/" + thisPack.name).toString()).getAbsoluteFile());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        WButton changeImageButton = new WButton(Text.literal("Change Img"));
        changeImageButton.setAlignment(HorizontalAlignment.CENTER);
        root.add(changeImageButton, leftX, 11, (leftW / 2) - 1, 2);
        changeImageButton.setOnClick(() -> {
            FileDialog dialog = new FileDialog((Frame) null, "Select File to Open");
            dialog.setFile("*.png");
            dialog.setMode(FileDialog.LOAD);
            dialog.setVisible(true);

            if (dialog.getFiles().length > 0 && dialog.getFiles()[0] != null) {
                if (dialog.getFiles()[0].exists()) {
                    File file = dialog.getFiles()[0];
                    try {
                        Path iconPath = Path.of(SamplePack.getSamplePackAsFile(thisPack.name).toPath() + "/icon.png");
                        if (new File(iconPath.toString()).exists()) {
                            Files.delete(iconPath);
                        }
                        Files.copy(file.toPath(), iconPath);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    dialog.dispose();
                }
            }
        });

        WButton saveChangesButton = new WButton(Text.literal("Save \uD83D\uDDAB").styled(style -> style.withBold(true)));
        saveChangesButton.setAlignment(HorizontalAlignment.CENTER);
        root.add(saveChangesButton, leftX + (leftW / 2) + 1, 11, (leftW / 2) -1, 2);
        saveChangesButton.setOnClick(() -> {
            SaveChanges();
            WLabel savedPopup = new WLabel(Text.literal("Changes Saved").styled(style -> style.withItalic(true)), 0xFF000000);
            savedPopup.setHorizontalAlignment(HorizontalAlignment.CENTER);
            root.add(savedPopup, leftX, 13, leftW, 1);
            Thread t = new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                root.remove(savedPopup);
            });
            t.start();
        });

        WButton deletePackButton = new WButton(Text.literal("Delete Pack").withColor(0xFFFF0000).styled(style -> style.withBold(true)));
        deletePackButton.setAlignment(HorizontalAlignment.CENTER);
        root.add(deletePackButton, leftX, 14, leftW, 2);
        deletePackButton.setOnClick(() -> {
            SaveChanges();
            if (ProvinceOfMusicClient.configSettings.activeSamplePack != null) {
                if (ProvinceOfMusicClient.configSettings.activeSamplePack.equals(thisPack.name)) {
                    ProvinceOfMusicClient.configSettings.activeSamplePack = null;
                    ProvinceOfMusicClient.saveConfigSettings();
                }
            }
            SamplePack.DeletePack(thisPack.name);
            BackOutToPreviousScreen();
        });

        WButton addNewButton = new WButton(Text.literal("Add+"));
        addNewButton.setAlignment(HorizontalAlignment.CENTER);
        root.add(addNewButton, leftX, 16, leftW, 2);
        addNewButton.setOnClick(() -> {
            copyChangesToCache();
            InstrumentDef temp = new InstrumentDef("null", "null", 0, 1.0f, false);
            thisPack.instrumentDefs.add(temp);
            MinecraftClient.getInstance().setScreen(new CottonClientScreen(new SamplePackEditor(thisPack)));
        });

        ArrayList<InstrumentDef> data = new ArrayList<>();
        for (int i = 0; i < thisPack.instrumentDefs.size(); i++) {
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

        instrumentList = new WListPanel<>(data, InstrumentWidget::new, configurator);
        instrumentList.setListItemHeight(90);


        int listH = (ProvinceOfMusicClient.guiSize == 3) ? 20 : 22 ;

        root.add(instrumentList, rightX, 1, rightW-rightX, listH);

        root.validate(this);
    }

    public void copyChangesToCache() {
        for (InstrumentWidget ins : instrumentWidgets) {
            InstrumentDef temp = new InstrumentDef(
                    ins.instrumentDirectory.getText(),
                    ins.noteType.getText(),
                    ins.transpose.getInt(),
                    ins.volume.getFloat(),
                    ins.singlePitchToggle.getToggle()
            );
            thisPack.instrumentDefs.set(ins.index, temp);
        }
    }

    public void SaveChanges() {
        copyChangesToCache();
        String nameCache = thisPack.name;

        SamplePack.RenameSamplePack(thisPack, nameField.getText());
        thisPack.author = authorField.getText();
        thisPack.name = nameField.getText();
        thisPack.WriteSamplePack();

        if (ProvinceOfMusicClient.configSettings.activeSamplePack != null) {
            if (ProvinceOfMusicClient.configSettings.activeSamplePack.equals(thisPack.name) || ProvinceOfMusicClient.configSettings.activeSamplePack.equals(nameCache)) {
                ProvinceOfMusicClient.configSettings.activeSamplePack = thisPack.name;
                ProvinceOfMusicClient.saveConfigSettings();
            }
        }
    }

    public void BackOutToPreviousScreen() {
        MinecraftClient.getInstance().setScreen(new CottonClientScreen(new SamplePackConfig()));
    }
}