package net.minecrashergg;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class MusicManager {
    private static Clip currentClip;
    private static boolean isPlaying;
    private static Thread repeatingThread;
    private static File[] musicFiles;

    public static void init() {
        // Optional: preload musicFiles?
    }

    public static void sendMessageToClientChat(String message) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client.player != null) {
			client.player.sendMessage(Text.literal(message), false);
		}
	}

    public static void playMusicList() { 
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
    public static void playSong(int songIndex) { if (songIndex < 1 || songIndex > musicFiles.length) {
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
    public static void stopSong() { 
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

