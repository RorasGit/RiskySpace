package riskyspace.sound;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

public class Sound {
	public static synchronized void playSound(final String name) {
		try {
			File file = new File("res/sound/select.wav");
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(file);
			AudioFormat format = inputStream.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			Clip clip = (Clip)AudioSystem.getLine(info);
			clip.open(inputStream);
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
//			System.err.println(e.getMessage());
			System.exit(1);
		}
	}
}
