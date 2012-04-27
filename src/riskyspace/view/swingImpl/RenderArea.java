package riskyspace.view.swingImpl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.Timer;

import riskyspace.GameManager;
import riskyspace.model.Player;
import riskyspace.model.Position;
import riskyspace.model.World;
import riskyspace.services.Event;
import riskyspace.services.Event.EventTag;
import riskyspace.services.EventBus;
import riskyspace.services.EventHandler;
import riskyspace.services.EventText;
import riskyspace.view.Clickable;
import riskyspace.view.SpriteMap;
import riskyspace.view.camera.Camera;
import riskyspace.view.camera.CameraController;
import riskyspace.view.menu.IMenu;
import riskyspace.view.menu.swingImpl.ColonyMenu;
import riskyspace.view.menu.swingImpl.FleetMenu;
import riskyspace.view.menu.swingImpl.PlanetMenu;
import riskyspace.view.menu.swingImpl.RecruitMenu;
import riskyspace.view.menu.swingImpl.TopMenu;

public class RenderArea extends JPanel implements EventHandler {

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
	
	/*
	 * Cameras
	 */
	private Camera currentCamera = null;
	private Map<Player, Camera> cameras = null;
	private CameraController cc = null;
	
	/*
	 * Side menu settings
	 */
	private IMenu colonyMenu = null;
	private IMenu recruitMenu = null;
	private IMenu fleetMenu = null;
	private IMenu topMenu = null;
	private IMenu planetMenu = null;
	
	/*
	 * Screen measures
	 */
	private int width;
	private int height;
	private int squareSize;
	
	/*
	 * BufferedImage background
	 */
	private BufferedImage background = null;
	
	/*
	 * SpriteMap
	 */
	private SpriteMap spriteMap = null;
	
	private ClickHandler clickHandler = null;
	private EventTextPrinter eventTextPrinter = null;
	
	private int rows, cols;
	
	public RenderArea(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		measureScreen();
		SpriteMap.init(squareSize);
		createBackground();
		initCameras();
		createMenues();
		eventTextPrinter = new EventTextPrinter();
		EventBus.INSTANCE.addHandler(this);
		clickHandler = new ClickHandler();
		addMouseListener(clickHandler);
	}

	private void createMenues() {
		int menuWidth = height / 3;
		colonyMenu = new ColonyMenu(width - menuWidth, 80, menuWidth, height-80);
		recruitMenu = new RecruitMenu(width - menuWidth, 80, menuWidth, height-80);
		fleetMenu = new FleetMenu(width - menuWidth, 80, menuWidth, height-80);
		planetMenu = new PlanetMenu(width - menuWidth, 80, menuWidth, height-80);
		topMenu = new TopMenu(0, 0, width, height);
	}
	
	private void createBackground() {
		int totalWidth = (cols + 2*EXTRA_SPACE_HORIZONTAL)*squareSize;
		int totalHeight = (rows + 2*EXTRA_SPACE_VERTICAL)*squareSize;
		background = new BufferedImage(totalWidth, totalHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2D = background.createGraphics();
		
		g2D.setColor(Color.BLACK);
		g2D.fillRect(0, 0, totalWidth, totalHeight);
		
		g2D.setColor(Color.LIGHT_GRAY);
		/*
		 * Draw Horizontal lines
		 */
		for (int row = 0; row <= rows; row++) {
			int x1 = (EXTRA_SPACE_HORIZONTAL) * squareSize;
			int x2 = (EXTRA_SPACE_HORIZONTAL + cols) * squareSize;
			int y = (EXTRA_SPACE_VERTICAL + row) * squareSize;
			g2D.drawLine(x1, y, x2, y);
		}
		/*
		 * Draw Vertical lines
		 */
		for (int col = 0; col <= rows; col++) {
			int x = (EXTRA_SPACE_HORIZONTAL + col) * squareSize;
			int y1 = (EXTRA_SPACE_VERTICAL) * squareSize;
			int y2 = (EXTRA_SPACE_VERTICAL + rows) * squareSize;
			g2D.drawLine(x, y1, x, y2);
		}
		
		/*
		 * Draw stars
		 */
		g2D.setColor(Color.WHITE);
		for (int i = 0; i < 5000; i++) {
			int x = (int) (Math.random()*(cols+2*EXTRA_SPACE_HORIZONTAL)*squareSize);
			int y = (int) (Math.random()*(rows+2*EXTRA_SPACE_VERTICAL)*squareSize);
			g2D.fillRect(x, y, 1, 1);
		}
	}
	
	private void initCameras() {
		cameras = new HashMap<Player, Camera>();
		/*
		 * TODO: Cameras only support 2 players
		 */
		cameras.put(Player.BLUE, new Camera(0.93f,0.92f));
		cameras.put(Player.RED, new Camera(0.07f,0.08f));
		currentCamera = cameras.get(Player.BLUE);
		cc = new CameraController();
		cc.setCamera(currentCamera);
		cc.start();
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
		spriteMap = SpriteMap.getSprites(player);
		currentCamera = cameras.get(player);
		cc.setCamera(currentCamera);
	}
	
	public int translatePixelsX() {
		return (int) (((cols+2*EXTRA_SPACE_HORIZONTAL)*squareSize - width)*currentCamera.getX());
	}
	
	public int translatePixelsY() {
		return (int) (((rows+2*EXTRA_SPACE_VERTICAL)*squareSize - height)*currentCamera.getY());
	}
	
	private int times = 0;
	Timer fpsTimer = new Timer(500, new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			fps = "FPS: " + (2*times);
			times = 0;
		}
		
	});
	private String fps = "";

	public void paintComponent(Graphics g) {
		if (!fpsTimer.isRunning()) {
			fpsTimer.start();
		}
		times++;
		/*
		 * Translate with cameras
		 */
		int xTrans = -translatePixelsX();
		int yTrans = -translatePixelsY();
		g.translate(xTrans, yTrans);
		
		// Draw background
		g.drawImage(background, 0, 0, null);
		
		spriteMap.draw(g, squareSize, EXTRA_SPACE_HORIZONTAL, EXTRA_SPACE_VERTICAL);
		
		// Draw texts
		eventTextPrinter.drawEventText(g);
		
		drawSelectionArea(g, xTrans, yTrans);
		
		// Draw menu
		g.translate(-xTrans, -yTrans);
		
		/*
		 * TODO: Do not check isVisible, the menus should handle that part
		 */
		if (colonyMenu.isVisible()) {
			colonyMenu.draw(g);
		}
		if (recruitMenu.isVisible()) {
			recruitMenu.draw(g);
		}
		if (fleetMenu.isVisible()) {
			fleetMenu.draw(g);
		}
		if (planetMenu.isVisible()) {
			planetMenu.draw(g);
		}
		topMenu.draw(g);
		g.setColor(Color.GREEN);
		g.drawString(fps, 20, 100);
	}
	
	private void drawSelectionArea(Graphics g, int xTrans, int yTrans) {
		g.setColor(Color.GREEN);
		if (clickHandler.pressedPoint != null) {
			Point p = MouseInfo.getPointerInfo().getLocation();
			int x = clickHandler.pressedPoint.x;
			int y = clickHandler.pressedPoint.y;
			int otherX = p.x - xTrans;
			int otherY = p.y - yTrans;
			int width = Math.abs(x - otherX);
			int height = Math.abs(y - otherY);
			x = x > otherX ? otherX : x;
			y = y > otherY ? otherY : y;
			g.drawRect(x, y, width, height);
			g.setColor(new Color(g.getColor().getRed(), g.getColor().getGreen(), g.getColor().getBlue(), 0x3F));
			g.fillRect(x + 1, y + 1, width, height);
		}
	}
	 
	public Position getPosition(Point point, boolean translated) {
		int xTrans = translated ? 0 : translatePixelsX();
		int yTrans = translated ? 0 : translatePixelsY();
		int row = ((point.y + yTrans) / squareSize) + 1 - EXTRA_SPACE_VERTICAL;
		int col = ((point.x  + xTrans) / squareSize) + 1 - EXTRA_SPACE_HORIZONTAL;
		return new Position(row, col); 
	}
	
	private boolean isLegalPos(Position pos) {
		boolean rowLegal = pos.getRow() >= 1 && pos.getRow() <= rows;
		boolean colLegal = pos.getCol() >= 1 && pos.getCol() <= cols;
		return rowLegal && colLegal;
	}

	@Override
	public void performEvent(Event evt) {
		if (evt.getTag() == Event.EventTag.ACTIVE_PLAYER_CHANGED) {
			setPlayer((Player) evt.getObjectValue());
		}
		if (evt.getTag() == EventTag.TERRITORY_CHANGED || evt.getTag() == EventTag.PATHS_UPDATED) {
			spriteMap = SpriteMap.getSprites(GameManager.INSTANCE.getCurrentPlayer());
		}
	}
	
	private class ClickHandler implements MouseListener {

		public Point pressedPoint;
		/*
		 * Click handling for different parts
		 */
		public boolean menuClick(Point point) {
			if (topMenu instanceof Clickable) {
				if (((Clickable) topMenu).mousePressed(point)) {
					return true;
				}
			}
			if (colonyMenu.isVisible()) {
				if (colonyMenu instanceof Clickable) {
					return ((Clickable) colonyMenu).mousePressed(point);
				}
			}
			if (recruitMenu.isVisible()) {
				if (recruitMenu instanceof Clickable) {
					return ((Clickable) recruitMenu).mousePressed(point);
				}
			}
			if (fleetMenu.isVisible()) {
				if (fleetMenu instanceof Clickable) {
					return ((Clickable) fleetMenu).mousePressed(point);
				}
			}
			if (planetMenu.isVisible()) {
				if (planetMenu instanceof Clickable) {
					return ((Clickable) planetMenu).mousePressed(point);
				}
			}
			
			return false;
		}

		public boolean colonizerClick(Point point) {
			Position pos = getPosition(point, false);
			if (isLegalPos(pos)) {
				int dX = (point.x + translatePixelsX()) % squareSize;
				int dY = (point.y + translatePixelsY()) % squareSize;
				if (dX > squareSize/2 && dY > squareSize/2) {
					Event evt = new Event(Event.EventTag.COLONIZER_SELECTED, pos);
					EventBus.INSTANCE.publish(evt);
					return true;
				}
			}
			return false;
		}
		
		public boolean fleetClick(MouseEvent me) {
			Point point = me.getPoint();
			Position pos = getPosition(point, false);
			if (isLegalPos(pos)) {
				int dX = (point.x + translatePixelsX()) % squareSize;
				int dY = (point.y + translatePixelsY()) % squareSize;
				if (dX <= squareSize/2 && dY >= squareSize/2) {
					if (me.isShiftDown()) {
						Event evt = new Event(Event.EventTag.ADD_FLEET_SELECTION, pos);
						EventBus.INSTANCE.publish(evt);
					} else {
						Event evt = new Event(Event.EventTag.NEW_FLEET_SELECTION, pos);
						EventBus.INSTANCE.publish(evt);
					}
					return true;
				}
			}
			return false;
		}

		public boolean planetClick(Point point) {
			Position pos = getPosition(point, false);
			if (isLegalPos(pos)) {
				Event evt = new Event(Event.EventTag.PLANET_SELECTED, pos);
				EventBus.INSTANCE.publish(evt);
				return true;
			}
			return false;
		}
		
		public boolean pathClick(Point point) {
			Position pos = getPosition(point, false);
			if (isLegalPos(pos)) {
				Event evt = new Event(Event.EventTag.SET_PATH, pos);
				EventBus.INSTANCE.publish(evt);
				return true;
			}
			return false;
		}
		
		@Override public void mousePressed(MouseEvent me) {
			if (me.getButton() == MouseEvent.BUTTON1) {
				pressedPoint = me.getPoint();
				pressedPoint.x += translatePixelsX();
				pressedPoint.y += translatePixelsY();
			} else if (me.getButton() == MouseEvent.BUTTON3) {
				if (pathClick(me.getPoint())) {return;}
				else {
					/*
					 * Click was not in any trigger zone. Call deselect.
					 */
					EventBus.INSTANCE.publish(new Event(Event.EventTag.DESELECT, null));
				}
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
					EventBus.INSTANCE.publish(new Event(Event.EventTag.DESELECT, null));
				}
			}
		}
		@Override public void mouseEntered(MouseEvent me) {}
		@Override public void mouseExited(MouseEvent me) {}
		@Override public void mouseReleased(MouseEvent me) {
			if (me.getButton() == MouseEvent.BUTTON1) {
				Point releasePoint = me.getPoint();
				releasePoint.x += translatePixelsX();
				releasePoint.y += translatePixelsY();
				if (Math.abs(releasePoint.x - pressedPoint.x) < squareSize/10 || Math.abs(releasePoint.y - pressedPoint.y) < squareSize/10) {
					pressedPoint = null;
					return;
				}
				
				Point ltCorner = new Point(Math.min(releasePoint.x, pressedPoint.x), Math.min(releasePoint.y, pressedPoint.y));
				Point rbCorner = new Point(Math.max(releasePoint.x, pressedPoint.x), Math.max(releasePoint.y, pressedPoint.y));
				Position ltPos = getPosition(ltCorner, true);
				Position rbPos = getPosition(rbCorner, true);
				if (ltCorner.getX() % squareSize >= squareSize/2) {
					ltPos = new Position(ltPos.getRow(), ltPos.getCol() + 1);
				}
				if (ltCorner.getY() % squareSize >= squareSize/2) {
					ltPos = new Position(ltPos.getRow() + 1, ltPos.getCol());
				}
				if (rbCorner.getX() % squareSize < squareSize/2) {
					rbPos = new Position(rbPos.getRow(), rbPos.getCol() - 1);
				}
				if (rbCorner.getY() % squareSize < squareSize/2) {
					rbPos = new Position(rbPos.getRow() - 1, rbPos.getCol());
				}
				
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
					EventBus.INSTANCE.publish(evt);
				} else {
					Event evt = new Event(Event.EventTag.DESELECT, null);
					EventBus.INSTANCE.publish(evt);
				}
				pressedPoint = null;
			}
		}
	}
	
	private class EventTextPrinter implements EventHandler {
		
		final List<EventText> texts = new ArrayList<EventText>();
		
		public EventTextPrinter() {
			EventBus.INSTANCE.addHandler(this);
		}
		
		@Override
		public void performEvent(Event evt) {
			if (evt.getTag() == Event.EventTag.EVENT_TEXT) {
				EventText et = (EventText) evt.getObjectValue();
				texts.add(et);
			}
		}
		
		public void drawEventText(Graphics g) {
			g.setColor(Color.GREEN);
			g.setFont(new Font("Arial", Font.PLAIN, 12));
			List<EventText> done = new ArrayList<EventText>();
			for (EventText eventText : texts) {
				Position pos = eventText.getPos();
				int x = (int) ((pos.getCol()+EXTRA_SPACE_HORIZONTAL-0.5)*squareSize)-g.getFontMetrics().stringWidth(eventText.getText())/2;
				int y = (int) ((pos.getRow()+EXTRA_SPACE_VERTICAL-0.5)*squareSize-eventText.getTimes()/12-g.getFontMetrics().getHeight()/2);
				g.drawString(eventText.getText(), x, y);
				eventText.incTimes();
				if (eventText.getTimes() == 120) {
					done.add(eventText);
				}
			}
			texts.removeAll(done);
		}
	}
}