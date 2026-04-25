package com.provinceofmusic.screen;

import com.provinceofmusic.ProvinceOfMusicClient;
import com.provinceofmusic.ui.InstrumentWidget;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WListPanel;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.io.File;
import java.util.ArrayList;
import java.util.function.BiConsumer;

public class SF2FilePickerScreen extends LightweightGuiDescription {

    public SamplePackEditor prevScreen;
    public InstrumentWidget widget;

    public SF2FilePickerScreen(SamplePackEditor inScreen) {
        prevScreen = inScreen;

        int cell = 9;
        int totalCellsX = (400 * (4 - ProvinceOfMusicClient.guiSize)) / cell;

        int margin = 2;
        int centerX = totalCellsX / 2;

        int leftX = margin;
        int leftW = centerX - margin - 2;

        int rightX = centerX;
        int rightW = totalCellsX - margin;

        WGridPanel root = new WGridPanel(cell);
        setRootPanel(root);
        root.setSize(400 * (4 - ProvinceOfMusicClient.guiSize), 220 * (4 - ProvinceOfMusicClient.guiSize));
        root.setInsets(Insets.ROOT_PANEL);
        root.setGaps(1, 3);

        this.setUseDefaultRootBackground(false);
        root.setBackgroundPainter(BackgroundPainter.createGuiSprite(Identifier.of("provinceofmusic", "gui_overlay")));

        WLabel title = new WLabel(Text.literal("Select File"), 0xFF000000);
        title.setHorizontalAlignment(HorizontalAlignment.CENTER);
        root.add(title, leftX, margin, leftW, 1);

        WButton backButton = new WButton(Text.literal("Back ↶"));
        backButton.setAlignment(HorizontalAlignment.CENTER);
        root.add(backButton, leftX, 3, leftW, 2);
        backButton.setOnClick(() -> MinecraftClient.getInstance().setScreen(new CottonClientScreen(prevScreen)));

        ArrayList<File> data = inScreen.thisPack.getInstrumentFiles();
        BiConsumer<File, WButton> configurator = (File s, WButton destination) -> {
            destination.setLabel(Text.literal(s.getName()));
            destination.setOnClick(() -> {
                if (widget != null) {
                    widget.instrumentDirectory.setText(s.getName());
                }
                MinecraftClient.getInstance().setScreen(new CottonClientScreen(prevScreen));
            });
        };

        WListPanel<File, WButton> sf2FileList = new WListPanel<>(data, WButton::new, configurator);
        sf2FileList.setListItemHeight(18);

        int listY = margin;
        int listH = (ProvinceOfMusicClient.guiSize == 3) ? 18 : 20;
        root.add(sf2FileList, rightX, listY, rightW - rightX, listH);

        root.validate(this);
    }
}