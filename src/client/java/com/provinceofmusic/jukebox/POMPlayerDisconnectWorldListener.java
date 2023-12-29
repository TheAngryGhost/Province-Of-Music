package com.provinceofmusic.jukebox;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;

public class POMPlayerDisconnectWorldListener implements ClientPlayConnectionEvents.Disconnect{
    @Override
    public void onPlayDisconnect(ClientPlayNetworkHandler handler, MinecraftClient client) {
        System.out.println("DISCONECTED!!!!!!!");
    }
}
