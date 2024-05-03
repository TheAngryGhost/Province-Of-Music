package com.provinceofmusic.ui;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class IntegerInputWidget extends TextFieldWidget {
    public IntegerInputWidget(TextRenderer textRenderer, int x, int y, int width, int height, Text text) {
        super(textRenderer, x, y, width, height, text);
    }

    @Override
    public void write(String text) {
        //System.out.println("Text field modified: " + text);
        //System.out.println("Text field " + getText());
        String[] chars = new String[]{"0","1","2","3","4","5","6","7","8","9"};
        boolean isInt = true;
        for(int i = 0; i < text.length(); i++){
            boolean isOther = true;
            for(int j = 0; j < chars.length; j++){
                if(text.charAt(i) == chars[j].charAt(0)){
                    isOther = false;
                }
            }
            if(isOther){
                isInt = false;
                break;
            }
        }

        if(getText().length() == 0){
            //if(text.length() > 0){
                if(text.charAt(0) == '-'){
                    super.write(text);
                }
            //}
        }




        //if(text.contains())
        if(isInt){
            super.write(text);
        }
        else{
            super.write("");
        }
    }

    public void forceWrite(String text){
        if(Integer.valueOf(text) != Integer.MIN_VALUE){
            super.write(text);
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

    public int getInt(){
        if(getText() == null || getText().equals("")){
            return Integer.MIN_VALUE;
        }
        return Integer.valueOf(getText());
    }
}
