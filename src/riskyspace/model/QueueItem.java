package riskyspace.model;
	
public class QueueItem {
	
	private int buildTime;
	private BuildAble item;
	
	public QueueItem (BuildAble buildAble) {
		this.item = buildAble;
		this.buildTime = buildAble.getBuildTime();
	}
	
	public BuildAble getItem() {
		return item;
	}
	
	public int getBuildTime() {
		return buildTime;
	}
	
	public void subtractBuildTime(int time) {
		buildTime -= time;
	}
}
