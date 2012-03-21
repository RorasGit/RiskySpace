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
	
	public boolean hasPlanet() {
		return planet != null;
	}
	
	public Planet getPlanet() {
		return planet;
	}
	
	public boolean hasColony() {
		if (!hasPlanet()) {
			return false;
		} else {
			return getPlanet().hasColony();
		}
	}
	
	public Colony getColony() {
		if (!hasColony()) {
			return null;
		} else {
			return getPlanet().getColony();
		}
	}
	
	public boolean hasFleet() {
		return !fleets.isEmpty();
	}
	
	public Fleet getFleet(int index) {
		return fleets.get(index);
	}
	
	public List<Fleet> getFleets() {
		return fleets;
	}
	
	public Player controlledBy() {
		if (!hasFleet() && (!hasPlanet() || !getPlanet().hasColony())) {
			return Player.WORLD;
		} else {
			if (hasPlanet()) {
				if (getPlanet().hasColony()) {
					return getPlanet().controlledBy();
				}
			}
			if (hasFleet()) {
				// if not-several-players
				return getFleet(0).getOwner();
			}
		}
		return null;
	}
}