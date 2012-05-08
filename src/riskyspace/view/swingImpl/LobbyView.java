package riskyspace.view.swingImpl;

import java.awt.GraphicsEnvironment;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import riskyspace.view.lobby.StartScreen;

public class LobbyView {
	
	private JFrame frame = null;
	private StartScreen startScreen = null;
	
	public LobbyView(KeyListener keyListener) {
		setFrame();
		startScreen = new StartScreen();
		frame.add(startScreen);
		frame.setVisible(true);
		frame.addKeyListener(keyListener);
	}
	
	private void setFrame() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setIgnoreRepaint(true);
		frame.setUndecorated(true);
		System.out.println("Mem: " + GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getAvailableAcceleratedMemory());
		/*
		 * If Mem is small set non transparent textures!
		 */
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