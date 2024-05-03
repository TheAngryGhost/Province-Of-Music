package com.provinceofmusic.jukebox;

import com.provinceofmusic.ProvinceOfMusicClient;
import net.minecraft.client.MinecraftClient;

import java.awt.geom.Rectangle2D;
import java.io.IOException;

public class RuleHandler {
    public static double PlayerPositionX = Double.MIN_VALUE;
    public static double PlayerPositionY = Double.MIN_VALUE;
    public static double PlayerPositionZ = Double.MIN_VALUE;

    public static boolean threadEnabled;

    public static PlayRuleSheet playRuleSheet;

    static POMSoundPlayer mst;
    static String currentSong = "";

    //have a spot here for cached messages that clear after each thread iteration

    public static Thread checkThread = new Thread() {

        public void run() {

            try {
                playRuleSheet = PlayRuleSheet.getSheetFromName(ProvinceOfMusicClient.configSettings.defaultPlayRuleSheet);
                //System.out.println("GotSheet");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            mst = new POMSoundPlayer();

            while(threadEnabled){
                assert MinecraftClient.getInstance().player != null;
                PlayerPositionX = MinecraftClient.getInstance().player.getX();
                PlayerPositionY = MinecraftClient.getInstance().player.getY();
                PlayerPositionZ = MinecraftClient.getInstance().player.getZ();
                for(int i = 0; i < playRuleSheet.rules.size(); i++){
                    PlayRule rule = playRuleSheet.rules.get(i);
                    if(rule.X1 != Float.MIN_VALUE){
                        //assume that this uses the location params
                        if(insideRect(rule.X1, rule.X2, rule.Z1, rule.Z2, PlayerPositionX, PlayerPositionZ)){
                            if(rule.trackName != null){
                                System.out.println("rule correct");
                                if(currentSong!=rule.trackName) {
                                    currentSong = rule.trackName;
                                    for (int j = 0; j < playRuleSheet.tracksNames.size(); j++) {
                                        if (playRuleSheet.tracksNames.get(j).equals(rule.trackName)) {
                                            System.out.println("attempted play");
                                            MusicSound ms = new MusicSound(playRuleSheet.tracks.get(j).getPath());
                                            mst.SwitchSong(ms);
                                        }
                                    }
                                }
                            }
                        }
                        else{
                            System.out.println("rule failed");
                        }
                    }
                }
                //System.out.println("RunningRuleHandlerThread");
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("attempted stop");
            //mst.stop();
        }
    };

    public static void StartThread(){
        threadEnabled = true;
        //checkThread.stop();
        checkThread.start();
    }

    public static void StopThread(){
        threadEnabled = false;
    }

    //Chat Gpt wrote this function
    public static boolean insideRect(double X1, double X2, double Y1, double Y2, double PX, double PY) {
        // Check if PX is within the X range of the rectangle
        boolean withinXRange = (PX >= Math.min(X1, X2)) && (PX <= Math.max(X1, X2));

        // Check if PY is within the Y range of the rectangle
        boolean withinYRange = (PY >= Math.min(Y1, Y2)) && (PY <= Math.max(Y1, Y2));

        // If PX and PY are within the X and Y ranges, then the point is inside the rectangle
        return withinXRange && withinYRange;
    }

    //Chat Gpt wrote this function
    public static boolean insideCube(double X1, double X2, double Y1, double Y2, double Z1, double Z2, double PX, double PY, double PZ) {
        // Check if PX is within the X range of the cube
        boolean withinXRange = (PX >= Math.min(X1, X2)) && (PX <= Math.max(X1, X2));

        // Check if PY is within the Y range of the cube
        boolean withinYRange = (PY >= Math.min(Y1, Y2)) && (PY <= Math.max(Y1, Y2));

        // Check if PZ is within the Z range of the cube
        boolean withinZRange = (PZ >= Math.min(Z1, Z2)) && (PZ <= Math.max(Z1, Z2));

        // If PX, PY, and PZ are within their respective ranges, then the point is inside the cube
        return withinXRange && withinYRange && withinZRange;
    }
}
