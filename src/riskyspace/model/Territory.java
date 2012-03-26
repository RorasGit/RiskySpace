package riskyspace.model;

import java.util.ArrayList;
import java.util.List;

public class Territory {
	private List<Fleet> fleets = null;
	private Planet planet = null;
	
	
	/**
	 * This territory�s id
	 */
	private int id = 0;
	
	/**
	 * Static save of the territory�s id whenever a new planet is created.
	 */
	private static int nextId = 0;
	
	public Territory() {
		fleets = new ArrayList<Fleet>();
		id = nextId;
		nextId++;
	}
	public void addFleet(Fleet fleet){
		this.fleets.add(fleet);
	}
	public void addFleets(List<Fleet> fleets) {
		for (int i = 0; i < fleets.size(); i++) {
			this.fleets.add(fleets.get(i));
		}
	}
	public void removeFleet(Fleet fleet){
		this.removeFleet(fleet);
	}

	public void removeFleets(List<Fleet> destroyedFleets) {
		fleets.removeAll(destroyedFleets);
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
				if (hasColony()) {
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
	public boolean isEmpty(){
		return !this.hasPlanet() && !this.hasFleet();
	}
	
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		} else if (other == null || this.getClass() != other.getClass()) {
			return false;
		} else {
			Territory otherTerritory = (Territory) other;
			return (otherTerritory.id == this.id);
		}
	}
	
	@Override
	public String toString() {
		return "[" + (hasPlanet()? planet.toString() : "No Planet present in this zone") 
				+ ", " + (hasFleet()? "Fleets present: " + fleets.size() : "No fleets present in this zone") + "]";
	}
	
	@Override
	public int hashCode() {
		return id*17;
	}
}