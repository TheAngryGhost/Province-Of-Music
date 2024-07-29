package com.provinceofmusic.screen;

import com.provinceofmusic.ProvinceOfMusicClient;
import com.provinceofmusic.jukebox.PlayRule;
import com.provinceofmusic.jukebox.PlayRuleSheet;
import com.provinceofmusic.ui.BooleanButtonWidget;
import com.provinceofmusic.ui.FloatInputWidget;
import com.provinceofmusic.ui.IntegerInputWidget;
import com.provinceofmusic.ui.TextInputWidget;
import com.provinceofmusic.POMUtils;
import net.fabricmc.fabric.impl.client.itemgroup.FabricCreativeGuiComponents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

import java.awt.*;
import java.io.IOException;

public class PlayRuleEditScreen extends Screen {

    Screen doneScreenInstance;
    //PlayRule fileInstance;

    PlayRuleSheet sheet;
    int ruleIndex;

    Screen previousScreen;

    //DrawContext context;
    //protected MidiEditScreen(Text title, Screen doneScreenInstance, File fileInstance) {
    //    super(title);
    //    this.doneScreenInstance = doneScreenInstance;
    //    this.fileInstance = fileInstance;
    //}

    protected PlayRuleEditScreen(int ruleIndex, PlayRuleSheet sheet, Screen previousScreen) {
        super(Text.of(""));
        //this.doneScreenInstance = doneScreenInstance;
        this.ruleIndex = ruleIndex;
        this.sheet = sheet;
        this.previousScreen = previousScreen;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        //this.renderBackground(matrices);
        super.render(context, mouseX, mouseY, delta);

        // Use the Minecraft client to access resources
        //ResourceLocation resourceLocation = new ResourceLocation("my_mod", "textures/gui/my_image.png");
        //MinecraftClient.getInstance().getTextureManager().bindTexture(new Identifier("wynncraftmusicmod", "textures/testimg.jpg"));

        //super.render(matrices, mouseX, mouseY, delta);
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
        //this.context = context;
        //context.drawTextWithShadow(textRenderer, Text.literal(fileInstance.getName()), 10, (0 * 20) + 10, 0xffffff);
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
        //context.drawTextWithShadow(textRenderer, Text.literal(fileInstance.getName()), 10, (0 * 20) + 10, 0xffffff);
        TextWidget ruleNameLabel = new TextWidget(10, 10, 56, 20, Text.literal("Rule Name:"), textRenderer);
        TextFieldWidget ruleName = new TextFieldWidget(textRenderer, 10 + 60, 10, 200, 20, Text.literal(sheet.rules.get(ruleIndex).ruleName));
        TextWidget trackNameLabel = new TextWidget(10, 30 + 5 - 2, 60, 20, Text.literal("Track Name:"), textRenderer);
        TextWidget trackNameText = new TextWidget(80, 30 + 5 - 2, 200, 20, Text.literal("RULE ! TEST EXISTS HERE TEST WIDTH"), textRenderer);

        ButtonWidget changeTrackButton = ButtonWidget.builder(Text.literal("⟳").styled(style -> style.withBold(true)),button -> {
                    PickTrackScreen screen = new PickTrackScreen();
                    MinecraftClient.getInstance().setScreen(screen.createGui(ruleIndex, sheet));
                })        .dimensions(280, 30, 20, 20)
                .tooltip(Tooltip.of(Text.literal("Change the track for another one from your tracks folder in this sheet")))
                .build();

        TextWidget X1Label = new TextWidget(10, 50 + 5, 20, 20, Text.literal("X1: "), textRenderer);
        FloatInputWidget X1 = new FloatInputWidget(textRenderer,30, 50 + 5, 100, 20, Text.literal("Position: "));

        TextWidget Y1Label = new TextWidget(140, 50 + 5, 20, 20, Text.literal("Y1: "), textRenderer);
        FloatInputWidget Y1 = new FloatInputWidget(textRenderer,160, 50 + 5, 100, 20, Text.literal("Position: "));

        TextWidget Z1Label = new TextWidget(140 + 130, 50 + 5, 20, 20, Text.literal("Z1: "), textRenderer);
        FloatInputWidget Z1 = new FloatInputWidget(textRenderer,160 + 130, 50 + 5, 100, 20, Text.literal("Position: "));



        TextWidget X2Label = new TextWidget(10, 50 + 30 + 5, 20, 20, Text.literal("X2: "), textRenderer);
        FloatInputWidget X2 = new FloatInputWidget(textRenderer,30, 50 + 30 + 5, 100, 20, Text.literal("Position: "));

        TextWidget Y2Label = new TextWidget(140, 50 + 30 + 5, 20, 20, Text.literal("Y2: "), textRenderer);
        FloatInputWidget Y2 = new FloatInputWidget(textRenderer,160, 50 + 30 + 5, 100, 20, Text.literal("Position: "));

        TextWidget Z2Label = new TextWidget(140 + 130, 50 + 30 + 5, 20, 20, Text.literal("Z2: "), textRenderer);
        FloatInputWidget Z2 = new FloatInputWidget(textRenderer,160 + 130, 50 + 30 + 5, 100, 20, Text.literal("Position: "));







        BooleanButtonWidget fastSwitchButton = new BooleanButtonWidget(10, 110 + 5, 90, 20, Text.literal("Fast Switch: "), button -> {
            button.setTooltip(Tooltip.of(Text.literal("If true this will make it so there is no transition between the previous track and this one")));
        }, false);

        BooleanButtonWidget refreshSeekButton = new BooleanButtonWidget(110, 110 + 5, 100, 20, Text.literal("Refresh Seek: "), button -> {
            button.setTooltip(Tooltip.of(Text.literal("When searching through the rules to see what should be played when this rule is on any previous rules played before this one regardless of priority will be re evaluated if they should be played at this time. Usually meanings ones based of location override ones based off of messages received ")));
        }, false);

        BooleanButtonWidget loopButton = new BooleanButtonWidget(110 + 90 + 10 + 10, 110 + 5, 60, 20, Text.literal("Loop: "), button -> {
            button.setTooltip(Tooltip.of(Text.literal("")));
        }, true);

        TextWidget postRuleNameLabel = new TextWidget((110 + 90 + 10 + 10) + 60, 110 + 5, 60, 20, Text.literal("Post Rule:"), textRenderer);
        TextWidget postRuleNameText = new TextWidget((110 + 90 + 10 + 10) + 60 + 70, 110 + 5, 130, 20, Text.literal("RULE ! TEST EXISTS HERE"), textRenderer);

        ButtonWidget changePostRuleButton = ButtonWidget.builder(Text.literal("⟳").styled(style -> style.withBold(true)),button -> {
                    PickPostRuleScreen screen = new PickPostRuleScreen();
                    MinecraftClient.getInstance().setScreen(screen.createGui(ruleIndex, sheet));
                })        .dimensions((110 + 90 + 10 + 10) + 60 + 130 + 80, 110 + 5, 20, 20)
                .tooltip(Tooltip.of(Text.literal("Change the post rule for another one from your list of rules in this sheet")))
                .build();

        TextWidget MinuteLabel1 = new TextWidget(10, 110 + 30 + 5, 80, 20, Text.literal("Start at Minute: "), textRenderer);
        IntegerInputWidget Minute1 = new IntegerInputWidget(textRenderer,30 + 60, 110 + 30 + 5, 60, 20, Text.literal("Position: "));

        TextWidget SecondLabel1 = new TextWidget(140 + 8, 110 + 30 + 5, 100, 20, Text.literal("Start at Second: "), textRenderer);
        IntegerInputWidget Second1 = new IntegerInputWidget(textRenderer,160 + 60 + 20, 110 + 30 + 5, 60, 20, Text.literal("Position: "));

        TextWidget MillisecondLabel1 = new TextWidget(140 + 130 + 40 - 4, 110 + 30 + 5, 105, 20, Text.literal("Start at Millisecond: "), textRenderer);
        IntegerInputWidget Millisecond1 = new IntegerInputWidget(textRenderer,160 + 130 + 60 + 40 + 20 - 4 + 5, 110 + 30 + 5, 60, 20, Text.literal("Position: "));

        TextWidget MinuteLabel2 = new TextWidget(10, 110 + 30 + 5 + 30, 80, 20, Text.literal("Loop at Minute: "), textRenderer);
        IntegerInputWidget Minute2 = new IntegerInputWidget(textRenderer,30 + 60, 110 + 30 + 5 + 30, 60, 20, Text.literal("Position: "));

        TextWidget SecondLabel2 = new TextWidget(140 + 8, 110 + 30 + 5 + 30, 100, 20, Text.literal("Loop at Second: "), textRenderer);
        IntegerInputWidget Second2 = new IntegerInputWidget(textRenderer,160 + 60 + 20, 110 + 30 + 5 + 30, 60, 20, Text.literal("Position: "));

        TextWidget MillisecondLabel2 = new TextWidget(140 + 130 + 40 - 4, 110 + 30 + 5 + 30, 105, 20, Text.literal("Loop at Millisecond: "), textRenderer);
        IntegerInputWidget Millisecond2 = new IntegerInputWidget(textRenderer,160 + 130 + 60 + 40 + 20 - 4 + 5, 110 + 30 + 5 + 30, 60, 20, Text.literal("Position: "));

        TextWidget messageReceivedLabel = new TextWidget(10, 175 + 30, 105, 20, Text.literal("Message Received:"), textRenderer);
        TextInputWidget messageReceived = new TextInputWidget(textRenderer, 10 + 90 + 10 + 5, 175 + 30, 200, 20, Text.literal(""));

        TextWidget armourEquippedLabel = new TextWidget(10, 175 + 30 + 30, 105, 20, Text.literal("Armour Equipped:"), textRenderer);
        TextInputWidget armourEquipped = new TextInputWidget(textRenderer, 10 + 90 + 10 + 5, 175 + 30 + 30, 200, 20, Text.literal(""));

        TextWidget itemButtonPressedLabel = new TextWidget(10, 175 + 30 + 30 + 30, 105, 20, Text.literal("Item Button Pressed:"), textRenderer);
        TextInputWidget itemButtonPressed = new TextInputWidget(textRenderer, 10 + 90 + 10 + 10, 175 + 30 + 30 + 30, 200, 20, Text.literal(""));

        TextWidget priorityLabel = new TextWidget(10 + 350, 10, 65, 20, Text.literal("Priority: "), textRenderer);
        IntegerInputWidget priority = new IntegerInputWidget(textRenderer,70 + 350, 10, 60, 20, Text.literal("Position: "));

        ButtonWidget saveChangesButton = ButtonWidget.builder(Text.literal("Save Changes"),button -> {
                    sheet.rules.get(ruleIndex).ruleName = ruleName.getText();
                    sheet.rules.get(ruleIndex).trackName = trackNameText.getMessage().getString();
                    sheet.rules.get(ruleIndex).X1 = X1.getFloat();
                    sheet.rules.get(ruleIndex).Y1 = Y1.getFloat();
                    sheet.rules.get(ruleIndex).Z1 = Z1.getFloat();
                    sheet.rules.get(ruleIndex).X2 = X2.getFloat();
                    sheet.rules.get(ruleIndex).Y2 = Y2.getFloat();
                    sheet.rules.get(ruleIndex).Z2 = Z2.getFloat();
                    sheet.rules.get(ruleIndex).fastSwitch = fastSwitchButton.state;
                    sheet.rules.get(ruleIndex).refreshSeek = refreshSeekButton.state;
                    sheet.rules.get(ruleIndex).startMinute = Minute1.getInt();
                    sheet.rules.get(ruleIndex).startSecond = Second1.getInt();
                    sheet.rules.get(ruleIndex).startMillisecond = Millisecond1.getInt();
                    sheet.rules.get(ruleIndex).loopMinute = Minute2.getInt();
                    sheet.rules.get(ruleIndex).loopSecond = Second2.getInt();
                    sheet.rules.get(ruleIndex).loopMillisecond = Millisecond2.getInt();
                    sheet.rules.get(ruleIndex).messageReceived = messageReceived.getText();
                    sheet.rules.get(ruleIndex).armourEquipped = armourEquipped.getText();
                    sheet.rules.get(ruleIndex).itemButtonPressed = itemButtonPressed.getText();
                    sheet.rules.get(ruleIndex).priority = priority.getInt();
                    sheet.rules.get(ruleIndex).loop = loopButton.state;
                    PlayRuleSheet.writeRuleSheet(sheet.getName(), sheet);
                    try {
                        PlayRuleSheetEditScreen screen = new PlayRuleSheetEditScreen(sheet.sheet);
                        MinecraftClient.getInstance().setScreen(screen.createGui());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                })        .dimensions(10 + 350, 175 + 30 + 30, 90, 20)
                .tooltip(Tooltip.of(Text.literal("")))
                .build();

        ButtonWidget cancelButton = ButtonWidget.builder(Text.literal("Cancel"),button -> {
                    try {
                        PlayRuleSheetEditScreen screen = new PlayRuleSheetEditScreen(sheet.sheet);
                        MinecraftClient.getInstance().setScreen(screen.createGui());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })        .dimensions(10 + 350, 175 + 30 + 30 + 30, 90, 20)
                .tooltip(Tooltip.of(Text.literal("")))
                .build();

        ButtonWidget duplicateButton = ButtonWidget.builder(Text.literal("☱↷☱"),button -> {
                    PlayRule temp = null;
                    try {
                        temp = (PlayRule) sheet.rules.get(ruleIndex).clone();
                    } catch (CloneNotSupportedException e) {
                        throw new RuntimeException(e);
                    }
                    temp.ruleName = sheet.rules.get(ruleIndex).ruleName + System.currentTimeMillis();
                    sheet.rules.add(ruleIndex + 1, temp);
                    PlayRuleSheet.writeRuleSheet(sheet.getName(), sheet);
                    try {
                        PlayRuleSheetEditScreen screen = new PlayRuleSheetEditScreen(sheet.sheet);
                        MinecraftClient.getInstance().setScreen(screen.createGui());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                })        .dimensions(10 + 350 + 90 + 10, 175 + 30 + 30, 40, 20)
                .tooltip(Tooltip.of(Text.literal("Duplicate")))
                .build();

        ButtonWidget deleteButton = ButtonWidget.builder(Text.literal("X"),button -> {
                    sheet.rules.remove(ruleIndex);
                    PlayRuleSheet.writeRuleSheet(sheet.getName(), sheet);
                    try {
                        PlayRuleSheetEditScreen screen = new PlayRuleSheetEditScreen(sheet.sheet);
                        MinecraftClient.getInstance().setScreen(screen.createGui());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                })        .dimensions(10 + 350 + 90 + 10, 175 + 30 + 30 + 30, 40, 20)
                .tooltip(Tooltip.of(Text.literal("Delete")))
                .build();




        //ButtonWidget deletebutton = ButtonWidget.builder(Text.literal("X"),button -> {
        //            //MinecraftClient.getInstance().setScreen(POMConfigScreen.getScreen());
        //            ReplayMusic.StopMusic();
        //            //ProvinceOfMusicClient.deletedFiles.add(fileInstance);
        //            //fileInstance.deleteOnExit();
        //            sheet.rules.remove(ruleIndex);
        //            //if(doneScreenInstance!= null){
        //            MinecraftClient.getInstance().setScreen(new ConfigScreen().createGui());
        //            //}
//
        //        })        //.dimensions((width / 2 - 205) + 20, 50, 20, 20)
        //        .dimensions((width - 10) - 80, height - 50, 20, 20)
        //        .tooltip(Tooltip.of(Text.literal("Delete File")))
        //        .build();

        //ButtonWidget replaybutton = ButtonWidget.builder(Text.literal("⟳").styled(style -> style.withBold(true)),button -> {
        //            //ReplayMusic.PlayMusic(fileInstance.getPath());
        //            //button.setMessage(Text.literal("test"));
        //            //FindFiles();
        //        })        .dimensions((width / 2 - 205) + 40, 50, 20, 20)
        //        .tooltip(Tooltip.of(Text.literal("Replay or Stop music file")))
        //        .build();
//
        //ButtonWidget openFolderButton = ButtonWidget.builder(Text.literal("Open Folder"),button -> {
        //            try {
        //                openFolderInExplorer();
        //            } catch (IOException e) {
        //                throw new RuntimeException(e);
        //            }
        //        })        .dimensions((width - 10) - 80, 10, 80, 20)
        //        //.tooltip(Tooltip.of(Text.literal("Open Folder")))
        //        .build();
//

        //ButtonWidget doneButton = ButtonWidget.builder(Text.literal("Done"), button -> {
        //            //ReplayMusic.StopMusic();
        //            //MinecraftClient.getInstance().setScreen(new TitleScreen());
        //            //MinecraftClient.getInstance().setScreen(new OptionsScreen(new GameMenuScreen()));
        //            //MinecraftClient.
        //            //MinecraftClient.getInstance().setScreen(new POMMenuScreen(Text.of("")));
        //            //if(doneScreenInstance!= null){
        //            //ConfigScreen configScreen = new ConfigScreen();
        //            MinecraftClient.getInstance().setScreen(previousScreen);
        //            //}
        //            //this.close();
        //            //ModMenuApi.createModsScreen()
        //        })        .dimensions(width / 2 - 205, height - 50, 200, 20)
        //        //.tooltip(Tooltip.of(Text.literal("Tooltip of button1")))
        //        .build();


        //ScrollPanelWidget scrollableWidget;

        //TextFieldWidget textFieldWidgetTest = new TextFieldWidget(textRenderer, 50, 50, 200, 20, Text.literal("")){
        //    //String textcurrent = "";
        //    @Override
        //    public void setText(String text) {
        //        super.setText(text);
//
        //        // Your code here
        //        System.out.println("Text field modified: " + text);
        //    }
//
        //    @Override
        //    public void setMessage(Text message) {
        //        super.setMessage(message);
//
        //        System.out.println("Text field modified: " + message);
        //    }
//
        //    @Override
        //    public Text getMessage() {
        //        return super.getMessage();
        //    }
//
        //    @Override
        //    public void write(String text) {
        //        System.out.println("Text field modified: " + text);
        //        super.write(text);
        //    }
//
        //    @Override
        //    public void eraseCharacters(int characterOffset) {
        //        //if(textcurrent.length() > 0){
        //        //    textcurrent = textcurrent.substring(0, textcurrent.length() - 1);
        //        //}
//
        //        System.out.println("Text field " + getText());
        //        System.out.println("Text field characterOffset: " + characterOffset);
        //        super.eraseCharacters(characterOffset);
        //    }
        //};

        //scrollableWidget = new ScrollableWidget(width, height, 32, height - 40, Text.of("Button List"));
        //CustomScrollableWidget scrollableWidget = new CustomScrollableWidget(MinecraftClient.getInstance(), 100, 100, 100, 50, 30);

        addDrawableChild(ruleNameLabel);
        addDrawableChild(ruleName);

        addDrawableChild(trackNameLabel);
        addDrawableChild(trackNameText);

        addDrawableChild(changeTrackButton);

        addDrawableChild(X1);
        addDrawableChild(X1Label);

        addDrawableChild(Y1);
        addDrawableChild(Y1Label);

        addDrawableChild(Z1);
        addDrawableChild(Z1Label);

        addDrawableChild(X2);
        addDrawableChild(X2Label);

        addDrawableChild(Y2);
        addDrawableChild(Y2Label);

        addDrawableChild(Z2);
        addDrawableChild(Z2Label);

        addDrawableChild(fastSwitchButton);
        addDrawableChild(refreshSeekButton);
        addDrawableChild(loopButton);
        addDrawableChild(postRuleNameLabel);
        addDrawableChild(postRuleNameText);
        addDrawableChild(changePostRuleButton);

        addDrawableChild(Minute1);
        addDrawableChild(MinuteLabel1);
        addDrawableChild(Second1);
        addDrawableChild(SecondLabel1);
        addDrawableChild(Millisecond1);
        addDrawableChild(MillisecondLabel1);

        addDrawableChild(Minute2);
        addDrawableChild(MinuteLabel2);
        addDrawableChild(Second2);
        addDrawableChild(SecondLabel2);
        addDrawableChild(Millisecond2);
        addDrawableChild(MillisecondLabel2);

        addDrawableChild(messageReceived);
        addDrawableChild(messageReceivedLabel);

        addDrawableChild(armourEquipped);
        addDrawableChild(armourEquippedLabel);

        addDrawableChild(itemButtonPressed);
        addDrawableChild(itemButtonPressedLabel);

        addDrawableChild(priority);
        addDrawableChild(priorityLabel);

        addDrawableChild(saveChangesButton);
        addDrawableChild(duplicateButton);
        addDrawableChild(cancelButton);
        addDrawableChild(deleteButton);

        ruleName.write(sheet.rules.get(ruleIndex).ruleName);
        trackNameText.setMessage(Text.of(sheet.rules.get(ruleIndex).trackName));
        X1.forceWrite("" + sheet.rules.get(ruleIndex).X1);
        Y1.forceWrite("" + sheet.rules.get(ruleIndex).Y1);
        Z1.forceWrite("" + sheet.rules.get(ruleIndex).Z1);
        X2.forceWrite("" + sheet.rules.get(ruleIndex).X2);
        Y2.forceWrite("" + sheet.rules.get(ruleIndex).Y2);
        Z2.forceWrite("" + sheet.rules.get(ruleIndex).Z2);
        fastSwitchButton.setState(sheet.rules.get(ruleIndex).fastSwitch);
        refreshSeekButton.setState(sheet.rules.get(ruleIndex).refreshSeek);
        loopButton.setState(sheet.rules.get(ruleIndex).loop);
        postRuleNameText.setMessage(Text.of(sheet.rules.get(ruleIndex).postPlayRuleName));
        Minute1.forceWrite("" + sheet.rules.get(ruleIndex).startMinute);
        Second1.forceWrite("" + sheet.rules.get(ruleIndex).startSecond);
        Millisecond1.forceWrite("" + sheet.rules.get(ruleIndex).startMillisecond);
        Minute2.forceWrite("" + sheet.rules.get(ruleIndex).loopMinute);
        Second2.forceWrite("" + sheet.rules.get(ruleIndex).loopSecond);
        Millisecond2.forceWrite("" + sheet.rules.get(ruleIndex).loopMillisecond);
        messageReceived.write(sheet.rules.get(ruleIndex).messageReceived);
        armourEquipped.write(sheet.rules.get(ruleIndex).armourEquipped);
        itemButtonPressed.write(sheet.rules.get(ruleIndex).itemButtonPressed);
        priority.forceWrite("" + sheet.rules.get(ruleIndex).priority);






        //addDrawableChild(deletebutton);
        //addDrawableChild(replaybutton);
        //addDrawableChild(openFolderButton);
        //addDrawableChild(doneButton);

        //addDrawableChild(textFieldWidgetTest);
        //addDrawableChild(scrollableWidget);
        //addDrawableChild(new MusicEntryWidget(100, 100, 100, 100, "idk"));
        //addDrawableChild(configbutton);
    }

    public void openFolderInExplorer() throws IOException {
        //File f = new File("recorded-music/");
        //if (!f.exists()){
        //    f.mkdirs();
        //}
        System.out.println(ProvinceOfMusicClient.recordedmusicdir.getAbsolutePath());
        Desktop.getDesktop().open(ProvinceOfMusicClient.recordedmusicdir.getAbsoluteFile());
    }

    //public void FindFiles(){
    //    File f = new File("recorded-music/");
    //    if (!f.exists()){
    //        f.mkdirs();
    //    }
    //    System.out.println(f.getAbsolutePath());
    //    int fileCount = f.listFiles().length;
    //    for(int i = 0; i < fileCount; i++){
    //        System.out.println(f.listFiles()[i].getName());
    //    }
    //}
}
