package com.provinceofmusic.screen;

import com.provinceofmusic.recorder.ConvertToMidi;
import com.provinceofmusic.recorder.ReplayMusic;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MidiEditScreen extends Screen {

    Screen doneScreenInstance;
    File fileInstance;
    protected MidiEditScreen(Text title, Screen doneScreenInstance, File fileInstance) {
        super(title);
        this.doneScreenInstance = doneScreenInstance;
        this.fileInstance = fileInstance;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);

        // Use the Minecraft client to access resources
        //ResourceLocation resourceLocation = new ResourceLocation("my_mod", "textures/gui/my_image.png");
        //MinecraftClient.getInstance().getTextureManager().bindTexture(new Identifier("wynncraftmusicmod", "textures/testimg.jpg"));

        super.render(matrices, mouseX, mouseY, delta);
        //drawCenteredTextWithShadow(matrices, textRenderer, Text.literal("You must see me"), width / 2, height / 2, 0xffffff);
        //MinecraftClient.getInstance().getResourceManager().getResource()

        // Draw the image on the screen
        //DrawableHelper.drawTexture(matrices, x, y, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);

        //DrawableHelper.
        //File f = new File("recorded_music/"

        //File f = new File("recorded_music/");
        //System.out.println(f.getAbsolutePath());
        //int fileCount = f.listFiles().length;
        //for(int i = 0; i < fileCount; i++){
        //    //System.out.println(f.listFiles()[i].getName());
        //    drawTextWithShadow(matrices, textRenderer, Text.literal(f.listFiles()[i].getName()), 10, (i * 20) + 10, 0xffffff);
        //}
//
        drawTextWithShadow(matrices, textRenderer, Text.literal(fileInstance.getName()), 10, (0 * 20) + 10, 0xffffff);
    }

    @Override
    protected void init() {
        //ButtonWidget midibutton = ButtonWidget.builder(Text.literal("MidiExporter"), button -> {
        //            MinecraftClient.getInstance().setScreen(POMConfigScreen.getScreen());
        //        })        .dimensions(width / 2 - 205, 50, 200, 20)
        //        .tooltip(Tooltip.of(Text.literal("Tooltip of button1")))
        //        .build();
//
        //ButtonWidget configbutton = ButtonWidget.builder(Text.literal("OpenConfig"), button -> {
        //            MinecraftClient.getInstance().setScreen(POMConfigScreen.getScreen());
        //        })        .dimensions(width / 2 - 205, 20, 200, 20)
        //        .tooltip(Tooltip.of(Text.literal("Tooltip of button1")))
        //        .build();
        //Text.literal("**bold**↑")
        //Widget w = new TexturedButtonWidget()
        //Widget w = new EmptyWidget(0, 0, 0, 0);
        //Widget w = new
        //Widget w = new LayoutWidget() {
        //    @Override
        //    public void forEachElement(Consumer<Widget> consumer) {
//
        //    }
//
        //    @Override
        //    public void setX(int x) {
//
        //    }
//
        //    @Override
        //    public void setY(int y) {
//
        //    }
//
        //    @Override
        //    public int getX() {
        //        return 0;
        //    }
//
        //    @Override
        //    public int getY() {
        //        return 0;
        //    }
//
        //    @Override
        //    public int getWidth() {
        //        return 0;
        //    }
//
        //    @Override
        //    public int getHeight() {
        //        return 0;
        //    }
        //};

        ButtonWidget exportbutton = ButtonWidget.builder(Text.literal("↑").styled(style -> style.withUnderline(true)),button -> {
                        //MinecraftClient.getInstance().setScreen(POMConfigScreen.getScreen());
                    //fileInstance.getName().substring(0, fileInstance.getName().length() - 4)
                    ConvertToMidi.convert(fileInstance, "exported-music/" + fileInstance.getName().substring(0, fileInstance.getName().length() - 4));
                    })        .dimensions(width / 2 - 205, 50, 20, 20)
                    .tooltip(Tooltip.of(Text.literal("Export As Midi")))
                    .build();

        ButtonWidget deletebutton = ButtonWidget.builder(Text.literal("X"),button -> {
                    //MinecraftClient.getInstance().setScreen(POMConfigScreen.getScreen());
                })        .dimensions((width / 2 - 205) + 20, 50, 20, 20)
                .tooltip(Tooltip.of(Text.literal("Delete")))
                .build();

        ButtonWidget replaybutton = ButtonWidget.builder(Text.literal("⟳").styled(style -> style.withBold(true)),button -> {
                    ReplayMusic.PlayMusic(fileInstance);
                    //FindFiles();
                })        .dimensions((width / 2 - 205) + 40, 50, 20, 20)
                .tooltip(Tooltip.of(Text.literal("Replay")))
                .build();

        ButtonWidget openFolderButton = ButtonWidget.builder(Text.literal("Open Folder"),button -> {
                    try {
                        openFolderInExplorer();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })        .dimensions((width - 10) - 80, 10, 80, 20)
                //.tooltip(Tooltip.of(Text.literal("Open Folder")))
                .build();


        ButtonWidget doneButton = ButtonWidget.builder(Text.literal("Done"), button -> {
                    //MinecraftClient.getInstance().setScreen(new TitleScreen());
                    //MinecraftClient.getInstance().setScreen(new OptionsScreen(new GameMenuScreen()));
                    //MinecraftClient.
                    //MinecraftClient.getInstance().setScreen(new POMMenuScreen(Text.of("")));
                    if(doneScreenInstance!= null){
                        MinecraftClient.getInstance().setScreen(doneScreenInstance);
                    }
                    //this.close();
                    //ModMenuApi.createModsScreen()
                })        .dimensions(width / 2 - 205, height - 50, 200, 20)
                .tooltip(Tooltip.of(Text.literal("Tooltip of button1")))
                .build();


        //ScrollPanelWidget scrollableWidget;
//
        //scrollableWidget = new ScrollableWidget(width, height, 32, height - 40, Text.of("Button List"));
        //CustomScrollableWidget scrollableWidget = new CustomScrollableWidget(MinecraftClient.getInstance(), 100, 100, 100, 50, 30);

        addDrawableChild(exportbutton);
        addDrawableChild(deletebutton);
        addDrawableChild(replaybutton);
        addDrawableChild(openFolderButton);
        addDrawableChild(doneButton);
        //addDrawableChild(scrollableWidget);
        //addDrawableChild(new MusicEntryWidget(100, 100, 100, 100, "idk"));
        //addDrawableChild(configbutton);
    }

    public void openFolderInExplorer() throws IOException {
        File f = new File("recorded-music/");
        if (!f.exists()){
            f.mkdirs();
        }
        System.out.println(f.getAbsolutePath());
        Desktop.getDesktop().open(f.getAbsoluteFile());
    }

    public void FindFiles(){
        File f = new File("recorded-music/");
        if (!f.exists()){
            f.mkdirs();
        }
        System.out.println(f.getAbsolutePath());
        int fileCount = f.listFiles().length;
        for(int i = 0; i < fileCount; i++){
            System.out.println(f.listFiles()[i].getName());
        }
    }
}
