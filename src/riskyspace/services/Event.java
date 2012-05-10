package riskyspace.services;

import java.io.Serializable;

public class Event implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4505609665084471020L;

	/*
	 * The different events that can occur.
	 */
	public enum EventTag {
		/*
		 * Model triggered events
		 */
		INCOME_CHANGED,
		STATS_CHANGED,
		/*
		 * View triggered events
		 */
		NEW_FLEET_SELECTION,
		ADD_FLEET_SELECTION,
		SET_PATH,
		MOVE,
		PLANET_SELECTED,
		COLONIZER_SELECTED,
		COLONIZE_PLANET,
		DESELECT,
		QUEUE_SHIP,
		SHIP_SELFDESTCRUCT,
		NEXT_TURN,
		/*
		 * Controller triggered events
		 */
		MOVES_COMPLETE,
		EVENT_TEXT,
		ACTIVE_PLAYER_CHANGED,
		UPDATE_SPRITEDATA,
		SELECTION,
		COLONIZER_PRESENT,
		/*
		 * Initiate new Player's View events
		 */
		INIT_ROWS, 
		INIT_COLS, 
		INIT_PLAYER;
		
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
