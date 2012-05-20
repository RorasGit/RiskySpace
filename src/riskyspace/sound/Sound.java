package riskyspace.sound;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;

/**
 * This is a utility class for playing wav files of any length.
 * @author flygarn
 *
 */
public class Sound {
	
	/**
	 * Play a sound from a file name.
	 * <p>
	 * wav files should be located in res/sound/
	 * @param name Name of the sound file.
	 */
	public static synchronized void playSound(final String name) {
		try {
			File file = new File("res/sound/select.wav");
			/*
			 * Create a AudioInputStream with the Sound File
			 */
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(file);

			/*
			 * Set up a supported format
			 */
			AudioFormat format = inputStream.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			Clip clip = (Clip)AudioSystem.getLine(info);
			clip.open(inputStream);
			try {
				/*
				 * Decrease volume by 20 decibel.
				 */
				FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				gainControl.setValue(-20.0f);
			} catch (IllegalArgumentException e) {
				System.err.println("Volume control not supported!");
			}
			/*
			 * Start playing the Sound
			 */
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		playMP3("zikweb_-_Not_too_quiet.mp3");
	}

	
	
	public static void playMP3(final String name) {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				try {
					FileInputStream fis = new FileInputStream("res/sound/" + name);
					Player play = new Player(fis);
					play.play();
					
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (JavaLayerException e) {
					e.printStackTrace();
				}
			}
		};
		Thread t = new Thread(r);
		t.start();
	}
}