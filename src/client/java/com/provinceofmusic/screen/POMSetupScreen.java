package com.provinceofmusic.screen;

import com.provinceofmusic.ProvinceOfMusicClient;
import com.provinceofmusic.background.WMRUpdater;
import com.provinceofmusic.background.RamManager;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Objects;

public class POMSetupScreen extends LightweightGuiDescription {

    public POMSetupScreen(boolean justDownloaded) {


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

        int leftS = margin+3;
        int rightE = totalCellsX - margin;
        int bottom = totalCellsY - margin;
        int spaceX = rightE-leftS;

        WGridPanel root = new WGridPanel(cell);
        setRootPanel(root);
        root.setSize(width, height);
        root.setInsets(Insets.ROOT_PANEL);

        this.setUseDefaultRootBackground(false);
        root.setBackgroundPainter(BackgroundPainter.createGuiSprite(Identifier.of("provinceofmusic", "menu_background") ));


        WLabel title = new WLabel(Text.literal("Province Of Music Setup"), 0xFF000000);
        root.add(title, leftS, 2, 9, 2);

        WSprite icon = new WSprite(Identifier.of("provinceofmusic","icon.png"));
        root.add(icon, rightE -5, 2, 5, 5);

        if(!ProvinceOfMusicClient.configSettings.saidNoToDownload && Objects.requireNonNull(ProvinceOfMusicClient.samplepacksdir.listFiles()).length == 0){
            WText missingSamplePackText = new WText(Text.literal("It appears that you have no sample packs yet. "), 0xFF000000);
            WText missingSamplePackText2 = new WText(Text.literal("Wynncraft's default music will play unless one is installed."), 0xFF000000);
            WText missingSamplePackText3 = new WText(Text.literal("Would you like to download the Wynn Music Remastered sample pack?"), 0xFF000000);



            root.add(missingSamplePackText, leftS, 5, spaceX, 3);
            root.add(missingSamplePackText2, leftS, 6, spaceX, 3);
            root.add(missingSamplePackText3, leftS, 7, spaceX, 3);

            WButton yesButton = new WButton(Text.literal("Yes"));
            root.add(yesButton, leftS +1, 9, 5, 2);
            Runnable yesButtonRunnable = () -> {
                WMRUpdater.download();
                MinecraftClient.getInstance().setScreen(new CottonClientScreen(new POMSetupScreen(true)));
            };
            yesButton.setOnClick(yesButtonRunnable);

            WButton noButton = new WButton(Text.literal("No"));
            root.add(noButton, leftS +7, 9, 5, 2);
            Runnable noButtonRunnable = () -> {
                ProvinceOfMusicClient.configSettings.saidNoToDownload = true;
                ProvinceOfMusicClient.saveConfigSettings();
                MinecraftClient.getInstance().setScreen(null);
            };
            noButton.setOnClick(noButtonRunnable);
        }

        if(justDownloaded) {
            WLabel downloadCompleteText = new WLabel(Text.literal("Download Complete"), 0xFF00AA00);
            root.add(downloadCompleteText, leftS, 8, 9, 3);
        }

        if(!RamManager.isRamGood()){
            WText ramWarnText = new WText(Text.literal("This mod requires at least 4GB of RAM to be allocated to Minecraft."), 0xFF000000);
            WText ramWarnText2 = new WText(Text.literal("This mod will not work correctly unless more RAM is allocated."), 0xFF000000);
            root.add(ramWarnText, leftS, 13, spaceX, 2);
            root.add(ramWarnText2, leftS, 15, spaceX, 2);
        }


        WButton closePopupButton = new WButton(Text.literal("Close this popup"));
        root.add(closePopupButton, leftS +1, bottom-3, 12, 2);
        Runnable closePopupButtonRunnable = () -> {
            MinecraftClient.getInstance().setScreen(null);
        };
        closePopupButton.setOnClick(closePopupButtonRunnable);


    }
}
