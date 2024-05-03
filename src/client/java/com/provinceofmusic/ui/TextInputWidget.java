package com.provinceofmusic.ui;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class TextInputWidget extends TextFieldWidget {

    public TextInputWidget(TextRenderer textRenderer, int x, int y, int width, int height, Text text) {
        super(textRenderer, x, y, width, height, text);
    }

    //String textcurrent = "";
    //@Override
    //public void setText(String text) {
    //    super.setText(text);
////
    //    // Your code here
    //    System.out.println("Text field modified: " + text);
    //}
//
    //@Override
    //public void setMessage(Text message) {
    //    super.setMessage(message);
////
    //    System.out.println("Text field modified: " + message);
    //}
//
    //@Override
    //public Text getMessage() {
    //    return super.getMessage();
    //}
//
    @Override
    public void write(String text) {
        //System.out.println("Text field modified: " + text);
        //System.out.println("Text field " + getText());
        if(text != null){
            if(text.length() != 0){
                super.write(text);
            }
        }
    }
    //
    @Override
    public void eraseCharacters(int characterOffset) {
        //if(textcurrent.length() > 0){
        //    textcurrent = textcurrent.substring(0, textcurrent.length() - 1);
        //}
//
        //System.out.println("Text field " + getText());
        //System.out.println("Text field characterOffset: " + characterOffset);
        if (getText().length() > characterOffset){
            super.eraseCharacters(characterOffset);
        }
        else{
            super.eraseCharacters(0);
        }
    }
}
