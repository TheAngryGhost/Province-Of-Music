package com.provinceofmusic.screen;

import com.provinceofmusic.ProvinceOfMusicClient;
import com.provinceofmusic.recorder.DebugMode;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.client.NinePatchBackgroundPainter;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.data.Texture;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import juuxel.libninepatch.NinePatch;
import net.minecraft.util.Identifier;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;

public class ConfigScreen extends LightweightGuiDescription {
    public ConfigScreen() {
        WGridPanel root = new WGridPanel(20);
        setRootPanel(root);
        root.setSize(400 * (4 - ProvinceOfMusicClient.guiSize), 220 * (4 - ProvinceOfMusicClient.guiSize));
        root.setInsets(Insets.ROOT_PANEL);
        root.setGaps(1,3);

        this.setUseDefaultRootBackground(false);


        root.setBackgroundPainter(BackgroundPainter.createGuiSprite(Identifier.of("provinceofmusic", "gui_overlay") ));






        WLabel title = new WLabel(Text.literal("Province of Music"), 0xFF000000);
        title.setHorizontalAlignment(HorizontalAlignment.CENTER);
        root.add(title, 10, 1, 10, 1);


        WSprite icon = new WSprite(Identifier.of("provinceofmusic","icon.png"));
        root.add(icon, 12, 2, 6, 6);

        WButton recordedMusicMenuButton = new WButton(Text.literal("Recorded Music Editor"));
        recordedMusicMenuButton.setAlignment(HorizontalAlignment.CENTER);
        root.add(recordedMusicMenuButton, 1, 1, 8, 1);
        Runnable recordingRunnable = () -> {
            MinecraftClient.getInstance().setScreen(new CottonClientScreen(new NoteRecordScreen()));
        };
        recordedMusicMenuButton.setOnClick(recordingRunnable);


        WButton samplePackMenuButton = new WButton(Text.literal("Sample Pack Editor"));
        recordedMusicMenuButton.setAlignment(HorizontalAlignment.CENTER);
        Runnable samplePackRunnable = () -> {
            MinecraftClient.getInstance().setScreen(new CottonClientScreen(new SamplePackConfig()));
        };
        root.add(samplePackMenuButton, 1, 2, 8, 1);
        samplePackMenuButton.setOnClick(samplePackRunnable);


        WToggleButton debugModeToggle = new WToggleButton(Text.literal("Debug"));
        root.add(debugModeToggle, 17, 8, 3, 1);
        debugModeToggle.setToggle(ProvinceOfMusicClient.configSettings.debugMode);

        WButton saveButton = new WButton(Text.literal("Save Changes \uD83D\uDDAB").styled(style -> style.withBold(true)));
        saveButton.setAlignment(HorizontalAlignment.CENTER);
        root.add(saveButton, 1, 7, 8, 1);
        Runnable saveRunnable = () -> {

            DebugMode.isOn = debugModeToggle.getToggle();
            ProvinceOfMusicClient.configSettings.debugMode = DebugMode.isOn;

            ProvinceOfMusicClient.saveConfigSettings();


            WLabel savedPopup = new WLabel(Text.literal("Changes Saved").styled(style -> style.withItalic(true)), 0xFF000000);
            root.add(savedPopup, 1, 8, 8, 1);
            savedPopup.setHorizontalAlignment(HorizontalAlignment.CENTER);
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
        saveButton.setOnClick(saveRunnable);

        root.validate(this);
    }
}
