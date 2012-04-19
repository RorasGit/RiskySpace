package riskyspace.model;

import java.util.ArrayList;
import java.util.List;

public enum ShipType {
	/*
	 * Declaration order important for compareTo
	 * DO NOT CHANGE!
	 */
	//		    Dmg Var  HP Atk  Init  E  Metal  Gas  BldTime
	COLONIZER	( 1, 1,  38, 1,   0,   2,  200,   0,    1), // Kollonajser
	SCOUT		( 5, 3,  12, 1,   3,   4,   50,   0,    1), // Skauwt
	HUNTER		(11, 3,  38, 1,   2,   3,  120,  20,    1), // Hannter
	DESTROYER	(11, 3, 100, 3,   1,   3,  400, 100,    2), // Dezztrojjer
	MAGIKARP	( 0, 0,   1, 0,   0,   0, 7000, 500,    2); // FOR THE AWESOMENESS
	
	private final int damage;
	private final int variation;
	private final int shield;
	private final int energy;
	private final int initiative;
	private final int nbrAttacks;
	private final int metalCost;
	private final int gasCost;
	private final int buildTime;
	
	private ShipType(int damage, int variation, int shield, int nbrAttacks, int initiative, int energy,
			int metalCost, int gasCost, int buildTime) {
		this.damage = damage;
		this.variation = variation;
		this.shield = shield;
		this.energy = energy;
		this.initiative = initiative;
		this.nbrAttacks = nbrAttacks;
		this.metalCost = metalCost;
		this.gasCost = gasCost;
		this.buildTime = buildTime;
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
	
	public int getVariation() {
		return variation;
	}
	
	public int getBaseDamage() {
		return damage;
	}
	
	public int getShield() {
		return shield;
	}
	
	public int getEnergy() {
		return energy;
	}
	
	public int getInitiative() {
		return initiative;
	}
	
	public int getGasCost() {
		return gasCost;
	}
	
	public int getMetalCost() {
		return metalCost;
	}
	
	public int getBuildTime() {
		return buildTime;
	}
}