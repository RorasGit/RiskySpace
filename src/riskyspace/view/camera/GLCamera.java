package riskyspace.view.camera;

/**
 * View camera used in the openGL coordinate system.
 * @author Alexander Hederstaf
 *
 */
public class GLCamera extends SwingCamera {

	public GLCamera(float x, float y) {
		super(x, y, 0.004f);
	}
	
	@Override
	public void moveCamera(Direction dir) {
		if (dir == Direction.UP) {
			setY(Math.min(getY() + getDiff(), 1.0f));
		} else if (dir == Direction.DOWN) {
			setY(Math.max(getY() - getDiff(), 0.0f));
		} else if (dir == Direction.RIGHT) {
			setX(Math.min(getX() + getDiff(), 1.0f));
		} else if (dir == Direction.LEFT) {
			setX(Math.max(getX() - getDiff(), 0.0f));
		} 
	}
}