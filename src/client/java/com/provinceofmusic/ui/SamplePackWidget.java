package com.provinceofmusic.ui;

import com.provinceofmusic.ProvinceOfMusicClient;
import com.provinceofmusic.jukebox.SamplePack;
import com.provinceofmusic.screen.SamplePackConfig;
import com.provinceofmusic.screen.SamplePackEditor;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WSprite;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class SamplePackWidget extends WPlainPanel {
    public WLabel nameLabel;

    public WLabel authorLabel;

    public WSprite icon;

    public SamplePack thisPack;

    public SamplePackWidget(){
        this.setSize(7, 3);
        this.setBackgroundPainter(BackgroundPainter.createColorful(0x000000));

        nameLabel = new WLabel(Text.literal("unnamed"));
        this.add(nameLabel, 5, 8-3, 5, 5);
        nameLabel.setColor(0xFFFFFF);

        authorLabel = new WLabel(Text.literal("unknown author"));
        this.add(authorLabel, 5, 20+3, 5, 5);
        authorLabel.setColor(0xFFFFFF);

        WButton backgroundButton = new WButton(Text.literal(""));
        this.add(backgroundButton, 0, 0, 235, 36);
        Runnable backgroundButtonRunnable = () -> {
            if(SamplePackConfig.setActive){
                ProvinceOfMusicClient.configSettings.activeSamplePack = thisPack.name;
                SamplePackConfig.activeSamplePackLabel.setText(Text.literal(ProvinceOfMusicClient.configSettings.activeSamplePack));
                SamplePackConfig.setActive = false;
                ProvinceOfMusicClient.saveConfigSettings();
            }
            else{
                MinecraftClient.getInstance().setScreen(new CottonClientScreen(new SamplePackEditor(thisPack)));
            }
        };
        backgroundButton.setOnClick(backgroundButtonRunnable);
    }
}
