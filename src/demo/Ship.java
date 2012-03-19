package demo;

import java.util.List;

public class Ship implements BattleAble {
	
	private ShipType type = null;
	private int shield = 0;
	private int energy = 0;
	
	public Ship (ShipType type) {
		this.type = type;
		reset();
	}

	public ShipType getType() {
		return type;
	}
	
	public int getInitiative() {
		return type.getInitiative();
	}

	public void reset() {
		this.shield = type.getShield();
		this.energy = type.getEnergy();
	}

	@Override
	public boolean takeDamage(int damage) {
		shield -= damage;
		return shield <= 0;
	}

	@Override
	public List<Integer> getAttacks() {
		return type.getDamage();
	}
	
	/*
	 * boolean has enough energy (int energy) 
	 */
	
	public boolean useEnergy(int energy) {
		if (this.energy >= energy) {
			this.energy -= energy;
			return true;
		} else {
			return false;
		}
	}
}