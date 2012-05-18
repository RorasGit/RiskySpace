package riskyspace.model;

import java.io.Serializable;
import java.util.List;

import riskyspace.model.building.Hangar;
import riskyspace.model.building.Mine;
import riskyspace.model.building.Radar;
import riskyspace.model.building.Turret;

public class Colony implements BattleAble, Sight, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6914731084464404944L;
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
		colonyName = ColonyNames.getName(owner);
		mine = new Mine(resource);
		radar = new Radar();
		turret = new Turret();
		hangar = new Hangar();
	}
	
	public Mine getMine() {
		return mine;
	}
	
	public Turret getTurret() {
		return turret;
	}
	
	public Radar getRadar() {
		return radar;
	}
	
	public Hangar getHangar() {
		return hangar;
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
	
	/**
	 * Is this colony the home colony of any Player
	 * default value false, override in subclass
	 */
	public boolean isHomeColony() {
		return false;
	}
	
	public boolean canBuild(ShipType type) {
		if (isHomeColony() && type == ShipType.COLONIZER) {
			return true;
		}
		return getHangar().canBuild(type);
	}
	
	public boolean canRepair() {
		return getHangar().canRepair();
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
			return this.owner == otherColony.owner && this.colonyName.equals(otherColony.colonyName)
					&& this.resource == otherColony.resource;
		}
	}
	
	@Override
	public String toString() {
		return "Colony [" + owner 
				+ ", " + mine + "]" 
				+ ", " + turret + "]" 
				+ ", " + radar + "]" 
				+ ", " + hangar + "]" + "]";
	}
	
	@Override
	public int hashCode() {
		return owner.hashCode()*13 + colonyName.hashCode()*7 + resource.hashCode()*17;
	}
}