package com.provinceofmusic.screen;

import com.provinceofmusic.ProvinceOfMusicClient;
import com.provinceofmusic.jukebox.PlayRule;
import com.provinceofmusic.jukebox.PlayRuleSheet;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class PlayRuleSheetNameScreen extends Screen {
    protected PlayRuleSheetNameScreen() {
        super(Text.of("Screen"));
    }

    @Override
    protected void init() {
        TextWidget screenLabel = new TextWidget(Text.literal("What would you like to name this RuleSheet?"), textRenderer);
        screenLabel.setX((width/2) - screenLabel.getWidth()/2);
        screenLabel.setY((0 * 20) + 30);


        TextFieldWidget nameInput = new TextFieldWidget(textRenderer, (width/2) - (200/2), (height/2), 200, 20, Text.literal(""));

        ButtonWidget createButton = ButtonWidget.builder(Text.literal("Create"), button -> {
                    //MinecraftClient.getInstance().setScreen(POMConfigScreen.getScreen());
                    //fileInstance.getName().substring(0, fileInstance.getName().length() - 4)
                    //ConvertToMidi.convert(fileInstance, "exported-music/" + fileInstance.getName().substring(0, fileInstance.getName().length() - 4));
                    //ConvertToMidi.convert(fileInstance, ProvinceOfMusicClient.exportedmusicdir.getPath() + "/" + fileInstance.getName().substring(0, fileInstance.getName().length() - 4));
                    //ConvertToMidi.convert(fileInstance, ProvinceOfMusicClient.exportedmusicdir.getPath() + fileInstance.getName().substring(0, fileInstance.getName().length() - 4));
                    //File folderTemp = new File(ProvinceOfMusicClient.playrulesheetsdir + "/" + nameInput.getText() + "/" + "tracks");
                    //folderTemp.mkdirs();
                    //File outputFile = new File(ProvinceOfMusicClient.playrulesheetsdir + "/" + nameInput.getText() + "/" + nameInput.getText() +".json");

                    PlayRuleSheet.createRuleSheet(nameInput.getText());
                    PlayRuleSheet sheet = new PlayRuleSheet();
                    sheet.rules = new ArrayList<>();
                    sheet.rules.add(new PlayRule("Rule1"));
                    PlayRuleSheet.writeRuleSheet(nameInput.getText(), sheet);

                    //try {
                    //    FileWriter fileWriter = new FileWriter(outputFile);
                    //    fileWriter.write("empty");
                    //    //fileWriter.write("empy");
                    //    fileWriter.close();
                    //} catch (IOException e) {
                    //    throw new RuntimeException(e);
                    //}
                    //PlayRuleSheet.getSheetFromFile(outputFile);
                    MinecraftClient.getInstance().setScreen(new ConfigScreen().createGui());


                })        .dimensions(((width / 2) - (50/2)) - 30, 250, 50, 20)
                //.tooltip(Tooltip.of(Text.literal("Export File As Midi")))
                .build();

        ButtonWidget cancelButton = ButtonWidget.builder(Text.literal("Cancel"), button -> {
                    //MinecraftClient.getInstance().setScreen(POMConfigScreen.getScreen());
                    //fileInstance.getName().substring(0, fileInstance.getName().length() - 4)
                    //ConvertToMidi.convert(fileInstance, "exported-music/" + fileInstance.getName().substring(0, fileInstance.getName().length() - 4));
                    //ConvertToMidi.convert(fileInstance, ProvinceOfMusicClient.exportedmusicdir.getPath() + "/" + fileInstance.getName().substring(0, fileInstance.getName().length() - 4));
                    //ConvertToMidi.convert(fileInstance, ProvinceOfMusicClient.exportedmusicdir.getPath() + fileInstance.getName().substring(0, fileInstance.getName().length() - 4));
                    //File outputFile = new File(ProvinceOfMusicClient.playrulesheetsdir+ "/" + nameInput.getText() +".json");
                    //try {
                    //    FileWriter fileWriter = new FileWriter(outputFile);
                    //    fileWriter.write("empty");
                    //    //fileWriter.write("empy");
                    //    fileWriter.close();
                    //} catch (IOException e) {
                    //    throw new RuntimeException(e);
                    //}
                    MinecraftClient.getInstance().setScreen(new ConfigScreen().createGui());


                })        .dimensions(((width / 2) - (50/2)) + 30, 250, 50, 20)
                //.tooltip(Tooltip.of(Text.literal("Export File As Midi")))
                .build();

        addDrawableChild(screenLabel);
        addDrawableChild(nameInput);
        addDrawableChild(createButton);
        addDrawableChild(cancelButton);
    }
}
