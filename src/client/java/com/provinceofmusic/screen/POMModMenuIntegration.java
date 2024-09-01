package com.provinceofmusic.screen;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.minecraft.client.MinecraftClient;

//import static com.provinceofmusic.ProvinceOfMusicClient.getConfig;

public class POMModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            // Return the screen here with the one you created from Cloth Config Builder
            //POMMenuScreen pomMenuScreen = new POMMenuScreen(Text.of(""));
            //return pomMenuScreen;

            //getConfig().createGui(new CustomScreen(Text.of(""));

            //TesterScreens ts = new TesterScreens();
            //ts.createGui(new CustomScreen(Text.of("")));
            //return getConfig().createGui();
            return new CottonClientScreen(new ConfigScreen());
        };
    }
}
