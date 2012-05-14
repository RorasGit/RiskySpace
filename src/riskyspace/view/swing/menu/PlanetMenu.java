package riskyspace.view.swing.menu;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import riskyspace.model.Fleet;
import riskyspace.model.Planet;
import riskyspace.model.Resource;
import riskyspace.model.Territory;
import riskyspace.services.Event;
import riskyspace.services.EventBus;
import riskyspace.view.Action;
import riskyspace.view.View;
import riskyspace.view.swing.impl.SwingButton;

public class PlanetMenu extends AbstractSideMenu{
	
	private int margin = 30;
	
	private Image planetPicture = null;
	private Image metalPlanetPicture = null;
	private Image gasPlanetPicture = null;
	
	private SwingButton colonizeButton;
	
	public PlanetMenu(int x, int y, int menuWidth, int menuHeight) {
		super(x, y, menuWidth, menuHeight);
		
		metalPlanetPicture = Toolkit.getDefaultToolkit().getImage("res/menu/metal_planet"  + View.res).
				getScaledInstance(menuWidth - 2*margin, ((menuWidth - 2*margin)*3)/4, Image.SCALE_DEFAULT);
		
		gasPlanetPicture = Toolkit.getDefaultToolkit().getImage("res/menu/gas_planet"  + View.res).
				getScaledInstance(menuWidth - 2*margin, ((menuWidth - 2*margin)*3)/4, Image.SCALE_DEFAULT);
		
		colonizeButton = new SwingButton(x + margin, y + menuHeight - 2*(menuWidth - 2*margin)/4, menuWidth-2*margin, (menuWidth - 2*margin)/4);
		colonizeButton.setAction(new Action(){
			@Override
			public void performAction() {
				Event evt = new Event(Event.EventTag.COLONIZE_PLANET, null);
				EventBus.CLIENT.publish(evt);
			}
		});
	}
	
	public void setTerritory(Territory selection) {
		planetPicture = selection.getPlanet().getType() == Resource.METAL? metalPlanetPicture : gasPlanetPicture;
		colonizeButton.setImage("res/menu/world/colonizeButton" + View.res);
		colonizeButton.setEnabled(false);
		for (Fleet fleet : selection.getFleets()) {
			if (fleet.hasColonizer()) {
				colonizeButton.setEnabled(true);
				break;
			}
		}
	}
	
	@Override
	public boolean mousePressed(Point p) {
		/*
		 * Only handle mouse event if enabled
		 */
		if (isVisible()) {
			if (colonizeButton.mousePressed(p)) {return true;}
			if (this.contains(p)) {return true;}
			else {
				return false;
			}
		}
		return false;
	}

	@Override
	public boolean mouseReleased(Point p) {
		/*
		 * Only handle mouse event if enabled
		 */
		if (isVisible()) {
			if (colonizeButton.mouseReleased(p)) {return true;}
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
			g.drawImage(planetPicture, getX() + margin, getY() + margin + 15,null);
			colonizeButton.draw(g);
		}
	}
}