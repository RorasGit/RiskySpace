package riskyspace.logic;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import riskyspace.model.Colony;
import riskyspace.model.Fleet;
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
	private Player currentPlayer;

	private Set<Fleet> selectedFleets = new HashSet<Fleet>();
	private Map<Fleet, Path> fleetPaths = new HashMap<Fleet, Path>();

	public ViewEventController(World world) {
		this.world = world;
		EventBus.INSTANCE.addHandler(this);
	}

	@Override
	public void performEvent(Event evt) {
		if (!FleetMove.isMoving()) {
			if (evt.getTag() == Event.EventTag.NEW_FLEET_SELECTION) {
				resetVariables(); // Reset all selections as we make a new selection
				if (evt.getObjectValue() instanceof Position) {
					Position pos = (Position) evt.getObjectValue();
					if (lastFleetSelectPos == null || !lastFleetSelectPos.equals(pos)) {
						lastFleetSelectPos = pos;
						fleetSelectionIndex = 0;
					}
					if (world.getTerritory(pos).hasFleet() && (world.getTerritory(pos).controlledBy() == currentPlayer)) {
						if (world.getTerritory(pos).getFleets().size() != world.getTerritory(pos).shipCount(ShipType.COLONIZER)) {
							Fleet fleet;
							do {
								fleet = world.getTerritory(pos).getFleet(fleetSelectionIndex);
								fleetSelectionIndex = (fleetSelectionIndex + 1) % world.getTerritory(pos).getFleets().size();
							} while(fleet.hasColonizer());
							selectedFleets.add(fleet);
							fleetPaths.put(fleet, new Path(pos));
							Event event = new Event(Event.EventTag.SHOW_FLEETMENU, Collections.unmodifiableSet(selectedFleets));
							EventBus.INSTANCE.publish(event);
							Sound.playSound("select.wav");
						}
					}
				}
			}

			if (evt.getTag() == Event.EventTag.ADD_FLEET_SELECTION) {
				selectedColony = null;
				if (selectedFleets.isEmpty()) {
					Event event = new Event(Event.EventTag.HIDE_MENU, null);
					EventBus.INSTANCE.publish(event);
				}
				if (evt.getObjectValue() instanceof Position) {
					Position pos = (Position) evt.getObjectValue();
					if (lastFleetSelectPos == null || !lastFleetSelectPos.equals(pos)) {
						lastFleetSelectPos = pos;
						fleetSelectionIndex = 0;
					}
					if (world.getTerritory(pos).hasFleet() && (world.getTerritory(pos).controlledBy() == currentPlayer)) {
						/*
						 * Check that there are other ships than colonizers
						 */
						if (world.getTerritory(pos).getFleets().size() != world.getTerritory(pos).shipCount(ShipType.COLONIZER)) {
							Fleet fleet;
							do {
								fleet = world.getTerritory(pos).getFleet(fleetSelectionIndex);
								fleetSelectionIndex = (fleetSelectionIndex + 1) % world.getTerritory(pos).getFleets().size();
							} while(fleet.hasColonizer());
							selectedFleets.add(fleet);
							fleetPaths.put(fleet, new Path(pos));
							Event event = new Event(Event.EventTag.SHOW_FLEETMENU, Collections.unmodifiableSet(selectedFleets));
							EventBus.INSTANCE.publish(event);
						}
					}
				}
			}

			if (evt.getTag() == Event.EventTag.COLONIZER_SELECTED) {
				resetVariables();
				if (evt.getObjectValue() instanceof Position) {
					Position pos = (Position) evt.getObjectValue();
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
						Event event = new Event(Event.EventTag.SHOW_FLEETMENU, Collections.unmodifiableSet(selectedFleets));
						EventBus.INSTANCE.publish(event);
					}
				}
			}

			if (evt.getTag() == Event.EventTag.COLONIZE_PLANET) {
				if (evt.getObjectValue() instanceof Position) {
					Position pos = (Position) evt.getObjectValue();
					if (world.getTerritory(pos).hasFleet() && world.getTerritory(pos).hasPlanet() && !world.getTerritory(pos).hasColony()) {
						for (Fleet fleet : world.getTerritory(pos).getFleets()) {
							if (fleet.hasColonizer()) {
								fleet.useColonizer();
								world.getTerritory(pos).getPlanet().buildColony(fleet.getOwner());
								EventText et = new EventText("Colony built", pos);
								EventBus.INSTANCE.publish(new Event(Event.EventTag.EVENT_TEXT, et));
								break; // Stop looping through fleets.
							}
						}
					}
				}
			}

			if (evt.getTag() == Event.EventTag.SET_PATH) {
				Position target = (Position) evt.getObjectValue();
				for (Fleet fleet : selectedFleets) {
					if (currentPlayer == fleet.getOwner()) {
						fleetPaths.get(fleet).setTarget(target);
					}
				}
				Event event = new Event(Event.EventTag.PATHS_UPDATED, null);
				EventBus.INSTANCE.publish(event);
			}

			if (evt.getTag() == Event.EventTag.BUILD_SHIP) {
				queueShip((ShipType) evt.getObjectValue());
			}

			if (evt.getTag() == Event.EventTag.PERFORM_MOVES) {
				fleetSelectionIndex = 0;
				FleetMove.move(world, fleetPaths, currentPlayer);
			}
		}
		
		if (evt.getTag() == Event.EventTag.ACTIVE_PLAYER_CHANGED) {
			resetVariables();
			lastFleetSelectPos = null;
			fleetSelectionIndex = 0;
			currentPlayer = (Player) evt.getObjectValue();
		}

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

		if(evt.getTag() == Event.EventTag.COLONY_SELECTED) {
			resetVariables();
			Territory selectedTerritory = world.getTerritory((Position) evt.getObjectValue());
			if (selectedTerritory.hasColony()) {
				selectedColony = selectedTerritory.getColony();
				if (selectedColony.getOwner() == currentPlayer) {
					Event mEvent = new Event(Event.EventTag.SHOW_MENU, selectedColony);
					EventBus.INSTANCE.publish(mEvent);
				} else {
					selectedColony = null;
				}
			}
		}

		if (evt.getTag() == Event.EventTag.SHIP_MENU) {
			Event mEvent = new Event(Event.EventTag.SHOW_RECRUITMENU, selectedColony);
			EventBus.INSTANCE.publish(mEvent);
		}
		
		if (evt.getTag() == Event.EventTag.MOVES_COMPLETE) {
			for (Position pos : world.getContentPositions()) {
				Territory terr = world.getTerritory(pos);
				if (terr.hasConflict()) {
					String battleString = Battle.doBattle(terr);
					EventText et = new EventText(battleString, pos);
					Event event = new Event(Event.EventTag.EVENT_TEXT, et);
					EventBus.INSTANCE.publish(event);
				}
			}
		}
		
		if (evt.getTag() == Event.EventTag.FLEET_REMOVED) {
			fleetPaths.remove((Fleet) evt.getObjectValue());
		}
		
		if (evt.getTag() == Event.EventTag.INTERRUPT_MOVES) {
			FleetMove.interrupt();
		}
		
		if (evt.getTag() == Event.EventTag.DESELECT) {
			resetVariables();
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

	private synchronized void queueShip(ShipType shipType) {
		for (Position pos : world.getContentPositions()) {
			if (world.getTerritory(pos).hasColony()) {
				if (world.getTerritory(pos).getColony() == selectedColony) {
					int metal = world.getResources(currentPlayer,
							Resource.METAL);
					int gas = world.getResources(currentPlayer, Resource.GAS);
					if (metal >= shipType.getMetalCost() && gas >= shipType.getGasCost()) {
						world.getTerritory(pos).addFleet(new Fleet(new Ship(shipType), world.getTerritory(pos).getColony().getOwner()));
						world.useResource(currentPlayer, Resource.METAL, shipType.getMetalCost());
						world.useResource(currentPlayer, Resource.GAS, shipType.getGasCost());
						EventText et = new EventText(shipType.toString().toLowerCase() + " built!", pos);
						Event event = new Event(Event.EventTag.EVENT_TEXT, et);
						EventBus.INSTANCE.publish(event);
					} else {
						/*
						 * Placeholder for grey buttons
						 */
						EventText et = new EventText("Not enough resources " + metal + " M " + gas + " G", pos);
						Event event = new Event(Event.EventTag.EVENT_TEXT, et);
						EventBus.INSTANCE.publish(event);
					}
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
