package com.provinceofmusic.screen;

import com.provinceofmusic.ProvinceOfMusicClient;
import com.provinceofmusic.background.PackUpgrader;
import com.provinceofmusic.jukebox.SamplePack;
import com.provinceofmusic.ui.SamplePackWidget;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.function.BiConsumer;

public class SamplePackConfig extends LightweightGuiDescription {

    public static boolean setActive = false;

    public static WLabel activeSamplePackLabel;
    public SamplePackConfig() {
        PackUpgrader.main();
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(256 * (4 - ProvinceOfMusicClient.guiSize), 200 * (4 - ProvinceOfMusicClient.guiSize));
        root.setInsets(Insets.ROOT_PANEL);

        WLabel title = new WLabel(Text.literal("Sample Pack Configuration"), 0x000000);
        root.add(title, 0, 0, 9, 3);

        WButton selectPackButton = new WButton(Text.literal("Select Pack"));
        root.add(selectPackButton, 0, 1, 6, 1);
        Runnable selectPackButtonRunnable = () -> {
            setActive = !setActive;
        };
        selectPackButton.setOnClick(selectPackButtonRunnable);

        activeSamplePackLabel = new WLabel(Text.literal("no pack selected"), 0x000000);
        if(ProvinceOfMusicClient.configSettings.activeSamplePack == null){
            activeSamplePackLabel.setText(Text.literal("no pack selected"));
        }
        else {
            activeSamplePackLabel.setText(Text.literal(ProvinceOfMusicClient.configSettings.activeSamplePack));
        }
        root.add(activeSamplePackLabel, 0, 2, 1, 1);

        WButton openFolderButton = new WButton(Text.literal("Open Folder \uD83D\uDDC1"));
        root.add(openFolderButton, 9, 1, 5, 1);
        Runnable openFolderButtonRunnable = () -> {
            try {
                Desktop.getDesktop().open(ProvinceOfMusicClient.samplepacksdir.getAbsoluteFile());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        openFolderButton.setOnClick(openFolderButtonRunnable);

        WButton refreshButton = new WButton(Text.literal("Refresh List ⟳"));
        root.add(refreshButton, 9, 2, 5, 1);
        Runnable refreshButtonRunnable = () -> {
            MinecraftClient.getInstance().setScreen(new CottonClientScreen(new SamplePackConfig()));
        };
        refreshButton.setOnClick(refreshButtonRunnable);

        WButton backButton = new WButton(Text.literal("Back ↶"));
        root.add(backButton, 0, 4, 3, 1);
        Runnable backButtonRunnable = () -> {
            MinecraftClient.getInstance().setScreen(new CottonClientScreen(new ConfigScreen()));
            setActive = false;
        };
        backButton.setOnClick(backButtonRunnable);

        WButton createNewButton = new WButton(Text.literal("Create New"));
        root.add(createNewButton, 9, 4, 4, 1);
        Runnable createNewButtonRunnable = () -> {
            SamplePack.CreateNewPack();
            MinecraftClient.getInstance().setScreen(new CottonClientScreen(new SamplePackConfig()));
        };
        createNewButton.setOnClick(createNewButtonRunnable);

        ArrayList<File> samplePacks = SamplePack.FetchSamplePackFiles();
        ArrayList<SamplePack> data = new ArrayList<>();
        for (int i = 0; i < samplePacks.size(); i++){
            data.add(SamplePack.getSamplePack(samplePacks.get(i)));
        }
        BiConsumer<SamplePack, SamplePackWidget> configurator = (SamplePack s, SamplePackWidget destination) -> {
            destination.nameLabel.setText(Text.literal(s.name));
            destination.authorLabel.setText(Text.literal(s.author));

            File inputImage = new File(Path.of(ProvinceOfMusicClient.samplepacksdir + "/" + s.name + "/" + "icon.png").toString());
            Identifier noimgfound = Identifier.of("provinceofmusic","noiconfound.png");

            if(inputImage != null){
                if (inputImage.exists()) {
                    String path = s.name.replaceAll("[^\\p{L}0-9/._-]", "").toLowerCase() + "" + "icon.png";
                    Identifier test = Identifier.of("provinceofmusic", path);
                    InputStream inputStream;
                    try {
                        inputStream = new FileInputStream(inputImage);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    NativeImage nativeImage;
                    try {
                        nativeImage = NativeImage.read(inputStream);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    destination.icon = new WSprite(test);

                    TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
                    textureManager.registerTexture(test, new NativeImageBackedTexture(nativeImage));
                }
                else {
                    destination.icon = new WSprite(noimgfound);
                }
            }
            else {
                destination.icon = new WSprite(noimgfound);
            }
            destination.add(destination.icon, 200, 2, 32, 32);
            destination.thisPack = s;
        };
        WListPanel packList = new WListPanel(data, SamplePackWidget::new, configurator);
        packList.setListItemHeight(36);
        root.add(packList, 0, 5, 14, 6);
    }
}
