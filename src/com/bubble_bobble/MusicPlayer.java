package com.bubble_bobble;

import java.io.InputStream;
import java.io.BufferedInputStream;

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
			InputStream bufferedIn = new BufferedInputStream(MusicPlayer.class.getClassLoader().getResourceAsStream("audio/" + filename + ".mid"));
			clip.open(AudioSystem.getAudioInputStream(bufferedIn));
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
