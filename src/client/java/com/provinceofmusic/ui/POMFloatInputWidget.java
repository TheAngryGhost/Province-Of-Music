package com.provinceofmusic.ui;

import io.github.cottonmc.cotton.gui.widget.WTextField;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import net.minecraft.client.input.CharInput;

public class POMFloatInputWidget extends WTextField {

    String prev;
    @Override
    public InputResult onCharTyped(CharInput input) {
        prev = getText();
        super.onCharTyped(input);
        if(!checkValid()){
            setText(prev);
        }
        return InputResult.PROCESSED;
    }

    boolean checkValid() {
        //0X?
        return getText().matches("^-?\\d+(\\.\\d+)?$");
    }

    
    public float getFloat(){
        try {
            return Float.parseFloat(getText());
        } catch (NumberFormatException e) {
            setText("0");
            return 0f;
        }
    }





}
