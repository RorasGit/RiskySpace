package demo;

import java.util.ArrayList;
import java.util.List;

public class Territory {
	private List<Fleet> fleets = null;
	private Planet planet = null;
	
	public Territory() {
		fleets = new ArrayList<Fleet>();
	}
	
	public void addFleets(List<Fleet> fleets) {
		for (int i = 0; i < fleets.size(); i++) {
			this.fleets.add(fleets.get(i));
		}
	}
	
	/**
	 * Set this territory to have a planet if there is not one already.
	 * @param res The kind of Resource to be found the planet.
	 * @return true if a planet was created
	 */
	public boolean setPlanet(Resource res) {
		if (planet == null) {
			planet = new Planet(res);
			return true;
		} else {
			return false;
		}
	}
}
