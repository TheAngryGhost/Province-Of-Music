package com.provinceofmusic.screen;

import com.provinceofmusic.ProvinceOfMusicClient;
import com.provinceofmusic.background.PackUpgrader;
import com.provinceofmusic.jukebox.SamplePack;
import com.provinceofmusic.ui.SamplePackWidget;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
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

        int cell = 9;
        int totalCellsX = (400 * (4 - ProvinceOfMusicClient.guiSize)) / cell;

        int margin = 1;
        int centerX = (totalCellsX / 2 ) -1 ;

        int leftX = margin;
        int leftW = centerX - margin - 2;

        int rightX = centerX+1;
        int rightW = totalCellsX - margin;

        WGridPanel root = new WGridPanel(cell);
        setRootPanel(root);
        root.setSize(400 * (4 - ProvinceOfMusicClient.guiSize), 220 * (4 - ProvinceOfMusicClient.guiSize));
        root.setInsets(Insets.ROOT_PANEL);
        root.setGaps(1, 3);

        this.setUseDefaultRootBackground(false);
        root.setBackgroundPainter(BackgroundPainter.createGuiSprite(Identifier.of("provinceofmusic", "gui_overlay")));

        WLabel title = new WLabel(Text.literal("Sample Pack Configuration"), 0xFF000000);
        title.setHorizontalAlignment(HorizontalAlignment.CENTER);
        root.add(title, leftX, margin, leftW, 1);

        WButton backButton = new WButton(Text.literal("Back ↶"));
        backButton.setAlignment(HorizontalAlignment.CENTER);
        root.add(backButton, leftX, 3, leftW, 2);
        backButton.setOnClick(() -> {
            MinecraftClient.getInstance().setScreen(new CottonClientScreen(new ConfigScreen()));
            setActive = false;
        });

        WButton selectPackButton = new WButton(Text.literal("Select Pack"));
        selectPackButton.setAlignment(HorizontalAlignment.CENTER);
        root.add(selectPackButton, leftX, 10, leftW, 2);
        selectPackButton.setOnClick(() -> setActive = !setActive);

        WButton openFolderButton = new WButton(Text.literal("Open Folder \uD83D\uDDC1"));
        openFolderButton.setAlignment(HorizontalAlignment.CENTER);
        root.add(openFolderButton, leftX, 5, (leftW / 2) - 1, 2);
        openFolderButton.setOnClick(() -> {
            try {
                Desktop.getDesktop().open(ProvinceOfMusicClient.samplepacksdir.getAbsoluteFile());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        WButton refreshButton = new WButton(Text.literal("Refresh List ⟳"));
        refreshButton.setAlignment(HorizontalAlignment.CENTER);
        root.add(refreshButton, leftX + (leftW / 2) + 1, 5, (leftW / 2) - 1, 2);
        refreshButton.setOnClick(() -> MinecraftClient.getInstance().setScreen(new CottonClientScreen(new SamplePackConfig())));

        WButton createNewButton = new WButton(Text.literal("Create New"));
        createNewButton.setAlignment(HorizontalAlignment.CENTER);
        root.add(createNewButton, leftX, 18, leftW, 2);
        createNewButton.setOnClick(() -> {
            SamplePack.CreateNewPack();
            MinecraftClient.getInstance().setScreen(new CottonClientScreen(new SamplePackConfig()));
        });

        activeSamplePackLabel = new WLabel(Text.literal(""), 0xFF000000);
        activeSamplePackLabel.setHorizontalAlignment(HorizontalAlignment.CENTER);
        if (ProvinceOfMusicClient.configSettings.activeSamplePack == null) {
            activeSamplePackLabel.setText(Text.literal("no pack selected"));
        } else {
            activeSamplePackLabel.setText(Text.literal(ProvinceOfMusicClient.configSettings.activeSamplePack));
        }
        root.add(activeSamplePackLabel, leftX, 12, leftW, 1);

        ArrayList<File> samplePacks = SamplePack.FetchSamplePackFiles();
        ArrayList<SamplePack> data = new ArrayList<>();
        for (int i = 0; i < samplePacks.size(); i++) {
            data.add(SamplePack.getSamplePack(samplePacks.get(i)));
        }

        BiConsumer<SamplePack, SamplePackWidget> configurator = (SamplePack s, SamplePackWidget destination) -> {
            destination.nameLabel.setText(Text.literal(s.name));
            destination.authorLabel.setText(Text.literal(s.author));

            File inputImage = new File(Path.of(ProvinceOfMusicClient.samplepacksdir + "/" + s.name + "/" + "icon.png").toString());
            Identifier noimgfound = Identifier.of("provinceofmusic", "noiconfound.png");

            if (inputImage.exists()) {
                String path = s.name.replaceAll("[^\\p{L}0-9/._-]", "").toLowerCase() + "icon.png";
                Identifier id = Identifier.of("provinceofmusic", path);

                try (InputStream inputStream = new FileInputStream(inputImage)) {
                    NativeImage nativeImage = NativeImage.read(inputStream);
                    destination.icon = new WSprite(id);

                    TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
                    textureManager.registerTexture(id, new NativeImageBackedTexture(id::toString, nativeImage));
                } catch (IOException e) {
                    destination.icon = new WSprite(noimgfound);
                }
            } else {
                destination.icon = new WSprite(noimgfound);
            }

            destination.add(destination.icon, 150, 2, 32, 32);
            destination.thisPack = s;
        };

        WListPanel<SamplePack, SamplePackWidget> packList = new WListPanel<>(data, SamplePackWidget::new, configurator);
        packList.setListItemHeight(36);

        int listY = 1;
        int listH = (ProvinceOfMusicClient.guiSize == 3) ? 20 : 21;
        root.add(packList, rightX, listY, rightW - rightX - 1, listH);

        root.validate(this);
    }
}