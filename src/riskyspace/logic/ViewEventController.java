package riskyspace.logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import riskyspace.model.Colony;
import riskyspace.model.Fleet;
import riskyspace.model.Position;
import riskyspace.model.Ship;
import riskyspace.model.ShipType;
import riskyspace.model.Territory;
import riskyspace.model.World;
import riskyspace.services.Event;
import riskyspace.services.EventBus;
import riskyspace.services.EventHandler;

public class ViewEventController implements EventHandler {
	
	private World world = null;
	private Set<Fleet> selectedFleets = new HashSet<Fleet>();
	private int fleetSelectionIndex = 0;
	private Position lastFleetSelectPos = null;
	private Colony selectedColony = null;
	
	private Map<Fleet, Path> fleetPaths = new HashMap<Fleet, Path>();
	
	/*TODO 
	 * sätt Path med bara "startnod" (Spara startnod serparat som currentPos?)
	 * lägg till targets stegvis (om shift)
	 * spara alla selectedships med en Path i Map (ger även önskad hashSet effekt)
	 * lägg bara till targets om det finns energi
	 */
	
	public ViewEventController(World world) {
		this.world = world;
		EventBus.INSTANCE.addHandler(this);
	}

	@Override
	public void performEvent(Event evt) {
		if (evt.getTag() == Event.EventTag.NEW_FLEET_SELECTION) {
			if (!FleetMove.isMoving()) {
				resetVariables(); // Reset all selections as we make a new selection
				if(evt.getObjectValue() instanceof Position) {
					Position pos = (Position) evt.getObjectValue();
					if (lastFleetSelectPos == null || !lastFleetSelectPos.equals(pos)) {
						lastFleetSelectPos = pos;
						fleetSelectionIndex = 0;
					}
					if (world.getTerritory(pos).hasFleet()) {
						Fleet fleet = world.getTerritory(pos).getFleet(fleetSelectionIndex); // Change this value somehow
						selectedFleets.add(fleet);
						fleetPaths.put(fleet, new Path(pos));
						fleetSelectionIndex = (fleetSelectionIndex + 1) % world.getTerritory(pos).getFleets().size();
					}
				}
			}
		}
		
		if (evt.getTag() == Event.EventTag.ADD_FLEET_SELECTION) {
			if (!FleetMove.isMoving()) {
				selectedColony = null;
				Event event = new Event(Event.EventTag.HIDE_MENU, null);
				EventBus.INSTANCE.publish(event);
				if(evt.getObjectValue() instanceof Position) {
					Position pos = (Position) evt.getObjectValue();
					if (lastFleetSelectPos == null || !lastFleetSelectPos.equals(pos)) {
						lastFleetSelectPos = pos;
						fleetSelectionIndex = 0;
					}
					if (world.getTerritory(pos).hasFleet()) {
						Fleet fleet = world.getTerritory(pos).getFleet(fleetSelectionIndex); // Change this value somehow
						selectedFleets.add(fleet);
						fleetPaths.put(fleet, new Path(pos));
						fleetSelectionIndex = (fleetSelectionIndex + 1) % world.getTerritory(pos).getFleets().size();
					}
				}
			}
		}

		if (evt.getTag() == Event.EventTag.SET_PATH) {
			Position target = (Position) evt.getObjectValue();
			for(Fleet fleet : selectedFleets) {
				fleetPaths.get(fleet).setTarget(target);
			}
		}
		
		if(evt.getTag() == Event.EventTag.COLONY_SELECTED) {
			resetVariables();
			Territory selectedTerritory = world.getTerritory((Position) evt.getObjectValue());
			if(selectedTerritory.hasColony()) {
				selectedColony = selectedTerritory.getColony();
				Event mEvent = new Event(Event.EventTag.SHOW_MENU, selectedColony);
				EventBus.INSTANCE.publish(mEvent);
			}
		}

		if(evt.getTag() == Event.EventTag.SHIP_MENU) {
			Event mEvent = new Event(Event.EventTag.SHOW_RECRUITMENU, selectedColony);
			EventBus.INSTANCE.publish(mEvent);
		}
		
		
		if(evt.getTag() == Event.EventTag.BUILD_SCOUT) {
			Position buildPos = null;
			for (Position pos : world.getContentPositions()) {
				if (world.getTerritory(pos).hasColony()) {
					if (world.getTerritory(pos).getColony().equals(selectedColony)) {
						/* TODO:
						 * Check that this position is already not queued
						 * Use resources if possible, else break?
						 * TEST:
						 * Builds a new Destroyer to change the picture!
						 * --Should be sent as event somehow something and then
						 * --queued and build when called from new turn thingy!
						 */
						System.out.println("Built at: " + pos);
						world.getTerritory(pos).addFleet(new Fleet(new Ship(ShipType.SCOUT), world.getTerritory(pos).getColony().getOwner()));
						buildPos = pos;
					}
				}
			}
		}
		
		if(evt.getTag() == Event.EventTag.BUILD_HUNTER) {
			Position buildPos = null;
			for (Position pos : world.getContentPositions()) {
				if (world.getTerritory(pos).hasColony()) {
					if (world.getTerritory(pos).getColony().equals(selectedColony)) {
						/* TODO:
						 * Check that this position is already not queued
						 * Use resources if possible, else break?
						 * TEST:
						 * Builds a new Destroyer to change the picture!
						 * --Should be sent as event somehow something and then
						 * --queued and build when called from new turn thingy!
						 */
						System.out.println("Built at: " + pos);
						world.getTerritory(pos).addFleet(new Fleet(new Ship(ShipType.HUNTER), world.getTerritory(pos).getColony().getOwner()));
						buildPos = pos;
					}
				}
			}
		}
		
		if(evt.getTag() == Event.EventTag.BUILD_DESTROYER) {
			Position buildPos = null;
			for (Position pos : world.getContentPositions()) {
				if (world.getTerritory(pos).hasColony()) {
					if (world.getTerritory(pos).getColony().equals(selectedColony)) {
						/* TODO:
						 * Check that this position is already not queued
						 * Use resources if possible, else break?
						 * TEST:
						 * Builds a new Destroyer to change the picture!
						 * --Should be sent as event somehow something and then
						 * --queued and build when called from new turn thingy!
						 */
						System.out.println("Built at: " + pos);
						world.getTerritory(pos).addFleet(new Fleet(new Ship(ShipType.DESTROYER), world.getTerritory(pos).getColony().getOwner()));
						buildPos = pos;
					}
				}
			}
		}
		if (evt.getTag() == Event.EventTag.PERFORM_MOVES) {
			fleetSelectionIndex = 0;
			FleetMove.move(world, fleetPaths);
		}
		
		if (evt.getTag() == Event.EventTag.INTERRUPT_MOVES) {
			FleetMove.interrupt();
		}
		
		if(evt.getTag() == Event.EventTag.BUILD_COLONIZER) {
			Position buildPos = null;
			for (Position pos : world.getContentPositions()) {
				if (world.getTerritory(pos).hasColony()) {
					if (world.getTerritory(pos).getColony().equals(selectedColony)) {
						/* TODO:
						 * Check that this position is already not queued
						 * Use resources if possible, else break?
						 * TEST:
						 * Builds a new Destroyer to change the picture!
						 * --Should be sent as event somehow something and then
						 * --queued and build when called from new turn thingy!
						 */
						System.out.println("Built at: " + pos);
						world.getTerritory(pos).addFleet(new Fleet(new Ship(ShipType.COLONIZER), world.getTerritory(pos).getColony().getOwner()));
						buildPos = pos;
					}
				}
			}
		}
		
		if (evt.getTag() == Event.EventTag.DESELECT) {
			resetVariables();
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
