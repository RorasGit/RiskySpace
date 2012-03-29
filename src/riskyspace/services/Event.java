package riskyspace.services;

public class Event {

	/*
	 * The different events that can occur.
	 */
	public enum EventTag {
		/*
		 * Model triggered events
		 */
		TERRITORY_CHANGED,
		RESOURCE_CHANGED,
		INCOME_CHANGED,
		/*
		 * View triggered events
		 */
		NEW_FLEET_SELECTION,
		ADD_FLEET_SELECTION,
		SET_PATH,
		PERFORM_MOVES, 
		COLONY_SELECTED,
		DESELECT,
		SHIP_MENU,
		BUILD_SCOUT,
		BUILD_HUNTER,
		BUILD_DESTROYER,
		BUILD_COLONIZER,
		NEXT_TURN,
		/*
		 * Controller triggered events
		 */		
		SHOW_MENU,
		SHOW_RECRUITMENU,
		HIDE_MENU,
		NEW_TURN;
		
		//TODO: add all events that can occur.
	}
	
	//The tag of the Event sent to the model.
	private final EventTag tag;
	
	//The value of the object sent to the model.
	private final Object objectValue;
	
	public Event(EventTag tag, Object objectValue) {
		this.tag = tag;
		this.objectValue = objectValue;
	}
	
	public EventTag getTag() {
		return tag;
	}
	
	public Object getObjectValue() {
		return objectValue;
	}
	
	
	@Override
    public String toString() {
        return "Event [tag=" + tag + ", value=" + objectValue + "]";
    } 
    
}
