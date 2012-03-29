package riskyspace.services;

import riskyspace.model.Player;
/**
 * @deprecated 
 */
public class ViewEvent {

	/*
	 * The different events that can occur.
	 */
	public enum EventTag {
		FLEET_SELECTED,
		FLEET_MOVED,
		COLONY_SELECTED,
		DESELECT,
		AFTER_BATTLE,//Is this really a View Event??
		NEXT_TURN, 
		BUILD_SHIP;
		
		//TODO: add all events that can occur.
	}
	
	//The tag of the Event sent to the view.
	private final EventTag tag;
	
	//The value of the object sent to the view.
	private final Object objectValue;
	
	//The "owner" of the object responsible for the event.
	private final Player player;
	
	public ViewEvent(EventTag tag, Object objectValue, Player player) {
		this.tag = tag;
		this.objectValue = objectValue;
		this.player = player;
	}
	
	public EventTag getTag() {
		return tag;
	}
	
	public Object getObjectValue() {
		return objectValue;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	@Override
    public String toString() {
        return "ViewEvent [tag=" + tag + ", value=" + objectValue + ", player=" + player + "]";
    } 
}