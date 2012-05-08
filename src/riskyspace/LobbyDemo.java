package riskyspace;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import riskyspace.view.swingImpl.LobbyView;

public class LobbyDemo {
	
	private LobbyView mainView = null;
	
	public static void main(String[] args) {
		new LobbyDemo();
	}
	
	public LobbyDemo() {
		mainView = new LobbyView(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
					System.exit(0);
				}
			}
			@Override public void keyReleased(KeyEvent arg0) {}
			@Override public void keyTyped(KeyEvent arg0) {}
			});
		
		while(true) {
			mainView.draw();
			try {
				Thread.sleep(1000/60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
