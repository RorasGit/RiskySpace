package riskyspace.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Fleet implements MoveAble, Sight, Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5624695760395662299L;
	private ShipType flagship = null;
	private Player owner = null;
	private List<Ship> ships = new ArrayList<Ship>();
	
	/**
	 * This Fleet id
	 */
	private int id = 0;
	
	/**
	 * Static save of id every time a new Fleet is created
	 */
	private static int nextId = 0;

	/**
	 * Create a Fleet with a List of Ships.
	 * @param ships The Fleets Ships
	 * @param owner The owner of the Fleet
	 */
	public Fleet(List<Ship> ships, Player owner) {
		if (owner == null) {
			throw new IllegalArgumentException("Owner can not be null");
		}
		this.owner = owner;
		this.ships.addAll(ships);
		if (fleetSize() == 0) {
			throw new IllegalArgumentException("Can not create empty fleet");
		}
		setFlagship();
		id = nextId;
		nextId++;
	}
	
	/**
	 * Create a Fleet with a Single Ship.
	 * @param ship The Fleets Ship
	 * @param owner The owner of the Fleet
	 */
	public Fleet(Ship ship, Player owner) {
		if (owner == null) {
			throw new IllegalArgumentException("Owner can not be null");
		}
		this.owner = owner;
		ships.add(ship);
		flagship = ship.getType();
		id = nextId;
		nextId++;
	}
	
	/**
	 * Create a Fleet by merging a List of other Fleets.
	 * @param fleets The List of Fleets to merge.
	 */
	public Fleet(Collection<Fleet> fleets) {
		if (fleets.isEmpty()) {
			throw new IllegalArgumentException("No Fleets sent");
		}
		this.owner = fleets.iterator().next().getOwner();
		Iterator<Fleet> it = fleets.iterator();
		while (it.hasNext()) {
			Fleet fleet = it.next();
			if (fleet.getOwner() != owner) {
				throw new IllegalArgumentException("The Fleets can not have different owners");
			}
			ships.addAll(fleet.ships);
		}
		if (fleetSize() == 0) {
			throw new IllegalArgumentException("Can not create empty Fleet");
		}
		setFlagship();
		id = nextId;
		nextId++;
	}
	
	public List<Integer> getAttacks(int initiative) {
		List<Integer> attacks = new ArrayList<Integer>();
		for (int i = 0; i < ships.size(); i++) {
			if (ships.get(i).getInitiative() == initiative) {
				attacks.addAll(ships.get(i).getAttacks());
			}
		}
		return attacks;
	} 
	
	/**
	 * Attack this Fleet with attacks at target indexes.
	 * @param attacks List of Integers of size n to represent each attack.
	 * @param targetIndexes List of Integers of size n to represent each attacks target.
	 * @throws IllegalArgumentException If the size of both lists are not the same.
	 */
	public void takeDamage(List<Integer> attacks, List<Integer> targetIndexes) {
		if (attacks.size() != targetIndexes.size()) {
			throw new IllegalArgumentException("Number of attacks must be equal to number of targets");
		}
		List<Ship> destroyedShips = new ArrayList<Ship>();
		for (int i = 0; i < targetIndexes.size(); i++) {
			if (targetIndexes.get(i) >= 0 && targetIndexes.get(i) < ships.size()) {
				if (ships.get(targetIndexes.get(i)).takeDamage(attacks.get(i))) {
					destroyedShips.add(ships.get(targetIndexes.get(i)));
				}
			} else {
				throw new IllegalArgumentException("targetIndex " + targetIndexes.get(i) 
						+ " at index " + i + " doesn't represent a ship");
			}
		}
		ships.removeAll(destroyedShips);
		setFlagship();
	}
	
	public boolean useColonizer() {
		if (hasColonizer()) {
			for (int i = 0; i < fleetSize(); i++) {
				if (ships.get(i).getType().equals(ShipType.COLONIZER)) {
					/*
					 * When Colonizer is found remove and stop the loop
					 */
					ships.remove(i);
					break;
				}
			}
			return true;
		} else {
			return false;
		}
	}
	
	public int shipCount(ShipType type) {
		int nbrOfType = 0;
		for (int i = 0; i < fleetSize(); i++) {
			if (ships.get(i).getType() == type) {
				nbrOfType++;
			}
		}
		return nbrOfType;
	}
	
	/*
	 * This sets the flagship, which is the ShipType that will represent the fleet graphically on the map.
	 */
	private void setFlagship() {
		flagship = null;
		for (int i = 0; i < ships.size(); i++) {
			if (flagship == null || flagship.compareTo(ships.get(i).getType()) < 0) {
				flagship = ships.get(i).getType();
			}
		}
	}
	
	public ShipType getFlagship() {
		return flagship;
	}
	
	public boolean hasColonizer() {
		return shipCount(ShipType.COLONIZER) > 0;
	}
	
	public void reset() {
		for (int i = 0; i < ships.size(); i++) {
			ships.get(i).reset();
		}
	}

	public Player getOwner() {
		return owner;
	}

	public int fleetSize() {
		return ships.size();
	}
	
	public int supplyCost() {
		int supply = 0;
		for (int i = 0; i < fleetSize(); i++) {
			supply += ships.get(i).getType().getSupplyCost();
		}
		return supply;
	}
	
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		} else if (other == null || this.getClass() != other.getClass()) {
			return false;
		} else {
			return id == ((Fleet) other).id;
		}
	}
	
	@Override
	public int hashCode() {
		return 7*id;
	}
	
	@Override
	public String toString() {
		return "Fleet [size=" + fleetSize() +", flagship=" + flagship + "]";
	}

	@Override
	public boolean hasEnergy() {
		for (Ship ship : ships) {
			if (!ship.hasEnergy()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int getEnergy() {
		int minEnergy = 4;
		for (Ship ship : ships) {
			minEnergy = Math.min(minEnergy, ship.getEnergy());
		}
		return minEnergy;
	}

	@Override
	public boolean useEnergy() {
		if (hasEnergy()) {
			for (Ship ship : ships) {
				ship.useEnergy();
			}
			return true;
		}
		return false;
	}

	@Override
	public int getSightRange() {
		int sight = 0;
		for (Ship ship : ships) {
			sight = Math.max(sight, ship.getType().getSightRange());
		}
		return sight;
	}
}