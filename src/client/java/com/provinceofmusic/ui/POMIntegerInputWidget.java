package com.provinceofmusic.ui;

import com.provinceofmusic.ProvinceOfMusicClient;
import io.github.cottonmc.cotton.gui.widget.WTextField;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;

public class POMIntegerInputWidget extends WTextField {

    String prev;
    @Override
    public InputResult onCharTyped(char ch) {
        prev = getText();
        super.onCharTyped(ch);
        if(!checkValid()){
            setText(prev);
        }
        return InputResult.PROCESSED;
    }

    boolean checkValid(){
        char[] characters = getText().toCharArray();
        for (int i = 0; i < characters.length; i++){
            char ch = characters[i];
            if(i == 0){
                if(ch == '0' || ch == '1' || ch == '2' || ch == '3' || ch == '4' || ch == '5' || ch == '6' || ch == '7' || ch == '8' || ch == '9' || ch == '-'){
                    continue;
                }
            } else if (i == 1 && characters[0] == '0') {
                return false;
            } else if (i == 1 && characters[0] == '-') {
                if(ch == '1' || ch == '2' || ch == '3' || ch == '4' || ch == '5' || ch == '6' || ch == '7' || ch == '8' || ch == '9'){
                    continue;
                }
            }
            else {
                if(ch == '0' || ch == '1' || ch == '2' || ch == '3' || ch == '4' || ch == '5' || ch == '6' || ch == '7' || ch == '8' || ch == '9'){
                    continue;
                }
            }
            return false;
        }
        return true;
    }

    public int getInt(){
        if (getText().indexOf('1') == -1 && getText().indexOf('2') == -1 && getText().indexOf('3') == -1 && getText().indexOf('4') == -1 && getText().indexOf('5') == -1 && getText().indexOf('6') == -1 && getText().indexOf('7') == -1 && getText().indexOf('8') == -1 && getText().indexOf('9') == -1) {
            setText("0");
            return Integer.parseInt(getText());
        }
        else{
            return Integer.parseInt(getText());
        }
    }
}
