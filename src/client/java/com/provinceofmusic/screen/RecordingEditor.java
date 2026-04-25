package com.provinceofmusic.screen;

import com.provinceofmusic.ProvinceOfMusicClient;
import com.provinceofmusic.recorder.ConvertToMidi;
import com.provinceofmusic.recorder.ReplayMusic;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class RecordingEditor extends LightweightGuiDescription {

    public RecordingEditor(File fileInstance) {
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

        WLabel title = new WLabel(Text.literal(fileInstance.getName()), 0xFF000000);
        title.setHorizontalAlignment(HorizontalAlignment.CENTER);
        root.add(title, leftX, margin, leftW, 1);

        WButton backButton = new WButton(Text.literal("Back ↶"));
        backButton.setAlignment(HorizontalAlignment.CENTER);
        root.add(backButton, leftX, 3, (leftW / 2) - 1, 2);
        backButton.setOnClick(() -> {
            ReplayMusic.StopMusic();
            MinecraftClient.getInstance().setScreen(new CottonClientScreen(new NoteRecordScreen()));
        });

        WButton openFolderButton = new WButton(Text.literal("Open Folder \uD83D\uDDC1"));
        openFolderButton.setAlignment(HorizontalAlignment.CENTER);
        root.add(openFolderButton, leftX + (leftW / 2) + 1, 3, (leftW / 2) - 1, 2);
        openFolderButton.setOnClick(() -> {
            try {
                Desktop.getDesktop().open(ProvinceOfMusicClient.exportedmusicdir.getAbsoluteFile());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        WButton exportButton = new WButton(Text.literal("Export ↥"));
        exportButton.setAlignment(HorizontalAlignment.CENTER);
        root.add(exportButton, leftX, 6, leftW, 2);
        exportButton.setOnClick(() -> ConvertToMidi.convert(
                fileInstance,
                ProvinceOfMusicClient.exportedmusicdir.getPath() + "/" + fileInstance.getName().substring(0, fileInstance.getName().length() - 4)
        ));

        WButton replayButton = new WButton(Text.literal("Replay ⟳"));
        replayButton.setAlignment(HorizontalAlignment.CENTER);
        root.add(replayButton, leftX, 8, leftW, 2);
        replayButton.setOnClick(() -> ReplayMusic.PlayMusic(fileInstance.getPath()));

        WButton deleteButton = new WButton(Text.literal("Delete ☒").withColor(0xFFFF0000));
        deleteButton.setAlignment(HorizontalAlignment.CENTER);
        root.add(deleteButton, leftX, 18, leftW, 2);
        deleteButton.setOnClick(() -> {
            ReplayMusic.StopMusic();
            fileInstance.delete();
            MinecraftClient.getInstance().setScreen(new CottonClientScreen(new NoteRecordScreen()));
        });

        WLabel infoTitle = new WLabel(Text.literal("Actions"), 0xFF000000);
        infoTitle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        root.add(infoTitle, rightX, margin, rightW - rightX, 1);

        WLabel info1 = new WLabel(Text.literal("Export: saves a .mid in exported-music"), 0xFF000000);
        root.add(info1, rightX, 3, rightW - rightX, 1);

        WLabel info2 = new WLabel(Text.literal("Replay: plays the recording"), 0xFF000000);
        root.add(info2, rightX, 4, rightW - rightX, 1);

        WLabel info3 = new WLabel(Text.literal("Delete: removes the recording file"), 0xFF000000);
        root.add(info3, rightX, 5, rightW - rightX, 1);

        root.validate(this);
    }
}