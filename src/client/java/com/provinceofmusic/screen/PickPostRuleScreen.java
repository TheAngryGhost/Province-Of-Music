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

public class PickPostRuleScreen {

    //public static final ConfigInstance<ConfigScreen> INSTANCE = new GsonConfigInstance<>(ConfigScreen.class, FabricLoader.getInstance().getConfigDir().resolve(""));

    Screen screenInstance;
    //PlayRuleSheet playRuleSheet;

    //ArrayList<ButtonOption> rules = new ArrayList<>();

    public Screen createGui(int ruleIndex, PlayRuleSheet playRuleSheet) {

        //for(int i = 0; i < playRuleSheet.rules.size(); i++){
        //    int finalI = i;
        //        rules.add(ButtonOption.createBuilder()
        //                .name(Text.of(playRuleSheet.rules.get(i).ruleName))
        //                .text(Text.of("SELECT"))
        //                .action((yaclScreen, buttonOption) -> {
        //                    playRuleSheet.rules.get(ruleIndex).postPlayRuleName = playRuleSheet.rules.get(finalI).ruleName;
        //                    PlayRuleEditScreen screen = new PlayRuleEditScreen(ruleIndex,playRuleSheet,this.screenInstance);
        //                    MinecraftClient.getInstance().setScreen(screen);
        //                })
        //                .build());
        //}


        //screenInstance = YetAnotherConfigLib.create(INSTANCE, (defaults, config, builder) -> builder
        //                .title(Text.of("Province Of Music"))
        //                .category(ConfigCategory.createBuilder()
        //                        .name(Text.of("Rules"))
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
        //                                .name(Text.of("No Rule"))
        //                                .text(Text.of("SELECT"))
        //                                .action((yaclScreen, buttonOption) -> {
        //                                    playRuleSheet.rules.get(ruleIndex).postPlayRuleName = "";
        //                                    PlayRuleEditScreen screen = new PlayRuleEditScreen(ruleIndex,playRuleSheet,this.screenInstance);
        //                                    MinecraftClient.getInstance().setScreen(screen);
        //                                })
        //                                .build()
        //                        )
        //                        .options(rules)
        //                        .build()
//
//
        //                )
        //).generateScreen(getConfig().createGui());
        return screenInstance;
    }
}
