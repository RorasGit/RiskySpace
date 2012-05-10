package riskyspace.services;

import java.io.Serializable;

import riskyspace.model.Position;

public class EventText implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3181348773628999176L;
	
	private String text;
	private Position pos;
	private int times = 0;
	public EventText(String text, Position pos) {
		this.text = text;
		this.pos = pos;
	}
	public String getText() {
		return text;
	}
	public Position getPos() {
		return pos;
	}
	public int getTimes() {
		return times;
	}
	public void incTimes() {
		times++;
	}
	
	@Override
	public String toString() {
		return "Message: " + text + " Pos: " + pos;
	}
}