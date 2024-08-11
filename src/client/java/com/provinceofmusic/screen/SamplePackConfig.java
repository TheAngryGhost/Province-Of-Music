package com.provinceofmusic.screen;

import com.provinceofmusic.ProvinceOfMusicClient;
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
import java.util.ArrayList;
import java.util.function.BiConsumer;

public class SamplePackConfig extends LightweightGuiDescription {

    public static boolean setActive = false;

    public static WLabel label;
    public SamplePackConfig() {
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(256, 240);
        root.setInsets(Insets.ROOT_PANEL);





        WLabel title = new WLabel(Text.literal("Sample Pack Configuration"), 0x000000);
        root.add(title, 0, 0, 9, 3);





        WButton selectPackButton = new WButton(Text.literal("Select Pack"));
        root.add(selectPackButton, 0, 1, 6, 1);
        Runnable selectPackButtonRunnable = () -> {
            setActive = !setActive;
        };
        selectPackButton.setOnClick(selectPackButtonRunnable);





        label = new WLabel(Text.literal("no pack selected"), 0x000000);
        if(ProvinceOfMusicClient.configSettings.activeSamplePack == null){
            label.setText(Text.literal("no pack selected"));
        }
        else {
            label.setText(Text.literal(ProvinceOfMusicClient.configSettings.activeSamplePack));
        }
        root.add(label, 0, 2, 1, 1);








        WButton openFolderButton = new WButton(Text.literal("Open Folder"));
        root.add(openFolderButton, 9, 1, 4, 1);
        Runnable openFolderButtonRunnable = () -> {
            try {
                Desktop.getDesktop().open(ProvinceOfMusicClient.samplepacksdir.getAbsoluteFile());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        openFolderButton.setOnClick(openFolderButtonRunnable);












        WButton refreshButton = new WButton(Text.literal("Refresh List"));
        root.add(refreshButton, 9, 2, 4, 1);
        Runnable refreshButtonRunnable = () -> {
            MinecraftClient.getInstance().setScreen(new CottonClientScreen(new SamplePackConfig()));
        };
        refreshButton.setOnClick(refreshButtonRunnable);











        WButton backButton = new WButton(Text.literal("Back"));
        root.add(backButton, 0, 4, 3, 1);
        Runnable backButtonRunnable = () -> {
            MinecraftClient.getInstance().setScreen(new CottonClientScreen(new ExampleGui()));
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







        ArrayList<File> samplePacks = FetchSamplePackFiles();
        ArrayList<SamplePack> data = new ArrayList<>();
        for (int i = 0; i < samplePacks.size(); i++){
            data.add(SamplePack.getSamplePack(samplePacks.get(i)));
        }
        BiConsumer<SamplePack, SamplePackWidget> configurator = (SamplePack s, SamplePackWidget destination) -> {
            destination.nameLabel.setText(Text.literal(s.name));
            destination.authorLabel.setText(Text.literal(s.author));
            System.err.println(ProvinceOfMusicClient.samplepacksdir + "\\" + s.name + "\\" + "icon.png");

            File inputImage = new File(ProvinceOfMusicClient.samplepacksdir + "\\" + s.name + "\\" + "icon.png");
            //Identifier noimgfound = new Identifier("provinceofmusic","noiconfound.png");
            Identifier noimgfound = Identifier.of("provinceofmusic","noiconfound.png");

            //System.err.println("provinceofmusic" + "      " + s.name.replaceAll("[^\\p{L}0-9/_-]", "") + "" + "icon.png" + " exists");

            if(inputImage != null){
                if (inputImage.exists()) {
                    String path = s.name.replaceAll("[^\\p{L}0-9/._-]", "").toLowerCase() + "" + "icon.png";
                    //String path = "ProvincePackicon.png";
                    //Identifier test = new Identifier("provinceofmusic", path);
                    Identifier test = Identifier.of("provinceofmusic", path);
                    //System.err.println(ProvinceOfMusicClient.samplepacksdir + "\\" + s.name + "\\" + "icon.png" + " exists");
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
        packList.setListItemHeight(2*18);
        root.add(packList, 0, 5, 14, 6);














    }

    public ArrayList<File> FetchSamplePackFiles(){
        ArrayList<File> tempFiles = new ArrayList<>();
        int fileCount = ProvinceOfMusicClient.samplepacksdir.listFiles().length;
        for(int i = 0; i < fileCount; i++){
            tempFiles.add(ProvinceOfMusicClient.samplepacksdir.listFiles()[i]);
        }
        return tempFiles;
    }
}