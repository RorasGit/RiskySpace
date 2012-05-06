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
import riskyspace.view.Fonts;
import riskyspace.view.View;
import riskyspace.view.menu.AbstractSideMenu;
import riskyspace.view.menu.IMenu;
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
	private Colony colony;
	
	private int margin;
	
	private Image colonyPicture = null;
	
	/*
	 * Buttons
	 */
	private Button buildShipButton = null;
	private Button buildingsButton = null;
	
	/*
	 * Sub Menus
	 */
	private RecruitMenu recruitMenu = null;
	private BuildingMenu buildingMenu = null;
	
	/*
	 * Images
	 */
	private Map<Player, Image> cities = new HashMap<Player, Image>();
	
	public ColonyMenu(int x, int y, int menuWidth, int menuHeight) {
		super(x, y, menuWidth, menuHeight);
		margin = menuHeight/20;
		recruitMenu = new RecruitMenu(x, y, menuWidth, menuHeight);
		buildingMenu = new BuildingMenu(x, y, menuWidth, menuHeight);
		cities.put(Player.BLUE, Toolkit.getDefaultToolkit().getImage("res/menu/blue/city" + View.res).
				getScaledInstance(menuWidth - 2*margin, ((menuWidth - 2*margin)*3)/4, Image.SCALE_DEFAULT));
		cities.put(Player.RED, Toolkit.getDefaultToolkit().getImage("res/menu/red/city" + View.res).
				getScaledInstance(menuWidth - 2*margin, ((menuWidth - 2*margin)*3)/4, Image.SCALE_DEFAULT));
		buildShipButton = new Button(x + margin, y + menuHeight - 2*(menuWidth - 2*margin)/4, menuWidth-2*margin, (menuWidth - 2*margin)/4);
		buildShipButton.setAction(new Action(){
			@Override
			public void performAction() {
				recruitMenu.setColony(colony);
				setVisible(false);
				recruitMenu.setVisible(true);
			}
		});
		buildingsButton = new Button(x + margin, y + menuHeight - 3*(menuWidth - margin)/4, menuWidth-2*margin, (menuWidth - 2*margin)/4);
		buildingsButton.setAction(new Action(){
			@Override
			public void performAction() {
				buildingMenu.setColony(colony);
				setVisible(false);
				buildingMenu.setVisible(true);
			}
		});
		EventBus.INSTANCE.addHandler(this);
	}
	
	public void setColony(Colony colony) {
		this.colony = colony;
		setMenuName(colony.getName());
		setPlayer(colony.getOwner());
		ownerColor = GameManager.INSTANCE.getInfo(colony.getOwner()).getColor();
		colonyPicture = cities.get(colony.getOwner());
		buildShipButton.setImage("res/menu/" + colony.getOwner().toString().toLowerCase() + "/recruitButton" + View.res);
		buildingsButton.setImage("res/menu/" + colony.getOwner().toString().toLowerCase() + "/buildingsButton" + View.res);
	}

	@Override
	public boolean mousePressed(Point p) {
		/*
		 * Only handle mouse event if enabled
		 */
		if (isVisible()) {
			if (buildShipButton.mousePressed(p)) {return true;}
			if (buildingsButton.mousePressed(p)) {return true;}
			if (this.contains(p)) {return true;}
			else {
				return false;
			}
		} else if (recruitMenu.isVisible()) {
			return recruitMenu.mousePressed(p);
		} else if (buildingMenu.isVisible()) {
			return buildingMenu.mousePressed(p);
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
			if (buildingsButton.mouseReleased(p)) {return true;}
			else {
				return false;
			}
		} else if (recruitMenu.isVisible()) {
			recruitMenu.mouseReleased(p);
		} else if (buildingMenu.isVisible()) {
			buildingMenu.mouseReleased(p);
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
			g.drawImage(colonyPicture, getX() + margin, getY() + margin + 15,null);
			drawColonyName(g);
			buildShipButton.draw(g);
			buildingsButton.draw(g);
		} else if (recruitMenu.isVisible()) {
			recruitMenu.draw(g);
		} else if (buildingMenu.isVisible()) {
			buildingMenu.draw(g);
		}
	}
	
	private void drawColonyName(Graphics g) {
		g.setColor(ownerColor);
		g.setFont(Fonts.getFont().deriveFont((float) getMenuHeight()/20));
		int textX = getX() - (g.getFontMetrics().stringWidth(getMenuName()) / 2) + (getMenuWidth() / 2);
		int textY = getY() + (g.getFontMetrics().getHeight() / 2) + (2*margin + colonyPicture.getHeight(null));
		g.drawString(getMenuName(), textX, textY);
	}

	@Override
	public void performEvent(Event evt) {
		if (evt.getTag() == Event.EventTag.SHOW_MENU) {
			if (evt.getObjectValue() instanceof Colony) {
				setColony((Colony) evt.getObjectValue());
				setVisible(true);
			}
		} else if (evt.getTag() == Event.EventTag.HIDE_MENU) {
			setVisible(false);
		}
	}
}