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
import io.github.cottonmc.cotton.gui.widget.WText;
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

        int guiSize = ProvinceOfMusicClient.guiSize;
        float[] scalers = {0.9f, 0.95f, 1.0f, 1.05f};
        float scaler = (guiSize >= 1 && guiSize <= 4) ? scalers[guiSize - 1] : 1.0f;

        int cell = 9;
        int margin = 2;
        int rawCellsX = (int) ((405 * scaler) / cell);
        int rawCellsY = (int) ((225 * scaler) / cell);

        int totalCellsX = (rawCellsX % 2 == 0) ? rawCellsX : rawCellsX - 1;
        int totalCellsY = (rawCellsY % 2 == 0) ? rawCellsY : rawCellsY - 1;

        int width = totalCellsX * cell;
        int height = totalCellsY * cell;

        int centerX = totalCellsX / 2;
        int leftS = margin - 1;
        int leftE = centerX - margin;
        int rightS = centerX + margin;
        int rightE = totalCellsX - margin;
        int top = margin;
        int bottom = totalCellsY - margin;
        int leftAdjust = ((leftE - leftS) % 2 == 0) ? (leftE - leftS) : (leftE - leftS) - 1;

        WGridPanel root = new WGridPanel(cell);
        setRootPanel(root);
        root.setSize(width, height);
        root.setInsets(Insets.ROOT_PANEL);


        this.setUseDefaultRootBackground(false);
        root.setBackgroundPainter(BackgroundPainter.createGuiSprite(Identifier.of("provinceofmusic", "gui_overlay")));

        WLabel title = new WLabel(Text.literal(fileInstance.getName()), 0xFF000000);
        title.setHorizontalAlignment(HorizontalAlignment.CENTER);
        root.add(title, leftS, top, leftAdjust, 1);

        WButton backButton = new WButton(Text.literal("Back ↶"));
        backButton.setAlignment(HorizontalAlignment.CENTER);
        root.add(backButton, leftS, top + 2, (leftAdjust / 2) - 1, 2);
        backButton.setOnClick(() -> {
            ReplayMusic.StopMusic();
            MinecraftClient.getInstance().setScreen(new CottonClientScreen(new NoteRecordScreen()));
        });

        WButton openFolderButton = new WButton(Text.literal("Open Folder \uD83D\uDDC1"));
        openFolderButton.setAlignment(HorizontalAlignment.CENTER);
        root.add(openFolderButton, leftS + (leftAdjust / 2), top + 2, (leftAdjust / 2), 2);
        openFolderButton.setOnClick(() -> {
            try {
                Desktop.getDesktop().open(ProvinceOfMusicClient.exportedmusicdir.getAbsoluteFile());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        WButton exportButton = new WButton(Text.literal("Export ↥"));
        exportButton.setAlignment(HorizontalAlignment.CENTER);
        root.add(exportButton, leftS, top + 5, leftAdjust, 2);
        exportButton.setOnClick(() -> ConvertToMidi.convert(
                fileInstance,
                ProvinceOfMusicClient.exportedmusicdir.getPath() + "/" + fileInstance.getName().substring(0, fileInstance.getName().length() - 4)
        ));

        WButton replayButton = new WButton(Text.literal("Replay ⟳"));
        replayButton.setAlignment(HorizontalAlignment.CENTER);
        root.add(replayButton, leftS, top + 7, leftAdjust, 2);
        replayButton.setOnClick(() -> ReplayMusic.PlayMusic(fileInstance.getPath()));

        WButton deleteButton = new WButton(Text.literal("Delete ☒").withColor(0xFFFF0000));
        deleteButton.setAlignment(HorizontalAlignment.CENTER);
        root.add(deleteButton, leftS, bottom - 2, leftAdjust, 2);
        deleteButton.setOnClick(() -> {
            ReplayMusic.StopMusic();
            fileInstance.delete();
            MinecraftClient.getInstance().setScreen(new CottonClientScreen(new NoteRecordScreen()));
        });

        WLabel infoTitle = new WLabel(Text.literal("Actions"), 0xFF000000);
        infoTitle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        root.add(infoTitle, rightS - 2, top, rightE - rightS + 2, 1);

        WText info1 = new WText(Text.literal("Export: saves a .mid in exported-music"), 0xFF000000);
        root.add(info1, rightS - 2, top + 2, rightE - rightS + 1, 1);

        WText info2 = new WText(Text.literal("Replay: plays the recording"), 0xFF000000);
        root.add(info2, rightS - 2, top + 4, rightE - rightS + 1, 1);

        WText info3 = new WText(Text.literal("Delete: removes the recording file"), 0xFF000000);
        root.add(info3, rightS - 2, top + 6, rightE - rightS + 1, 1);

        root.validate(this);
    }
}