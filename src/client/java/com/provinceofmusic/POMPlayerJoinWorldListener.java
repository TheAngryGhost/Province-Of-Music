package com.provinceofmusic;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;

public class POMPlayerJoinWorldListener implements ClientPlayConnectionEvents.Join{
    @Override
    public void onPlayReady(ClientPlayNetworkHandler handler, PacketSender sender, MinecraftClient client) {
        System.out.println("CONECTED!!!!!!!");
        //MinecraftClient.getInstance().setScreen(new CustomScreen(Text.literal("my tutorial screen")));

        //MinecraftClient.getInstance().setOverlay(new CustomOverlay());
    }
}
