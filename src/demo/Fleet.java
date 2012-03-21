package demo;

import java.util.ArrayList;
import java.util.List;

public class Fleet {
	
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
	
	public Fleet(List<Ship> ships, Player owner) {
		if (owner == null) {
			throw new IllegalArgumentException("Owner can not be null");
		}
		this.owner = owner;
		for (int i = 0; i < ships.size(); i++) {
			this.ships.add(ships.get(i));
		}
		id = nextId;
		nextId++;
	}
	
	public Fleet(Ship ship, Player owner) {
		if (owner == null) {
			throw new IllegalArgumentException("Owner can not be null");
		}
		this.owner = owner;
		ships.add(ship);
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
	
	/*
	 * DEAL DAMAGE TO SHIPS METHOD TODO:
	 */
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
	}
	
	public int shipCount(ShipType type) {
		int nbrOfType = 0;
		for (int i = 0; i < ships.size(); i++) {
			if (ships.get(i).getType() == type) {
				nbrOfType++;
			}
		}
		return nbrOfType;
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
