package riskyspace;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import riskyspace.logic.FleetMove;
import riskyspace.model.Player;
import riskyspace.model.Resource;
import riskyspace.model.World;
import riskyspace.services.Event;
import riskyspace.services.EventBus;
import riskyspace.services.EventHandler;

public class GameManager implements EventHandler {

	//private Player[] players = null;
	private Player currentPlayer = null;
	private World world = null;
	private int turn;
	
	private Map<Player, PlayerInfo> players = new HashMap<Player, PlayerInfo>();
	
	public GameManager(World world, int nbrOfPlayers) {
		this.world = world;
		//players = new Player[]{Player.BLUE, Player.RED};
		initPlayers();
		changePlayer();
		EventBus.INSTANCE.addHandler(this);
		turn = 1;
	}
	
	public void initPlayers() {
		players.put(Player.RED, new PlayerInfo(Color.RED));
		players.put(Player.BLUE, new PlayerInfo(Color.BLUE));
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	public int getTurn() {
		return turn;
	}
	
	private void changePlayer() {
		currentPlayer = (currentPlayer == Player.BLUE) ? Player.RED : Player.BLUE;
		/*
		 * Give income in ViewEventController instead?
		 */
		Event event = new Event(Event.EventTag.ACTIVE_PLAYER_CHANGED, currentPlayer);
		EventBus.INSTANCE.publish(event);
		world.giveIncome(currentPlayer);
		world.updatePlayerStats(currentPlayer);
		event = new Event(Event.EventTag.METAL_CHANGED, world.getResources(currentPlayer, Resource.METAL));
		EventBus.INSTANCE.publish(event);
		event = new Event(Event.EventTag.GAS_CHANGED, world.getResources(currentPlayer, Resource.GAS));
		EventBus.INSTANCE.publish(event);
		event = new Event(Event.EventTag.SUPPLY_CHANGED, world.getSupply(currentPlayer));
		EventBus.INSTANCE.publish(event);
	}

	@Override
	public void performEvent(Event evt) {
		if (evt.getTag() == Event.EventTag.NEXT_TURN) {
			if (!FleetMove.isMoving()) {
				changePlayer();
				if (currentPlayer == Player.BLUE) {
					turn++;
				}
				world.resetShips();
			}
		}
	}
}
