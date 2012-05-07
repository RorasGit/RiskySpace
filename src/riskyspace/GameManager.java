package riskyspace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import riskyspace.logic.FleetMove;
import riskyspace.logic.Path;
import riskyspace.model.Colony;
import riskyspace.model.Fleet;
import riskyspace.model.Player;
import riskyspace.model.PlayerStats;
import riskyspace.model.Position;
import riskyspace.model.Resource;
import riskyspace.model.ShipType;
import riskyspace.model.Territory;
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
		world.resetShips();
		/*
		 * Is this only to make the view show the correct supply?
		 * TODO: Fix this when implementing networking or at other previous time.
		 */
		event = new Event(Event.EventTag.SUPPLY_CHANGED, world.getSupply(currentPlayer));
		EventBus.INSTANCE.publish(event);
		if (currentPlayer == players[0]) {
			turn++;
		}
	}

	private Position lastFleetSelectPos;
	private int fleetSelectionIndex;
	private Set<Fleet> selectedFleets;
	private Map<Fleet, Path> fleetPaths;
	private Colony selectedColony;

	@Override
	public void performEvent(Event evt) {
		if (!FleetMove.isMoving()) {
			if(evt.getObjectValue() instanceof Position){
				Position pos = (Position) evt.getObjectValue();
				if (evt.getTag() == Event.EventTag.SET_PATH) {
					setPath(pos);
				}
				if (evt.getTag() == Event.EventTag.COLONY_REMOVED) {
					removeColony(pos);
				}
				if (evt.getTag() == Event.EventTag.COLONIZER_SELECTED) {
					setColonizerSelected(pos);
				}
				if (evt.getTag() == Event.EventTag.ADD_FLEET_SELECTION) {
					addFleetSelection(pos);
				}
				if (evt.getTag() == Event.EventTag.NEW_FLEET_SELECTION) {
					newFleetSelection(pos);
				}
			}
			if (evt.getTag() == Event.EventTag.NEXT_TURN) {
				changePlayer();
			}
			
			if (evt.getTag() == Event.EventTag.COLONIZE_PLANET) {
				colonizePlanet(evt);
			}
			if (evt.getTag() == Event.EventTag.ADD_FLEET_SELECTION) {
				newFleetsSelection(evt);
			}
			if (evt.getTag() == Event.EventTag.PERFORM_MOVES) {
				preformMoves();
			}
			if (evt.getTag() == Event.EventTag.QUEUE_SHIP) {
				queueShip((ShipType) evt.getObjectValue());
			}
		}
		if (evt.getTag() == Event.EventTag.ACTIVE_PLAYER_CHANGED) {
			playerChanged();
		}
	}

	private void playerChanged() {
		resetVariables();
		lastFleetSelectPos = null;
		fleetSelectionIndex = 0;
		world.processBuildQueue(GameManager.INSTANCE.getCurrentPlayer());
	}

	private void preformMoves() {
		fleetSelectionIndex = 0;
		FleetMove.move(world, fleetPaths, GameManager.INSTANCE.getCurrentPlayer());
	}

	private void setPath(Position target) {
		for (Fleet fleet : selectedFleets) {
			if (GameManager.INSTANCE.getCurrentPlayer() == fleet.getOwner()) {
				fleetPaths.get(fleet).setTarget(target);
			}
		}
		Event event = new Event(Event.EventTag.PATHS_UPDATED, null);
		EventBus.INSTANCE.publish(event);
	}

	private void removeColony(Position pos) {
		world.getTerritory(pos).getPlanet().destroyColony();
		// TODO : use position to remove buildQueue
	}
	private void colonizePlanet(Event evt) {
		if (evt.getObjectValue() instanceof Territory) {
			Territory ter = (Territory) evt.getObjectValue();
			if (ter.hasFleet() && ter.hasPlanet() && !ter.hasColony()) {
				for (Fleet fleet : ter.getFleets()) {
					if (fleet.hasColonizer()) {
						fleet.useColonizer();
						ter.getPlanet().buildColony(fleet.getOwner());
						ter.removeFleet(fleet);
						//EventText et = new EventText("Colony built !!", )
						world.updatePlayerStats(GameManager.INSTANCE.getCurrentPlayer());
						//EventBus.INSTANCE.publish(new Event(Event.EventTag.EVENT_TEXT, et));
						Event event = new Event(Event.EventTag.SUPPLY_CHANGED, world.getSupply(GameManager.INSTANCE.getCurrentPlayer()));
						EventBus.INSTANCE.publish(event);
						break; // Stop looping through fleets.
					}
				}
				selectedColony = ter.getColony();
				Event event = new Event(Event.EventTag.HIDE_MENU, null);
				EventBus.INSTANCE.publish(event);
				event = new Event(Event.EventTag.SHOW_MENU, selectedColony);
				EventBus.INSTANCE.publish(event);
			}
		}
	}
	private void setColonizerSelected(Position pos) {
		resetVariables();
		fleetSelectionIndex = 0;
		lastFleetSelectPos = null;
		if (world.getTerritory(pos).hasColonizer()) {
			for (Fleet fleet : world.getTerritory(pos).getFleets()) {
				if (fleet.hasColonizer()) {
					selectedFleets.add(fleet);
					fleetPaths.put(fleet, new Path(pos));
					break;
				}
			}
			EventBus.INSTANCE.publish(new Event(Event.EventTag.SHOW_FLEETMENU, Collections.unmodifiableSet(selectedFleets)));
		}
		
	}
	
	private void addFleetSelection(Position pos) {
		selectedColony = null;
		if (selectedFleets.isEmpty()) {
			EventBus.INSTANCE.publish(new Event(Event.EventTag.HIDE_MENU, null));
		} else if (selectedFleets.iterator().next().hasColonizer()) {
			selectedFleets.clear();
			EventBus.INSTANCE.publish(new Event(Event.EventTag.HIDE_MENU, null));
		}
		
		if (lastFleetSelectPos == null || !lastFleetSelectPos.equals(pos)) {
			lastFleetSelectPos = pos;
			fleetSelectionIndex = 0;
		}
		if (world.getTerritory(pos).hasFleet() && (world.getTerritory(pos).controlledBy() == GameManager.INSTANCE.getCurrentPlayer())) {
			/*
			 * Check that there are other ships than colonizers
			 */
			if (world.getTerritory(pos).getFleets().size() != world.getTerritory(pos).shipCount(ShipType.COLONIZER)) {
				Fleet fleet;
				do {
					fleet = world.getTerritory(pos).getFleet(fleetSelectionIndex);
					fleetSelectionIndex = (fleetSelectionIndex + 1) % world.getTerritory(pos).getFleets().size();
				} while(fleet.hasColonizer());
				
				addSelectedFleet(fleet, pos);
				EventBus.INSTANCE.publish(new Event(Event.EventTag.SHOW_FLEETMENU, Collections.unmodifiableSet(selectedFleets)));
			}
		
	}
	}

	private void newFleetSelection(Position pos) {
		resetVariables(); // Reset all selections as we make a new selection
		if (lastFleetSelectPos == null || !lastFleetSelectPos.equals(pos)) {
			lastFleetSelectPos = pos;
			fleetSelectionIndex = 0;
		}
		if (world.getTerritory(pos).hasFleet() && (world.getTerritory(pos).controlledBy() == GameManager.INSTANCE.getCurrentPlayer())) {
			if (world.getTerritory(pos).getFleets().size() != world.getTerritory(pos).shipCount(ShipType.COLONIZER)) {
				Fleet fleet;
				do {
					fleet = world.getTerritory(pos).getFleet(fleetSelectionIndex);
					fleetSelectionIndex = (fleetSelectionIndex + 1) % world.getTerritory(pos).getFleets().size();
				} while(fleet.hasColonizer());
				addSelectedFleet(fleet, pos);
			}
		}
		if (!selectedFleets.isEmpty()) {
			EventBus.INSTANCE.publish(new Event(Event.EventTag.SHOW_FLEETMENU, Collections.unmodifiableSet(selectedFleets)));
		}
	}

	private void newFleetsSelection(Event evt) {
		resetVariables(); // Reset all selections as we make a new selection
		if (evt.getObjectValue() instanceof List) {
			List<?> positions = (List<?>) evt.getObjectValue();
			for (int i = 0; i < positions.size(); i++) {
				if (positions.get(i) instanceof Position) {
					Position pos = (Position) positions.get(i);
					if (world.getTerritory(pos).hasFleet() && (world.getTerritory(pos).controlledBy() == getCurrentPlayer())) {
						for (Fleet fleet : world.getTerritory(pos).getFleets()) {
							if (!fleet.hasColonizer()) {
								addSelectedFleet(fleet, pos);
							}
						}
					}
				}
			}
		}
		if (!selectedFleets.isEmpty()) {
			EventBus.INSTANCE.publish(new Event(Event.EventTag.SHOW_FLEETMENU, Collections.unmodifiableSet(selectedFleets)));
		}
	}
	private void addSelectedFleet(Fleet fleet, Position pos){
		selectedFleets.add(fleet);
		if (!fleetPaths.containsKey(fleet)) {
			fleetPaths.put(fleet, new Path(pos));
		}
	}

	private void queueShip(ShipType shipType) {
		for (Position pos : world.getContentPositions()) {
			if (world.getTerritory(pos).hasColony()) {
				if (world.getTerritory(pos).getColony() == selectedColony) {
					world.addToBuildQueue(shipType, GameManager.INSTANCE.getCurrentPlayer(), pos);
				}
			}
		}
	}
	/*
	 * Resets all the instance variables.
	 */
	private void resetVariables() {
		selectedFleets.clear();
		selectedColony = null;
		Event event = new Event(Event.EventTag.HIDE_MENU, null);
		EventBus.INSTANCE.publish(event);
	}

}
