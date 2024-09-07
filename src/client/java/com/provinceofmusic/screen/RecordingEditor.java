package com.provinceofmusic.screen;

import com.provinceofmusic.ProvinceOfMusicClient;
import com.provinceofmusic.recorder.ConvertToMidi;
import com.provinceofmusic.recorder.ReplayMusic;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class RecordingEditor extends LightweightGuiDescription {



    public RecordingEditor(File fileInstance){
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(256 * (4 - ProvinceOfMusicClient.guiSize), 200 * (4 - ProvinceOfMusicClient.guiSize));
        root.setInsets(Insets.ROOT_PANEL);

        WLabel title = new WLabel(Text.literal(fileInstance.getName()), 0x000000);
        root.add(title, 0, 0, 9, 3);

        WButton backButton = new WButton(Text.literal("Back ↶"));
        root.add(backButton, 12, 1, 3,1);
        Runnable backButtonRunnable = () -> {
            ReplayMusic.StopMusic();
            MinecraftClient.getInstance().setScreen(new CottonClientScreen(new NoteRecordScreen()));
        };
        backButton.setOnClick(backButtonRunnable);

        WButton openFolderButton = new WButton(Text.literal("Open Folder \uD83D\uDDC1"));
        root.add(openFolderButton, 9, 0, 6, 1);
        Runnable openFolderButtonRunnable = () -> {
            try {
                Desktop.getDesktop().open(ProvinceOfMusicClient.exportedmusicdir.getAbsoluteFile());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        openFolderButton.setOnClick(openFolderButtonRunnable);

        WButton exportButton = new WButton(Text.literal("Export ↥"));
        root.add(exportButton, 1, 2, 3, 1);
        Runnable exportButtonRunnable = () -> {
            ConvertToMidi.convert(fileInstance, ProvinceOfMusicClient.exportedmusicdir.getPath() + "/" + fileInstance.getName().substring(0, fileInstance.getName().length() - 4));
        };
        exportButton.setOnClick(exportButtonRunnable);

        WButton deleteButton = new WButton(Text.literal("Delete ☒").withColor(0xFF0000));
        root.add(deleteButton, 1, 6, 3, 1);
        Runnable deleteButtonRunnable = () -> {
            ReplayMusic.StopMusic();
            fileInstance.delete();
            MinecraftClient.getInstance().setScreen(new CottonClientScreen(new NoteRecordScreen()));
        };
        deleteButton.setOnClick(deleteButtonRunnable);

        WButton replayButton = new WButton(Text.literal("Replay ⟳"));
        root.add(replayButton, 1, 4, 3, 1);
        Runnable replayButtonRunnable = () -> {
            ReplayMusic.PlayMusic(fileInstance.getPath());
        };
        replayButton.setOnClick(replayButtonRunnable);
    }
}
