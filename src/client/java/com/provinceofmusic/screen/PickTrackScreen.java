package com.provinceofmusic.screen;

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

import java.util.ArrayList;

//import static com.provinceofmusic.ProvinceOfMusicClient.getConfig;

public class PickTrackScreen {

    //public static final ConfigInstance<ConfigScreen> INSTANCE = new GsonConfigInstance<>(ConfigScreen.class, FabricLoader.getInstance().getConfigDir().resolve(""));

    Screen screenInstance;
    //PlayRuleSheet playRuleSheet;

    //ArrayList<ButtonOption> tracks = new ArrayList<>();

    public Screen createGui(int ruleIndex, PlayRuleSheet playRuleSheet) {

        //for(int i = 0; i < playRuleSheet.tracks.size(); i++){
        //    int finalI = i;
        //    if(playRuleSheet.tracks.get(i).getName().indexOf(".wav") != -1){
        //    tracks.add(ButtonOption.createBuilder()
        //            .name(Text.of("♫ "+ playRuleSheet.tracksNames.get(i)))
        //            .text(Text.of("SELECT"))
        //            .action((yaclScreen, buttonOption) -> {
        //                playRuleSheet.rules.get(ruleIndex).trackName = playRuleSheet.tracksNames.get(finalI);
        //                PlayRuleEditScreen screen = new PlayRuleEditScreen(ruleIndex,playRuleSheet,this.screenInstance);
        //                MinecraftClient.getInstance().setScreen(screen);
        //            })
        //            .build());
        //    }
//
        //}


        //screenInstance = YetAnotherConfigLib.create(INSTANCE, (defaults, config, builder) -> builder
        //                .title(Text.of("Province Of Music"))
        //                .category(ConfigCategory.createBuilder()
        //                        .name(Text.of("Tracks"))
        //                        .option(ButtonOption.createBuilder()
        //                                .name(Text.of("Cancel"))
        //                                .text(Text.of(""))
        //                                .action((yaclScreen, buttonOption) -> {
        //                                    PlayRuleEditScreen screen = new PlayRuleEditScreen(ruleIndex,playRuleSheet,this.screenInstance);
        //                                    MinecraftClient.getInstance().setScreen(screen);
        //                                })
        //                                .build()
        //                        )
        //                        .option(ButtonOption.createBuilder()
        //                                .name(Text.of("♫ "+ "No Track"))
        //                                .text(Text.of("SELECT"))
        //                                .action((yaclScreen, buttonOption) -> {
        //                                    playRuleSheet.rules.get(ruleIndex).trackName = "";
        //                                    PlayRuleEditScreen screen = new PlayRuleEditScreen(ruleIndex,playRuleSheet,this.screenInstance);
        //                                    MinecraftClient.getInstance().setScreen(screen);
        //                                })
        //                                .build()
        //                        )
        //                        .options(tracks)
        //                        .build()
//
//
        //                )
        //).generateScreen(getConfig().createGui());
        return screenInstance;
    }
}
