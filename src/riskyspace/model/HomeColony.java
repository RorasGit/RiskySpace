package riskyspace.model;

import riskyspace.model.building.Ranked;

public class HomeColony extends Colony {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7409590473246616859L;

	public HomeColony(Resource resource, Player owner) {
		super(resource, owner);
		// Level 2 Mine
		Ranked m = getMine();
		m.upgrade();
		
		// Level 2 Turret
		Ranked t = getTurret();
		t.upgrade();
		
		// Level 2 Hangar
		Ranked h = getHangar();
		h.upgrade();
		h.upgrade();
	}
	
	@Override
	public boolean isHomeColony() {
		return true;
	}
}