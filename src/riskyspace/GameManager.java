package riskyspace;

import riskyspace.model.Player;
import riskyspace.model.World;
import riskyspace.services.Event;
import riskyspace.services.EventBus;
import riskyspace.services.EventHandler;

public class GameManager implements EventHandler {

	// For future changePlayer() that isn't limited to/specified for Player.BLUE and Player.RED
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
	
	public int getTurn() {
		return turn;
	}
	
	private void changePlayer() {
		currentPlayer = (currentPlayer == Player.BLUE) ? Player.RED : Player.BLUE;
		Event event = new Event(Event.EventTag.ACTIVE_PLAYER_CHANGED, currentPlayer);
		EventBus.INSTANCE.publish(event);
	}

	@Override
	public void performEvent(Event evt) {
		if (evt.getTag() == Event.EventTag.NEXT_TURN) {
			changePlayer();
			world.giveIncome(currentPlayer);
			if (currentPlayer == Player.BLUE) {
				turn++;
			}
			world.resetShips();
		}
	}
}
