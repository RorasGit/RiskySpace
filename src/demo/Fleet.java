package demo;

import java.util.ArrayList;
import java.util.List;

import sun.org.mozilla.javascript.ast.FunctionNode.Form;

public class Fleet {
	
	private Player owner = null;
	private List<Ship> ships = null;
	
	public Fleet(List<Ship> ships, Player owner) {
		if (owner == null) {
			throw new IllegalArgumentException("Owner can not be null");
		}
		this.owner = owner;
		for (int i = 0; i < ships.size(); i++) {
			this.ships.add(ships.get(i));
		}
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
			if (ships.get(i).takeDamage(attacks.get(i))) {
				destroyedShips.add(ships.get(i));
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
}