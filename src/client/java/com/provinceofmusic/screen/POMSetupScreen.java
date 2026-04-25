package com.provinceofmusic.screen;

import com.provinceofmusic.ProvinceOfMusicClient;
import com.provinceofmusic.background.WMRUpdater;
import com.provinceofmusic.background.RamManager;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WSprite;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Objects;

public class POMSetupScreen extends LightweightGuiDescription {

    public POMSetupScreen(boolean justDownloaded) {


        int cell = 9;
        int totalCellsX = (400 * (4 - ProvinceOfMusicClient.guiSize)) / cell;

        int margin = 2;

        int leftX = margin+3;
        int rightW = totalCellsX - margin;



        WGridPanel root = new WGridPanel(cell);
        setRootPanel(root);
        root.setSize(400 * (4 - ProvinceOfMusicClient.guiSize), 220 * (4 - ProvinceOfMusicClient.guiSize));
        root.setInsets(Insets.ROOT_PANEL);

        this.setUseDefaultRootBackground(false);
        root.setBackgroundPainter(BackgroundPainter.createGuiSprite(Identifier.of("provinceofmusic", "menu_background") ));


        WLabel title = new WLabel(Text.literal("Province Of Music Setup"), 0xFF000000);
        root.add(title, leftX, 2, 9, 3);

        WSprite icon = new WSprite(Identifier.of("provinceofmusic","icon.png"));
        root.add(icon, rightW-5, 2, 5, 5);

        if(!ProvinceOfMusicClient.configSettings.saidNoToDownload && Objects.requireNonNull(ProvinceOfMusicClient.samplepacksdir.listFiles()).length == 0){
            WLabel missingSamplePackText = new WLabel(Text.literal("It appears that you have no sample packs yet. "), 0xFF000000);
            WLabel missingSamplePackText2 = new WLabel(Text.literal("Wynncraft's default music will play unless one is installed."), 0xFF000000);
            WLabel missingSamplePackText3 = new WLabel(Text.literal("Would you like to download the Wynn Music Remastered sample pack?"), 0xFF000000);
            root.add(missingSamplePackText, leftX, 5, 9, 3);
            root.add(missingSamplePackText2, leftX, 6, 9, 3);
            root.add(missingSamplePackText3, leftX, 7, 9, 3);

            WButton yesButton = new WButton(Text.literal("Yes"));
            root.add(yesButton, leftX+1, 9, 5, 2);
            Runnable yesButtonRunnable = () -> {
                WMRUpdater.download();
                MinecraftClient.getInstance().setScreen(new CottonClientScreen(new POMSetupScreen(true)));
            };
            yesButton.setOnClick(yesButtonRunnable);

            WButton noButton = new WButton(Text.literal("No"));
            root.add(noButton, leftX+7, 9, 5, 2);
            Runnable noButtonRunnable = () -> {
                ProvinceOfMusicClient.configSettings.saidNoToDownload = true;
                ProvinceOfMusicClient.saveConfigSettings();
                MinecraftClient.getInstance().setScreen(null);
            };
            noButton.setOnClick(noButtonRunnable);
        }

        if(justDownloaded) {
            WLabel downloadCompleteText = new WLabel(Text.literal("Download Complete"), 0xFF00AA00);
            root.add(downloadCompleteText, leftX, 8, 9, 3);
        }

        if(!RamManager.isRamGood()){
            WLabel ramWarnText = new WLabel(Text.literal("This mod requires at least 4GB of RAM to be allocated to Minecraft."), 0xFF000000);
            WLabel ramWarnText2 = new WLabel(Text.literal("This mod will not work correctly unless more RAM is allocated."), 0xFF000000);
            root.add(ramWarnText, leftX, 13, 9, 3);
            root.add(ramWarnText2, leftX, 12, 9, 3);
        }


        WButton closePopupButton = new WButton(Text.literal("Close this popup"));
        root.add(closePopupButton, leftX+1, 20, 10, 2);
        Runnable closePopupButtonRunnable = () -> {
            MinecraftClient.getInstance().setScreen(null);
        };
        closePopupButton.setOnClick(closePopupButtonRunnable);


    }
}
