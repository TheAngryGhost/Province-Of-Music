package com.provinceofmusic.screen;

import com.provinceofmusic.ProvinceOfMusicClient;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WListPanel;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.BiConsumer;

public class NoteRecordScreen extends LightweightGuiDescription {

    public NoteRecordScreen(){
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        //if(MinecraftClient.getInstance().options.getGuiScale().getValue() == 3){
            root.setSize(256, 240);
        //} else{
            //root.setSize(256, 400);
        //}
        root.setInsets(Insets.ROOT_PANEL);



        WLabel title = new WLabel(Text.literal("Recording Editor"), 0x000000);
        root.add(title, 0, 0, 9, 3);

        WButton backButton = new WButton(Text.literal("Back"));
        Runnable runnable4 = () -> {
            MinecraftClient.getInstance().setScreen(new CottonClientScreen(new ConfigScreen()));
        };
        root.add(backButton, 6, 0, 3,1);
        backButton.setOnClick(runnable4);

        WButton openFolderButton = new WButton(Text.literal("Open Folder"));
        root.add(openFolderButton, 10, 0, 4, 1);
        Runnable openFolderButtonRunnable = () -> {
            try {
                Desktop.getDesktop().open(ProvinceOfMusicClient.recordedmusicdir.getAbsoluteFile());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        openFolderButton.setOnClick(openFolderButtonRunnable);

        ArrayList<File> data = FetchMidiFiles();
        BiConsumer<File, WButton> configurator = (File s, WButton destination) -> {
            destination.setLabel(Text.of(s.getName()));
            destination.setOnClick(new Runnable() {
                @Override
                public void run() {
                    MinecraftClient.getInstance().setScreen(new CottonClientScreen(new RecordingEditor(s)));
                }
            });
        };
        WListPanel packList;
        packList = new WListPanel(data, WButton::new, configurator);
        packList.setListItemHeight(20);
        root.add(packList, 0, 4, 15, 8);

    }
    public ArrayList<File> FetchMidiFiles(){
        ArrayList<File> tempFiles = new ArrayList<>();
        int fileCount = ProvinceOfMusicClient.recordedmusicdir.listFiles().length;
        for(int i = 0; i < fileCount; i++){
            tempFiles.add(ProvinceOfMusicClient.recordedmusicdir.listFiles()[i]);
        }
        return tempFiles;
    }
}
