package com.provinceofmusic;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
//import net.minecraft.text.LiteralText;

public class CustomScreen extends Screen {
    private final int textureWidth = 128; // Replace with your image's width
    private final int textureHeight = 128; // Replace with your image's height
    private final int x = 100; // X-coordinate to render the image
    private final int y = 100; // Y-coordinate to render the image

    public CustomScreen(Text title) {
        super(title);
    }

    //protected CustomScreen() {
        //super(new LiteralText("Custom Screen Title"));
    //}

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);

        // Use the Minecraft client to access resources
        //ResourceLocation resourceLocation = new ResourceLocation("my_mod", "textures/gui/my_image.png");
        //MinecraftClient.getInstance().getTextureManager().bindTexture(new Identifier("wynncraftmusicmod", "textures/testimg.jpg"));

        super.render(matrices, mouseX, mouseY, delta);
        drawCenteredTextWithShadow(matrices, textRenderer, Text.literal("You must see me"), width / 2, height / 2, 0xffffff);
        ///MinecraftClient.getInstance().getResourceManager().getResource()

        // Draw the image on the screen
        DrawableHelper.drawTexture(matrices, x, y, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
    }
}
