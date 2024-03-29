package com.provinceofmusic.screen;

import com.provinceofmusic.ProvinceOfMusicClient;
import dev.isxander.yacl3.api.ButtonOption;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.config.ConfigInstance;
import dev.isxander.yacl3.config.GsonConfigInstance;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.io.File;
import java.util.ArrayList;

import static com.provinceofmusic.ProvinceOfMusicClient.getConfig;

public class PlayRuleSheetEditScreen {
    Screen screenInstance;


    public static final ConfigInstance<ConfigScreen> INSTANCE = new GsonConfigInstance<>(ConfigScreen.class, FabricLoader.getInstance().getConfigDir().resolve(""));

    File sheet;
    //public static void save() {
    //    /* save your config! */
    //    INSTANCE.save();
    //}

    public PlayRuleSheetEditScreen(File sheetIn){
        sheet = sheetIn;
    }

    public Screen createGui() {

        //ArrayList<File> midiFiles = FetchMidiFiles();
        ArrayList<File> playrulesheetFiles = FetchPlayRuleSheetFiles();

        //ArrayList<ButtonOption> unconvertedmidibuttons = new ArrayList<>();

        ArrayList<ButtonOption> playrules = new ArrayList<>();





        //for(int i = 0; i < midiFiles.size(); i++){
        //    int finalI = i;
        //    unconvertedmidibuttons.add(ButtonOption.createBuilder()
        //            .name(Text.of(midiFiles.get(i).getName()))
        //            .text(Text.of("Modify File"))
//
        //            //.tooltip(Text.of("This is so easy!")) // optional
        //            .action((yaclScreen, buttonOption) -> {
        //                if(screenInstance != null){
        //                    MinecraftClient.getInstance().setScreen(new MidiEditScreen(Text.of(""), midiFiles.get(finalI)));
        //                }
        //                //System.out.println("Button has been pressed!");
        //            })
        //            //.controller(new ActionController(buttonOption /* provided by builder */, Text.of("Run") /* optional */))
        //            //.controller(opt -> new ActionController())
        //            .build());
        //}

        //playrules.add(ButtonOption.createBuilder()
        //        //.options(unconvertedmidibuttons)
        //        .name(Text.of("CreatePlayRuleSheet"))
        //        .text(Text.of(""))
//
        //        //.tooltip(Text.of("This is so easy!")) // optional
        //        .action((yaclScreen, buttonOption) -> {
        //            //if(screenInstance != null){
        //            //MinecraftClient.getInstance().setScreen(new MidiEditScreen(Text.of(""), midiFiles.get(finalI)));
        //            //}
        //            //System.out.println("Button has been pressed!");
        //            MinecraftClient.getInstance().setScreen(new PlayRuleSheetNameScreen());
        //        })
        //        .build());
//
        //playrules.add(ButtonOption.createBuilder()
        //        //.options(unconvertedmidibuttons)
        //        .name(Text.of("ImportPlayRuleSheet"))
        //        .text(Text.of(""))
//
        //        //.tooltip(Text.of("This is so easy!")) // optional
        //        .action((yaclScreen, buttonOption) -> {
        //            //if(screenInstance != null){
        //            //MinecraftClient.getInstance().setScreen(new MidiEditScreen(Text.of(""), midiFiles.get(finalI)));
        //            //}
        //            //System.out.println("Button has been pressed!");
        //        })
        //        .build());

        playrules.add(ButtonOption.createBuilder()
                .name(Text.of("Delete Sheet"))
                .text(Text.of(""))
                .action((yaclScreen, buttonOption) -> {
                    if(screenInstance != null){
                        sheet.delete();
                        //MinecraftClient.getInstance().setScreen(new MidiEditScreen(Text.of(""), playrulesheetFiles.get(finalI)));
                    }
                })
                .build());


        //for(int i = 0; i < playrulesheetFiles.size(); i++){
        //    int finalI = i;
        //    playrules.add(ButtonOption.createBuilder()
        //            .name(Text.of(playrulesheetFiles.get(i).getName()))
        //            .text(Text.of("Modify Rule"))
        //            .action((yaclScreen, buttonOption) -> {
        //                if(screenInstance != null){
        //                    MinecraftClient.getInstance().setScreen(new MidiEditScreen(Text.of(""), playrulesheetFiles.get(finalI)));
        //                }
        //            })
        //            .build());
        //}

        screenInstance = YetAnotherConfigLib.create(INSTANCE, (defaults, config, builder) -> builder
                        .title(Text.of("Province Of Music"))
                        //.category(ConfigCategory.createBuilder()
                        //        .name(Text.of("Midi Recorder Editor"))
                        //        //.tooltip(Text.of("This displays when you hover over a category button")) // optional
                        //        //.option(Option.createBuilder(boolean.class)
                        //        //        .name(Text.of("My Boolean Option"))
                        //        //        //.tooltip(Text.of("This option displays the basic capabilities of YetAnotherConfigLib")) // optional
                        //        //        .binding(
                        //        //                defaults.booleanToggle, // default
                        //        //                () -> config.booleanToggle, // getter
                        //        //                value -> config.booleanToggle = value // setter
                        //        //        )
                        //        //        //.controller(TickBoxController::new)
                        //        //        .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
                        //        //        //.controller(new BooleanController(Option.createBuilder(Binding.immutable(Binding<Integer>))))
                        //        //        .build())
                        //        //.option(ButtonOption.createBuilder()
                        //        //        .name(Text.of("Pressable Button"))
                        //        //        //.tooltip(Text.of("This is so easy!")) // optional
                        //        //        .action((yaclScreen, buttonOption) -> {
                        //        //            System.out.println("Button has been pressed!");
                        //        //        })
                        //        //        //.controller(new ActionController(buttonOption /* provided by builder */, Text.of("Run") /* optional */))
                        //        //        //.controller(opt -> new ActionController())
                        //        //        .build())
                        //        .options(unconvertedmidibuttons)
                        //        .build())
                        .category(ConfigCategory.createBuilder()
                                .name(Text.of("Play Rule SheetEditor"))
                                .option(ButtonOption.createBuilder()
                                        //.options(unconvertedmidibuttons)
                                        .name(Text.of("CreateUnnamedPlayRule"))
                                        .text(Text.of(""))

                                        //.tooltip(Text.of("This is so easy!")) // optional
                                        .action((yaclScreen, buttonOption) -> {
                                            //if(screenInstance != null){
                                            //MinecraftClient.getInstance().setScreen(new MidiEditScreen(Text.of(""), midiFiles.get(finalI)));
                                            //}
                                            //System.out.println("Button has been pressed!");
                                            MinecraftClient.getInstance().setScreen(new PlayRuleSheetNameScreen());
                                        })
                                        .build()
                                )
                                //.option(ButtonOption.createBuilder()
                                //        //.options(unconvertedmidibuttons)
                                //        .name(Text.of("ImportPlayRuleSheet"))
                                //        .text(Text.of(""))
//
                                //        //.tooltip(Text.of("This is so easy!")) // optional
                                //        .action((yaclScreen, buttonOption) -> {
                                //            //if(screenInstance != null){
                                //            //MinecraftClient.getInstance().setScreen(new MidiEditScreen(Text.of(""), midiFiles.get(finalI)));
                                //            //}
                                //            //System.out.println("Button has been pressed!");
                                //        })
                                //        .build()
                                //)
                                .options(playrules)
                                .build()


                        )
                //.save(ConfigScreen::save)).generateScreen(null);
        ).generateScreen(getConfig().createGui());
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

    //public ArrayList<File> FetchMidiFiles(){
    //    ArrayList<File> tempFiles = new ArrayList<>();
    //    int fileCount = ProvinceOfMusicClient.recordedmusicdir.listFiles().length;
    //    for(int i = 0; i < fileCount; i++){
    //        tempFiles.add(ProvinceOfMusicClient.recordedmusicdir.listFiles()[i]);
    //    }
    //    return tempFiles;
    //}

    public ArrayList<File> FetchPlayRuleSheetFiles(){
        ArrayList<File> tempFiles = new ArrayList<>();
        int fileCount = ProvinceOfMusicClient.playrulesheetsdir.listFiles().length;
        for(int i = 0; i < fileCount; i++){
            tempFiles.add(ProvinceOfMusicClient.playrulesheetsdir.listFiles()[i]);
        }
        return tempFiles;
    }
}
