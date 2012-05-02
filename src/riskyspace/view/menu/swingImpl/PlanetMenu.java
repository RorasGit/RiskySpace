package riskyspace.view.menu.swingImpl;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import riskyspace.GameManager;
import riskyspace.model.Player;
import riskyspace.model.Position;
import riskyspace.model.Resource;
import riskyspace.model.Territory;
import riskyspace.model.World;
import riskyspace.services.Event;
import riskyspace.services.EventBus;
import riskyspace.view.Action;
import riskyspace.view.Button;
import riskyspace.view.View;
import riskyspace.view.menu.AbstractSideMenu;

public class PlanetMenu extends AbstractSideMenu{
	
	private int margin = 30;
	
	private Image planetPicture = null;
	private Image metalPlanetPicture = null;
	private Image gasPlanetPicture = null;
	
	private Territory planetTer = null;
	
	private Button colonizeButton;
	
	public PlanetMenu(int x, int y, int menuWidth, int menuHeight) {
		super(x, y, menuWidth, menuHeight);
		
		metalPlanetPicture = Toolkit.getDefaultToolkit().getImage("res/menu/metal_planet"  + View.res).
				getScaledInstance(menuWidth - 2*margin, ((menuWidth - 2*margin)*3)/4, Image.SCALE_DEFAULT);
		
		gasPlanetPicture = Toolkit.getDefaultToolkit().getImage("res/menu/gas_planet"  + View.res).
				getScaledInstance(menuWidth - 2*margin, ((menuWidth - 2*margin)*3)/4, Image.SCALE_DEFAULT);
		
		colonizeButton = new Button(x + margin, y + menuHeight - 2*(menuWidth - 2*margin)/4, menuWidth-2*margin, (menuWidth - 2*margin)/4);
		colonizeButton.setAction(new Action(){
			@Override
			public void performAction() {
				Event evt = new Event(Event.EventTag.COLONIZE_PLANET, planetTer);
				EventBus.INSTANCE.publish(evt);
			}
		});
		EventBus.INSTANCE.addHandler(this);
	}
	
	public void setPlanet(Territory ter) {
		setPlayer(Player.WORLD);
		planetTer = ter;
		planetPicture = ter.getPlanet().getType() == Resource.METAL? metalPlanetPicture : gasPlanetPicture;
		colonizeButton.setImage("res/menu/world/colonizeButton" + View.res);
		colonizeButton.setEnabled(false);
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
		super.draw(g);
		/*
		 * Only draw if enabled
		 */
		if (isVisible()) {
			g.drawImage(planetPicture, getX() + margin, getY() + margin + 15,null);
			colonizeButton.draw(g);
		}
	}
	
	@Override
	public void performEvent(Event evt) {
		if (evt.getTag() == Event.EventTag.SHOW_PLANETMENU) {
			setPlanet((Territory)evt.getObjectValue());
			setVisible(true);
		} else if (evt.getTag() == Event.EventTag.HIDE_MENU) {
			setVisible(false);
		}
		if (evt.getTag() == Event.EventTag.COLONIZER_PRESENT) {
			colonizeButton.setEnabled(true);
		}
	}

}
