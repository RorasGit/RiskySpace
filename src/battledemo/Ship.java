package battledemo;

public class Ship {
	private int firepower, variation;
	private int shield;
	private final int initative;
	
	public Ship(int firepower, int variation, int shield, int initative) {
		this.firepower = firepower;
		this.variation = variation;
		this.shield = shield;
		this.initative = initative;
	}
	
	public int fire() {
		return firepower + (int)(Math.random()*(variation+1));
	}
	public int getInitative() {
		return initative;
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
