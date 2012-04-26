package riskyspace.view.menu.swingImpl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;

import riskyspace.GameManager;
import riskyspace.model.Colony;
import riskyspace.model.Player;
import riskyspace.services.Event;
import riskyspace.services.EventBus;
import riskyspace.view.Action;
import riskyspace.view.Button;
import riskyspace.view.View;
import riskyspace.view.menu.AbstractSideMenu;
/**
 * 
 * @author flygarn
 * Menu showing information and options for a Colony
 */
public class ColonyMenu extends AbstractSideMenu{

	/*
	 * Strings to be printed on the menu
	 */
	
	private Color ownerColor = null;
	
	private int margin = 30;
	
	private Image colonyPicture = null;
	private Button buildShipButton = null;
	
	/*
	 * Images
	 */
	private Map<Player, Image> cities = new HashMap<Player, Image>();
	
	public ColonyMenu(int x, int y, int menuWidth, int menuHeight) {
		super(x, y, menuWidth, menuHeight);
		
		cities.put(Player.BLUE, Toolkit.getDefaultToolkit().getImage("res/menu/blue/city" + View.res).
				getScaledInstance(menuWidth - 2*margin, ((menuWidth - 2*margin)*3)/4, Image.SCALE_DEFAULT));
		cities.put(Player.RED, Toolkit.getDefaultToolkit().getImage("res/menu/red/city" + View.res).
				getScaledInstance(menuWidth - 2*margin, ((menuWidth - 2*margin)*3)/4, Image.SCALE_DEFAULT));
		buildShipButton = new Button(x + margin, y + menuHeight - 2*(menuWidth - 2*margin)/4, menuWidth-2*margin, (menuWidth - 2*margin)/4);
		buildShipButton.setAction(new Action(){
			@Override
			public void performAction() {
				Event evt = new Event(Event.EventTag.SHIP_MENU, null);
				EventBus.INSTANCE.publish(evt);
			}
		});
		EventBus.INSTANCE.addHandler(this);
	}
	
	public void setColony(Colony colony) {
		setMenuName(colony.getName());
		setPlayer(colony.getOwner());
		ownerColor = GameManager.INSTANCE.getInfo(colony.getOwner()).getColor();
		colonyPicture = cities.get(colony.getOwner());
		buildShipButton.setImage("res/menu/" + colony.getOwner().toString().toLowerCase() + "/recruitButton" + View.res);
	}

	@Override
	public boolean mousePressed(Point p) {
		/*
		 * Only handle mouse event if enabled
		 */
		if (isVisible()) {
			if (buildShipButton.mousePressed(p)) {return true;}
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
			if (buildShipButton.mouseReleased(p)) {return true;}
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
			g.drawImage(colonyPicture, getX() + margin, getY() + margin + 15,null);
			drawColonyName(g);
			buildShipButton.draw(g);
		}
	}
	
	private void drawColonyName(Graphics g) {
		g.setColor(ownerColor);
		Font saveFont = g.getFont();
		g.setFont(new Font("Monotype", Font.BOLD, 38));
		int textX = getX() - (g.getFontMetrics().stringWidth(getMenuName()) / 2) + (getMenuWidth() / 2);
		int textY = getY() + (g.getFontMetrics().getHeight() / 2) + (2*margin + colonyPicture.getHeight(null));
		g.drawString(getMenuName(), textX, textY);
		g.setFont(saveFont);
	}

	@Override
	public void performEvent(Event evt) {
		// TEST EVENT (if object sent is colony)
		if (evt.getTag() == Event.EventTag.SHOW_MENU) {
			if (evt.getObjectValue() instanceof Colony) {
				setColony((Colony) evt.getObjectValue());
				setVisible(true);
			}
		} else if (evt.getTag() == Event.EventTag.HIDE_MENU || evt.getTag() == Event.EventTag.SHOW_RECRUITMENU) {
			setVisible(false);
		}
	}
}