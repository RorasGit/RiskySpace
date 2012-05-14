package riskyspace.view.camera;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;

/**
 * Camera controller that takes one Camera argument and adjusts this Camera 
 * when the mouse is at the border of the screen.
 * @author Alexander Hederstaf
 *
 */
public class CameraController extends Thread {
	
	private static final long SLEEP_TIME = 1000/60;
	private SwingCamera currentCamera;
	private double maximumXValue, maximumYValue;
	
	public void setCamera(SwingCamera cam) {
		currentCamera = cam;
		maximumXValue = Toolkit.getDefaultToolkit().getScreenSize().getWidth()-1;
		maximumYValue = Toolkit.getDefaultToolkit().getScreenSize().getHeight()-1;
	}
	
	@Override
	public void run() {
		while(!isInterrupted()) {
			Point loc = MouseInfo.getPointerInfo().getLocation();
			if (loc.x >= maximumXValue) {
				currentCamera.moveCamera(SwingCamera.Direction.RIGHT);
				
			} else if (loc.x <= 0) {
				currentCamera.moveCamera(SwingCamera.Direction.LEFT);
			}
			if (loc.y >= maximumYValue) {
				currentCamera.moveCamera(SwingCamera.Direction.DOWN);
			} else if (loc.y <= 0) {
				currentCamera.moveCamera(SwingCamera.Direction.UP);
			}
			try {
				sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				
			}
		}
	}

}
