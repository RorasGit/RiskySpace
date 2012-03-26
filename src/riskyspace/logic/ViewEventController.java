package riskyspace.logic;

import java.util.List;
import java.util.ArrayList;

import riskyspace.model.Fleet;
import riskyspace.model.Planet;
import riskyspace.model.Territory;
import riskyspace.model.World;
import riskyspace.services.ViewEvent;
import riskyspace.services.ViewEventBus;
import riskyspace.services.ViewEventHandler;

public class ViewEventController implements ViewEventHandler {
	
	private World world;
	private List<Fleet> fleets = new ArrayList<Fleet>();
	private Planet selectedPlanet;
	
	public ViewEventController(World world) {
		this.world = world;
		ViewEventBus.INSTANCE.addHandler(this);
	}

	@Override
	public void preformEvent(ViewEvent evt) {
		
		if (evt.getTag() == ViewEvent.EventTag.FLEET_SELECTED) {
			// är det List<Fleet> eller Fleet
			if(evt.getObjectValue().getClass() == Fleet.class) {
				fleets.add((Fleet) evt.getObjectValue());
			} else {
				List<Fleet> list = (List<Fleet>) evt.getObjectValue();
				for(int i=0; i<list.size(); i++) {
					fleets.add(list.get(i));
				}
			}
			
			//TODO: Draw circle around the fleet that is selected.
			
		}
		
		if (evt.getTag() == ViewEvent.EventTag.DESELECT_UNIT) {
			fleets.clear();
			selectedPlanet = null;
		}
		
		if(evt.getTag() == ViewEvent.EventTag.TERRITORY_SELECTED) {
			Territory selectedTerritory = (Territory) evt.getObjectValue();
			if(selectedTerritory.hasFleet()) {
				fleets = selectedTerritory.getFleets();
			}
			if(selectedTerritory.hasPlanet()) {
				selectedPlanet = selectedTerritory.getPlanet();
			}
			
			//TODO: Draw / mark the selected area.
		}
		
	}

	
}
