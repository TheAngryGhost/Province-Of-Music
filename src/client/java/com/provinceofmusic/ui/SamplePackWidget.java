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
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SamplePackWidget extends WPlainPanel {
    public WLabel nameLabel;

    public WLabel authorLabel;

    public WSprite icon;

    public SamplePack thisPack;

    public SamplePackWidget(){
        WButton backgroundButton = new WButton(Text.literal(""));
        this.add(backgroundButton, 0, 0, 235, 36);
        //35

        nameLabel = new WLabel(Text.literal("unnamed"));
        this.add(nameLabel, 5, 8-3, 5, 5);
        nameLabel.setColor(0xFFFFFF);

        authorLabel = new WLabel(Text.literal("unknown author"));
        this.add(authorLabel, 5, 20+3, 5, 5);
        authorLabel.setColor(0xFFFFFF);

        this.setSize(7, 3);

        this.setBackgroundPainter(BackgroundPainter.createColorful(0x000000));

        Runnable runnable = () -> {
            //System.err.println("Button Pressed");
            if(SamplePackConfig.setActive){
                ProvinceOfMusicClient.configSettings.activeSamplePack = thisPack.name;
                SamplePackConfig.label.setText(Text.literal(ProvinceOfMusicClient.configSettings.activeSamplePack));
                SamplePackConfig.setActive = false;
                ProvinceOfMusicClient.setConfigSettings();
            }
            else{
                MinecraftClient.getInstance().setScreen(new CottonClientScreen(new SamplePackEditor(thisPack)));
            }
        };
        backgroundButton.setOnClick(runnable);


        //this.setBackgroundPainter(new BackgroundPainter() {
        //    @Override
        //    public void paintBackground(DrawContext context, int left, int top, WWidget panel) {
        //        context.fill(-1000, -1000, 1000, 1000, 0xFF0000);
        //    }
        //});
        //this.addPainters();


        //TextureManager textureManager = new TextureManager();
        //this.client.getTextureManager();




        //InputSupplier<InputStream> inputSupplier = new InputSupplier<InputStream>() {
        //
        //
        //    @Override
        //    public InputStream get() throws IOException {
        //        return null;
        //    }
        //}
        //WSprite icon = new WSprite(test);
        //this.add(icon, 200, 2, 32, 32);


    }
}
