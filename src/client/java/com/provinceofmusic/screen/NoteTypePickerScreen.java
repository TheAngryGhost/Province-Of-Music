package com.provinceofmusic.screen;

import com.provinceofmusic.ProvinceOfMusicClient;
import com.provinceofmusic.listeners.NoteListenerHelper;
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

import java.util.ArrayList;
import java.util.function.BiConsumer;

public class NoteTypePickerScreen extends LightweightGuiDescription {

    public SamplePackEditor prevScreen;
    public InstrumentWidget widget;

    public NoteTypePickerScreen() {

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

        WGridPanel root = new WGridPanel(cell);
        setRootPanel(root);
        root.setSize(width, height);
        root.setInsets(Insets.ROOT_PANEL);

        this.setUseDefaultRootBackground(false);
        root.setBackgroundPainter(BackgroundPainter.createGuiSprite(Identifier.of("provinceofmusic", "gui_overlay")));

        WLabel title = new WLabel(Text.literal("Select Type"), 0xFF000000);
        title.setHorizontalAlignment(HorizontalAlignment.CENTER);
        root.add(title, leftS, top, leftE - leftS, 1);

        WButton backButton = new WButton(Text.literal("Back ↶"));
        backButton.setAlignment(HorizontalAlignment.CENTER);
        root.add(backButton, leftS, top + 2, leftE - leftS, 2);
        backButton.setOnClick(() -> MinecraftClient.getInstance().setScreen(new CottonClientScreen(prevScreen)));

        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < NoteListenerHelper.instruments.size(); i++) {
            data.add(NoteListenerHelper.instruments.get(i).registeredName);
        }

        BiConsumer<String, WButton> configurator = (String s, WButton destination) -> {
            destination.setLabel(Text.literal(s));
            destination.setAlignment(HorizontalAlignment.CENTER);
            destination.setOnClick(() -> {
                widget.noteType.setText(s);
                MinecraftClient.getInstance().setScreen(new CottonClientScreen(prevScreen));
            });
        };

        WListPanel<String, WButton> noteTypeList = new WListPanel<>(data, WButton::new, configurator);
        noteTypeList.setListItemHeight(18);


        root.add(noteTypeList, rightS - 2, top, rightE - rightS + 2, bottom-top);

        root.validate(this);
    }
}