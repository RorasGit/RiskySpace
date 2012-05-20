package riskyspace.sound;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class PlayList {
	
	public static final String[] STANDARD_GAME_LOOP = {"morgantj_-_Wayfarer.mp3",
													   "T-sonic - Collapsar.mp3",
													   "zikweb_-_Black_Snow.mp3",
													   "T-sonic - Expanse.mp3",
													   "zikweb_-_Not_too_quiet.mp3",
													   "zikweb_-_Lament_Harp.mp3",
													   "T-sonic - Memory of Moon.p3",
													   "T-sonic - Noising.mp3",
													   "T-sonic - Singularity.mp3",};
	public static final String[] STANDARD_LOBBY_LOOP = {"T-sonic - Nucleosynthesis.mp3", 
														"T-sonic - AirTrance.mp3",};
	
	private String[] tracks;
	private int songIndex = 0;
	private boolean stopped = false;
	private Player soundPlayer;
	
	private Thread playThread;

	public static void main(String[] args) {
		new PlayList(STANDARD_GAME_LOOP).start();
	}
	
	public PlayList(String[] tracks) {
		this.tracks = new String[tracks.length];
		for (int i = 0; i < tracks.length; i++) {
			this.tracks[i] = tracks[i];
		}
	}

	/**
	 * Start playing this playList
	 */
	public void start() {
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
	
	/**
	 * Pause this PlayList. Can be continued by calling start().
	 */
	public void pause() {
		if (playThread != null && playThread.isInterrupted()) {
			playThread.interrupt();
		}
		songIndex++;
	}
}