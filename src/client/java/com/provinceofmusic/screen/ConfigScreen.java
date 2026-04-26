package com.provinceofmusic.screen;

import com.provinceofmusic.ProvinceOfMusicClient;
import com.provinceofmusic.recorder.DebugMode;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WSprite;
import io.github.cottonmc.cotton.gui.widget.WToggleButton;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ConfigScreen extends LightweightGuiDescription {
    public ConfigScreen() {
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
        int rightE = totalCellsX - margin - 2;
        int top = margin;
        int bottom = totalCellsY - margin;
        int leftAdjust = ((leftE - leftS) % 2 == 0) ? (leftE - leftS) : (leftE - leftS) - 1; //always with a center
        int rightAdjust = ((rightE - rightS) % 2 == 0) ? (rightE - rightS) : (rightE - rightS) - 1;

        WGridPanel root = new WGridPanel(cell);
        setRootPanel(root);
        root.setSize(width, height);
        root.setInsets(Insets.ROOT_PANEL);

        this.setUseDefaultRootBackground(false);
        root.setBackgroundPainter(BackgroundPainter.createGuiSprite(Identifier.of("provinceofmusic", "gui_overlay")));

        WLabel title = new WLabel(Text.literal("Province of Music"), 0xFF000000);
        title.setHorizontalAlignment(HorizontalAlignment.CENTER);
        root.add(title, rightS, top, rightAdjust, 5);

        WSprite icon = new WSprite(Identifier.of("provinceofmusic", "icon.png"));
        root.add(icon, rightS, top + 2, rightAdjust, rightAdjust);

        WButton recordedMusicMenuButton = new WButton(Text.literal("Recorded Music Editor"));
        recordedMusicMenuButton.setAlignment(HorizontalAlignment.CENTER);
        root.add(recordedMusicMenuButton, leftS, top, leftAdjust, 2);
        recordedMusicMenuButton.setOnClick(() ->
                MinecraftClient.getInstance().setScreen(new CottonClientScreen(new NoteRecordScreen()))
        );

        WButton samplePackMenuButton = new WButton(Text.literal("Sample Pack Editor"));
        samplePackMenuButton.setAlignment(HorizontalAlignment.CENTER);
        root.add(samplePackMenuButton, leftS, top + 3, leftAdjust, 2);
        samplePackMenuButton.setOnClick(() ->
                MinecraftClient.getInstance().setScreen(new CottonClientScreen(new SamplePackConfig()))
        );

        WToggleButton debugModeToggle = new WToggleButton(Text.literal("Debug"));
        root.add(debugModeToggle, rightE - 6, bottom - 2, 6, 2);
        debugModeToggle.setToggle(ProvinceOfMusicClient.configSettings.debugMode);

        WButton saveButton = new WButton(Text.literal("Save Changes \uD83D\uDDAB").styled(style -> style.withBold(true)));
        saveButton.setAlignment(HorizontalAlignment.CENTER);
        root.add(saveButton, leftS, bottom - 4, leftAdjust, 2);

        saveButton.setOnClick(() -> {
            DebugMode.isOn = debugModeToggle.getToggle();
            ProvinceOfMusicClient.configSettings.debugMode = DebugMode.isOn;

            ProvinceOfMusicClient.saveConfigSettings();

            WLabel savedPopup = new WLabel(Text.literal("Changes Saved").styled(style -> style.withItalic(true)), 0xFF000000);
            savedPopup.setHorizontalAlignment(HorizontalAlignment.CENTER);
            root.add(savedPopup, leftS, bottom - 2, leftAdjust, 1);

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

        root.validate(this);
    }
}