package com.provinceofmusic.screen;

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

import java.util.ArrayList;

import static com.provinceofmusic.ProvinceOfMusicClient.getConfig;

public class PickTrackScreen {

    public static final ConfigInstance<ConfigScreen> INSTANCE = new GsonConfigInstance<>(ConfigScreen.class, FabricLoader.getInstance().getConfigDir().resolve(""));

    Screen screenInstance;
    //PlayRuleSheet playRuleSheet;

    ArrayList<ButtonOption> tracks = new ArrayList<>();

    public Screen createGui(int ruleIndex, PlayRuleSheet playRuleSheet) {

        for(int i = 0; i < playRuleSheet.tracks.size(); i++){
            //System.out.println(playRuleSheet.tracks.get(i).getName());
            int finalI = i;
            //String name = playRuleSheet.tracks.get(i).getName();
            if(playRuleSheet.tracks.get(i).getName().indexOf(".wav") != -1){
            tracks.add(ButtonOption.createBuilder()
                    .name(Text.of("♫ "+ playRuleSheet.tracksNames.get(i)))
                    .text(Text.of("SELECT"))
                    .action((yaclScreen, buttonOption) -> {
                        playRuleSheet.rules.get(ruleIndex).trackName = playRuleSheet.tracksNames.get(finalI);
                        PlayRuleEditScreen screen = new PlayRuleEditScreen(ruleIndex,playRuleSheet,this.screenInstance);
                        MinecraftClient.getInstance().setScreen(screen);
                        //if(screenInstance != null){
                        //    MinecraftClient.getInstance().setScreen(new MidiEditScreen(Text.of(""), playrulesheetFiles.get(finalI)));
                        //}
                    })
                    .build());
            }

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
                                .name(Text.of("Tracks"))
                                .option(ButtonOption.createBuilder()
                                        //.options(unconvertedmidibuttons)
                                        .name(Text.of("Cancel"))
                                        .text(Text.of(""))
                                        //.tooltip(Text.of("This is so easy!")) // optional
                                        .action((yaclScreen, buttonOption) -> {
                                            PlayRuleEditScreen screen = new PlayRuleEditScreen(ruleIndex,playRuleSheet,this.screenInstance);
                                            MinecraftClient.getInstance().setScreen(screen);

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
                                        .name(Text.of("♫ "+ "No Track"))
                                        .text(Text.of("SELECT"))
                                        //.tooltip(Text.of("This is so easy!")) // optional
                                        .action((yaclScreen, buttonOption) -> {
                                            playRuleSheet.rules.get(ruleIndex).trackName = "";
                                            PlayRuleEditScreen screen = new PlayRuleEditScreen(ruleIndex,playRuleSheet,this.screenInstance);
                                            MinecraftClient.getInstance().setScreen(screen);
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
}
