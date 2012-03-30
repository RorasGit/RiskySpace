package riskyspace;

import riskyspace.model.Fleet;
import riskyspace.model.Player;
import riskyspace.model.Position;
import riskyspace.model.World;
import riskyspace.services.Event;
import riskyspace.services.EventBus;
import riskyspace.services.EventHandler;

public class GameManager implements EventHandler {

	private Player[] players = {Player.BLUE, Player.RED};
	private Player currentPlayer = null;
	
	private World world = null;
	private int turn = 1;
	
	public GameManager(World world, int nbrOfPlayers) {
		this.world = world;
		changePlayer();
		EventBus.INSTANCE.addHandler(this);
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	private void changePlayer() {
		currentPlayer = (currentPlayer == Player.BLUE) ? Player.RED : Player.BLUE;
	}

	@Override
	public void performEvent(Event evt) {
		if (evt.getTag() == Event.EventTag.NEXT_TURN) {
			changePlayer();
			world.giveIncome(currentPlayer);
			if (currentPlayer == Player.BLUE) {
				turn++;
			}
			for (Position pos : world.getContentPositions()) {
				for (Fleet fleet : world.getTerritory(pos).getFleets()) {
					fleet.reset();
				}
			}
			Event event = new Event(Event.EventTag.ACTIVE_PLAYER_CHANGED, currentPlayer);
			EventBus.INSTANCE.publish(event);
		}
		
	}
	
}
