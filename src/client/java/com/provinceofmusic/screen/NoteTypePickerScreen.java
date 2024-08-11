package com.provinceofmusic.screen;

import com.provinceofmusic.jukebox.InstrumentDef;
import com.provinceofmusic.jukebox.NoteReplacer;
import com.provinceofmusic.ui.InstrumentWidget;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WListPanel;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.function.BiConsumer;

public class NoteTypePickerScreen extends LightweightGuiDescription {

    public SamplePackEditor prevScreen;
    public InstrumentWidget widget;
    public NoteTypePickerScreen(){
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        if(MinecraftClient.getInstance().options.getGuiScale().getValue() == 3){
            root.setSize(256, 240);
        } else{
            root.setSize(256, 400);
        }
        root.setInsets(Insets.ROOT_PANEL);

        WLabel title = new WLabel(Text.literal("Select Type"), 0x000000);
        root.add(title, 0, 0, 9, 3);

        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < NoteReplacer.NoteTypes.size(); i++){
            data.add(NoteReplacer.NoteTypes.get(i));
        }


        BiConsumer<String, WButton> configurator = (String s, WButton destination) -> {
            destination.setLabel(Text.literal(s));
            destination.setOnClick(() -> {
                widget.noteType.setText(s);
                MinecraftClient.getInstance().setScreen(new CottonClientScreen(prevScreen));
            });
        };

        WListPanel packList = new WListPanel(data, WButton::new, configurator);
        packList.setListItemHeight(18);
        if(MinecraftClient.getInstance().options.getGuiScale().getValue() == 3){
            root.add(packList, 0, 1, 14, 10);
        } else{
            root.add(packList, 0, 1, 14, 18);
        }
    }
}