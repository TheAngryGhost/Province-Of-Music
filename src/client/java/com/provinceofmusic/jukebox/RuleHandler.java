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

    public static Thread checkThread;
    public static Thread ruleThread;
    public static double regionSurfaceArea = 1000000000;
    public static PlayRule cachedRule = null;

    public static void StartThread(){
        threadEnabled = true;
        cachedRule = null;
        checkThread = new Thread(() -> {

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
                    PlayRule tempRule = playRuleSheet.rules.get(i);
                    //AreaCheck
                    if(tempRule.X1 != Float.MIN_VALUE){
                        //assume that this uses the location params
                        if(insideRect(tempRule.X1, tempRule.X2, tempRule.Z1, tempRule.Z2, PlayerPositionX, PlayerPositionZ)){
                            if(cachedRule != null){ //move these checks outside location checks
                                if(!cachedRule.equals(tempRule)){ //move these checks outside location checks
                                    if(cachedRule.X1 != Float.MIN_VALUE){
                                        double cachedRegionSurfaceArea = rectArea(cachedRule.X1, cachedRule.X2, cachedRule.Z1, cachedRule.Z2);
                                        if(rectArea(tempRule.X1, tempRule.X2, tempRule.Z1, tempRule.Z2) < cachedRegionSurfaceArea && insideRect(cachedRule.X1, cachedRule.X2, cachedRule.Z1, cachedRule.Z2, PlayerPositionX, PlayerPositionZ)){
                                            //rule true
                                            runRule(playRuleSheet.rules.get(i));
                                        } else if (!insideRect(cachedRule.X1, cachedRule.X2, cachedRule.Z1, cachedRule.Z2, PlayerPositionX, PlayerPositionZ)) {
                                            runRule(playRuleSheet.rules.get(i));
                                        }
                                        //System.out.println("first check " + (rectArea(tempRule.X1, tempRule.X2, tempRule.Z1, tempRule.Z2) < cachedRegionSurfaceArea));
                                        //System.out.println("second check " + insideRect(cachedRule.X1, cachedRule.X2, cachedRule.Z1, cachedRule.Z2, PlayerPositionX, PlayerPositionZ));
                                    }
                                    else {
                                        //rule true
                                        runRule(playRuleSheet.rules.get(i));
                                    }
                                }
                            }
                            else{
                                //rule true
                                runRule(playRuleSheet.rules.get(i));
                            }
                        }
                    }
                }


                //for(int i = 0; i < playRuleSheet.rules.size(); i++){
                //    PlayRule rule = playRuleSheet.rules.get(i);
                //    if(rule.X1 != Float.MIN_VALUE){
                //        //assume that this uses the location params
                //        if(insideRect(rule.X1, rule.X2, rule.Z1, rule.Z2, PlayerPositionX, PlayerPositionZ)){
                //            if(cachedRule != null){
                //                if(rectArea(rule.X1, rule.X2, rule.Z1, rule.Z2) > regionSurfaceArea && insideRect(cachedRule.X1, cachedRule.X2, cachedRule.Z1, cachedRule.Z2, PlayerPositionX, PlayerPositionZ)){
                //                    continue;
                //                }
//
                //            }
                //            //System.out.println("rule correct");
                //            regionSurfaceArea = rectArea(rule.X1, rule.X2, rule.Z1, rule.Z2);
                //            if(rule.trackName != null){
                //                if(currentSong!=rule.trackName) {
                //                    currentSong = rule.trackName;
                //                    for (int j = 0; j < playRuleSheet.tracksNames.size(); j++) {
                //                        if (playRuleSheet.tracksNames.get(j).equals(rule.trackName)) {
                //                            System.out.println("attempted play");
                //                            MusicSound ms = new MusicSound(playRuleSheet.tracks.get(j).getPath());
                //                            ms.fastSwitch = rule.fastSwitch;
                //                            ms.loop = rule.loop;
                //                            mst.SwitchSong(ms);
                //                        }
                //                    }
                //                }
                //            }
                //        }
                //        else{
                //            //System.out.println("rule failed");
                //        }
                //    }
                //}
                //System.out.println("RunningRuleHandlerThread");
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("attempted stop");
            if(ruleThread != null){
                if(ruleThread.isAlive()){
                    ruleThread.interrupt();
                }
            }
            //mst.stop();
        });
        checkThread.start();


    }

    public static void runRule(PlayRule inRule){
        System.out.println("TRY TO RUN RULE" + System.currentTimeMillis());
        PlayRule prevRule = cachedRule;
        cachedRule = inRule;
        if(ruleThread != null){
            if(ruleThread.isAlive()){
                ruleThread.interrupt();
            }
        }
        ruleThread = new Thread(() -> {
            if(prevRule == null){
                System.out.println("attempted play");
                MusicSound ms = playRuleSheet.getMusicSoundFromRule(cachedRule);
                mst.SwitchSong(ms);
            }
            else if(!prevRule.trackName.equals(cachedRule.trackName)){
                System.out.println("attempted play");
                MusicSound ms = playRuleSheet.getMusicSoundFromRule(cachedRule);
                mst.SwitchSong(ms);
            }
            if(cachedRule.postPlayRuleName != null){
                if(!cachedRule.postPlayRuleName.equals("")){
                    while (!mst.songFinished){
                        try {
                            Thread.sleep(2);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println("RULE SWITCHING" + System.currentTimeMillis());
                    runRule(playRuleSheet.rules.get(playRuleSheet.getRuleIndexFromName(cachedRule.postPlayRuleName)));
                }
            }
        });
        ruleThread.start();
    }

    public static void StopThread(){
        threadEnabled = false;
        mst.StopSong();
        //checkThread.interrupt();
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

    public static double rectArea(double X1, double X2, double Y1, double Y2) {
        double length = ((Math.min(X1, X2) + Math.max(X1, X2)) / 2d);

        double width = ((Math.min(Y1, Y2) + Math.max(Y1, Y2)) / 2d);

        return length * width;
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
