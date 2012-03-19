package demo;

import java.util.ArrayList;
import java.util.List;

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