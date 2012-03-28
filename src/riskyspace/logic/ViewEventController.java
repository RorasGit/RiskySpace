package riskyspace.logic;

import java.util.ArrayList;
import java.util.List;

import riskyspace.model.Colony;
import riskyspace.model.Fleet;
import riskyspace.model.Territory;
import riskyspace.model.World;
import riskyspace.services.ViewEvent;
import riskyspace.services.ViewEventBus;
import riskyspace.services.ViewEventHandler;

public class ViewEventController implements ViewEventHandler {
	
	private World world = null;
	private List<Fleet> selectedFleets = new ArrayList<Fleet>();
	private Colony selectedColony = null;
	
	public ViewEventController(World world) {
		this.world = world;
		ViewEventBus.INSTANCE.addHandler(this);
	}

	@Override
	public void preformEvent(ViewEvent evt) {
		
		if (evt.getTag() == ViewEvent.EventTag.FLEET_SELECTED) {
			/*
			 * Clear old selections
			 */
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
		
		if (evt.getTag() == ViewEvent.EventTag.DESELECT) {
			resetVariables();
		}
		
		/*
		 * selects the fleet if the territory has one, otherwise the colony.
		 * if it has neither nothing is selected.
		 */
		if(evt.getTag() == ViewEvent.EventTag.TERRITORY_SELECTED) {
			resetVariables();
			Territory selectedTerritory = (Territory) evt.getObjectValue();
			if(selectedTerritory.hasFleet()) {
				for(int i=0; i<selectedTerritory.getFleets().size(); i++)
					selectedFleets.add(selectedTerritory.getFleets().get(i));
			} else if(selectedTerritory.hasColony()) {
				selectedColony = selectedTerritory.getColony();
			}
			
			//TODO: Draw / mark the selected area.
		}
		
		if(evt.getTag() == ViewEvent.EventTag.FLEET_MOVED) {
			/*
			 *  Move selectedFleets somehow....
			 */
		}
		
		if(evt.getTag() == ViewEvent.EventTag.AFTER_BATTLE) {
			/*
			 * Display battle results...
			 */
		}
	}
	
	/*
	 * Resets all the instance variables.
	 */
	private void resetVariables() {
		selectedFleets.clear();
		selectedColony = null;
	}
}
