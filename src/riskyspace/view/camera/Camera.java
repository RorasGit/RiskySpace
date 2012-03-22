package riskyspace.view.camera;



public class Camera {
	
	enum Direction {
		UP, DOWN, LEFT, RIGHT;
	}
	
	private float x;
	private float y;
	
	/*
	 * The change value for x and y whenever moveCamera() is called.
	 */
	private float diff;
	
	public Camera(float x, float y, float diff) {
		this.x = x;
		this.y = y;
		this.diff = diff;
	}
	
	public Camera() {
		this(0 , 0 , 0.01f);
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
}
