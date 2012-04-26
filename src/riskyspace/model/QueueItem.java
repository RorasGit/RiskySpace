package riskyspace.model;

public class QueueItem {
	
	private int buildTime;
	private Position pos;
	private Object item;
	
	public QueueItem (Object object, Position pos, int buildTime) {
		this.item = object;
		this.pos = pos;
		this.buildTime = buildTime;
	}
	
	public Object getItem() {
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
