package riskyspace.model;

import java.util.ArrayList;
import java.util.List;

public class Fleet {
	
	private boolean hasColonizer = false;
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
	public Fleet(List<Fleet> fleets) {
		if (fleets.isEmpty()) {
			throw new IllegalArgumentException("No Fleets sent");
		}
		this.owner = fleets.get(0).getOwner();
		for (int i = 0; i < fleets.size(); i++) {
			if (fleets.get(i).getOwner() != owner) {
				throw new IllegalArgumentException("The Fleets can not have different owners");
			}
			ships.addAll(fleets.get(i).ships);
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
		if (hasColonizer) {
			for (int i = 0; i < fleetSize(); i++) {
				if (ships.get(i).getType().equals(ShipType.COLONIZER)) {
					/*
					 * When Colonizer is found remove and stop the loop
					 */
					ships.remove(i);
					setFlagship();
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
	
	private void setFlagship() {
		if (ships.size() == 0) {
			return;
		}
		flagship = null;
		int hp = 0;
		for (int i = 0; i < ships.size(); i++) {
			if (ships.get(i).getType().getShield() > hp && ships.get(i).getType() != ShipType.COLONIZER) {
				flagship = ships.get(i).getType();
				hp = ships.get(i).getType().getShield();
			}
			if (ships.get(i).getType() == ShipType.COLONIZER) {
				hasColonizer = true;
			}
		}
	}
	
	public ShipType getFlagship() {
		return flagship;
	}
	
	public boolean hasColonizer() {
		return hasColonizer;
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
}
