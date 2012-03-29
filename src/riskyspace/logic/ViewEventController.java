package riskyspace.logic;

import java.util.ArrayList;
import java.util.List;

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
	private List<Fleet> selectedFleets = new ArrayList<Fleet>();
	private Colony selectedColony = null;
	
	public ViewEventController(World world) {
		this.world = world;
		EventBus.INSTANCE.addHandler(this);
	}

	@Override
	public void performEvent(Event evt) {
		if (evt.getTag() == Event.EventTag.FLEET_SELECTED) {
			resetVariables();
			/*
			 * Is it List<Fleet> or Fleet
			 */
			if(evt.getObjectValue().getClass() == Fleet.class) {
				selectedFleets.add((Fleet) evt.getObjectValue());
			} else {
				List<Fleet> list = (List<Fleet>) evt.getObjectValue();
				for(int i=0; i<list.size(); i++) {
					selectedFleets.add(list.get(i));
				}
			}
			//TODO: Send event Draw circle around the fleet that is selected.
			
		}
		
		if (evt.getTag() == Event.EventTag.DESELECT) {
			resetVariables();
		}
		
		/*
		 * selects the fleet if the territory has one, otherwise the colony.
		 * if it has neither nothing is selected.
		 */
		if(evt.getTag() == Event.EventTag.COLONY_SELECTED) {
			resetVariables();
			Territory selectedTerritory = world.getTerritory((Position) evt.getObjectValue());
			if(selectedTerritory.hasColony()) {
				selectedColony = selectedTerritory.getColony();
				Event mEvent = new Event(Event.EventTag.SHOW_MENU, selectedColony);
				EventBus.INSTANCE.publish(mEvent);
			}
		}


		if(evt.getTag() == Event.EventTag.BUILD_SHIP) {
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
