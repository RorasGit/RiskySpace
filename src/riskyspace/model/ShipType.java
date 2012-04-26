package riskyspace.model;

import java.util.ArrayList;
import java.util.List;

public enum ShipType implements BuildAble {
	/*
	 * Declaration order important for compareTo
	 * DO NOT CHANGE!
	 */
	//		    Dmg Var  HP Atk  Init  E  BuildTime Supply	Metal	Gas
	COLONIZER	( 1, 1,  38, 1,   0,   2,	1,		  0,	200,	0), // Kollonajser
	SCOUT		( 5, 3,  12, 1,   1,   4,	1,		  1,	50,		0), // Skauwt
	HUNTER		(11, 3,  38, 1,   1,   3,	1,		  1,	100,	30), // Hannter
	DESTROYER	(11, 3, 100, 3,   1,   3,	2,		  2,	350,	100); // Dezztrojjer
	
	private final int damage;
	private final int variation;
	private final int shield;
	private final int energy;
	private final int initiative;
	private final int nbrAttacks;
	private final int buildTime;
	private final int supplyCost;
	private final int metalCost;
	private final int gasCost;
	
	private ShipType(int damage, int variation, int shield, int nbrAttacks, int initiative, 
			int energy, int buildTime, int supplyCost, int metalCost, int gasCost) {
		this.damage = damage;
		this.variation = variation;
		this.shield = shield;
		this.energy = energy;
		this.initiative = initiative;
		this.nbrAttacks = nbrAttacks;
		this.buildTime = buildTime;
		this.supplyCost = supplyCost;
		this.metalCost = metalCost;
		this.gasCost = gasCost;
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

	@Override
	public int getSupplyCost() {
		return supplyCost;
	}

	@Override
	public int getMetalCost() {
		return metalCost;
	}

	@Override
	public int getGasCost() {
		return gasCost;
	}

	@Override
	public int getBuildTime() {
		return buildTime;
	}
}