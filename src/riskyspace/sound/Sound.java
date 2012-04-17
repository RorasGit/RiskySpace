package riskyspace.sound;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;

public class Sound {
	public static synchronized void playSound(final String name) {
		try {
			File file = new File("res/sound/select.wav");
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(file);
			AudioFormat format = inputStream.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			Clip clip = (Clip)AudioSystem.getLine(info);
			clip.open(inputStream);
			try {
				FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				gainControl.setValue(-20.0f);
			} catch (IllegalArgumentException e) {
				System.err.println("Volume control not supported!");
			}
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
