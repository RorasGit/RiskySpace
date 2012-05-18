package riskyspace.view.opengl.impl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import riskyspace.logic.SpriteMapData;
import riskyspace.model.BuildAble;
import riskyspace.model.Colony;
import riskyspace.model.Fleet;
import riskyspace.model.Player;
import riskyspace.model.PlayerStats;
import riskyspace.model.Position;
import riskyspace.model.Territory;
import riskyspace.services.Event;
import riskyspace.services.EventBus;
import riskyspace.view.ViewResources;
import riskyspace.view.camera.Camera;
import riskyspace.view.camera.CameraController;
import riskyspace.view.camera.GLCamera;
import riskyspace.view.opengl.GLRenderAble;
import riskyspace.view.opengl.Rectangle;
import riskyspace.view.opengl.menu.GLColonyMenu;
import riskyspace.view.opengl.menu.GLFleetMenu;
import riskyspace.view.opengl.menu.GLPlanetMenu;
import riskyspace.view.opengl.menu.GLTopMenu;

import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

public class GLRenderArea implements GLRenderAble {
	
	/**
	 * Extra space at the Right and Left sides
	 * of the screen.
	 */
	public static final int EXTRA_SPACE_HORIZONTAL = 3;
	
	/**
	 * Extra space at the Top and Bottom sides
	 * of the screen.
	 */
	public static final int EXTRA_SPACE_VERTICAL = 2;
	
	private Rectangle screenArea = null;
	private int squareSize;
	private int totalWidth;
	private int totalHeight;
	private BufferedImage backgroundImage;
	private Texture backgroundTexture;
	
	/**
	 * Game size
	 */
	private int rows, cols;
	
	/*
	 * Cameras
	 */
	private Camera currentCamera = null;
	private Map<Player, Camera> cameras = null;
	private CameraController cc = null;
	
	/*
	 * Clickhandler to handle all clicks on the screen
	 */
	private ClickHandler clickHandler;
	
	/**
	 * The player viewing this renderArea
	 */
	private Player viewer = null;


	/*
	 * Sprites
	 */
	private GLSpriteMap sprites;
	
	/*
	 * Menus
	 */
	private GLColonyMenu colonyMenu = null;
	private GLPlanetMenu planetMenu = null;
	private GLFleetMenu fleetMenu = null;
	private GLTopMenu topMenu = null;

	private TextRenderer textRenderer;
	
	public GLRenderArea(int width, int height, int rows, int cols) {
		screenArea = new Rectangle(0, 0, width, height);
		squareSize = Math.min(width/6,height/6);
		totalWidth = (cols + 2*EXTRA_SPACE_HORIZONTAL)*squareSize;
		totalHeight = (rows + 2*EXTRA_SPACE_VERTICAL)*squareSize;
		this.rows = rows;
		this.cols = cols;
		initCameras();
		createBackground();
		createMenus();
		textRenderer = new TextRenderer(ViewResources.getFont().deriveFont(30.0f));
	}

	
	private void createMenus() {
		int menuWidth = screenArea.getHeight() / 3;
		colonyMenu = new GLColonyMenu(screenArea.getWidth() - menuWidth, 80,
				menuWidth, screenArea.getHeight()-80);
		fleetMenu = new GLFleetMenu(screenArea.getWidth() - menuWidth, 80,
				menuWidth, screenArea.getHeight()-80);
		planetMenu = new GLPlanetMenu(screenArea.getWidth() - menuWidth, 80,
				menuWidth, screenArea.getHeight()-80);
		
	}


	private void createBackground() {
		backgroundImage = new BufferedImage(totalWidth, totalHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = backgroundImage.createGraphics();
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, totalWidth, totalHeight);
		
		g.setColor(Color.LIGHT_GRAY);
		/*
		 * Draw Horizontal lines
		 */
		for (int row = 0; row <= rows; row++) {
			int x1 = (EXTRA_SPACE_HORIZONTAL) * squareSize;
			int x2 = (EXTRA_SPACE_HORIZONTAL + cols) * squareSize;
			int y = (EXTRA_SPACE_VERTICAL + row) * squareSize;
			g.drawLine(x1, y, x2, y);
		}
		/*
		 * Draw Vertical lines
		 */
		for (int col = 0; col <= rows; col++) {
			int x = (EXTRA_SPACE_HORIZONTAL + col) * squareSize;
			int y1 = (EXTRA_SPACE_VERTICAL) * squareSize;
			int y2 = (EXTRA_SPACE_VERTICAL + rows) * squareSize;
			g.drawLine(x, y1, x, y2);
		}
		
		/*
		 * Draw stars
		 */
		g.setColor(Color.WHITE);
		for (int i = 0; i < 5000; i++) {
			int x = (int) (Math.random()*(cols+2*EXTRA_SPACE_HORIZONTAL)*squareSize);
			int y = (int) (Math.random()*(rows+2*EXTRA_SPACE_VERTICAL)*squareSize);
			g.fillRect(x, y, 1, 1);
		}
	}

	private void initCameras() {
		cameras = new HashMap<Player, Camera>();
		cameras.put(Player.BLUE, new GLCamera(0.93f,0.08f));
		cameras.put(Player.RED, new GLCamera(0.07f,0.92f));
		cameras.put(Player.GREEN, new GLCamera(0.07f,0.08f));
		cameras.put(Player.PINK, new GLCamera(0.93f,0.92f));
		cc = new CameraController();
	}
	
	public void setViewer(Player player) {
		this.viewer = player;
		currentCamera = cameras.get(player);
		cc.setCamera(currentCamera);
		if (!cc.isAlive()) {
			cc.start();
		}
	}
	
	public void setActivePlayer(Player player) {
		/*
		 * TODO: 
		 * Print text if not active==viewer
		 */
	}
	
	@Override
	public Rectangle getBounds() {
		return screenArea;
	}

	@Override
	public void draw(GLAutoDrawable drawable, Rectangle objectRect, Rectangle targetArea, int zIndex) {
		
		drawBackground(drawable);
		
		if (sprites != null) {
			sprites.draw(drawable, getCameraRect(), getCameraRect(), 2);
		}
		
		drawSelectionBox(drawable, 10);
		
		textRenderer.beginRendering(screenArea.getWidth(), screenArea.getHeight());
		textRenderer.setColor(1.0f, 0.2f, 0.2f, 0.8f);
	    textRenderer.draw("That was easy, check GLRenderArea", 300, 400);
	    textRenderer.draw("Rapp is noob LOL player!", 300, 350);
	    textRenderer.setColor(0.2f, 0.7f, 0.7f, 0.8f);
	    textRenderer.draw("//Roras", 300, 300);
	    textRenderer.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		textRenderer.endRendering();
		/*
		 * Draw menus
		 */
		//colonyMenu.draw(drawable, colonyMenu.getBounds(), screenArea, 50);
	}

	private Rectangle getCameraRect() {
		int x = (int) ((totalWidth - screenArea.getWidth())*currentCamera.getX());
		int y = (int) ((totalHeight - screenArea.getHeight())*currentCamera.getY());
		return new Rectangle(x, y, screenArea.getWidth(), screenArea.getHeight());
	}
	
	public int translatePixelsX() {
		return (int) (((cols+2*EXTRA_SPACE_HORIZONTAL)*squareSize - getBounds().getWidth())*currentCamera.getX());
	}
	
	public int translatePixelsY() {
		return (int) (((rows+2*EXTRA_SPACE_VERTICAL)*squareSize - getBounds().getHeight())*currentCamera.getY());
	}
	
	private void drawSelectionBox(GLAutoDrawable drawable, int zIndex) {
		/*
		 * Draw a transparent box from pressed point
		 * to current selection
		 * 
		 * If mouse has not yet been clicked return.
		 */
		if (clickHandler == null || clickHandler.pressedPoint == null)
			return;

		Point mouseLoc = MouseInfo.getPointerInfo().getLocation();
		
		float x = 2*((float) (clickHandler.pressedPoint.x - translatePixelsX())/screenArea.getWidth()) -1f;
		float y = 2*((float) (clickHandler.pressedPoint.y - translatePixelsY())/screenArea.getHeight()) -1f;
		float x1 = 2*((float) mouseLoc.x)/screenArea.getWidth() - 1f;
		float y1 = 2*(float) (screenArea.getHeight() - mouseLoc.y)/screenArea.getHeight() - 1f;
		
		GL2 gl = drawable.getGL().getGL2();
		gl.glDisable(GL.GL_TEXTURE_2D);
		
		gl.glColor4f(0.1f, 1.0f, 0.2f, 1.0f);
		gl.glBegin(GL2.GL_LINE_STRIP);
		
		gl.glVertex3f(x, y, 1f / zIndex);
		gl.glVertex3f(x1, y, 1f / zIndex);
		gl.glVertex3f(x1, y1, 1f / zIndex);
		gl.glVertex3f(x, y1, 1f / zIndex);
		gl.glVertex3f(x, y, 1f / zIndex);
		
		gl.glEnd();
		
		gl.glColor4f(0.1f, 1.0f, 0.2f, 0.3f);
		gl.glBegin(GL2.GL_QUADS);
		
		gl.glVertex3f(x, y, 1f / zIndex);
		gl.glVertex3f(x1, y, 1f / zIndex);
		gl.glVertex3f(x1, y1, 1f / zIndex);
		gl.glVertex3f(x, y1, 1f / zIndex);
		
		gl.glEnd();
		
		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	}
	
	private void drawBackground(GLAutoDrawable drawable) {
		if (backgroundTexture == null) {
			backgroundTexture = AWTTextureIO.newTexture(drawable.getGLProfile(), backgroundImage, false);
		}
		backgroundTexture.bind(drawable.getGL());
		GL2 gl = drawable.getGL().getGL2();
		
		float x = ((float) (backgroundTexture.getWidth() - screenArea.getWidth())/backgroundTexture.getWidth())*currentCamera.getX();
		float y = ((float) (backgroundTexture.getHeight() - screenArea.getHeight())/backgroundTexture.getHeight())*currentCamera.getY();
		float width = (float) screenArea.getWidth() / backgroundTexture.getWidth();
		float height = (float) screenArea.getHeight() / backgroundTexture.getHeight();
		float x1 = x + width;
		float y1 = y + height;
		
		gl.glBegin(GL2.GL_QUADS);
		
		gl.glTexCoord2f(x, y);
		gl.glVertex3f(-1, -1, 0.99f);

		gl.glTexCoord2f(x, y1);
		gl.glVertex3f(-1, 1, 0.99f);

		gl.glTexCoord2f(x1, y1);
		gl.glVertex3f(1, 1, 0.99f);

		gl.glTexCoord2f(x1, y);
		gl.glVertex3f(1, -1, 0.99f);
		
		gl.glEnd();
	}

	public void updateSize(int width, int height) {
		this.squareSize = Math.min(width/6,height/6);
		screenArea.setHeight(height);
		screenArea.setWidth(width);
	}
	
	public MouseListener getClickHandler() {
		if (clickHandler == null) {
			clickHandler = new ClickHandler();
		}
		return clickHandler;
	}
	
	public KeyListener getCameraKeyListener() {
		return cc;
	}
	
	/* Data management Methods*/
	public void updateData(SpriteMapData data) {
		sprites = GLSpriteMap.getSprites(data, squareSize);
	}
	
	public void setStats(PlayerStats stats) {
		topMenu.setStats(stats);
		colonyMenu.setStats(stats);
	}

	public void setQueue(Map<Colony, List<BuildAble>> colonyQueues) {
		colonyMenu.setQueues(colonyQueues);
		/*
		 * Set for BuildQueueMenu
		 */
	}
	
	public void showFleet(Fleet selection) {
		hideSideMenus();
		fleetMenu.setFleet(selection);
		fleetMenu.setVisible(true);
	}
	
	public void showColony(Colony selection) {
		if (selection.equals(colonyMenu.getColony()) && colonyMenu.isVisible()) {
			// Update colony
			colonyMenu.setColony(selection);
		} else {
			hideSideMenus();
			colonyMenu.setColony(selection);
			colonyMenu.setVisible(true);
		}
	}
	
	public void showTerritory(Territory selection) {
		hideSideMenus();
		planetMenu.setTerritory(selection);
		planetMenu.setVisible(true);
	}
	
	public void hideSideMenus() {
		colonyMenu.setVisible(false);
		fleetMenu.setVisible(false);
		planetMenu.setVisible(false);
	}
	
	private class ClickHandler implements MouseListener {

		public Point pressedPoint;
		/*
		 * Click handling for different parts
		 */
		private boolean menuClick(Point point) {
			boolean clicked = false;
			return clicked;
		}

		private boolean isLegalPos(Position pos) {
			boolean rowLegal = pos.getRow() >= 1 && pos.getRow() <= rows;
			boolean colLegal = pos.getCol() >= 1 && pos.getCol() <= cols;
			return rowLegal && colLegal;
		}
		
		private Position getPosition(Point point, boolean translated) {
			int col = 0, row = 0;
			if (translated) {
				int rows1 = (rows + EXTRA_SPACE_VERTICAL + 1)*squareSize;
				row = (rows1 - point.y) / squareSize;
			} else {
				int yTrans = translatePixelsY();
				row = rows - (((screenArea.getHeight() - point.y) + yTrans) / squareSize - EXTRA_SPACE_VERTICAL);
			}
			int xTrans = translated ? 0 : translatePixelsX();
			col = ((point.x  + xTrans) / squareSize) + 1 - EXTRA_SPACE_HORIZONTAL;
			return new Position(row, col);
		}
		
		private boolean colonizerClick(Point point) {
			Position pos = getPosition(point, false);
			if (isLegalPos(pos)) {
				int dX = (point.x + translatePixelsX()) % squareSize;
				int dY = (point.y + translatePixelsY()) % squareSize;
				if (dX > squareSize/2 && dY > squareSize/2) {
					Event evt = new Event(Event.EventTag.COLONIZER_SELECTED, pos);
					EventBus.CLIENT.publish(evt);
					return true;
				}
			}
			return false;
		}
		
		private boolean fleetClick(MouseEvent me) {
			Point point = me.getPoint();
			Position pos = getPosition(point, false);
			if (isLegalPos(pos)) {
				int dX = (point.x + translatePixelsX()) % squareSize;
				int dY = (point.y + translatePixelsY()) % squareSize;
				if (dX <= squareSize/2 && dY >= squareSize/2) {
					if (me.isShiftDown()) {
						Event evt = new Event(Event.EventTag.ADD_FLEET_SELECTION, pos);
						EventBus.CLIENT.publish(evt);
					} else {
						Event evt = new Event(Event.EventTag.NEW_FLEET_SELECTION, pos);
						EventBus.CLIENT.publish(evt);
					}
					return true;
				}
			}
			return false;
		}

		private boolean planetClick(Point point) {
			Position pos = getPosition(point, false);
			if (isLegalPos(pos)) {
				Event evt = new Event(Event.EventTag.PLANET_SELECTED, pos);
				EventBus.CLIENT.publish(evt);
				return true;
			}
			return false;
		}
		
		private boolean pathClick(Point point) {
			Position pos = getPosition(point, false);
			if (isLegalPos(pos)) {
				Event evt = new Event(Event.EventTag.SET_PATH, pos);
				EventBus.CLIENT.publish(evt);
				return true;
			}
			return false;
		}
		
		@Override public void mousePressed(MouseEvent me) {
			if (me.getButton() == MouseEvent.BUTTON1) {
				pressedPoint = me.getPoint();
				pressedPoint.x += translatePixelsX();
				pressedPoint.y = (screenArea.getHeight() - pressedPoint.y) + translatePixelsY();
			} else if (me.getButton() == MouseEvent.BUTTON3) {
				if (pathClick(me.getPoint())) {return;}
			}
		}
		@Override public void mouseClicked(MouseEvent me) {
			/*
			 * Check each level of interaction in order.
			 */
			if (me.getButton() == MouseEvent.BUTTON1) {
				if (menuClick(me.getPoint())) {return;}
				if (fleetClick(me)) {return;}
				if (colonizerClick(me.getPoint())) {return;}
				if (planetClick(me.getPoint())) {return;}
				else {
					/*
					 * Click was not in any trigger zone. Call deselect.
					 */
					EventBus.CLIENT.publish(new Event(Event.EventTag.DESELECT, null));
//					hideSideMenus();
				}
			}
		}
		@Override public void mouseEntered(MouseEvent me) {}
		@Override public void mouseExited(MouseEvent me) {}
		@Override public void mouseReleased(MouseEvent me) {
			if (me.getButton() == MouseEvent.BUTTON1) {
				Point releasePoint = me.getPoint();
				releasePoint.x += translatePixelsX();
				releasePoint.y = (screenArea.getHeight() - releasePoint.y) + translatePixelsY();
				if (Math.abs(releasePoint.x - pressedPoint.x) < squareSize/4 || Math.abs(releasePoint.y - pressedPoint.y) < squareSize/4) {
					pressedPoint = null;
					return;
				}
				
				Point ltCorner = new Point(Math.min(releasePoint.x, pressedPoint.x), Math.max(releasePoint.y, pressedPoint.y));
				Point rbCorner = new Point(Math.max(releasePoint.x, pressedPoint.x), Math.min(releasePoint.y, pressedPoint.y));
				Position ltPos = getPosition(ltCorner, true);
				Position rbPos = getPosition(rbCorner, true);
				
				System.out.println("lt" + ltPos);
				System.out.println("rb" + rbPos);
				
				if (ltCorner.getX() % squareSize >= squareSize/2) {
					ltPos = new Position(ltPos.getRow(), ltPos.getCol() + 1);
				}
				if (ltCorner.getY() % squareSize < squareSize/2) {
					ltPos = new Position(ltPos.getRow() + 1, ltPos.getCol());
				}
				if (rbCorner.getX() % squareSize < squareSize/2) {
					rbPos = new Position(rbPos.getRow(), rbPos.getCol() - 1);
				}
				if (rbCorner.getY() % squareSize >= squareSize/2) {
					rbPos = new Position(rbPos.getRow() - 1, rbPos.getCol());
				}
				
				System.out.println("lt" + ltPos);
				System.out.println("rb" + rbPos);
								
				List<Position> selectPos = new ArrayList<Position>();
				for (int col = ltPos.getCol(); col <= rbPos.getCol(); col++) {
					for (int row = ltPos.getRow(); row <= rbPos.getRow(); row++) {
						Position pos = new Position(row, col);
						if (isLegalPos(pos)) {
							selectPos.add(pos);
						}
					}
				}
				if (!selectPos.isEmpty()) {
					Event evt = new Event(Event.EventTag.NEW_FLEET_SELECTION, selectPos);
					EventBus.CLIENT.publish(evt);
				} else {
					Event evt = new Event(Event.EventTag.DESELECT, null);
					EventBus.CLIENT.publish(evt);
//					hideSideMenus();
				}
				pressedPoint = null;
			}
		}
	}
}