package riskyspace.logic;

import java.util.List;
import java.util.ArrayList;

import riskyspace.model.Colony;
import riskyspace.model.Fleet;
import riskyspace.model.Planet;
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
			selectedFleets.clear();
			selectedColony = null;
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
			selectedFleets.clear();
			selectedColony = null;
		}
		
		if(evt.getTag() == ViewEvent.EventTag.TERRITORY_SELECTED) {
			Territory selectedTerritory = (Territory) evt.getObjectValue();
			if(selectedTerritory.hasColony()) {
				selectedColony = selectedTerritory.getColony();
			} else {
				// Can't be selected
			}
			
			//TODO: Draw / mark the selected area.
		}
	}
}