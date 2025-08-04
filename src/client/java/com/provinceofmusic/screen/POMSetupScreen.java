package com.provinceofmusic.screen;

import com.provinceofmusic.ProvinceOfMusicClient;
import com.provinceofmusic.background.WMRUpdater;
import com.provinceofmusic.background.RamManager;
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
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(256 * (4 - ProvinceOfMusicClient.guiSize), 200 * (4 - ProvinceOfMusicClient.guiSize));
        root.setInsets(Insets.ROOT_PANEL);

        WLabel title = new WLabel(Text.literal("Province Of Music Setup"), 0x000000);
        root.add(title, 0, 0, 9, 3);

        WSprite icon = new WSprite(Identifier.of("provinceofmusic","icon.png"));
        root.add(icon, 0, 1, 3, 3);

        if(!ProvinceOfMusicClient.configSettings.saidNoToDownload && Objects.requireNonNull(ProvinceOfMusicClient.samplepacksdir.listFiles()).length == 0){
            WLabel missingSamplePackText = new WLabel(Text.literal("It appears that you have no sample packs yet. "), 0x000000);
            WLabel missingSamplePackText2 = new WLabel(Text.literal("Wynncraft's default music will play unless one is installed."), 0x000000);
            WLabel missingSamplePackText3 = new WLabel(Text.literal("Would you like to download the Wynn Music Remastered sample pack?"), 0x000000);
            root.add(missingSamplePackText, 0, 5, 9, 3);
            root.add(missingSamplePackText2, 0, 6, 9, 3);
            root.add(missingSamplePackText3, 0, 7, 9, 3);

            WButton yesButton = new WButton(Text.literal("Yes"));
            root.add(yesButton, 0, 8, 5, 1);
            Runnable yesButtonRunnable = () -> {
                WMRUpdater.download();
                MinecraftClient.getInstance().setScreen(new CottonClientScreen(new POMSetupScreen(true)));
            };
            yesButton.setOnClick(yesButtonRunnable);

            WButton noButton = new WButton(Text.literal("No"));
            root.add(noButton, 6, 8, 5, 1);
            Runnable noButtonRunnable = () -> {
                ProvinceOfMusicClient.configSettings.saidNoToDownload = true;
                ProvinceOfMusicClient.saveConfigSettings();
            };
            noButton.setOnClick(noButtonRunnable);
        }

        if(justDownloaded) {
            WLabel downloadCompleteText = new WLabel(Text.literal("Download Complete"), 0x00aa00);
            root.add(downloadCompleteText, 0, 8, 9, 3);
        }

        if(!RamManager.isRamGood()){
            WLabel ramWarnText = new WLabel(Text.literal("This mod requires at least 4GB of RAM to be allocated to Minecraft."), 0x000000);
            WLabel ramWarnText2 = new WLabel(Text.literal("This mod will not work correctly unless more RAM is allocated."), 0x000000);
            root.add(ramWarnText, 0, 9, 9, 3);
            root.add(ramWarnText2, 0, 10, 9, 3);
        }


        WButton closePopupButton = new WButton(Text.literal("Close this popup"));
        root.add(closePopupButton, 0, 11, 5, 1);
        Runnable closePopupButtonRunnable = () -> {
            MinecraftClient.getInstance().setScreen(null);
        };
        closePopupButton.setOnClick(closePopupButtonRunnable);


    }
}
