package com.provinceofmusic.ui;

import io.github.cottonmc.cotton.gui.widget.WTextField;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import net.minecraft.client.input.CharInput;

public class POMIntegerInputWidget extends WTextField {

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


    boolean checkValid(){
        return getText().matches("^-?\\d+$");
    }



    public int getInt(){
        try {
            return Integer.parseInt(getText());
        } catch (NumberFormatException e) {
            setText("0");
            return 0;
        }
    }

}
