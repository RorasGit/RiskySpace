package riskyspace.view.swingImpl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import riskyspace.model.World;
import riskyspace.view.camera.Camera;
import riskyspace.view.camera.CameraController;

public class RenderArea extends JPanel {

	private static final long serialVersionUID = 8209691542499926289L;
	
	/**
	 * Extra space at the Right and Left sides
	 * of the screen.
	 */
	private static final int EXTRA_SPACE_RL = 3;
	
	/**
	 * Extra space at the Top and Bottom sides
	 * of the screen.
	 */
	private static final int EXTRA_SPACE_TB = 2;
	
	private final World world;
	private Camera[] cameras = null;
	private CameraController cc = null;
	private List<Point> stars = null;

	
	/*
	 * Screen measures
	 */
	private int width;
	private int height;
	private int squareSize;
	
	public RenderArea(World world) {
		measureScreen();
		this.world = world;
		setStars();
		initCameras();
		// start camera Thread soonish
	}
	
	public void setStars() {
		stars = new ArrayList<Point>();
		for (int i = 0; i < stars.size(); i++) {
			int x = (int) (Math.random()*(world.getCols()+2*EXTRA_SPACE_RL));
			int y = (int) (Math.random()*(world.getRows()+2*EXTRA_SPACE_TB));
			stars.add(new Point(x, y));
		}
	}
	
	/*
	 * TODO: Listener for swapping cameras??
	 */
	
	private void initCameras() {
		cameras = new Camera[2];
		cameras[0] = new Camera();
		cameras[1] = new Camera();
//		cc = new CameraController();
//		cc.setCamera(cameras[0]);
	}
	
	/*
	 * Measure the screen and set the squareSize
	 */
	public void measureScreen() {
		width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		squareSize = Math.min(width/6,height/6);
	}
	
	public void render() {
		Graphics g = getGraphics();
		// Draw black background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
		/*
		 * Translate with cameras
		 */
		
		// Draw stars
		drawStars(g);
		
		// Draw grid
		drawGrid(g);
		
		// Draw Planets
		
		
		// Draw Paths
		/*
		 * Multiple path saved in some move handler class?
		 * example: Map<Fleet, Path>
		 * TODO:
		 */
		
		// Draw Fleets
	}

	/*
	 * Draw methods
	 */
	
	private void drawGrid(Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		/*
		 * Draw Horizontal lines
		 */
		for (int row = 0; row < world.getRows(); row++) {
			int x1 = (EXTRA_SPACE_RL) * squareSize;
			int y1 = (EXTRA_SPACE_TB + row) * squareSize;
			int x2 = (EXTRA_SPACE_RL + world.getCols()) * squareSize;
			int y2 = (EXTRA_SPACE_TB + row) * squareSize;
			g.drawLine(x1, y1, x2, y2);
		}
		/*
		 * Draw Vertical lines
		 */
		for (int col = 0; col < world.getCols(); col++) {
			int x1 = (EXTRA_SPACE_RL + col) * squareSize;
			int y1 = (EXTRA_SPACE_TB) * squareSize;
			int x2 = (EXTRA_SPACE_RL + col) * squareSize;
			int y2 = (EXTRA_SPACE_TB + world.getRows()) * squareSize;
			g.drawLine(x1, y1, x2, y2);
		}
	}

	private void drawStars(Graphics g) {
		g.setColor(Color.WHITE);
		for (int i = 0; i < stars.size(); i++) {
			g.fillRect(stars.get(i).x, stars.get(i).y, 1, 1);
		}
	}
}
