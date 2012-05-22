package riskyspace;

import javax.swing.JFrame;

import riskyspace.data.GameDataHandler;
import riskyspace.view.lobby.LobbyView;

/**
 * 
 * Application Main entry point
 */
public class Main {
	private static JFrame frame;
	
	public static void main(String[] args) {
		GameDataHandler.init();
		new LobbyView();
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