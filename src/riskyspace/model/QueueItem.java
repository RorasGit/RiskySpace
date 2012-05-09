package riskyspace.model;

import java.io.Serializable;

public class QueueItem implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3514012858339042796L;
	private int buildTime;
	private Position pos;
	private BuildAble item;
	
	public QueueItem (BuildAble object, Position pos, int buildTime) {
		this.item = object;
		this.pos = pos;
		this.buildTime = buildTime;
	}
	
	public BuildAble getItem() {
		return item;
	}
	
	public Position getPosition() {
		return pos;
	}
	
	public int getBuildTime() {
		return buildTime;
	}
	
	public void subtractBuildTime() {
		buildTime -= 1;
	}
}
