package com.provinceofmusic.mixin.client;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ClickType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ScreenHandler.class)
public class ItemClickMixin {
	@Inject(at = @At("HEAD"), method = "handleSlotClick")
	//@ModifyArgs(method = "onSlotClick", at @At(value = "INVOKE", target = "Lnet/minecraft/screen/ScreenHandler;onSlotClick()"))
	private void run(PlayerEntity player, ClickType clickType, Slot slot, ItemStack stack, ItemStack cursorStack, CallbackInfoReturnable<Boolean> cir) {
		// This code is injected into the start of MinecraftClient.run()V

		System.out.println("Player clicked!" + stack.getName().getString());

	}
}