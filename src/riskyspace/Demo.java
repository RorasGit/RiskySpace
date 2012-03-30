package riskyspace;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import riskyspace.logic.FleetMove;
import riskyspace.logic.ViewEventController;
import riskyspace.model.Fleet;
import riskyspace.model.Player;
import riskyspace.model.Position;
import riskyspace.model.Resource;
import riskyspace.model.World;
import riskyspace.services.Event;
import riskyspace.services.EventBus;
import riskyspace.view.View;
import riskyspace.view.ViewFactory;

public class Demo {
	
	/*
	 * Players
	 */
	
//	private Map<Player, PlayerInfo> = null;
	
	/*
	 * Models
	 */
	
	
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
		final World world = new World();
		new ViewEventController(world);
		final GameManager gm  = new GameManager(world, 2);
		mainView = ViewFactory.getView(ViewFactory.SWING_IMPL, world, new KeyListener() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ESCAPE) {
					System.exit(0);
				} else if (arg0.getKeyCode() == KeyEvent.VK_SPACE) {
					Event evt = new Event(FleetMove.isMoving() ? Event.EventTag.INTERRUPT_MOVES : Event.EventTag.PERFORM_MOVES, null);
					EventBus.INSTANCE.publish(evt);
				} else if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					Event evt = new Event(Event.EventTag.NEXT_TURN, null);
					EventBus.INSTANCE.publish(evt);
					System.out.println(world.getResources(gm.getCurrentPlayer(), Resource.METAL));
					mainView.setViewer(gm.getCurrentPlayer());
					for (Position pos : world.getContentPositions()) {
						for (Fleet fleet : world.getTerritory(pos).getFleets()) {
							fleet.reset();
						}
					}
				}
			}
			@Override public void keyReleased(KeyEvent arg0) {}
			@Override public void keyTyped(KeyEvent arg0) {}
		});
		mainView.setViewer(gm.getCurrentPlayer());
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
