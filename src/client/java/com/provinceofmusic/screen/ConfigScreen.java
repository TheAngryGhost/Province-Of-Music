package com.provinceofmusic.screen;

import com.provinceofmusic.ProvinceOfMusicClient;
import dev.isxander.yacl3.api.ButtonOption;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.config.ConfigEntry;
import dev.isxander.yacl3.config.ConfigInstance;
import dev.isxander.yacl3.config.GsonConfigInstance;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
//import dev.isxander.yacl3.*;
//import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;

public class ConfigScreen {

    public static final ConfigInstance<ConfigScreen> INSTANCE = new GsonConfigInstance<>(ConfigScreen.class, FabricLoader.getInstance().getConfigDir().resolve("province-of-music.json"));
    //private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @ConfigEntry public boolean booleanToggle = true;
    public int intSlider = 5;
    private Boolean myBooleanOption;

    Screen screenInstance;

    public static void save() {
        /* save your config! */
        INSTANCE.save();
    }

    public Screen createGui() {

        ArrayList<File> midiFiles = FetchFiles();

        ArrayList<ButtonOption> unconvertedmidibuttons = new ArrayList<>();

        for(int i = 0; i < midiFiles.size(); i++){
            int finalI = i;
            unconvertedmidibuttons.add(ButtonOption.createBuilder()
                    .name(Text.of(midiFiles.get(i).getName()))
                    .text(Text.of("Modify File"))

                    //.tooltip(Text.of("This is so easy!")) // optional
                    .action((yaclScreen, buttonOption) -> {
                        if(screenInstance != null){
                            MinecraftClient.getInstance().setScreen(new MidiEditScreen(Text.of(""), midiFiles.get(finalI)));
                        }
                        //System.out.println("Button has been pressed!");
                    })
                    //.controller(new ActionController(buttonOption /* provided by builder */, Text.of("Run") /* optional */))
                    //.controller(opt -> new ActionController())
                    .build());
        }

        screenInstance = YetAnotherConfigLib.create(INSTANCE, (defaults, config, builder) -> builder
                .title(Text.of("Province Of Music"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.of("Province Of Music"))
                        //.tooltip(Text.of("This displays when you hover over a category button")) // optional
                        //.option(Option.createBuilder(boolean.class)
                        //        .name(Text.of("My Boolean Option"))
                        //        //.tooltip(Text.of("This option displays the basic capabilities of YetAnotherConfigLib")) // optional
                        //        .binding(
                        //                defaults.booleanToggle, // default
                        //                () -> config.booleanToggle, // getter
                        //                value -> config.booleanToggle = value // setter
                        //        )
                        //        //.controller(TickBoxController::new)
                        //        .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
                        //        //.controller(new BooleanController(Option.createBuilder(Binding.immutable(Binding<Integer>))))
                        //        .build())
                        //.option(ButtonOption.createBuilder()
                        //        .name(Text.of("Pressable Button"))
                        //        //.tooltip(Text.of("This is so easy!")) // optional
                        //        .action((yaclScreen, buttonOption) -> {
                        //            System.out.println("Button has been pressed!");
                        //        })
                        //        //.controller(new ActionController(buttonOption /* provided by builder */, Text.of("Run") /* optional */))
                        //        //.controller(opt -> new ActionController())
                        //        .build())
                        .options(unconvertedmidibuttons)
                        .build())
                //.save(ConfigScreen::save)).generateScreen(null);
        ).generateScreen(null);
        return screenInstance;
    }

    //void RefreshScreen(Screen screenIn){
    //    ConfigScreen temp = new ConfigScreen();
    //    screenIn = temp.createGui(ProvinceOfMusicClient.getConfig());
    //}


    //public void openFolderInExplorer() throws IOException {
    //    File f = new File("recorded-music/");
    //    if (!f.exists()){
    //        f.mkdirs();
    //    }
    //    System.out.println(f.getAbsolutePath());
    //    Desktop.getDesktop().open(f.getAbsoluteFile());
    //}

    public ArrayList<File> FetchFiles(){


        ArrayList<File> tempFiles = new ArrayList<>();
        //File f = new File("recorded-music/");
        //if (!f.exists()){
        //    f.mkdirs();
        //}
        //System.out.println(ProvinceOfMusicClient.recordedmusicdir.getAbsolutePath());
        int fileCount = ProvinceOfMusicClient.recordedmusicdir.listFiles().length;
        for(int i = 0; i < fileCount; i++){
            //System.out.println(f.listFiles()[i].getName());
            //boolean existsInDeletedFilesList = false;
            //for(int j = 0; j < ProvinceOfMusicClient.deletedFiles.size(); j++){
                //if(ProvinceOfMusicClient.recordedmusicdir.listFiles()[i].equals(ProvinceOfMusicClient.deletedFiles.get(j))){
                    //existsInDeletedFilesList = true;
                //}
            //}
            //if(!existsInDeletedFilesList){
                tempFiles.add(ProvinceOfMusicClient.recordedmusicdir.listFiles()[i]);
            //}
        }
        return tempFiles;
    }
}
