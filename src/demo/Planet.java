package demo;

public class Planet {

	private Resource type = null;
	private Colony colony = null;
	
	public Planet(Resource resource) {
		this.type = resource;
	}
	
	public void buildColony(Player owner) {
		this.colony = new Colony(type, owner);
	}
	
	public boolean hasColony() {
		return colony != null;
	}
	
	public Player controlledBy() {
		if (hasColony()) {
			return colony.getOwner();
		} else {
			return Player.WORLD;
		}
	}
	
	public Resource getType() {
		return type;
	}
	
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		} else if (other == null || this.getClass() != other.getClass()) {
			return false;
		} else {
			Planet otherPlanet = (Planet) other;
			return (this.type == otherPlanet.type && this.colony == otherPlanet.colony);
		}
	}
	
	@Override
	public String toString() {
		return "[" + type + ", " + (this.hasColony()? colony.getOwner() : "Uninhabited") + "]";
	}
	
	public int hashCode() {
		return 0;
		
	}
}