package com.provinceofmusic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.provinceofmusic.jukebox.ConsoleCaptureExample;
import  com.provinceofmusic.jukebox.POMPlayerDisconnectWorldListener;
import com.provinceofmusic.jukebox.POMPlayerJoinWorldListener;
import com.provinceofmusic.jukebox.PlayRule;
import com.provinceofmusic.recorder.MusicYoinker;
import com.provinceofmusic.screen.ConfigScreen;
import com.provinceofmusic.sfz.PitchShifterExample;
import com.provinceofmusic.sfz.WavPlayer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.jfugue.player.Player;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;

import org.boris.jvst.AEffect;
import org.boris.jvst.VST;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class ProvinceOfMusicClient implements ClientModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("provinceofmusic");

	MusicYoinker musicYoinker = new MusicYoinker();

	public static File recordedmusicdir;
	public static File exportedmusicdir;
	public static File playrulesheetsdir;
	public static File configsettingsdir;

	public static POMConfigObject configSettings;

	//public static ArrayList<File> deletedFiles = new ArrayList<>();


	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		//LOGGER.info("Hello Fabric world!");



		setupFiles();
		getConfigSettings();
		setupListeners();

    }

	public void setupListeners(){
		ClientTickEvents.START_CLIENT_TICK.register(client -> {
			musicYoinker.PassTime();
		});

		ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
			MinecraftClient.getInstance().getSoundManager().registerListener(musicYoinker);
		});

		musicYoinker.main();

		ClientTickEvents.START_WORLD_TICK.register(client -> {
		});

		ClientPlayConnectionEvents.JOIN.register(new POMPlayerJoinWorldListener());

		ClientPlayConnectionEvents.DISCONNECT.register(new POMPlayerDisconnectWorldListener());

		musicYoinker.recordBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("MusicYoinkerRecordMidi", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R, "Province of Music"));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
		});

		ConfigScreen.INSTANCE.load();
	}

	public void setupFiles(){
		recordedmusicdir = new File("provinceofmusic/recorded-music/");
		if (!recordedmusicdir.exists()){
			recordedmusicdir.mkdirs();
		}

		exportedmusicdir = new File("provinceofmusic/exported-music/");
		if (!exportedmusicdir.exists()){
			exportedmusicdir.mkdirs();
		}

		playrulesheetsdir = new File("provinceofmusic/playrulesheets/");
		if (!playrulesheetsdir.exists()){
			playrulesheetsdir.mkdirs();
		}

		configsettingsdir = new File("provinceofmusic/");
		if (!configsettingsdir.exists()){
			configsettingsdir.mkdirs();
		}

		//recordedmusicdir = new File();

		//System.out.println(ProvinceOfMusicClient.exportedmusicdir.getPath());
	}

	public void getConfigSettings() {

		File jsonTemp = new File(ProvinceOfMusicClient.configsettingsdir + "/configSettings"+".json");
		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting().serializeNulls();
		Gson gson = builder.create();

		try {
			configSettings = gson.fromJson(Files.readString(jsonTemp.toPath(), Charset.defaultCharset()) + "", POMConfigObject.class);
		} catch (IOException e) {
			configSettings = new POMConfigObject();
			setConfigSettings();
			//throw new RuntimeException(e);
		}
	}

	public static void setConfigSettings(){
		File jsonTemp = new File(ProvinceOfMusicClient.configsettingsdir + "/configSettings"+".json");
		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting().serializeNulls();
		Gson gson = builder.create();

		try {
			FileWriter fileWriter = new FileWriter(jsonTemp);
			fileWriter.write(gson.toJson(configSettings));
			//fileWriter.write("empy");
			fileWriter.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	//public void ReloadConfig(){
	//	ConfigScreen.INSTANCE.load();
	//}

	public static ConfigScreen getConfig() {
		return ConfigScreen.INSTANCE.getConfig();
	}
}