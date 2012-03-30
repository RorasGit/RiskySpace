package riskyspace.services;

import riskyspace.model.Position;

public class EventText {
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