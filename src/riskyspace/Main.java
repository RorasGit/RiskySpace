package riskyspace;

import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import riskyspace.data.GameDataHandler;
import riskyspace.data.Settings;
import riskyspace.view.lobby.LobbyView;

/**
 * 
 * Application Main entry point
 */
public class Main {
	private static JFrame frame;
	
	public static void main(String[] args) {
		GameDataHandler.init();
		try {
			Settings.loadSetting(GameDataHandler.getSaveFolder() + File.separator + "settings.txt");
		} catch (IOException e) {
		}
		new LobbyView();
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				Settings.saveProperties();
			}
		}));
	}
	public static JFrame getFrame(){
		if(frame == null){
			frame = new JFrame();
		}
		frame.setVisible(false);
		frame.getContentPane().removeAll();
		frame.validate();
		return frame;
	}
}