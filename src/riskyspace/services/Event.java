package riskyspace.services;

import java.io.Serializable;

import riskyspace.model.Player;
/**
 * 
 * @author Joachim von Hacht
 * @modified Alexander Hederstaf
 *
 */
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
		HOME_LOST,
		GAME_OVER,
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
		QUEUE_BUILDING,
		NEXT_TURN,
		SOUND,
		DISCONNECT,
		SAVE_GAME,
		SHOW_GAME_MENU,
		/*
		 * Controller triggered events
		 */
		MOVES_COMPLETE,
		EVENT_TEXT,
		ACTIVE_PLAYER_CHANGED,
		UPDATE_SPRITEDATA,
		SELECTION,
		COLONIZER_PRESENT,
		BUILDQUEUE_CHANGED,
		/*
		 * Initiate new Player's View events
		 */
		INIT_ROWS, 
		INIT_COLS, 
		INIT_PLAYER;
		
	}
	
	//The tag of the Event sent to the model.
	private final EventTag tag;
	
	//The value of the object sent to the model.
	private final Object objectValue;
	
	//The player connected to this event
	private Player player;

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
	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
	@Override
    public String toString() {
        return "Event [tag=" + tag + ", value=" + objectValue +", player="+ player +"]";
    } 
}
