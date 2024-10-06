package com.provinceofmusic.screen;

import com.provinceofmusic.ProvinceOfMusicClient;
import com.provinceofmusic.ui.InstrumentWidget;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WListPanel;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.function.BiConsumer;

public class SF2FilePickerScreen  extends LightweightGuiDescription {
    public SamplePackEditor prevScreen;
    public InstrumentWidget widget;
    public SF2FilePickerScreen(SamplePackEditor inScreen){
        prevScreen = inScreen;
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(256, 200 * (4 - ProvinceOfMusicClient.guiSize));
        root.setInsets(Insets.ROOT_PANEL);

        WLabel title = new WLabel(Text.literal("Select Type"), 0x000000);
        root.add(title, 0, 0, 9, 3);

        WButton backButton = new WButton(Text.literal("Back ↶"));
        Runnable backButtonRunnable = () -> {
            MinecraftClient.getInstance().setScreen(new CottonClientScreen(prevScreen));
        };
        root.add(backButton, 9, 0, 3,1);
        backButton.setOnClick(backButtonRunnable);

        ArrayList<File> data = inScreen.thisPack.getInstrumentFiles();
        BiConsumer<File, WButton> configurator = (File s, WButton destination) -> {
            destination.setLabel(Text.literal(s.getName()));
            destination.setOnClick(() -> {
                widget.instrumentDirectory.setText(s.getName());
                MinecraftClient.getInstance().setScreen(new CottonClientScreen(prevScreen));
            });
        };
        WListPanel sf2FileList = new WListPanel(data, WButton::new, configurator);
        sf2FileList.setListItemHeight(18);
        if(ProvinceOfMusicClient.guiSize == 3){
            root.add(sf2FileList, 0, 1, 14, 10);
        } else{
            root.add(sf2FileList, 0, 1, 14, 18);
        }
    }
}
