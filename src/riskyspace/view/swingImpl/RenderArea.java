package riskyspace.view.swingImpl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import riskyspace.model.Player;
import riskyspace.model.Position;
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
	private Image hunter_blue = null;
	private Image destroyer_blue = null;
	private Image scout_red = null;
	private Image hunter_red = null;
	private Image destroyer_red = null;
	
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
		scout_blue = Toolkit.getDefaultToolkit().getImage("res/icons/blue/scout.png");
		hunter_blue = Toolkit.getDefaultToolkit().getImage("res/icons/blue/hunter.png");
		destroyer_blue = Toolkit.getDefaultToolkit().getImage("res/icons/blue/destroyer.png");
		scout_red = Toolkit.getDefaultToolkit().getImage("res/icons/red/scout.png");
		hunter_red = Toolkit.getDefaultToolkit().getImage("res/icons/red/hunter.png");
		destroyer_red = Toolkit.getDefaultToolkit().getImage("res/icons/red/destroyer.png");
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
		
		// Draw Planets
		for (Position pos : world.getContentPositions()) {
			if (world.getTerritory(pos).hasPlanet()) {
				if (world.getTerritory(pos).hasColony()) {
					g.setColor(world.getTerritory(pos).getColony().getOwner() == Player.BLUE ?
							Color.BLUE : Color.RED);
					g.fillOval((int) ((EXTRA_SPACE_HORIZONTAL + pos.getCol() - 0.5) * squareSize - 2),
							(int) ((EXTRA_SPACE_VERTICAL + pos.getRow() - 1) * squareSize + 2),
							squareSize/2 - 2, squareSize/2 - 2);
				}
				g.drawImage(planet,	(int) ((EXTRA_SPACE_HORIZONTAL + pos.getCol() - 0.5) * squareSize - 2),
						(int) ((EXTRA_SPACE_VERTICAL + pos.getRow() - 1) * squareSize + 1), null);
			}
		}
		
		// Draw Paths
		/*
		 * Multiple path saved in some move handler class?
		 * example: Map<Fleet, Path>
		 * TODO:
		 */
		
		for (Position pos : world.getContentPositions()) {
			if (world.getTerritory(pos).hasFleet()) {
				Player controller = world.getTerritory(pos).controlledBy();
				Image image = null;
				image = controller == Player.BLUE ? scout_blue : scout_red;
				g.drawImage(image, (int) ((EXTRA_SPACE_HORIZONTAL + pos.getCol() - 0.75) * squareSize) - image.getWidth(null)/2,
						(int) ((EXTRA_SPACE_VERTICAL + pos.getRow() - 0.25) * squareSize) - image.getWidth(null)/2, null);
			}
		}
	}

	/*
	 * 
	 */
	public boolean menuClick(Point point) {
//		if (!menu.isActive()) {
//			return false;
//		} else {
			return true;
//		}
	}
	
	public boolean shipClick(Point point) {
		return false;
	}

	public boolean colonyClick(Point point) {
		return false;
	}
	
	class ClickHandler implements MouseListener {
		@Override public void mousePressed(MouseEvent me) {
			/*
			 * Check each level of interaction in order.
			 */
			if (menuClick(me.getPoint())) {return;}
			if (shipClick(me.getPoint())) {return;}
			if (colonyClick(me.getPoint())) {return;}
			else {
				/*
				 * Click was not in any trigger zone
				 * Call deselect.
				 */
			}
		}		
		@Override public void mouseClicked(MouseEvent me) {}
		@Override public void mouseEntered(MouseEvent me) {}
		@Override public void mouseExited(MouseEvent me) {}
		@Override public void mouseReleased(MouseEvent me) {}
	}
}