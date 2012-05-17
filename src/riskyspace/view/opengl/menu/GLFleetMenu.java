package riskyspace.view.opengl.menu;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.media.opengl.GLAutoDrawable;

import riskyspace.model.Fleet;
import riskyspace.model.ShipType;
import riskyspace.view.opengl.Rectangle;
import riskyspace.view.opengl.impl.GLSprite;

public class GLFleetMenu extends GLAbstractSideMenu {
	
	private int itemSize;
	
	private int margin;
	private GLSprite fleetPicture = null;
	
	private Map<String, GLSprite> shipIcons = new HashMap<String, GLSprite>();
	private List<GLSprite> fleetIcons = new ArrayList<GLSprite>();
	
	private int shipSize;
	
	public GLFleetMenu(int x, int y, int menuWidth, int menuHeight) {
		super(x, y, menuWidth, menuHeight);
		margin = menuWidth/10;
		fleetPicture = new GLSprite("menu/palpatine.png", 640, 480);
		fleetPicture.setBounds(new Rectangle(getX() + margin + margin/2 + 2, menuHeight - (margin + 15), menuWidth - 3*margin, 2*(menuWidth - 3*margin)/3));
		itemSize = (menuWidth - margin*2) / 5;
		initSprites(itemSize - margin/10);
	}
	
	public void initSprites(int size) { 
		shipSize = size;
		shipIcons.put("SCOUT_RED", 		new GLSprite("shipicon", 0, 	0, 		64, 64));
		shipIcons.put("HUNTER_RED", 	new GLSprite("shipicon", 64,	0, 		64, 64));
		shipIcons.put("DESTROYER_RED",	new GLSprite("shipicon", 128,	0, 		64, 64));
		shipIcons.put("COLONIZER_RED",	new GLSprite("shipicon", 192,	0, 		64, 64));
		
		shipIcons.put("SCOUT_BLUE", 	new GLSprite("shipicon", 0, 	64, 	64, 64));
		shipIcons.put("HUNTER_BLUE", 	new GLSprite("shipicon", 64, 	64, 	64, 64));
		shipIcons.put("DESTROYER_BLUE", new GLSprite("shipicon", 128, 	64, 	64, 64));
		shipIcons.put("COLONIZER_BLUE",	new GLSprite("shipicon", 192, 	64, 	64, 64));
		
		shipIcons.put("SCOUT_PINK", 	new GLSprite("shipicon", 0, 	128, 	64, 64));
		shipIcons.put("HUNTER_PINK", 	new GLSprite("shipicon", 64, 	128, 	64, 64));
		shipIcons.put("DESTROYER_PINK", new GLSprite("shipicon", 128, 	128, 	64, 64));
		shipIcons.put("COLONIZER_PINK", new GLSprite("shipicon", 192, 	128, 	64, 64));
		
		shipIcons.put("SCOUT_GREEN", 	new GLSprite("shipicon", 0, 	192, 	64, 64));
		shipIcons.put("HUNTER_GREEN", 	new GLSprite("shipicon", 64, 	192, 	64, 64));
		shipIcons.put("DESTROYER_GREEN",new GLSprite("shipicon", 128, 	192, 	64, 64));
		shipIcons.put("COLONIZER_GREEN",new GLSprite("shipicon", 192, 	192, 	64, 64));
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
			fleetPicture.draw(drawable, fleetPicture.getBounds(), targetArea, zIndex);
			int height = fleetPicture.getBounds().getHeight();
			for (int i = 0; i < fleetIcons.size(); i++) {
				int col = i % 5;
				int row = i / 5;
				fleetIcons.get(i).draw(drawable, new Rectangle(getX() + margin + col*itemSize + 2, getMenuHeight() - (2*margin + height + row*itemSize) - shipSize, shipSize, shipSize), targetArea, zIndex);
			}
		}
	}
	public void setFleet(Fleet fleet) {
		createFleetIcons(fleet);
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