package riskyspace.view.swing.menu;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import riskyspace.model.Fleet;
import riskyspace.model.ShipType;

public class FleetMenu extends SwingAbstractSideMenu {
	
	private int itemSize;
	
	private int margin;
	private Image fleetPicture = null;
	
	private Map<String, Image> shipIcons = new HashMap<String, Image>();
	private List<Image> fleetIcons = new ArrayList<Image>();
	
	public FleetMenu(int x, int y, int menuWidth, int menuHeight) {
		super(x, y, menuWidth, menuHeight);
		margin = menuWidth/10;
		fleetPicture = Toolkit.getDefaultToolkit().getImage("res/menu/palpatine.png").getScaledInstance(menuWidth - 3*margin, 2*(menuWidth - 3*margin)/3, Image.SCALE_DEFAULT);
		itemSize = (menuWidth - margin*2) / 5;
		initSprites(itemSize - margin/10);
	}
	
	public void initSprites(int size) {
		shipIcons.put("SCOUT_BLUE", Toolkit.getDefaultToolkit().getImage("res/menu/blue/ships/scout_icon.png").getScaledInstance(size, size, Image.SCALE_DEFAULT));
		shipIcons.put("HUNTER_BLUE", Toolkit.getDefaultToolkit().getImage("res/menu/blue/ships/hunter_icon.png").getScaledInstance(size, size, Image.SCALE_DEFAULT));
		shipIcons.put("DESTROYER_BLUE", Toolkit.getDefaultToolkit().getImage("res/menu/blue/ships/destroyer_icon.png").getScaledInstance(size, size, Image.SCALE_DEFAULT));
		shipIcons.put("COLONIZER_BLUE", Toolkit.getDefaultToolkit().getImage("res/menu/blue/ships/colonizer_icon.png").getScaledInstance(size, size, Image.SCALE_DEFAULT));
		
		shipIcons.put("SCOUT_RED", Toolkit.getDefaultToolkit().getImage("res/menu/red/ships/scout_icon.png").getScaledInstance(size, size, Image.SCALE_DEFAULT));
		shipIcons.put("HUNTER_RED", Toolkit.getDefaultToolkit().getImage("res/menu/red/ships/hunter_icon.png").getScaledInstance(size, size, Image.SCALE_DEFAULT));
		shipIcons.put("DESTROYER_RED", Toolkit.getDefaultToolkit().getImage("res/menu/red/ships/destroyer_icon.png").getScaledInstance(size, size, Image.SCALE_DEFAULT));
		shipIcons.put("COLONIZER_RED", Toolkit.getDefaultToolkit().getImage("res/menu/red/ships/colonizer_icon.png").getScaledInstance(size, size, Image.SCALE_DEFAULT));
	}
	
	public void setFleet(Fleet fleet) {
		createFleetIcons(fleet);
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
	public void draw(Graphics g) {
				/*
		 * Only draw if enabled
		 */
		if (isVisible()) {
			super.draw(g);
			g.drawImage(fleetPicture, getX() + margin + margin/2 + 2, getY() + margin + 15, null);
			int height = fleetPicture != null ? fleetPicture.getHeight(null) : 0;
			for (int i = 0; i < fleetIcons.size(); i++) {
				int col = i % 5;
				int row = i / 5;
				int x = getX() + margin + col * itemSize + 2;
				int y = getY() + 2 * margin + height + row * itemSize;
				g.drawImage(fleetIcons.get(i), x, y, null);
			}
		}
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