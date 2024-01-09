package com.provinceofmusic;

import com.provinceofmusic.jukebox.POMPlayerDisconnectWorldListener;
import com.provinceofmusic.jukebox.POMPlayerJoinWorldListener;
import com.provinceofmusic.recorder.MusicYoinker;
import com.provinceofmusic.screen.ConfigScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProvinceOfMusicClient implements ClientModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("provinceofmusic");

	MusicYoinker musicYoinker = new MusicYoinker();

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		LOGGER.info("Hello Fabric world!");

		ClientTickEvents.START_CLIENT_TICK.register(client -> {
			musicYoinker.PassTime();
		});

		ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
			MinecraftClient.getInstance().getSoundManager().registerListener(musicYoinker);
		});
		musicYoinker.main();
		ClientTickEvents.START_WORLD_TICK.register(client -> {
			//musicYoinker.PassTime();
		});
		ClientPlayConnectionEvents.JOIN.register(new POMPlayerJoinWorldListener());
		ClientPlayConnectionEvents.DISCONNECT.register(new POMPlayerDisconnectWorldListener());
		musicYoinker.recordBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("MusicYoinkerRecordMidi", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R, "key.category.first.test"));
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
		});
		ConfigScreen.INSTANCE.load();
	}

	public static ConfigScreen getConfig() {
		return ConfigScreen.INSTANCE.getConfig();
	}
}