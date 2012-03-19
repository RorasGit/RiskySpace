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
	public List<Integer> getDamage() {
		return type.getDamage();
	}
}