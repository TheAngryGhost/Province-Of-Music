package com.provinceofmusic.screen;

import com.provinceofmusic.ProvinceOfMusicClient;
import com.provinceofmusic.jukebox.PlayRule;
import com.provinceofmusic.jukebox.PlayRuleSheet;
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
import java.io.IOException;
import java.util.ArrayList;

import static com.provinceofmusic.ProvinceOfMusicClient.getConfig;

public class PlayRuleSheetEditScreen {
    Screen screenInstance;


    public static final ConfigInstance<ConfigScreen> INSTANCE = new GsonConfigInstance<>(ConfigScreen.class, FabricLoader.getInstance().getConfigDir().resolve(""));

    //File sheet;

    ArrayList<PlayRule> playRules = new ArrayList<>();

    PlayRuleSheet playRuleSheet;
    //public static void save() {
    //    /* save your config! */
    //    INSTANCE.save();
    //}

    public PlayRuleSheetEditScreen(File sheetIn) throws IOException {
        //sheet = sheetIn;
        playRuleSheet = PlayRuleSheet.getSheetFromName(sheetIn.getName());
    }

    public Screen createGui() {

        //ArrayList<File> midiFiles = FetchMidiFiles();
        ArrayList<File> playrulesheetFiles = FetchPlayRuleSheetFiles();

        //ArrayList<ButtonOption> unconvertedmidibuttons = new ArrayList<>();

        ArrayList<ButtonOption> playrules = new ArrayList<>();
        ArrayList<ButtonOption> tracks = new ArrayList<>();

        Screen previousScreen;







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
                        playRuleSheet.sheet.delete();
                        //MinecraftClient.getInstance().setScreen(new MidiEditScreen(Text.of(""), playrulesheetFiles.get(finalI)));
                    }
                })
                .build());


        for(int i = 0; i < playRuleSheet.rules.size(); i++){
            int finalI = i;
            playrules.add(ButtonOption.createBuilder()
                    .name(Text.of("   " + playRuleSheet.rules.get(i).ruleName))
                    .text(Text.of("Modify Rule"))
                    .action((yaclScreen, buttonOption) -> {
                        MinecraftClient.getInstance().setScreen(new PlayRuleEditScreen(finalI, playRuleSheet, this.createGui()));
                        //if(screenInstance != null){
                        //    MinecraftClient.getInstance().setScreen(new MidiEditScreen(Text.of(""), playrulesheetFiles.get(finalI)));
                        //}
                    })
                    .build());
        }
        //System.out.println(playRuleSheet.tracks.size());
        for(int i = 0; i < playRuleSheet.tracks.size(); i++){
            //System.out.println(playRuleSheet.tracks.get(i).getName());
            //int finalI = i;
            //String name = playRuleSheet.tracks.get(i).getName();
            //name = name.substring(0, playRuleSheet.tracks.get(i).getName().indexOf(".wav"));

            tracks.add(ButtonOption.createBuilder()
                    .name(Text.of("♫ "+ playRuleSheet.tracksNames.get(i)))
                    .text(Text.of("Listen"))
                    .action((yaclScreen, buttonOption) -> {
                        //if(screenInstance != null){
                        //    MinecraftClient.getInstance().setScreen(new MidiEditScreen(Text.of(""), playrulesheetFiles.get(finalI)));
                        //}
                    })
                    .build());
        }

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
                                .name(Text.of("Rules"))
                                .option(ButtonOption.createBuilder()
                                        //.options(unconvertedmidibuttons)
                                        .name(Text.of("Create Unnamed Play Rule"))
                                        .text(Text.of(""))

                                        //.tooltip(Text.of("This is so easy!")) // optional
                                        .action((yaclScreen, buttonOption) -> {
                                            PlayRule tempPlayRule = new PlayRule("Unnamed Rule " + System.currentTimeMillis());
                                            playRuleSheet.rules.add(tempPlayRule);
                                            MinecraftClient.getInstance().setScreen(new PlayRuleEditScreen(playRuleSheet.rules.size()-1, playRuleSheet, this.createGui()));
                                            //if(screenInstance != null){
                                            //MinecraftClient.getInstance().setScreen(new MidiEditScreen(Text.of(""), midiFiles.get(finalI)));
                                            //}
                                            //System.out.println("Button has been pressed!");
                                            //MinecraftClient.getInstance().setScreen(new PlayRuleSheetNameScreen());
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
                        .category(ConfigCategory.createBuilder()
                                        .name(Text.of("Tracks"))
                                        .option(ButtonOption.createBuilder()
                                                        //.options(unconvertedmidibuttons)
                                                        .name(Text.of("Open Tracks Folder"))
                                                        .text(Text.of(""))
                                                        //.tooltip(Text.of("This is so easy!")) // optional
                                                        .action((yaclScreen, buttonOption) -> {
                                                            //PlayRule tempPlayRule = new PlayRule("Unnamed" + playRules.size());
                                                            //playRules.add(tempPlayRule);
                                                            //MinecraftClient.getInstance().setScreen(new PlayRuleEditScreen(Text.of(""), tempPlayRule, playRules, this.createGui()));
                                                            //if(screenInstance != null){
                                                            //MinecraftClient.getInstance().setScreen(new MidiEditScreen(Text.of(""), midiFiles.get(finalI)));
                                                            //}
                                                            //System.out.println("Button has been pressed!");
                                                            //MinecraftClient.getInstance().setScreen(new PlayRuleSheetNameScreen());
                                                        })
                                                        .build()
                                        )
                                        .option(ButtonOption.createBuilder()
                                                //.options(unconvertedmidibuttons)
                                                .name(Text.of("Refresh List"))
                                                .text(Text.of(""))
                                                //.tooltip(Text.of("This is so easy!")) // optional
                                                .action((yaclScreen, buttonOption) -> {
                                                    //PlayRule tempPlayRule = new PlayRule("Unnamed" + playRules.size());
                                                    //playRules.add(tempPlayRule);
                                                    //MinecraftClient.getInstance().setScreen(new PlayRuleEditScreen(Text.of(""), tempPlayRule, playRules, this.createGui()));
                                                    //if(screenInstance != null){
                                                    //MinecraftClient.getInstance().setScreen(new MidiEditScreen(Text.of(""), midiFiles.get(finalI)));
                                                    //}
                                                    //System.out.println("Button has been pressed!");
                                                    //MinecraftClient.getInstance().setScreen(new PlayRuleSheetNameScreen());
                                                })
                                                .build()
                                        )
                                        //.option(ButtonOption.createBuilder()
                                        //        //.options(unconvertedmidibuttons)
                                        //        .name(Text.of("ImportPlayRuleSheet"))
                                        //        .text(Text.of(""))

                                //        //.tooltip(Text.of("This is so easy!")) // optional
                                //        .action((yaclScreen, buttonOption) -> {
                                //            //if(screenInstance != null){
                                //            //MinecraftClient.getInstance().setScreen(new MidiEditScreen(Text.of(""), midiFiles.get(finalI)));
                                //            //}
                                //            //System.out.println("Button has been pressed!");
                                //        })
                                //        .build()
                                //)
                        .options(tracks)
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
