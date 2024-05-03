package com.provinceofmusic.ui;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;

import java.util.function.Supplier;

public class BooleanButtonWidget extends ButtonWidget {
    //boolean defaultPosition = false;

    public boolean state;
    String prefix = "";

    public BooleanButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress, boolean defaultPosition) {super(x, y, width, height, message, onPress, new ButtonWidget.NarrationSupplier() {
            @Override
            public MutableText createNarrationMessage(Supplier<MutableText> textSupplier) {
                return MutableText.of(TextContent.EMPTY);
            }});
        state = defaultPosition;
        prefix = message.getString();
        setMessage(Text.literal(prefix + " " + booleanRepresentation(state)));
    }


    //public BooleanButtonWidget build2() {
    //    BooleanButtonWidget buttonWidget = new BooleanButtonWidget(this.getX(), this.getY(), this.width, this.height, this.getMessage(), this.onPress, this.narrationSupplier, defaultPosition);
    //    buttonWidget.setTooltip(this.getTooltip());
    //    return buttonWidget;
    //}
//
//
    //public static BooleanButtonWidget.Builder builder2(Text message, PressAction onPress) {
    //    return new BooleanButtonWidget.Builder(message, onPress);
    //}




    @Override
    public void onPress() {

        if(state){
            state = false;
            setMessage(Text.literal(prefix + " " + booleanRepresentation(state)));
        }
        else{
            state = true;
            setMessage(Text.literal(prefix + " " + booleanRepresentation(state)));
        }

        super.onPress();
    }

    public void setState(boolean stateIn){
        state = stateIn;
        setMessage(Text.literal(prefix + " " + booleanRepresentation(state)));
    }

    String booleanRepresentation(boolean in){
        if(in){
            return "☑";
        }
        else{
            return "☐";
        }
    }
}
