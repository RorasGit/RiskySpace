package riskyspace.model;

import java.io.Serializable;

import riskyspace.GameManager;
import riskyspace.services.Event;
import riskyspace.services.EventBus;

public class Planet implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1929426414384910072L;

	/**
	 * This Planets Resource type
	 */
	private Resource type = null;
	
	/**
	 * This Planets Colony, set to null if there is no Colony.
	 */
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
	 * Creates a new Planet with one type of Resource.
	 * @param resource The kind of Resource to be found on the Planet.
	 */
	public Planet(Resource resource) {
		this.type = resource;
		id = nextId;
		nextId++;
	}

	
	/**
	 * Places a new Colony on this Planet.
	 * @param owner The Player that will own the Colony.
	 */
	public void buildColony(Player owner) {
		colony = new Colony(type, owner);
		Event evt = new Event(Event.EventTag.UPDATE_SPRITEDATA, null);
		EventBus.SERVER.publish(evt);
	}
	
	public void buildHomeColony(Player owner) {
		colony = new HomeColony(type, owner);
	}
	
	/**
	 * Removes the Colony on this Planet if there is one, otherwise it does nothing.
	 */
	public boolean destroyColony() {
		boolean wasHome = colony.isHomeColony();
		colony = null;
		Event evt = new Event(Event.EventTag.UPDATE_SPRITEDATA, null);
		EventBus.SERVER.publish(evt);
		return wasHome;
	}
	public boolean hasColony() {
		return colony != null;
	}
	
	/**
	 * Checks what Player owns the colony on this Planet
	 * and returns the value. If there is no Colony the 
	 * Planet is owned by Player.<code>WORLD</code>
	 * @return The Player controlling this Planet
	 */
	public Player controlledBy() {
		if (hasColony()) {
			return colony.getOwner();
		} else {
			return Player.WORLD;
		}
	}
	
	/**
	 * The Resource of this Planet
	 */
	public Resource getType() {
		return type;
	}
	
	/**
	 * The Colony on this Planet
	 * @return If there is a Colony on this Planet it is returned, else <code>null</code>.
	 */
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
		return "Planet [" + type + ", " + (this.hasColony()? colony.getOwner() : "Uninhabited") + "]";
	}
	
	@Override
	public int hashCode() {
		return id*3;
		
	}
}