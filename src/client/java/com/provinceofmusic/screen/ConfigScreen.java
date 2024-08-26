package com.provinceofmusic.screen;

import com.provinceofmusic.ProvinceOfMusicClient;
import com.provinceofmusic.jukebox.NoteReplacer;
import com.provinceofmusic.jukebox.SamplePack;
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
        root.setSize(256, 240);
        root.setInsets(Insets.ROOT_PANEL);

        //WSprite icon = new WSprite(Identifier.of("minecraft","textures/item/redstone.png"));
        WLabel title = new WLabel(Text.literal("Province of Music"), 0x000000);
        root.add(title, 0, 0, 9, 3);

        //new Identifier(FabricLoader.getInstance().getConfigDir().resolve("splits.yml"))

        WSprite icon = new WSprite(Identifier.of("provinceofmusic","icon.png"));
        root.add(icon, 0, 1, 3, 3);

        WLabel label = new WLabel(Text.literal("Music Volume"), 0x000000);
        root.add(label, 0, 2+3, 1, 1);

        WSlider slider = new WSlider(0, 100, Axis.HORIZONTAL);
        slider.setValue((int) (ProvinceOfMusicClient.configSettings.volume * 100));
        NoteReplacer.musicVolume = slider.getValue() / 100f;
        root.add(slider, 0, 3+2, 5, 1);

        Runnable samplePackRunnable = () -> {
            MinecraftClient.getInstance().setScreen(new CottonClientScreen(new SamplePackConfig()));
        };

        Runnable recordingRunnable = () -> {
            MinecraftClient.getInstance().setScreen(new CottonClientScreen(new NoteRecordScreen()));
        };

        WButton button = new WButton(Text.literal("Sample Pack Editor"));
        button.setOnClick(samplePackRunnable);
        root.add(button, 0, 6, 6, 1);

        WButton button2 = new WButton(Text.literal("Recorded Music Editor"));
        root.add(button2, 0, 7, 7, 1);
        button2.setOnClick(recordingRunnable);


        Runnable runnable = () -> {
            //System.err.println("Button Pressed");

            ProvinceOfMusicClient.setConfigSettings();

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



            WLabel savedPopup = new WLabel(Text.literal("Changes Saved"), 0x000000);
            root.add(savedPopup, 0, 10, 2, 1);
            //MinecraftClient.getInstance().setScreen(new CottonClientScreen(ProvinceOfMusicClient.RootConfigScreen));
            NoteReplacer.musicVolume = slider.getValue() / 100f;
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

        WButton saveButton = new WButton(Text.literal("Save Changes"));
        saveButton.setOnClick(runnable);
        root.add(saveButton, 0, 9, 7, 1);


        //WButton button = new WButton(Text.literal("gui.examplemod.examplebutton"));
        //root.add(button, 0, 3, 4, 1);

        //WLabel nameLabel = new WLabel(Text.literal("Test"), 0xFFFFFF);
        //root.add(nameLabel, 0, 4, 2, 1);

        root.validate(this);
    }
}
