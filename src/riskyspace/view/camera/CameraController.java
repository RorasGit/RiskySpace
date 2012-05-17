package riskyspace.view.camera;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Camera controller that takes one Camera argument and adjusts this Camera 
 * when the mouse is at the border of the screen.
 * @author Alexander Hederstaf
 *
 */
public class CameraController extends Thread implements KeyListener {
	
	private static final long SLEEP_TIME = 1000/60;
	private Camera currentCamera;
	private double maximumXValue, maximumYValue;
	
	private boolean keyPressed = false;
	private Camera.Direction keyDirection = null;
	
	public void setCamera(Camera cam) {
		currentCamera = cam;
		maximumXValue = Toolkit.getDefaultToolkit().getScreenSize().getWidth()-1;
		maximumYValue = Toolkit.getDefaultToolkit().getScreenSize().getHeight()-1;
	}
	
	@Override
	public void run() {
		while(!isInterrupted()) {
			Point loc = MouseInfo.getPointerInfo().getLocation();
			if (keyPressed) {
				currentCamera.moveCamera(keyDirection);
			} else {
				if (loc.x >= maximumXValue) {
					currentCamera.moveCamera(Camera.Direction.RIGHT);
					
				} else if (loc.x <= 0) {
					currentCamera.moveCamera(Camera.Direction.LEFT);
				}
				if (loc.y >= maximumYValue) {
					currentCamera.moveCamera(Camera.Direction.DOWN);
				} else if (loc.y <= 0) {
					currentCamera.moveCamera(Camera.Direction.UP);
				}
			}
			try {
				sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				
			}
		}
	}

	@Override public void keyTyped(KeyEvent evt) {}
	@Override
	public void keyPressed(KeyEvent evt) {
		keyPressed = true;
		switch (evt.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			keyDirection = Camera.Direction.LEFT;
			break;
		case KeyEvent.VK_RIGHT:
			keyDirection = Camera.Direction.RIGHT;
			break;
		case KeyEvent.VK_UP:
			keyDirection = Camera.Direction.UP;
			break;
		case KeyEvent.VK_DOWN:
			keyDirection = Camera.Direction.DOWN;
			break;
		default:
			keyPressed = false;
			break;
		}
	}
	@Override 
	public void keyReleased(KeyEvent evt) {
		keyPressed = false;
	}
}