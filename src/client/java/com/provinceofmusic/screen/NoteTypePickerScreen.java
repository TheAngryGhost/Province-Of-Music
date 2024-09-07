package com.provinceofmusic.screen;

import com.provinceofmusic.ProvinceOfMusicClient;
import com.provinceofmusic.listeners.NoteListenerHelper;
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

import java.util.ArrayList;
import java.util.function.BiConsumer;

public class NoteTypePickerScreen extends LightweightGuiDescription {

    public SamplePackEditor prevScreen;
    public InstrumentWidget widget;
    public NoteTypePickerScreen(){
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

        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < NoteListenerHelper.instrumentSounds.size(); i++){
            data.add(NoteListenerHelper.instrumentSounds.get(i).registeredName);
        }
        BiConsumer<String, WButton> configurator = (String s, WButton destination) -> {
            destination.setLabel(Text.literal(s));
            destination.setOnClick(() -> {
                widget.noteType.setText(s);
                MinecraftClient.getInstance().setScreen(new CottonClientScreen(prevScreen));
            });
        };
        WListPanel noteTypeList = new WListPanel(data, WButton::new, configurator);
        noteTypeList.setListItemHeight(18);
        if(MinecraftClient.getInstance().options.getGuiScale().getValue() == 3){
            root.add(noteTypeList, 0, 1, 14, 10);
        } else{
            root.add(noteTypeList, 0, 1, 14, 18);
        }
    }
}
