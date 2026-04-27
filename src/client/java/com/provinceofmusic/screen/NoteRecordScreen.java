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

        int guiSize = ProvinceOfMusicClient.guiSize;
        float[] scalers = {0.9f, 0.95f, 1.0f, 1.05f};
        float scaler = (guiSize >= 1 && guiSize <= 4) ? scalers[guiSize - 1] : 1.0f;


        int cell = 9;
        int margin = 2;
        int rawCellsX = (int) ((405 * scaler) / cell);
        int rawCellsY = (int) ((225 * scaler) / cell);

        int totalCellsX = (rawCellsX % 2 == 0) ? rawCellsX : rawCellsX - 1; //just to make a perfect center
        int totalCellsY = (rawCellsY % 2 == 0) ? rawCellsY : rawCellsY - 1;

        int width = totalCellsX * cell;
        int height = totalCellsY * cell;

        int centerX = totalCellsX / 2 ;
        int leftS = margin-1;
        int leftE = centerX - margin;
        int rightS = centerX + margin;
        int rightE = totalCellsX - margin;
        int top = margin;
        int bottom = totalCellsY - margin;

        WGridPanel root = new WGridPanel(cell);
        setRootPanel(root);
        root.setSize(width, height);
        root.setInsets(Insets.ROOT_PANEL);



        this.setUseDefaultRootBackground(false);
        root.setBackgroundPainter(BackgroundPainter.createGuiSprite(Identifier.of("provinceofmusic", "gui_overlay")));

        WLabel title = new WLabel(Text.literal("Recording Editor"), 0xFF000000);
        title.setHorizontalAlignment(HorizontalAlignment.CENTER);
        root.add(title, leftS, top, leftE - leftS, 2);



        WButton backButton = new WButton(Text.literal("Back ↶"));
        backButton.setAlignment(HorizontalAlignment.CENTER);
        root.add(backButton, leftS, top + 3, ((leftE - leftS) / 2) - 1, 2);
        backButton.setOnClick(() -> MinecraftClient.getInstance().setScreen(new CottonClientScreen(new ConfigScreen())));

        WButton openFolderButton = new WButton(Text.literal("Open Folder \uD83D\uDDC1"));
        openFolderButton.setAlignment(HorizontalAlignment.CENTER);
        root.add(openFolderButton, leftS + ((leftE - leftS) / 2), top + 3, ((leftE - leftS) / 2), 2);
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
        root.add(packList, rightS - 2, top, rightE - rightS + 2, bottom-top);

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