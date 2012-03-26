package riskyspace.view.swingImpl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import riskyspace.model.Player;
import riskyspace.model.Position;
import riskyspace.model.Territory;
import riskyspace.model.World;
import riskyspace.view.camera.Camera;
import riskyspace.view.camera.CameraController;

public class RenderArea extends JPanel {

	private static final long serialVersionUID = 8209691542499926289L;
	
	/**
	 * Extra space at the Right and Left sides
	 * of the screen.
	 */
	private static final int EXTRA_SPACE_HORIZONTAL = 3;
	
	/**
	 * Extra space at the Top and Bottom sides
	 * of the screen.
	 */
	private static final int EXTRA_SPACE_VERTICAL = 2;
	
	private final World world;
	private Map<Player, Camera> cameras = null;
	private CameraController cc = null;
	private List<Point> stars = null;
	private Camera currentCamera = null;
	
	/*
	 * Screen measures
	 */
	private int width;
	private int height;
	private int squareSize;
	
	/*
	 * BufferedImages
	 */
	private BufferedImage background = null;
	
	/*
	 * Textures
	 */
	private Image planet = null;
	private Image scout_blue = null;
	private Image scout_red = null;
	
	public RenderArea(World world) {
		this.world = world;
		measureScreen();
		setTextures();
		setStars();
		createBackground();
		initCameras();
		cc.start();
	}
	
	private void setTextures() {
		planet = Toolkit.getDefaultToolkit().getImage("res/planet.png").getScaledInstance(64, 64, Image.SCALE_DEFAULT);
		scout_red = Toolkit.getDefaultToolkit().getImage("res/scout_red.png");
		scout_blue = Toolkit.getDefaultToolkit().getImage("res/scout_blue.png");
	}
	
	private void createBackground() {
		int totalWidth = (world.getCols() + 2*EXTRA_SPACE_HORIZONTAL)*squareSize;
		int totalHeight = (world.getRows() + 2*EXTRA_SPACE_VERTICAL)*squareSize;
		background = new BufferedImage(totalWidth, totalHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2D = background.createGraphics();
		
		g2D.setColor(Color.BLACK);
		g2D.fillRect(0, 0, totalWidth, totalHeight);
		
		g2D.setColor(Color.LIGHT_GRAY);
		/*
		 * Draw Horizontal lines
		 */
		for (int row = 0; row <= world.getRows(); row++) {
			int x1 = (EXTRA_SPACE_HORIZONTAL) * squareSize;
			int y1 = (EXTRA_SPACE_VERTICAL + row) * squareSize;
			int x2 = (EXTRA_SPACE_HORIZONTAL + world.getCols()) * squareSize;
			int y2 = (EXTRA_SPACE_VERTICAL + row) * squareSize;
			g2D.drawLine(x1, y1, x2, y2);
		}
		/*
		 * Draw Vertical lines
		 */
		for (int col = 0; col <= world.getCols(); col++) {
			int x1 = (EXTRA_SPACE_HORIZONTAL + col) * squareSize;
			int y1 = (EXTRA_SPACE_VERTICAL) * squareSize;
			int x2 = (EXTRA_SPACE_HORIZONTAL + col) * squareSize;
			int y2 = (EXTRA_SPACE_VERTICAL + world.getRows()) * squareSize;
			g2D.drawLine(x1, y1, x2, y2);
		}
		
		/*
		 * Draw stars
		 */
		g2D.setColor(Color.WHITE);
		for (int i = 0; i < stars.size(); i++) {
			g2D.fillRect(stars.get(i).x, stars.get(i).y, 1, 1);
		}
	}

	public void setStars() {
		stars = new ArrayList<Point>();
		for (int i = 0; i < 5000; i++) {
			int x = (int) (Math.random()*(world.getCols()+2*EXTRA_SPACE_HORIZONTAL)*squareSize);
			int y = (int) (Math.random()*(world.getRows()+2*EXTRA_SPACE_VERTICAL)*squareSize);
			stars.add(new Point(x, y));
		}
	}
	
	private void initCameras() {
		cameras = new HashMap<Player, Camera>();
		cameras.put(Player.BLUE, new Camera());
		cameras.put(Player.RED, new Camera());
		currentCamera = cameras.get(Player.BLUE);
		cc = new CameraController();
		cc.setCamera(currentCamera);
	}
	
	/*
	 * Measure the screen and set the squareSize
	 */
	public void measureScreen() {
		width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		squareSize = Math.min(width/6,height/6);
	}
	
	public void setPlayer(Player player) {
		currentCamera = cameras.get(player);
		cc.setCamera(currentCamera);
	}
	
	public void paintComponent(Graphics g) {
		/*
		 * Translate with cameras
		 */
		int xTrans = -(int) (((world.getCols()+2*EXTRA_SPACE_HORIZONTAL)*squareSize - width)*currentCamera.getX());
		int yTrans = -(int) (((world.getRows()+2*EXTRA_SPACE_VERTICAL)*squareSize - height)*currentCamera.getY());
		g.translate(xTrans, yTrans);
		
		// Draw background
		g.drawImage(background, 0, 0, null);
		
		Map<Position, Territory> terr = world.getTerritories();
		// Draw Planets
		for (int row = 1; row <= world.getRows(); row++) {
			for (int col = 1; col <= world.getCols(); col++) {
				if (terr.get(new Position(row, col)).hasPlanet()) {
					if (terr.get(new Position(row, col)).hasColony()) {
						g.setColor(terr.get(new Position(row, col)).getColony().getOwner() == Player.BLUE ?
								Color.BLUE : Color.RED);
						g.fillOval((int) ((EXTRA_SPACE_HORIZONTAL + col - 0.5) * squareSize - 2),
								(int) ((EXTRA_SPACE_VERTICAL + row - 1) * squareSize + 2),
								squareSize/2 - 2, squareSize/2 - 2);
					}
					g.drawImage(planet,	(int) ((EXTRA_SPACE_HORIZONTAL + col - 0.5) * squareSize - 2),
							(int) ((EXTRA_SPACE_VERTICAL + row - 1) * squareSize + 1), null);
				}
			}
		}
		
		// Draw Paths
		/*
		 * Multiple path saved in some move handler class?
		 * example: Map<Fleet, Path>
		 * TODO:
		 */
		
		// Draw Fleets (Scouts)
		for (int row = 1; row <= world.getRows(); row++) {
			for (int col = 1; col <= world.getCols(); col++) {
				if (terr.get(new Position(row, col)).hasFleet()) {
					Player controller = terr.get(new Position(row, col)).controlledBy();
					g.setColor(controller == Player.BLUE ? Color.BLUE : Color.RED);
					g.drawOval((int) ((EXTRA_SPACE_HORIZONTAL + col - 1) * squareSize + 2),
							(int) ((EXTRA_SPACE_VERTICAL + row - 0.5) * squareSize - 2),
							squareSize/2, squareSize/2);
					g.drawImage(controller == Player.BLUE ? scout_blue : scout_red,	
							(int) ((EXTRA_SPACE_HORIZONTAL + col - 1) * squareSize + 1),
							(int) ((EXTRA_SPACE_VERTICAL + row - 0.5) * squareSize - 1), null);
				}
			}
		}
	}
}