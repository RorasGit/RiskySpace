package riskyspace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import riskyspace.logic.FleetMove;
import riskyspace.model.Player;
import riskyspace.model.Resource;
import riskyspace.model.World;
import riskyspace.services.Event;
import riskyspace.services.EventBus;
import riskyspace.services.EventHandler;

public enum GameManager implements EventHandler {


	INSTANCE;
	
	private Player[] players = {Player.BLUE, Player.RED, Player.GREEN, Player.PINK};
	
	private Player currentPlayer = null;
	
	private World world = null;
	private int turn;
	
	private List<Player> activePlayers = new ArrayList<Player>();
	private Map<Player, PlayerInfo> playerInfo = new HashMap<Player, PlayerInfo>();
	
	public void init(World world, int nbrOfPlayers) {
		this.world = world;
		initPlayers(nbrOfPlayers);
		changePlayer();
		EventBus.INSTANCE.addHandler(this);
		turn = 1;
	}
	
	public void initPlayers(int nbrOfPlayers) {
		for (int i = 0; i < nbrOfPlayers; i++) {
			activePlayers.add(players[i]);
		}
		for (Player player : activePlayers) {
			playerInfo.put(player, new PlayerInfo(player));
		}
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	public int getTurn() {
		return turn;
	}
	
	public PlayerInfo getInfo(Player player) {
		return playerInfo.get(player);
	}
	
	private void changePlayer() {
		currentPlayer = activePlayers.get(((activePlayers.indexOf(currentPlayer) + 1) % activePlayers.size()));
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
