package com.provinceofmusic.screen;

import com.provinceofmusic.ProvinceOfMusicClient;
import com.provinceofmusic.jukebox.NoteReplacer;
import com.provinceofmusic.jukebox.SamplePack;
import com.provinceofmusic.recorder.DebugMode;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Timer;
import java.util.TimerTask;

public class ConfigScreen extends LightweightGuiDescription {
    public ConfigScreen() {
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(256 * (4 - ProvinceOfMusicClient.guiSize), 250 * (4 - ProvinceOfMusicClient.guiSize));
        root.setInsets(Insets.ROOT_PANEL);

        WLabel title = new WLabel(Text.literal("Province of Music"), 0x000000);
        root.add(title, 0, 0, 9, 3);

        WSprite icon = new WSprite(Identifier.of("provinceofmusic","icon.png"));
        root.add(icon, 0, 1, 3, 3);

        WLabel rootLabel = new WLabel(Text.literal("Music Volume"), 0x000000);
        root.add(rootLabel, 0, 2+3, 1, 1);

        WSlider volumeSlider = new WSlider(0, 100, Axis.HORIZONTAL);
        volumeSlider.setValue((int) (ProvinceOfMusicClient.configSettings.volume * 100));
        NoteReplacer.musicVolume = volumeSlider.getValue() / 100f;
        root.add(volumeSlider, 0, 3+2, 5, 1);

        WButton recordedMusicMenuButton = new WButton(Text.literal("Recorded Music Editor"));
        root.add(recordedMusicMenuButton, 0, 7, 7, 1);
        Runnable recordingRunnable = () -> {
            MinecraftClient.getInstance().setScreen(new CottonClientScreen(new NoteRecordScreen()));
        };
        recordedMusicMenuButton.setOnClick(recordingRunnable);

        WButton samplePackMenuButton = new WButton(Text.literal("Sample Pack Editor"));
        Runnable samplePackRunnable = () -> {
            MinecraftClient.getInstance().setScreen(new CottonClientScreen(new SamplePackConfig()));
        };
        root.add(samplePackMenuButton, 0, 6, 6, 1);
        samplePackMenuButton.setOnClick(samplePackRunnable);


        WToggleButton debugModeToggle = new WToggleButton(Text.literal("Debug Mode"));
        root.add(debugModeToggle, 6, 0, 3, 1);
        debugModeToggle.setToggle(ProvinceOfMusicClient.configSettings.debugMode);


        WButton saveButton = new WButton(Text.literal("Save Changes \uD83D\uDDAB").styled(style -> style.withBold(true)));
        root.add(saveButton, 0, 9, 7, 1);
        Runnable saveRunnable = () -> {
            NoteReplacer.musicVolume = volumeSlider.getValue() / 100f;
            ProvinceOfMusicClient.configSettings.volume = NoteReplacer.musicVolume;
            DebugMode.isOn = debugModeToggle.getToggle();
            ProvinceOfMusicClient.configSettings.debugMode = DebugMode.isOn;

            ProvinceOfMusicClient.saveConfigSettings();

            if(ProvinceOfMusicClient.configSettings.activeSamplePack != null){
                NoteReplacer.interupt = true;
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        NoteReplacer.instruments = SamplePack.getSamplePack(SamplePack.getFile(ProvinceOfMusicClient.configSettings.activeSamplePack)).getInstruments(NoteReplacer.instruments);
                        NoteReplacer.interupt = false;
                    }
                };

                Timer timer = new Timer(true);
                timer.schedule(task, 300);
            }

            WLabel savedPopup = new WLabel(Text.literal("Changes Saved").styled(style -> style.withItalic(true)), 0x000000);
            root.add(savedPopup, 0, 10, 2, 1);
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
