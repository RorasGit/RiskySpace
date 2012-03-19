package demo;

public class Ship {
	
	private ShipType type = null;
	private int shield = 0;
	private int energy = 0;
	
	public Ship (ShipType type) {
		this.type = type;
		reset();
	}

	public void reset() {
		this.shield = type.getShield();
		this.energy = type.getEnergy();
	}

	public ShipType getType() {
		return type;
	}	
}