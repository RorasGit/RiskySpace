package riskyspace.model;

import java.util.List;

import riskyspace.model.building.Hangar;
import riskyspace.model.building.Mine;
import riskyspace.model.building.Radar;
import riskyspace.model.building.Turret;

public class Colony implements BattleAble, Sight {
	
	private Player owner = null;
	private String colonyName = null;
	private Resource resource = null;
	
	private Mine mine = null;
	private Radar radar = null;
	private Turret turret = null;
	private Hangar hangar = null;
	
	public Colony (Resource resource, Player owner) {
		this.owner = owner;
		this.resource = resource;
		if (owner == Player.BLUE) {
			colonyName = "Atlantis";
		} else {
			colonyName = "Gargaloo";
		}
		mine = new Mine(resource);
		radar = new Radar();
		turret = new Turret();
		hangar = new Hangar();
	}
	
	public int getIncome() {
		return mine.mine();
	}
	
	public Player getOwner() {
		return owner;
	}
	
	public String getName() {
		return colonyName;
	}

	@Override
	public boolean takeDamage(int damage) {
		return turret.takeDamage(damage);
	}

	@Override
	public List<Integer> getAttacks() {
		return turret.getAttacks();
	}

	@Override
	public int getSightRange() {
		return radar.getSightRange();
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		} else if (other == null || this.getClass() != other.getClass()) {
			return false;
		} else {
			Colony otherColony = (Colony) other;
			return this.owner == otherColony.owner && this.mine.equals(otherColony.mine) && this.radar.equals(otherColony.radar)
					&& this.turret.equals(otherColony.turret) && this.hangar.equals(otherColony.hangar);
		}
	}
	
	@Override
	public String toString() {
		return "Colony [" + owner + "]";
	}
	
	@Override
	public int hashCode() {
		return owner.hashCode()*13 + colonyName.hashCode()*7 + resource.hashCode()*17;
	}

	public void upgradeMine() {
		mine.upgrade();
	}
}