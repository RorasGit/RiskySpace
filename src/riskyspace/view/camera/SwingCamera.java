package riskyspace.view.camera;

/**
 * View camera used in the awt coordinate system.
 * @author Alexander Hederstaf
 *
 */
public class SwingCamera {
	
	enum Direction {
		UP, DOWN, LEFT, RIGHT;
	}
	
	private float x;
	private float y;
	
	/*
	 * The change value for x and y whenever moveCamera() is called.
	 */
	private float diff;
	
	public SwingCamera(float x, float y, float diff) {
		this.x = x;
		this.y = y;
		this.diff = diff;
	}
	
	public SwingCamera(float x, float y) {
		this(x, y, 0.005f);
	}
	
	public SwingCamera() {
		this(0 , 0 , 0.005f);
	}
	
	/**
	 * Move the coordinates for the camera.
	 * @param dir = the direction of the camera movement.
	 */
	public void moveCamera(Direction dir) {
		if (dir == Direction.UP) {
			y = Math.max(y - diff, 0.0f);
		} else if (dir == Direction.DOWN) {
			y = Math.min(y + diff, 1.0f);
		} else if (dir == Direction.RIGHT) {
			x = Math.min(x + diff, 1.0f);
		} else if (dir == Direction.LEFT) {
			x = Math.max(x - diff, 0.0f);
		} 
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setDiff(float diff) {
		this.diff = diff;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public float getDiff() {
		return diff;
	}
}
