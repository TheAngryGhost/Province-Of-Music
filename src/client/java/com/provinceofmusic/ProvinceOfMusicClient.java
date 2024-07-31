package com.provinceofmusic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.provinceofmusic.jukebox.*;
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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class ProvinceOfMusicClient implements ClientModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("provinceofmusic");

	MusicYoinker musicYoinker = new MusicYoinker();
	NoteReplacer noteReplacer = new NoteReplacer();

	public static File recordedmusicdir;
	public static File exportedmusicdir;
	public static File playrulesheetsdir;
	public static File configsettingsdir;

	public static POMConfigObject configSettings;

	public static boolean replaceMusic = false;

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
			noteReplacer.PassTime();

		});

		ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
			MinecraftClient.getInstance().getSoundManager().registerListener(musicYoinker);
			MinecraftClient.getInstance().getSoundManager().registerListener(noteReplacer);
		});

		musicYoinker.main();
		noteReplacer.main();
		noteReplacer.RunSetup();

		ClientTickEvents.START_WORLD_TICK.register(client -> {
		});

		ClientPlayConnectionEvents.JOIN.register(new POMPlayerJoinWorldListener());

		ClientPlayConnectionEvents.DISCONNECT.register(new POMPlayerDisconnectWorldListener());

		musicYoinker.recordBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("Record Midi", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R, "Province of Music"));
		NoteReplacer.replaceNoteBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("Toggle Replace Music", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_L, "Province of Music"));

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