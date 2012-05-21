package riskyspace.view.opengl.menu;

import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.media.opengl.GLAutoDrawable;

import riskyspace.model.Fleet;
import riskyspace.model.Player;
import riskyspace.model.ShipType;
import riskyspace.view.opengl.Rectangle;
import riskyspace.view.opengl.impl.GLSprite;

public class GLFleetMenu extends GLAbstractSideMenu {
	
	private int itemSize;
	private Rectangle shipRect;
	
	private int margin;
	
	private Map<Player, GLSprite> fleetPictures = new HashMap<Player, GLSprite>();
	private GLSprite fleetPicture = null;
	private Rectangle commanderRect = null;
	
	private Map<String, GLSprite> shipIcons = new HashMap<String, GLSprite>();
	private List<GLSprite> fleetIcons = new ArrayList<GLSprite>();
	
	public GLFleetMenu(int x, int y, int menuWidth, int menuHeight) {
		super(x, y, menuWidth, menuHeight);
		margin = menuWidth / 10;
		itemSize = (menuWidth - margin * 2) / 5;
		int size = itemSize - margin / 10;
		shipRect = new Rectangle(0, 0, size, size);
		setFleetPictures();
		initSprites();
	}
	
	private void setFleetPictures() {
		int sHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
		int imageWidth = getMenuWidth() - 8 * margin / 5;
		int imageHeight = (int) (imageWidth * 0.8f);
		commanderRect = new Rectangle(getX() + 4 * margin / 5,
				sHeight - (getY() + 2 * margin / 3 + imageHeight),
				imageWidth,
				imageHeight);
		fleetPictures.put(Player.RED, new GLSprite("palpatine", 640, 480));
		fleetPictures.put(Player.BLUE, new GLSprite("palpatine", 640, 480));
		fleetPictures.put(Player.GREEN, new GLSprite("palpatine", 640, 480));
		fleetPictures.put(Player.PINK, new GLSprite("palpatine", 640, 480));
	}

	public void initSprites() { 
		shipIcons.put("SCOUT_GREEN", 	new GLSprite("ship_icons", 	0, 		0, 		64, 64));
		shipIcons.put("HUNTER_GREEN", 	new GLSprite("ship_icons", 	64,		0, 		64, 64));
		shipIcons.put("COLONIZER_GREEN",new GLSprite("ship_icons", 	128,	0, 		64, 64));
		shipIcons.put("DESTROYER_GREEN",new GLSprite("ship_icons", 	192,	0, 		64, 64));
		
		shipIcons.put("SCOUT_PINK", 	new GLSprite("ship_icons", 	0, 		64, 	64, 64));
		shipIcons.put("HUNTER_PINK", 	new GLSprite("ship_icons", 	64, 	64, 	64, 64));
		shipIcons.put("COLONIZER_PINK", new GLSprite("ship_icons", 	128, 	64, 	64, 64));
		shipIcons.put("DESTROYER_PINK",	new GLSprite("ship_icons", 	192, 	64, 	64, 64));
		
		shipIcons.put("SCOUT_BLUE", 	new GLSprite("ship_icons", 	0, 		128, 	64, 64));
		shipIcons.put("HUNTER_BLUE", 	new GLSprite("ship_icons", 	64, 	128, 	64, 64));
		shipIcons.put("COLONIZER_BLUE", new GLSprite("ship_icons", 	128, 	128, 	64, 64));
		shipIcons.put("DESTROYER_BLUE", new GLSprite("ship_icons", 	192, 	128, 	64, 64));
		
		shipIcons.put("SCOUT_RED", 		new GLSprite("ship_icons", 	0, 		192, 	64, 64));
		shipIcons.put("HUNTER_RED", 	new GLSprite("ship_icons", 	64, 	192, 	64, 64));
		shipIcons.put("COLONIZER_RED",	new GLSprite("ship_icons", 	128, 	192,	64, 64));
		shipIcons.put("DESTROYER_RED",	new GLSprite("ship_icons", 	192, 	192, 	64, 64));
	}

	@Override
	public boolean mousePressed(Point p) {
		/*
		 * Only handle mouse event if enabled
		 */
		if (isVisible()) {
			if (this.contains(p)) {return true;}
			else {
				return false;
			}
		}
		return false;
	}
	@Override
	public boolean mouseReleased(Point p) {
		if (isVisible()) {
			if (this.contains(p)) {return true;}
			else {
				return false;
			}
		}
		return false;
	}
	@Override
	public void draw(GLAutoDrawable drawable, Rectangle objectRect, Rectangle targetArea, int zIndex) {
		if (isVisible()) {
			super.draw(drawable, objectRect, targetArea, zIndex);
			zIndex++;
			fleetPicture.draw(drawable, commanderRect, targetArea, zIndex);
			int cHeight = commanderRect.getHeight();
			for (int i = 0; i < fleetIcons.size(); i++) {
				int col = i % 5;
				int row = i / 5;
				Rectangle rect = new Rectangle(shipRect);
				rect.setX(getX() + margin + col * itemSize);
				rect.setY(getMenuHeight() - (3*margin + cHeight + row * itemSize));
				fleetIcons.get(i).draw(drawable, rect, targetArea, zIndex);
			}
		}
	}
	public void setFleet(Fleet fleet) {
		createFleetIcons(fleet);
		fleetPicture = fleetPictures.get(fleet.getOwner());
	}
	
	private void createFleetIcons(Fleet fleet) {
		fleetIcons.clear();
		createFleetIcons(fleet, ShipType.DESTROYER);
		createFleetIcons(fleet, ShipType.HUNTER);
		createFleetIcons(fleet, ShipType.SCOUT);
		createFleetIcons(fleet, ShipType.COLONIZER);
	}
	
	private void createFleetIcons(Fleet fleet, ShipType type) {
		int number = fleet.shipCount(type);
		for (int i = 0; i < number; i++) {
			fleetIcons.add(shipIcons.get(type.toString() + "_" + fleet.getOwner().toString()));
		}
	}
}