import java.io.File;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * 
 * Music Player provides a way to play background music for Bubble Bobble game.
 *
 * @author Joshua Eckels and Sabri Amer Created Nov 9, 2018.
 */
public class MusicPlayer {

	private static Clip clip = null;

	public static void set(String filename) {
		if (clip != null && clip.isRunning()) {
			stop();
		}
		try {
			clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(new File("audio/" + filename + ".mid").getAbsoluteFile()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void play() {
		if (clip != null && clip.isRunning()) {
			stop();
		}
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public static void pause() {
		if (clip != null) {
			clip.stop();
		}
	}

	public static void stop() {
		if (clip != null) {
			clip.stop();
			clip.setFramePosition(0);
		}
	}

}
