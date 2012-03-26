package riskyspace;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.InputStream;

import riskyspace.model.Player;
import riskyspace.model.World;
import riskyspace.view.View;
import riskyspace.view.ViewFactory;

public class Demo {
	
	/*
	 * Players
	 */
	private Player[] players = {Player.BLUE, Player.RED};
	private Player currentPlayer = null;
//	private Map<Player, PlayerInfo> = null;
	
	/*
	 * Models
	 */
	private World world = null;
	
	/*
	 * Controllers
	 */
//	private GameThread gameThread = null;
//	no threads and listeners instead somewhere?
	
	/*
	 * Views
	 */
	private View mainView = null;
	
	public static void main(String[] args) {
		new Demo();
	}
	
	public Demo () {
		world = new World();
		mainView = ViewFactory.getView(ViewFactory.SWING_IMPL, world, new KeyListener() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ESCAPE) {
					System.exit(0);
				}
				currentPlayer = (currentPlayer == Player.BLUE) ? Player.RED : Player.BLUE;
				mainView.setViewer(currentPlayer);
			}
			@Override public void keyReleased(KeyEvent arg0) {}
			@Override public void keyTyped(KeyEvent arg0) {}
		});
		currentPlayer = Player.BLUE;
		mainView.setViewer(currentPlayer);
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
