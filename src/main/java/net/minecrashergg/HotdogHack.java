package net.minecrashergg;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecrashergg.modules.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.Clip;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class HotdogHack implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("hotdoghack");
	private static final List<Hack> HACKS = new ArrayList<>();

	@Override
	public void onInitialize() {
		HACKS.add(new Flight());
		HACKS.add(new NoFall());
		HACKS.add(new BoatFly());
		HACKS.add(new Speed());
		HACKS.add(new Step());
		HACKS.add(new Jesus());
		HACKS.add(new SuperJump());
		HACKS.add(new FullBright());
		HACKS.add(new XRay());
		HACKS.add(new NoWeather());
		HACKS.add(new AntiHunger());
		HACKS.add(new KillAura());
//		HACKS.add(new Listener());

		ClientTickEvents.START_CLIENT_TICK.register(client ->
			HACKS.forEach(hack -> {
				if (hack.isEnabled())
					hack.tick();
			})
		);

		ClientTickEvents.END_CLIENT_TICK.register(client ->
			HACKS.forEach(hack -> {
				if (hack.isEnabled())
					hack.postTick();
			})
		);

		HudRenderCallback.EVENT.register((drawContext, delta) -> {
			if (MinecraftClient.getInstance().getDebugHud().shouldShowDebugHud()) return;
			drawHud(drawContext);
		});
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			dispatcher.register(ClientCommandManager.literal("listmusic").executes(context -> {
				playMusicList();  // This method should list or handle the music files
				return 1;
			}));
		});
		// Register /play <number> command
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			dispatcher.register(
					ClientCommandManager.literal("play")
							.then(ClientCommandManager.argument("songNumber", IntegerArgumentType.integer(1))
									.executes(context -> {
										int songNumber = IntegerArgumentType.getInteger(context, "songNumber");
										playSong(songNumber);  // Play the selected song
										return 1;
									})
							)
			);
		});
		// Register /stop command
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			dispatcher.register(ClientCommandManager.literal("stop")
							.executes(context -> {
								stopSong();  // Stop the currently playing song
								return 1;
							})
			);
		});
	}


	/**
	 * Draw the HUD
	 * @param drawContext	the context of drawing
	 */
	private static void drawHud(DrawContext drawContext) {
		var player = MinecraftClient.getInstance().player;
		if (player == null) return;

		var textRenderer = MinecraftClient.getInstance().textRenderer;

		drawWatermark(drawContext, textRenderer, player.age);

		var index = 1;
		for (var hack : getHacks()) {
			if (!hack.isEnabled()) continue;

			float hue = ((float) player.age / 200f + (float) index / (HotdogHack.numHacks() + 1)) % 1f;
			var color = MathHelper.hsvToRgb(hue, 1f, 1f);

			drawContext.drawText(
				textRenderer,
//				Text.translatable("hacks.", hack.getClass().getSimpleName().toLowerCase()),
				HotdogHack.getTranslatableText(hack.getClass()),
				10, 25 + index * 10,
				color, true
			);

			index++;
		}
	}

	/**
	 * Draw the watermark on the hud
	 * @param drawContext	the context of drawing
	 * @param textRenderer	the renderer of text
	 * @param age			the age of the player
	 */
	private static final Identifier HOTDOG_LOGO = Identifier.of("hotdogclient", "textures/gui/hotdog_logo.png");

	private static void drawWatermark(DrawContext drawContext, TextRenderer textRenderer, int age) {
		float hue = ((float) age / 200f) % 1f;
		var color = MathHelper.hsvToRgb(hue, 1f, 1f);


		var scale = 1.5f;

		// Draw the logo
		//drawContext.drawTexture(HOTDOG_LOGO, 10, 4, 0, 0, 32, 32, 32, 32); // (texture, x, y, u, v, width, height, textureWidth, textureHeight)

		//draw the text after the logo
		var matrices = drawContext.getMatrices().peek().getPositionMatrix();
		matrices.scale(scale);

		//textRenderer.draw(
		//		Text.translatable("hacks.title"),
		//		30, 10,  // Offset the text to avoid overlap with the image
		//		color, true,
		//		matrices,
		//		drawContext.getVertexConsumers(),
		//		TextRenderer.TextLayerType.NORMAL,
		//		0, 0xF000F0
		//);

		matrices.scale(1f / scale);
		drawContext.draw();

	}

	/**
	 * Get the hack from {@code HACKS} that matches the class {@code hackClass}
	 * @param 	hackClass the hack to get
	 * @return	the matching hack from {@code HACKS}
	 * @param 	<T> extends Hack
	 */
	public static <T extends Hack> T getHack(Class<T> hackClass) {
		for (var hack : getHacks()) {
			if (hack.getClass() == hackClass) {
				return hackClass.cast(hack);
			}
		}

		return null;
	}

	/**
	 * Checks if the hack with class {@code hackClass} is enabled
	 * @param 	hackClass the class to search for
	 * @return	whether the hack is enabled
	 */
	public static boolean isEnabled(Class<? extends Hack> hackClass) {
		var hack = getHack(hackClass);
		return hack != null && hack.isEnabled();
	}

	/**
	 * Toggles enabled for the hack with class {@code hackClass}
	 * @param hackClass the class to toggle
	 */
	public static void toggle(Class<? extends Hack> hackClass) {
		var hack = getHack(hackClass);
		if (hack != null) {
			hack.toggle();
		}
	}

	/**
	 * Gets a list of loaded hacks
	 * @return a list of hacks
	 */
	public static List<Hack> getHacks() {
		return HACKS;
	}

	/**
	 * Get the translatable text from the given {@code hackclass}
	 * @param hackClass	the class to give the translatable text for
	 * @return			the translatable text
	 */
	public static Text getTranslatableText(Class<? extends Hack> hackClass) {
		return Text.translatable("hacks." + hackClass.getSimpleName().toLowerCase());
	}

	public static int numHacks() {
		return getHacks().size();
	}

	public static void sendMessageToClientChat(String message) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client.player != null) {
			client.player.sendMessage(Text.literal(message), false);
		}
	}

	private Clip clip;
	private Clip currentClip = null;   // To track the currently playing song
	private boolean isPlaying = false; // To track if a song is currently playing
	private Thread repeatingThread;    // For repeating the song in the background

	// List of songs available in the folder
	private File[] musicFiles = null;

	public void playMusicList() {
		// Get the user's home directory
		String userHome = System.getProperty("user.home");
		File defaultMusicDirectory = new File(userHome, "Music");
		File musicFolder = new File(defaultMusicDirectory.getAbsolutePath());
		musicFiles = musicFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".wav"));

		if (musicFiles == null || musicFiles.length == 0) {
			sendMessageToClientChat("No music files found!");
			return;
		}

		sendMessageToClientChat("Music files found:");
		for (int i = 0; i < musicFiles.length; i++) {
			sendMessageToClientChat((i + 1) + ": " + musicFiles[i].getName());
		}
	}



	public static String currentSong;
	public void playSong(int songIndex) {
		if (songIndex < 1 || songIndex > musicFiles.length) {
			sendMessageToClientChat("Invalid song number! Please choose a number from the list.");
			return;
		}

		// Stop any currently playing song
		stopSong();

		try {
			File selectedFile = musicFiles[songIndex - 1]; // -1 because the user sees 1-based index
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(selectedFile);
			currentClip = AudioSystem.getClip();
			currentClip.open(audioStream);
			sendMessageToClientChat("Now playing: " + selectedFile.getName());
			currentSong = selectedFile.getName();

			isPlaying = true;

			// Start a thread to repeat the song indefinitely
			repeatingThread = new Thread(() -> {
				while (isPlaying) {
					currentClip.start(); // Start the clip
					currentClip.loop(Clip.LOOP_CONTINUOUSLY); // Repeat the song continuously
					try {
						Thread.sleep(currentClip.getMicrosecondLength() / 1000); // Wait until song ends
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
			});
			repeatingThread.start();

		} catch (Exception e) {
			sendMessageToClientChat("Error playing the song.");
		}
	}
	public void stopSong() {
		if (currentClip != null && currentClip.isRunning()) {
			isPlaying = false;
			currentClip.stop(); // Stop the current clip
			currentClip.close();
			currentClip = null;

			if (repeatingThread != null && repeatingThread.isAlive()) {
				repeatingThread.interrupt(); // Stop the repeating thread
				repeatingThread = null;
			}

			sendMessageToClientChat("Music stopped.");
		} else {
			sendMessageToClientChat("No song is currently playing.");
		}
	}
}