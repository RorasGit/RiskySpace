package riskyspace.view.swingImpl;

import java.awt.GraphicsEnvironment;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import riskyspace.view.lobby.StartScreen;

public class LobbyView {
	
	private JFrame frame = null;
	private StartScreen startScreen = null;
	
	public LobbyView() {
		setFrame();
		startScreen = new StartScreen();
		frame.add(startScreen);
		frame.setVisible(true);
	}
	
	private void setFrame() {
		frame = new JFrame("RiskySpace");
		frame.setIgnoreRepaint(true);
		frame.setUndecorated(true);
		if (GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().isFullScreenSupported()) {
			GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(frame);
		} else {
			System.err.println("Fullscreen not supported");
		}
	}

	public void draw() {
		startScreen.repaint();
	}

}