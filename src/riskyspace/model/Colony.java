package riskyspace.model;

import java.util.ArrayList;
import java.util.List;

import riskyspace.services.Event;
import riskyspace.services.EventBus;

public class Colony implements BattleAble {
	
	private Player owner = null;
	private int income = 0;
	private String colonyName = null;
	/*
	 * Turret stats will be kept in a seperate class later.
	 */
	private int damage = 0;
	private int shield = 0;
	private int variation = 0;
	
	public Colony (Resource resource, Player owner) {
		this.owner = owner;
		if (resource == Resource.METAL) {
			income = 40;
		} else {
			income = 20;
		}
		constructTurret();
		/*
		 * Test name setter (Semi-Random the names later)
		 */
		if (owner == Player.BLUE) {
			colonyName = "Atlantis";
		} else {
			colonyName = "Gargaloo";
		}
	}
	
	public void constructTurret() {
		this.damage = 5;
		this.shield = 30;
		this.variation = 1;
	}
	
	public int getIncome() {
		return income;
	}

	@Override
	public boolean takeDamage(int damage) {
		shield -= damage;
		return shield <= 0;
	}

	@Override
	public List<Integer> getAttacks() {
		List<Integer> damage = new ArrayList<Integer>();
		damage.add(this.damage + (int) (Math.random()*(variation+1)));
		return damage;
	}

	public Player getOwner() {
		return owner;
	}
	
	public void resetShield() {
		shield = 30;
	}
	
	public String getName() {
		return colonyName;
	}
	
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		} else if (other == null || this.getClass() != other.getClass()) {
			return false;
		} else {
			Colony otherColony = (Colony) other;
			return (this.income == otherColony.income && this.owner == otherColony.owner);
		}
	}
	
	@Override
	public String toString() {
		return "Colony [" + owner + ", " + income + "]";
	}
	
	@Override
	public int hashCode() {
		return income*13;
	}
}