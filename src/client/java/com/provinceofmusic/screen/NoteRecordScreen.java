package com.provinceofmusic.screen;

import com.provinceofmusic.ProvinceOfMusicClient;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WListPanel;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.BiConsumer;

public class NoteRecordScreen extends LightweightGuiDescription {

    public NoteRecordScreen() {
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

        WLabel title = new WLabel(Text.literal("Recording Editor"), 0xFF000000);
        title.setHorizontalAlignment(HorizontalAlignment.CENTER);
        root.add(title, leftX, margin, leftW, 1);

        WButton backButton = new WButton(Text.literal("Back ↶"));
        backButton.setAlignment(HorizontalAlignment.CENTER);
        root.add(backButton, leftX, 3, (leftW / 2) - 1, 2);
        backButton.setOnClick(() -> MinecraftClient.getInstance().setScreen(new CottonClientScreen(new ConfigScreen())));

        WButton openFolderButton = new WButton(Text.literal("Open Folder \uD83D\uDDC1"));
        openFolderButton.setAlignment(HorizontalAlignment.CENTER);
        root.add(openFolderButton, leftX + (leftW / 2) + 1, 3, (leftW / 2) - 1, 2);
        openFolderButton.setOnClick(() -> {
            try {
                Desktop.getDesktop().open(ProvinceOfMusicClient.recordedmusicdir.getAbsoluteFile());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        ArrayList<File> data = fetchMidiFiles();
        BiConsumer<File, WButton> configurator = (File s, WButton destination) -> {
            destination.setLabel(Text.of(s.getName()));
            destination.setAlignment(HorizontalAlignment.CENTER);
            destination.setOnClick(() -> MinecraftClient.getInstance().setScreen(new CottonClientScreen(new RecordingEditor(s))));
        };

        WListPanel<File, WButton> packList = new WListPanel<>(data, WButton::new, configurator);
        packList.setListItemHeight(20);

        int listH = (ProvinceOfMusicClient.guiSize == 3) ? 14 : 18;
        root.add(packList, rightX, 3, rightW - rightX, listH);

        root.validate(this);
    }

    public ArrayList<File> fetchMidiFiles() {
        ArrayList<File> tempFiles = new ArrayList<>();
        File dir = ProvinceOfMusicClient.recordedmusicdir;
        File[] files = dir.listFiles();
        if (files != null) {
            tempFiles.addAll(Arrays.asList(files));
        }
        return tempFiles;
    }
}