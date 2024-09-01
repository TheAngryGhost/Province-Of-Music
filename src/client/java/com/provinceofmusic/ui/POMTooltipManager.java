package com.provinceofmusic.ui;

import com.provinceofmusic.ProvinceOfMusicClient;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class POMTooltipManager extends WPlainPanel {

    //WButton button;
    WText tooltipText;

    WBox boundingBox;

    //private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    //@Override
    //public void propertyChange(PropertyChangeEvent evt) {
    //    button.isHovered()
    //}

    ArrayList<WWidget> widgets = new ArrayList<>();
    ArrayList<Text> tooltipStrings = new ArrayList<>();
    boolean isOn = false;

    public POMTooltipManager() {
        //this.button = original;

        boundingBox = new WBox(Axis.HORIZONTAL);
        //add(boundingBox, 0,0, 100, 50);
        //Alpha, Red, Green, Blue
        boundingBox.setBackgroundPainter(BackgroundPainter.createColorful(0xDD000000));
        tooltipText = new WText(Text.literal(""), 0xFFFFFF);
        //add(tooltipText, 0,0, 100, 50);
        Thread thread = new Thread(() -> {
            while(true){
                //if(button.isHovered()){
                //tooltipText.setText(Text.literal(""));
                //this.add(boundingBox, 0,0, 100, 50);
                boolean found = false;
                for(int i = 0; i < widgets.size(); i++){
                    if(widgets.get(i).isHovered()){
                        found = true;
                        tooltipText.setText(tooltipStrings.get(i));
                        break;
                    }
                }

                if(found && !isOn) {
                    isOn = true;
                    add(boundingBox, 0,0, 100, 50);
                    add(tooltipText, 0,0, 100, 50);
                }
                else if(!found && isOn){
                    isOn = false;
                    remove(boundingBox);
                    remove(tooltipText);
                }



                if(ProvinceOfMusicClient.guiSize == 2){
                    int xlocation = (((int) MinecraftClient.getInstance().mouse.getX() - 420) / ProvinceOfMusicClient.guiSize) + 60;
                    int ylocation = (((int) MinecraftClient.getInstance().mouse.getY() - 120) / ProvinceOfMusicClient.guiSize) + 50;
                    tooltipText.setLocation(xlocation + 5, ylocation + 5);
                    boundingBox.setLocation(xlocation, ylocation);
                }
                else{
                    int xlocation = ((int) MinecraftClient.getInstance().mouse.getX() - 420) / ProvinceOfMusicClient.guiSize;
                    int ylocation = ((int) MinecraftClient.getInstance().mouse.getY() - 120) / ProvinceOfMusicClient.guiSize;
                    tooltipText.setLocation(xlocation + 5, ylocation + 5);
                    boundingBox.setLocation(xlocation, ylocation);
                }







                //}
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        });
        thread.start();
        //this.addPropertyChangeListener(evt -> {
        //    if ("hovered".equals(evt.getPropertyName())) {
        //        pcs.firePropertyChange("hovered", evt.getOldValue(), evt.getNewValue());
        //        System.out.println("CHANGED2");
        //    }
        //    System.out.println("CHANGED");
        //});
    }

    public void addTooltip(WWidget widget, Text text){
        widgets.add(widget);
        tooltipStrings.add(text);
    }

   //public void addPropertyChangeListener(PropertyChangeListener listener) {
   //    pcs.addPropertyChangeListener(listener);
   //}

   //public boolean getValue() {
   //    System.out.println("CHANGED3");
   //    return button.isHovered();
   //}
}
