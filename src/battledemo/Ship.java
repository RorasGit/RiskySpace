package battledemo;

public class Ship {
	private int firepower, variation;
	private int shield;
	private int shieldMax;
	private final int initiative;
	protected ShipType shipType = null;
	protected int attacks = 1;
	
	public Ship(int firepower, int variation, int shield, int initiative) {
		this.firepower = firepower;
		this.variation = variation;
		this.shield = shield;
		this.shieldMax = shield;
		this.initiative = initiative;
	}
	
	public int fire() {
		return firepower + (int)(Math.random()*(variation+1));
	}
	public int getInitiative() {
		return initiative;
	}
	public int getAttacks() {
		return attacks;
	}
	/**
	 * 
	 * @param damage done to this ship
	 * @return true if the shields held!
	 */
	public boolean shield(int damage) {
		shield = shield - damage;
		return shield > 0;
	}

	public void reset() {
		this.shield = shieldMax;
	}
}
