package com.provinceofmusic.download;

public class RamManager {
    public static boolean isRamGood(){
        Runtime runtime = Runtime.getRuntime();

        long maxMemory   = runtime.maxMemory();     // Max memory the JVM can use (set by -Xmx)
        long allocated   = runtime.totalMemory();   // Currently allocated memory
        long freeMemory  = runtime.freeMemory();    // Unused memory within the allocated portion
        long usedMemory  = allocated - freeMemory;  // Actual memory in use

        return maxMemory / 1024 / 1024 >= 8176;
    }
}
