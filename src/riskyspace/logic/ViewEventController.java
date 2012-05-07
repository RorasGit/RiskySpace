package riskyspace.logic;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import riskyspace.GameManager;
import riskyspace.model.Colony;
import riskyspace.model.Fleet;
import riskyspace.model.Planet;
import riskyspace.model.Player;
import riskyspace.model.Position;
import riskyspace.model.Resource;
import riskyspace.model.Ship;
import riskyspace.model.ShipType;
import riskyspace.model.Territory;
import riskyspace.model.World;
import riskyspace.services.Event;
import riskyspace.services.EventBus;
import riskyspace.services.EventHandler;
import riskyspace.services.EventText;
import riskyspace.sound.Sound;

public class ViewEventController implements EventHandler {

	private World world = null;
	private int fleetSelectionIndex = 0;
	private Position lastFleetSelectPos = null;
	private Colony selectedColony = null;
	private BattleStats battleStats;

	private Set<Fleet> selectedFleets = new HashSet<Fleet>();
	private Map<Fleet, Path> fleetPaths = new HashMap<Fleet, Path>();

	public ViewEventController(World world) {
		this.world = world;
		EventBus.INSTANCE.addHandler(this);
	}

	@Override
	public void performEvent(Event evt) {
		
		if (evt.getTag() == Event.EventTag.INCOME_CHANGED) {
			Player affectedPlayer = (Player) evt.getObjectValue();
			int metalIncome = 10;
			int gasIncome = 0;
			for (Position pos : world.getContentPositions()) {
				Territory terr = world.getTerritory(pos);
				if (terr.hasColony()) {
					if (terr.getColony().getOwner() == affectedPlayer) {
						if (terr.getPlanet().getType() == Resource.METAL) {
							metalIncome += terr.getColony().getIncome();
						} else if (terr.getPlanet().getType() == Resource.GAS) {
							gasIncome += terr.getColony().getIncome();
						}
					}
				}
			}
			world.setIncome(affectedPlayer, Resource.METAL, metalIncome);
			world.setIncome(affectedPlayer, Resource.GAS, gasIncome);
		}
		
		if (evt.getTag() == Event.EventTag.MERGE_FLEET) {
			//TODO: make one fleet of the list of fleets selected.
		}
		
		if (evt.getTag() == Event.EventTag.SPLIT_FLEET) {
			//TODO: make two different fleets of the selected fleet.
		}

		if(evt.getTag() == Event.EventTag.PLANET_SELECTED) {
			resetVariables();
			Territory selectedTerritory = world.getTerritory((Position) evt.getObjectValue());
			if (selectedTerritory.hasColony()) {
				selectedColony = selectedTerritory.getColony();
				if (selectedColony.getOwner() == GameManager.INSTANCE.getCurrentPlayer()) {
					Event mEvent = new Event(Event.EventTag.SHOW_MENU, selectedColony);
					EventBus.INSTANCE.publish(mEvent);
				} else {
					selectedColony = null;
				}
			} else if (selectedTerritory.hasPlanet()) {
				Event mEvent = new Event(Event.EventTag.SHOW_PLANETMENU, (Territory) selectedTerritory);
				EventBus.INSTANCE.publish(mEvent);
				if (selectedTerritory.hasColonizer()) {
					mEvent = new Event(Event.EventTag.COLONIZER_PRESENT, selectedTerritory);
					EventBus.INSTANCE.publish(mEvent);
				}
			}
		}

		if (evt.getTag() == Event.EventTag.SHIP_MENU) {
			Event mEvent = new Event(Event.EventTag.SHOW_RECRUITMENU, selectedColony);
			EventBus.INSTANCE.publish(mEvent);
		}
		
		if (evt.getTag() == Event.EventTag.BACK) {
			Event mEvent = new Event(Event.EventTag.SHOW_MENU, selectedColony);
			EventBus.INSTANCE.publish(mEvent);
		}
		
		if (evt.getTag() == Event.EventTag.MOVES_COMPLETE) {
			for (Position pos : world.getContentPositions()) {
				Territory terr = world.getTerritory(pos);
				if (terr.hasConflict()) {
					battleStats = Battle.doBattle(terr);
					for (Fleet f : battleStats.getDestroyedFleets()) {
						fleetPaths.remove(f);
					}
					EventText et = new EventText(battleStats.getWinnerString(), pos);
					Event event = new Event(Event.EventTag.EVENT_TEXT, et);
					EventBus.INSTANCE.publish(event);
					if (battleStats.isColonyDestroyed()) {
						event = new Event(Event.EventTag.COLONY_REMOVED, pos);
					}
				}
			}
		}
		
		
		if (evt.getTag() == Event.EventTag.INTERRUPT_MOVES) {
			FleetMove.interrupt();
		}
		
		if (evt.getTag() == Event.EventTag.DESELECT) {
			resetVariables();
		}
		
		/*
		 * TODO: Removes ships, but does so in an uncontrolled manner and needs to be redone somehow.
		 * 		 It's in other words unfinished...
		 */
		if (evt.getTag() == Event.EventTag.SHIP_SELFDESTCRUCT) {
			if (world.getTerritory(lastFleetSelectPos).hasFleet() && fleetSelectionIndex >=0) {
				selectedFleets.remove(world.getTerritory(lastFleetSelectPos).getFleet(fleetSelectionIndex));
				world.getTerritory(lastFleetSelectPos).removeFleet(world.getTerritory(lastFleetSelectPos).getFleet(fleetSelectionIndex));
				System.out.println(lastFleetSelectPos);
				System.out.println(world.getTerritory(lastFleetSelectPos).getFleets().size());
				if (!world.getTerritory(lastFleetSelectPos).hasFleet()) {
					resetVariables();
				} else {
					fleetSelectionIndex = Math.max(fleetSelectionIndex - 1, 0);
					Event event = new Event(Event.EventTag.SHOW_FLEETMENU, Collections.unmodifiableSet(selectedFleets));
					EventBus.INSTANCE.publish(event);
				}
			}
		}
	}
	
	public Position[][] getPaths(Player player) {
		Position[][] tmp = new Position[fleetPaths.size()][];
		int i = 0;
		for (Fleet fleet : fleetPaths.keySet()) {
			if (fleet.getOwner() == player) {
				tmp[i] = fleetPaths.get(fleet).getPositions();
				i++;
			}
		}
		Position[][] paths = new Position[i][];
		for (int j = 0; j < paths.length; j++) {
			paths[j] = tmp[j];
		}
		return paths;
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
