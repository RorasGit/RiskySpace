package riskyspace.sound;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 * This is a utility class for playing sound files
 * @author Alexander Hederstaf
 *
 */
public class SoundEffect {
	
	public final static String FLEET_MOVE = "";
	public final static String BATTLE_SOUND = "";
	public final static String YOUR_TURN = "";

	private final static List<String> currentlyPlaying = new ArrayList<String>();
	
	public synchronized static void playEffect(final String name) {
		if (currentlyPlaying.contains(name)) {
			return; // Ignore effect sound if is already playing
		}
		Runnable r = new Runnable() {
			@Override
			public void run() {
				try {
					currentlyPlaying.add(name);
					FileInputStream fis = new FileInputStream("res/sound/" + name);
					Player play = new Player(fis);
					play.play();
					currentlyPlaying.remove(name);
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