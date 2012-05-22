package riskyspace.sound;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import riskyspace.data.Settings;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 * PlayList play sound files in a loop with a 45second break in between sound files.
 * @author Alexander Hederstaf + DJ rapp
 *
 */
public class PlayList {
	
	/**
	 * Standard game loop playlist order
	 */
	public static final String[] STANDARD_GAME_LOOP = {"T-sonic - AirTrance.mp3",
													   "zikweb_-_Not_too_quiet.mp3",
													   "T-sonic - Expanse.mp3",
													   "T-sonic - Collapsar.mp3",
													   "T-sonic - Memory of Moon.mp3",
													   "T-sonic - Noising.mp3",
													   "morgantj_-_Wayfarer.mp3",
													   "T-sonic - Singularity.mp3",
													   "T-sonic - Nucleosynthesis.mp3"};
	
	/**
	 * Standard lobby loop playlist
	 */
	public static final String[] STANDARD_LOBBY_LOOP = {"zikweb_-_Lament_Harp.mp3",
														"zikweb_-_Black_Snow.mp3"};
	
	private String[] tracks;
	private int songIndex = 0;
	private boolean stopped = false;
	private Player soundPlayer;
	
	private Thread playThread;

	/**
	 * Create a new PlayList that will Loop a set of songs
	 * @param tracks A String Array with song names.
	 */
	public PlayList(String[] tracks) {
		this.tracks = new String[tracks.length];
		if (tracks == STANDARD_LOBBY_LOOP) {
			// Random the starting track for Lobby
			int r = (int) (Math.random() * 2);
			this.tracks[0] = tracks[r];
			this.tracks[1] = tracks[(r + 1) % 2];
		} else {
			for (int i = 0; i < tracks.length; i++) {
				this.tracks[i] = tracks[i];
			}
		}
	}

	/**
	 * Start playing this playList
	 */
	public void start() {
		if (Settings.isMusicOn()) {
			stopped = false;
			Runnable r = new Runnable() {
				@Override
				public void run() {
					try {
						if (tracks.length == 0) {
							stopped = true;
						}
						while (!stopped) {
							for (int i = songIndex; i < tracks.length; i++) {
								FileInputStream fis = new FileInputStream("res/sound/" + tracks[i]);
								soundPlayer = new Player(fis);
								soundPlayer.play();
								try {
									Thread.sleep(45 * 1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								if (stopped) {
									break;
								}
							}
							songIndex = 0;
						}
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (JavaLayerException e) {
						e.printStackTrace();
					}
				}
			};
			playThread = new Thread(r);
			playThread.start();
		}
	}
	
	/**
	 * Pause this PlayList. Can be continued by calling start().
	 */
	public void pause() {
		if (playThread != null && soundPlayer != null) {
			soundPlayer.close();
			stopped = true;
		}
		songIndex++;
	}
}