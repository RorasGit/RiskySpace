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
	
>>>>>>> THEIRS
}
