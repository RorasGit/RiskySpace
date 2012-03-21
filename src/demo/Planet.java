package demo;

public class Planet {

	private Resource type = null;
	private Colony colony = null;
	
	/**
	 * This Planets id
	 */
	private int id = 0;
	
	/**
	 * Static save of planetId whenever a new Planet is created.
	 */
	private static int nextId = 0;
	
	/**
	 * 
	 * @param resource
	 */
	public Planet(Resource resource) {
		this.type = resource;
		id = nextId;
		nextId++;
	}
	
	public void buildColony(Player owner) {
		colony = new Colony(type, owner);
	}

	public void destroyColony() {
		colony = null;
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
	
	public Colony getColony() {
		return colony;
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
	
	@Override
	public int hashCode() {
		return id*3;
		
	}
}