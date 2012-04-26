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
		INCOME_CHANGED,
		COLONY_REMOVED,
		GAS_CHANGED,
		METAL_CHANGED,
		SUPPLY_CHANGED,
		
		/*
		 * View triggered events
		 */
		SPLIT_FLEET,
		MERGE_FLEET,
		NEW_FLEET_SELECTION,
		ADD_FLEET_SELECTION,
		SET_PATH,
		PERFORM_MOVES, 
		INTERRUPT_MOVES,
		PLANET_SELECTED,
		COLONY_SELECTED,
		COLONIZER_SELECTED,
		COLONIZE_PLANET,
		COLONIZER_PRESENT,
		DESELECT,
		SHIP_MENU,
		QUEUE_SHIP,
		SHIP_SELFDESTCRUCT,
		NEXT_TURN,
		/*
		 * Controller triggered events
		 */
		SHOW_MENU,
		SHOW_RECRUITMENU,
		SHOW_FLEETMENU,
		SHOW_PLANETMENU,
		BACK,
		MOVES_COMPLETE,
		HIDE_MENU,
		EVENT_TEXT,
		ACTIVE_PLAYER_CHANGED,
		PATHS_UPDATED;
		
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
