package com.provinceofmusic.screen;

import com.provinceofmusic.ProvinceOfMusicClient;
import com.provinceofmusic.jukebox.PlayRule;
import com.provinceofmusic.jukebox.PlayRuleSheet;
//import dev.isxander.yacl3.api.ButtonOption;
//import dev.isxander.yacl3.api.ConfigCategory;
//import dev.isxander.yacl3.api.YetAnotherConfigLib;
//import dev.isxander.yacl3.config.ConfigInstance;
//import dev.isxander.yacl3.config.GsonConfigInstance;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

//import static com.provinceofmusic.ProvinceOfMusicClient.getConfig;

public class PlayRuleSheetEditScreen {
    Screen screenInstance;


    //public static final ConfigInstance<ConfigScreen> INSTANCE = new GsonConfigInstance<>(ConfigScreen.class, FabricLoader.getInstance().getConfigDir().resolve(""));

    //File sheet;

    ArrayList<PlayRule> playRules = new ArrayList<>();

    PlayRuleSheet playRuleSheet;

    public PlayRuleSheetEditScreen(File sheetIn) throws IOException {
        //sheet = sheetIn;
        playRuleSheet = PlayRuleSheet.getSheetFromName(sheetIn.getName());
    }

    public Screen createGui() {


//
        //ArrayList<ButtonOption> playrules = new ArrayList<>();
        //ArrayList<ButtonOption> tracks = new ArrayList<>();
        //
//
        //playrules.add(ButtonOption.createBuilder()
        //        .name(Text.of("Delete Sheet"))
        //        .text(Text.of(""))
        //        .action((yaclScreen, buttonOption) -> {
        //            if(screenInstance != null){
        //                playRuleSheet.sheet.delete();
        //            }
        //        })
        //        .build());
//
//
        //for(int i = 0; i < playRuleSheet.rules.size(); i++){
        //    int finalI = i;
        //    playrules.add(ButtonOption.createBuilder()
        //            .name(Text.of("   " + playRuleSheet.rules.get(i).ruleName))
        //            .text(Text.of("Modify Rule"))
        //            .action((yaclScreen, buttonOption) -> {
        //                MinecraftClient.getInstance().setScreen(new PlayRuleEditScreen(finalI, playRuleSheet, this.createGui()));
        //            })
        //            .build());
        //}
        //for(int i = 0; i < playRuleSheet.tracks.size(); i++){
//
        //    tracks.add(ButtonOption.createBuilder()
        //            .name(Text.of("♫ "+ playRuleSheet.tracksNames.get(i)))
        //            .text(Text.of("Listen"))
        //            .action((yaclScreen, buttonOption) -> {
        //            })
        //            .build());
        //}
//
        //screenInstance = YetAnotherConfigLib.create(INSTANCE, (defaults, config, builder) -> builder
        //                .title(Text.of("Province Of Music"))
        //                .category(ConfigCategory.createBuilder()
        //                        .name(Text.of("Rules"))
        //                        .option(ButtonOption.createBuilder()
        //                                .name(Text.of("Create Unnamed Play Rule"))
        //                                .text(Text.of(""))
        //                                .action((yaclScreen, buttonOption) -> {
        //                                    PlayRule tempPlayRule = new PlayRule("Unnamed Rule " + System.currentTimeMillis());
        //                                    playRuleSheet.rules.add(tempPlayRule);
        //                                    MinecraftClient.getInstance().setScreen(new PlayRuleEditScreen(playRuleSheet.rules.size()-1, playRuleSheet, this.createGui()));
        //                                })
        //                                .build()
        //                        )
        //                        .options(playrules)
        //                        .build()
        //                )
        //                .category(ConfigCategory.createBuilder()
        //                                .name(Text.of("Tracks"))
        //                                .option(ButtonOption.createBuilder()
        //                                                .name(Text.of("Open Tracks Folder"))
        //                                                .text(Text.of(""))
        //                                                .action((yaclScreen, buttonOption) -> {
        //                                                })
        //                                                .build()
        //                                )
        //                                .option(ButtonOption.createBuilder()
        //                                        .name(Text.of("Refresh List"))
        //                                        .text(Text.of(""))
        //                                        .action((yaclScreen, buttonOption) -> {
        //                                        })
        //                                        .build()
        //                                )
        //                .options(tracks)
        //                .build()
        //                )
        //).generateScreen(getConfig().createGui());
        return screenInstance;
    }


    public ArrayList<File> FetchPlayRuleSheetFiles(){
        ArrayList<File> tempFiles = new ArrayList<>();
        int fileCount = ProvinceOfMusicClient.playrulesheetsdir.listFiles().length;
        for(int i = 0; i < fileCount; i++){
            tempFiles.add(ProvinceOfMusicClient.playrulesheetsdir.listFiles()[i]);
        }
        return tempFiles;
    }


}
