package com.provinceofmusic.screen;

import com.provinceofmusic.ProvinceOfMusicClient;
import com.provinceofmusic.background.PackUpgrader;
import com.provinceofmusic.jukebox.SamplePack;
import com.provinceofmusic.ui.SamplePackWidget;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WListPanel;
import io.github.cottonmc.cotton.gui.widget.WSprite;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.function.BiConsumer;

public class SamplePackConfig extends LightweightGuiDescription {

    public static boolean setActive = false;
    public static WLabel activeSamplePackLabel;

    public SamplePackConfig() {
        PackUpgrader.main();

        int guiSize = ProvinceOfMusicClient.guiSize;
        float[] scalers = {0.9f, 0.95f, 1.0f, 1.05f};
        float scaler = (guiSize >= 1 && guiSize <= 4) ? scalers[guiSize - 1] : 1.0f;


        int cell = 9;
        int margin = 2;
        int rawCellsX = (int) ((405 * scaler) / cell);
        int rawCellsY = (int) ((225 * scaler) / cell);

        int totalCellsX = (rawCellsX % 2 == 0) ? rawCellsX : rawCellsX - 1; //just to make a perfect center
        int totalCellsY = (rawCellsY % 2 == 0) ? rawCellsY : rawCellsY - 1;

        int width = totalCellsX * cell;
        int height = totalCellsY * cell;

        int centerX = totalCellsX / 2 ;
        int leftS = margin-1;
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

        WLabel title = new WLabel(Text.literal("Sample Pack Configuration"), 0xFF000000);
        title.setHorizontalAlignment(HorizontalAlignment.CENTER);
        root.add(title, leftS, top, leftAdjust, 1);

        WButton backButton = new WButton(Text.literal("Back ↶"));
        backButton.setAlignment(HorizontalAlignment.CENTER);
        root.add(backButton, leftS, top + 2, leftAdjust, 2);
        backButton.setOnClick(() -> {
            MinecraftClient.getInstance().setScreen(new CottonClientScreen(new ConfigScreen()));
            setActive = false;
        });


        WButton refreshButton = new WButton(Text.literal("Refresh ⟳"));
        refreshButton.setAlignment(HorizontalAlignment.CENTER);
        root.add(refreshButton, leftS, top + 5, (leftAdjust / 2) - 1, 2);
        refreshButton.setOnClick(() -> MinecraftClient.getInstance().setScreen(new CottonClientScreen(new SamplePackConfig())));

        WButton openFolderButton = new WButton(Text.literal("Open Folder \uD83D\uDDC1"));
        openFolderButton.setAlignment(HorizontalAlignment.CENTER);
        root.add(openFolderButton, leftS + (leftAdjust / 2), top + 5, (leftAdjust / 2), 2);
        openFolderButton.setOnClick(() -> {
            try {
                Desktop.getDesktop().open(ProvinceOfMusicClient.samplepacksdir.getAbsoluteFile());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


        WButton selectPackButton = new WButton(Text.literal("Select Pack"));
        selectPackButton.setAlignment(HorizontalAlignment.CENTER);
        root.add(selectPackButton, leftS, top + 9, leftAdjust, 2);
        selectPackButton.setOnClick(() -> setActive = !setActive);

        activeSamplePackLabel = new WLabel(Text.literal(""), 0xFF000000);
        activeSamplePackLabel.setHorizontalAlignment(HorizontalAlignment.CENTER);
        if (ProvinceOfMusicClient.configSettings.activeSamplePack == null) {
            activeSamplePackLabel.setText(Text.literal("no pack selected"));
        } else {
            activeSamplePackLabel.setText(Text.literal(ProvinceOfMusicClient.configSettings.activeSamplePack));
        }
        root.add(activeSamplePackLabel, leftS, top + 12, leftE - leftS, 1);

        WButton createNewButton = new WButton(Text.literal("Create New"));
        createNewButton.setAlignment(HorizontalAlignment.CENTER);
        root.add(createNewButton, leftS, bottom - 3, leftAdjust, 2);
        createNewButton.setOnClick(() -> {
            SamplePack.CreateNewPack();
            MinecraftClient.getInstance().setScreen(new CottonClientScreen(new SamplePackConfig()));
        });

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

            destination.add(destination.icon, (int) (126* scaler), 2, 32, 32);
            destination.thisPack = s;
        };

        WListPanel<SamplePack, SamplePackWidget> packList = new WListPanel<>(data, SamplePackWidget::new, configurator);
        packList.setListItemHeight(36);

        root.add(packList, rightS - 2, top, rightE - rightS + 2, bottom-top);

        root.validate(this);
    }
}