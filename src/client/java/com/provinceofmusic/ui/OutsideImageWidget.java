package com.provinceofmusic.ui;

import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.Identifier;

public class OutsideImageWidget extends WWidget {
    private static final Identifier TEXTURE = Identifier.of("mymod", "textures/gui/my_widget.png");
    // ...
    @Environment(EnvType.CLIENT)
    @Override
    public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
        //Sprite sprite = new Sprite()
        //context.drawSprite();
        //context.

        //TitleScreen


        // If you want the whole texture
        //ScreenDrawing.texturedRect(context, x, y, width, height, TEXTURE, 0xFFFFFFFF);

        // or for partial textures:
        //ScreenDrawing.texturedRect(context, x, y, width, height, TEXTURE, u1, v1, u2, v2, 0xFFFFFFFF);
        // u1, v1, u2, v2 are fractions of the texture dimensions, so for example for the top half (u: 0-1, v: 0-0.5):
        //ScreenDrawing.texturedRect(context, x, y, width, height, TEXTURE, 0f, 0f, 1f, 0.5f, 0xFFFFFFFF);

        // the 0xFFFFFFFF is the color in ARGB format, which in this case is white
    }
}
