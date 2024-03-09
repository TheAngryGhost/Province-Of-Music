package com.provinceofmusic.screen;

import com.provinceofmusic.ProvinceOfMusicClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PlayRuleSheetNameScreen extends Screen {
    protected PlayRuleSheetNameScreen() {
        super(Text.of("Screen"));
    }

    @Override
    protected void init() {
        TextWidget screenLabel = new TextWidget(Text.literal("What would you like to name this RuleSheet?"), textRenderer);
        screenLabel.setX((width/2) - screenLabel.getWidth()/2);
        screenLabel.setY((0 * 20) + 30);

        TextFieldWidget nameInput = new TextFieldWidget(textRenderer, (width/2) - (200/2), (height/2), 200, 20, Text.literal("")){
            //String textcurrent = "";
            //@Override
            //public void setText(String text) {
            //    super.setText(text);
////
            //    // Your code here
            //    System.out.println("Text field modified: " + text);
            //}
//
            //@Override
            //public void setMessage(Text message) {
            //    super.setMessage(message);
////
            //    System.out.println("Text field modified: " + message);
            //}
//
            //@Override
            //public Text getMessage() {
            //    return super.getMessage();
            //}
//
            @Override
            public void write(String text) {
                //System.out.println("Text field modified: " + text);
                System.out.println("Text field " + getText());
                super.write(text);
            }
//
            @Override
            public void eraseCharacters(int characterOffset) {
                //if(textcurrent.length() > 0){
                //    textcurrent = textcurrent.substring(0, textcurrent.length() - 1);
                //}
//
                System.out.println("Text field " + getText());
                //System.out.println("Text field characterOffset: " + characterOffset);
                super.eraseCharacters(characterOffset);
            }
        };

        ButtonWidget createButton = ButtonWidget.builder(Text.literal("Create"), button -> {
                    //MinecraftClient.getInstance().setScreen(POMConfigScreen.getScreen());
                    //fileInstance.getName().substring(0, fileInstance.getName().length() - 4)
                    //ConvertToMidi.convert(fileInstance, "exported-music/" + fileInstance.getName().substring(0, fileInstance.getName().length() - 4));
                    //ConvertToMidi.convert(fileInstance, ProvinceOfMusicClient.exportedmusicdir.getPath() + "/" + fileInstance.getName().substring(0, fileInstance.getName().length() - 4));
                    //ConvertToMidi.convert(fileInstance, ProvinceOfMusicClient.exportedmusicdir.getPath() + fileInstance.getName().substring(0, fileInstance.getName().length() - 4));
                    File outputFile = new File(ProvinceOfMusicClient.playrulesheetsdir+ "/" + nameInput.getText() +".json");
                    try {
                        FileWriter fileWriter = new FileWriter(outputFile);
                        fileWriter.write("empty");
                        //fileWriter.write("empy");
                        fileWriter.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
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
