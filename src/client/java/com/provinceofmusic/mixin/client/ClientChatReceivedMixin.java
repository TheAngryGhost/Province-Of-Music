package com.provinceofmusic.mixin.client;

import com.mojang.authlib.GameProfile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.ProfilelessChatMessageS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.text.Text;
import net.minecraft.network.packet.Packet;

import java.util.UUID;

@Environment(EnvType.CLIENT)
//@Mixin(ChatHud.class)
//@Mixin(InGameHud.class)
@Mixin(ClientPlayNetworkHandler.class)
//@Mixin(MessageHandler.class)
public class ClientChatReceivedMixin {


    //void djkfhjsdf(){
    //    ServerPlayNetworkHandler
    //}

    //@Inject(method = "logChatMessage", at = @At("HEAD"))
    //private void run(Text message, MessageIndicator indicator, CallbackInfo ci) {
    //    // This code is injected into the start of MinecraftClient.run()V
//////
    //    //System.out.println("Message Recieved" + message.getString());
    //    System.out.println("Message Recieved " + indicator.text() + " " + message.getString());
//////
    //    //put this at tail and get string 1 and 2'
//////
//////
//
    //    //ChatMessageS2CPacket packet = new ChatMessageS2CPacket packet();
    //    //packet.unsignedContent();
    //}


    //@Inject(method = "<init>", at = @At("TAIL"))
    //private void run(MinecraftClient client, ItemRenderer itemRenderer, CallbackInfo ci) {
    //    // This code is injected into the start of MinecraftClient.run()V
////
//
//
    //    System.out.println("Message Recieved" + .getString());
////
    //    //put this at tail and get string 1 and 2'
////
////
    //}

    //@Inject(method = "onChatMessage", at = @At("HEAD"))
    //private void run(ChatMessageS2CPacket packet, CallbackInfo ci) {
    //    // This code is injected into the start of MinecraftClient.run()V
//////
    //    //System.out.println("Message Recieved" + message.getString());
    //    System.out.println("Message Recieved " + packet.unsignedContent());
//////
    //    //put this at tail and get string 1 and 2'
//////
//////
////
    //    //ChatMessageS2CPacket packet = new ChatMessageS2CPacket packet();
    //    //packet.unsignedContent();
//
    //    //MinecraftClient.getInstance().getMessageHandler().onChatMessage();
    //}

    @Inject(method = "onGameMessage", at = @At("HEAD"))
    private void run(GameMessageS2CPacket packet, CallbackInfo ci) {
        // This code is injected into the start of MinecraftClient.run()V
////
        //System.out.println("Message Recieved" + message.getString());

        if(!packet.content().getString().contains("❤") && !packet.content().getString().contains("/") && !packet.content().getString().contains("✺")){
            System.out.println("Message Recieved " + packet.content().getString());

        }

////
        //put this at tail and get string 1 and 2'
////
////
//
        //ChatMessageS2CPacket packet = new ChatMessageS2CPacket packet();
        //packet.unsignedContent();

        //MinecraftClient.getInstance().getMessageHandler().onChatMessage();
    }

    //@Inject(method = "onProfilelessChatMessage", at = @At("HEAD"))
    //private void run(ProfilelessChatMessageS2CPacket packet, CallbackInfo ci) {
    //    // This code is injected into the start of MinecraftClient.run()V
//////
    //    //System.out.println("Message Recieved" + message.getString());
    //    System.out.println("MessageProfileless Recieved " + packet.message());
//////
    //    //put this at tail and get string 1 and 2'
//////
//////
////
    //    //ChatMessageS2CPacket packet = new ChatMessageS2CPacket packet();
    //    //packet.unsignedContent();
//
    //    //MinecraftClient.getInstance().getMessageHandler().onChatMessage();
    //}


    //@Inject(method = "onChatMessage", at = @At("HEAD"))
    //private void run(SignedMessage message, GameProfile sender, MessageType.Parameters params, CallbackInfo ci) {
    //    // This code is injected into the start of MinecraftClient.run()V
//////
    //    //System.out.println("Message Recieved" + message.getString());
    //    System.out.println("Message Recieved " + message);
//////
    //    //put this at tail and get string 1 and 2'
//////
//////
////
    //    //ChatMessageS2CPacket packet = new ChatMessageS2CPacket packet();
    //    //packet.unsignedContent();
//
    //    //MinecraftClient.getInstance().getMessageHandler().onChatMessage();
    //}
//
//
//
    //@Inject(method = "onUnverifiedMessage", at = @At("HEAD"))
    //private void run(UUID sender, MessageType.Parameters parameters, CallbackInfo ci) {
    //    // This code is injected into the start of MinecraftClient.run()V
//////
    //    //System.out.println("Message Recieved" + message.getString());
    //    Text VALIDATION_ERROR_TEXT = Text.translatable("chat.validation_error").formatted(new Formatting[]{Formatting.RED, Formatting.ITALIC});
    //    Text text = parameters.applyChatDecoration(VALIDATION_ERROR_TEXT);
    //    System.out.println("MessageUnverified  Recieved " + text);
//////
    //    //put this at tail and get string 1 and 2'
//////
//////
//////
    //    //ChatMessageS2CPacket packet = new ChatMessageS2CPacket packet();
    //    //packet.unsignedContent();
////
    //    //MinecraftClient.getInstance().getMessageHandler().onChatMessage();
    //}
//
    //@Inject(method = "onProfilelessMessage", at = @At("HEAD"))
    //private void run(Text content, MessageType.Parameters params, CallbackInfo ci) {
    //    // This code is injected into the start of MinecraftClient.run()V
//////
    //    //System.out.println("Message Recieved" + message.getString());
    //    //Text VALIDATION_ERROR_TEXT = Text.translatable("chat.validation_error").formatted(new Formatting[]{Formatting.RED, Formatting.ITALIC});
    //    //Text text = parameters.applyChatDecoration(VALIDATION_ERROR_TEXT);
    //    System.out.println("MessageProfileless  Recieved " + content.getString());
//////
    //    //put this at tail and get string 1 and 2'
//////
//////
//////
    //    //ChatMessageS2CPacket packet = new ChatMessageS2CPacket packet();
    //    //packet.unsignedContent();
////
    //    //MinecraftClient.getInstance().getMessageHandler().onChatMessage();
    //}
//
    //@Inject(method = "onGameMessage", at = @At("HEAD"))
    //private void run(Text message, boolean overlay, CallbackInfo ci) {
    //    // This code is injected into the start of MinecraftClient.run()V
//////
    //    //System.out.println("Message Recieved" + message.getString());
    //    //Text VALIDATION_ERROR_TEXT = Text.translatable("chat.validation_error").formatted(new Formatting[]{Formatting.RED, Formatting.ITALIC});
    //    //Text text = parameters.applyChatDecoration(VALIDATION_ERROR_TEXT);
    //    System.out.println("MessageGame  Recieved " + message);
//////
    //    //put this at tail and get string 1 and 2'
//////
//////
//////
    //    //ChatMessageS2CPacket packet = new ChatMessageS2CPacket packet();
    //    //packet.unsignedContent();
////
    //    //MinecraftClient.getInstance().getMessageHandler().onChatMessage();
    //}



}
