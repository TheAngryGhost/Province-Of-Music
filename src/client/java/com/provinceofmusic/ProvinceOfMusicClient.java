package com.provinceofmusic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.provinceofmusic.download.WMRUpdater;
import com.provinceofmusic.download.RamManager;
import com.provinceofmusic.jukebox.*;
import com.provinceofmusic.listeners.NoteListenerHelper;
import com.provinceofmusic.recorder.DebugMode;
import com.provinceofmusic.recorder.MusicRecorder;
import com.provinceofmusic.screen.ConfigScreen;
import com.provinceofmusic.screen.POMSetupScreen;
import com.provinceofmusic.screen.SamplePackEditor;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class ProvinceOfMusicClient implements ClientModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("provinceofmusic");

	public static MusicRecorder musicRecorder = new MusicRecorder();
	public static NoteReplacer noteReplacer = new NoteReplacer();

	public static KeyBinding openConfigScreenBinding;
	public static KeyBinding openSamplePackConfigScreenBinding;



	DebugMode debugMode = new DebugMode(); //this many not be used but this class needs to be instantiated at least once for it to work

	public static NoteListenerHelper noteListenerHelper = new NoteListenerHelper();

	public static File recordedmusicdir;
	public static File exportedmusicdir;
	public static File playrulesheetsdir;
	public static File configsettingsdir;
	public static File samplepacksdir;

	public static POMConfigObject configSettings;

	public static int guiSize = 1;

	private boolean popupShown = false;

	private boolean showPopup = false;

	@Override
	public void onInitializeClient() {
		setupFiles();
		getConfigSettings();
		setupListeners();
		setupCommands();
    }

	public void setupListeners(){
		ClientTickEvents.START_CLIENT_TICK.register(client -> {
			musicRecorder.PassTime();
			noteReplacer.PassTime();
			noteListenerHelper.tick();
			if(MinecraftClient.getInstance().options.getGuiScale().getValue() == 0){
				guiSize = 3;
			}
			else{
				guiSize = MinecraftClient.getInstance().options.getGuiScale().getValue();
			}

		});

		musicRecorder.main();
		if(RamManager.isRamGood()){
			noteReplacer.main();
			noteReplacer.RunSetup();
		}
		else{
			showPopup = true;
		}


		ClientTickEvents.START_WORLD_TICK.register(client -> {
		});

		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			//TODO add a one min delay after world start up before this is ran
			if(ProvinceOfMusicClient.configSettings.activeSamplePack.contains("Wynn Music Remastered") && !ProvinceOfMusicClient.configSettings.activeSamplePack.equals(WMRUpdater.currentVersion)){
				Text message = Text.literal("[Click here to Update Wynn Music Remastered]")
						.setStyle(Style.EMPTY
								.withColor(Formatting.YELLOW)
								.withClickEvent(new ClickEvent(
										ClickEvent.Action.RUN_COMMAND,
										"/updateWMR"
								))
						);
				assert client.player != null;
				client.player.sendMessage(Text.literal("You are running an outdated version of Wynn Music Remastered").setStyle(Style.EMPTY.withColor(Formatting.YELLOW)), false);
				client.player.sendMessage(message, false);
			}
		});

		musicRecorder.recordBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("Record Midi", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "Province of Music"));
		noteReplacer.replaceNoteBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("Toggle Replace Music", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "Province of Music"));
		openConfigScreenBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("Open POM Settings", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "Province of Music"));
		openSamplePackConfigScreenBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("Open Sample Pack Config", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "Province of Music"));

		ClientLifecycleEvents.CLIENT_STARTED.register(client -> {

		});

		ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
			if (screen instanceof TitleScreen && !popupShown && showPopup) {
				popupShown = true;
				MinecraftClient.getInstance().setScreen(new CottonClientScreen(new POMSetupScreen(false)));
			}
		});

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if(openConfigScreenBinding.wasPressed()){
				MinecraftClient.getInstance().setScreen(new CottonClientScreen(new ConfigScreen()));
			}
			if(openSamplePackConfigScreenBinding.wasPressed()){
				if(ProvinceOfMusicClient.configSettings.activeSamplePack != null) {
					if(SamplePack.getFile(ProvinceOfMusicClient.configSettings.activeSamplePack).exists()) {
						SamplePack pack = SamplePack.getSamplePack(SamplePack.getFile(ProvinceOfMusicClient.configSettings.activeSamplePack));
						if(pack != null) {
							MinecraftClient.getInstance().setScreen(new CottonClientScreen(new SamplePackEditor(pack)));
						}
					}
				}
			}
		});
	}

	public void setupFiles(){

		configsettingsdir = new File("provinceofmusic/");
		if (!configsettingsdir.exists()){
			configsettingsdir.mkdirs();
		}

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

		samplepacksdir = new File("provinceofmusic/samplepacks");
		if (!samplepacksdir.exists()){
			samplepacksdir.mkdirs();
		}
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
			saveConfigSettings();
		}

		if(Objects.requireNonNull(samplepacksdir.listFiles()).length == 0 && !configSettings.saidNoToDownload){
			showPopup = true;
		}
	}

	public static void saveConfigSettings(){
		File jsonTemp = new File(ProvinceOfMusicClient.configsettingsdir + "/configSettings"+".json");
		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting().serializeNulls();
		Gson gson = builder.create();

		try {
			FileWriter fileWriter = new FileWriter(jsonTemp);
			fileWriter.write(gson.toJson(configSettings));
			fileWriter.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		if(ProvinceOfMusicClient.configSettings.activeSamplePack == null){
			NoteReplacer.replaceMusic = false;
		}
		else {
			if(RamManager.isRamGood()) {
				NoteReplacer.interupt = true;
				TimerTask task = new TimerTask() {
					@Override
					public void run() {
						noteReplacer.RunSetup();
						NoteReplacer.interupt = false;
					}
				};

				Timer timer = new Timer(true);
				timer.schedule(task, 300);
			}
		}
	}

	public static void setupCommands(){
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			dispatcher.register(ClientCommandManager.literal("updateWMR")
					.executes(context -> {
						context.getSource().getPlayer().sendMessage(Text.literal("Updating..."), false);
						WMRUpdater.download();
						return 1;
					}));
		});
	}
}