package battledemo;

public class Ship {
	private int firepower, variation;
	private int shield;
	
	public Ship(int firepower, int variation, int shield) {
		this.firepower = firepower;
		this.variation = variation;
		this.shield = shield;
	}
	
	public int fire() {
		return firepower + (int)(Math.random()*(variation+1));
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
}
