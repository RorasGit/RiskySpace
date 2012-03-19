package demo;

import java.util.ArrayList;
import java.util.List;

public enum ShipType {
//			    Dmg Var HP  Atk  E
	SCOUT		( 5, 3,  12, 1,  4),
	HUNTER		(11, 3,  38, 1,  3),
	DESTROYER	(11, 3, 100, 3,  3),
	COLONIZER	( 1, 1,  38, 1,  2),
	MAGIKARP	( 0, 0,   1, 0,  0); //FOR THE AWESOME
	
	private final int damage;
	private final int variation;
	private final int shield;
	private final int energy;
	private final int nbrAttacks;
	
	private ShipType(int damage, int variation, int shield, int nbrAttacks, int energy) {
		this.damage = damage;
		this.variation = variation;
		this.shield = shield;
		this.energy = energy;
		this.nbrAttacks = nbrAttacks;
	}
	
	/*
	 * Return values may later be modified by a players Research.
	 */
	
	public List<Integer> getDamage() {
		List<Integer> damages = new ArrayList<Integer>();
		for (int i = 0; i < nbrAttacks; i++) {
			damages.add(damage + (int)(Math.random()*(variation+1)));
		}
		return damages;
	}
	
	public int getShield() {
		return shield;
	}
	
	public int getEnergy() {
		return energy;
	}
}