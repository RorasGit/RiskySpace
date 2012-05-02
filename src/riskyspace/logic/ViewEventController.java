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
		if (!FleetMove.isMoving()) {
			if (evt.getTag() == Event.EventTag.NEW_FLEET_SELECTION) {
				resetVariables(); // Reset all selections as we make a new selection
				if (evt.getObjectValue() instanceof Position) {
					Position pos = (Position) evt.getObjectValue();
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
							selectedFleets.add(fleet);
							if (!fleetPaths.containsKey(fleet)) {
								fleetPaths.put(fleet, new Path(pos));
							}
						}
					}
				} else if (evt.getObjectValue() instanceof List) {
					List positions = (List) evt.getObjectValue();
					for (int i = 0; i < positions.size(); i++) {
						if (positions.get(i) instanceof Position) {
							Position pos = (Position) positions.get(i);
							if (world.getTerritory(pos).hasFleet() && (world.getTerritory(pos).controlledBy() == GameManager.INSTANCE.getCurrentPlayer())) {
								if (world.getTerritory(pos).getFleets().size() != world.getTerritory(pos).shipCount(ShipType.COLONIZER)) {
									for (Fleet fleet : world.getTerritory(pos).getFleets()) {
										if (!fleet.hasColonizer()) {
											selectedFleets.add(fleet);
											if (!fleetPaths.containsKey(fleet)) {
												fleetPaths.put(fleet, new Path(pos));
											}
										}
									}
								}
							}
						}
					}
				}
				if (!selectedFleets.isEmpty()) {
					Event event = new Event(Event.EventTag.SHOW_FLEETMENU, Collections.unmodifiableSet(selectedFleets));
					EventBus.INSTANCE.publish(event);
				}
			}

			if (evt.getTag() == Event.EventTag.ADD_FLEET_SELECTION) {
				selectedColony = null;
				if (selectedFleets.isEmpty()) {
					Event event = new Event(Event.EventTag.HIDE_MENU, null);
					EventBus.INSTANCE.publish(event);
				} else if (selectedFleets.iterator().next().hasColonizer()) {
					selectedFleets.clear();
					Event event = new Event(Event.EventTag.HIDE_MENU, null);
					EventBus.INSTANCE.publish(event);
				}
				if (evt.getObjectValue() instanceof Position) {
					Position pos = (Position) evt.getObjectValue();
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
			
			if (evt.getTag() == Event.EventTag.COLONY_REMOVED) {
				Position pos = (Position) evt.getObjectValue();
				world.getTerritory(pos).getPlanet().destroyColony();
				// TODO : use position to remove buildQueue
				
			}

			if (evt.getTag() == Event.EventTag.SET_PATH) {
				Position target = (Position) evt.getObjectValue();
				for (Fleet fleet : selectedFleets) {
					if (GameManager.INSTANCE.getCurrentPlayer() == fleet.getOwner()) {
						fleetPaths.get(fleet).setTarget(target);
					}
				}
				Event event = new Event(Event.EventTag.PATHS_UPDATED, null);
				EventBus.INSTANCE.publish(event);
			}

			if (evt.getTag() == Event.EventTag.QUEUE_SHIP) {
				queueShip((ShipType) evt.getObjectValue());
			}

			if (evt.getTag() == Event.EventTag.PERFORM_MOVES) {
				fleetSelectionIndex = 0;
				FleetMove.move(world, fleetPaths, GameManager.INSTANCE.getCurrentPlayer());
			}
		}
		
		if (evt.getTag() == Event.EventTag.ACTIVE_PLAYER_CHANGED) {
			resetVariables();
			lastFleetSelectPos = null;
			fleetSelectionIndex = 0;
			world.processBuildQueue(GameManager.INSTANCE.getCurrentPlayer());
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
