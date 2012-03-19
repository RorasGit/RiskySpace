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
}