package riskyspace.view.swing.impl;

import java.awt.GraphicsEnvironment;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

import riskyspace.view.lobby.StartScreen;

public class LobbyView implements Observer {
	
	private JFrame frame = null;
	private StartScreen startScreen = null;
	private Thread renderThread;
	
	public LobbyView () {
		setFrame();
		startScreen = new StartScreen();
		startScreen.setObserver(this);
		frame.addKeyListener(startScreen.getKeyListener());
		frame.add(startScreen);
		frame.setVisible(true);
		renderThread = new Thread(new Runnable(){
			@Override
			public void run() {
				while (true) {
					frame.repaint();
					try {
						Thread.sleep(1000/30);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
		}});
		renderThread.start();
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

	@Override
	public void update(Observable o, Object arg) {
		if ("dispose".equals(arg)) {
			if (renderThread != null && renderThread.isAlive()) {
				renderThread.interrupt();
			}
			frame.dispose();
		}
	}
}