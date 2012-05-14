package riskyspace.view.swing.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import riskyspace.GameManager;
import riskyspace.PlayerColors;
import riskyspace.model.BuildAble;
import riskyspace.model.Colony;
import riskyspace.model.Player;
import riskyspace.model.PlayerStats;
import riskyspace.services.Event;
import riskyspace.services.EventBus;
import riskyspace.view.Action;
import riskyspace.view.IMenu;
import riskyspace.view.ViewResources;
import riskyspace.view.View;
import riskyspace.view.swing.impl.SwingButton;
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
	private SwingButton buildShipButton = null;
	private SwingButton buildingsButton = null;
	
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
		recruitMenu = new RecruitMenu(x, y, menuWidth, menuHeight, new Action() {
			@Override
			public void performAction() {
				setVisible(true);
				recruitMenu.setVisible(false);
			}
		});
		buildingMenu = new BuildingMenu(x, y, menuWidth, menuHeight, new Action() {
			@Override
			public void performAction() {
				setVisible(true);
				buildingMenu.setVisible(false);
			}
		});
		cities.put(Player.BLUE, Toolkit.getDefaultToolkit().getImage("res/menu/blue/city" + View.res).
				getScaledInstance(menuWidth - 2*margin, ((menuWidth - 2*margin)*3)/4, Image.SCALE_DEFAULT));
		cities.put(Player.RED, Toolkit.getDefaultToolkit().getImage("res/menu/red/city" + View.res).
				getScaledInstance(menuWidth - 2*margin, ((menuWidth - 2*margin)*3)/4, Image.SCALE_DEFAULT));
		buildShipButton = new SwingButton(x + margin, y + menuHeight - 2*(menuWidth - 2*margin)/4, menuWidth-2*margin, (menuWidth - 2*margin)/4);
		buildShipButton.setImage("res/menu/recruit" + View.res);
		buildShipButton.setAction(new Action(){
			@Override
			public void performAction() {
				setVisible(false);
				recruitMenu.setVisible(true);
			}
		});
		buildingsButton = new SwingButton(x + margin, y + menuHeight - 3*(menuWidth - margin)/4, menuWidth-2*margin, (menuWidth - 2*margin)/4);
		buildingsButton.setImage("res/menu/build" + View.res);
		buildingsButton.setAction(new Action(){
			@Override
			public void performAction() {
				setVisible(false);
				buildingMenu.setVisible(true);
			}
		});
	}
	
	public void setColony(Colony colony) {
		this.colony = colony;
		buildingMenu.setColony(colony);
		recruitMenu.setColony(colony);
		setMenuName(colony.getName());
		ownerColor = PlayerColors.getColor(colony.getOwner());
		colonyPicture = cities.get(colony.getOwner());
	}

	public Colony getColony() {
		return colony;
	}
	
	@Override
	public boolean mousePressed(Point p) {
		/*
		 * Only handle mouse event if enabled
		 */
		if (recruitMenu.isVisible()) {
			return recruitMenu.mousePressed(p);
		} else if (buildingMenu.isVisible()) {
			return buildingMenu.mousePressed(p);
		} else if (isVisible()) {
			if (buildShipButton.mousePressed(p)) {return true;}
			if (buildingsButton.mousePressed(p)) {return true;}
			if (this.contains(p)) {return true;}
		}
		return false;
	}

	public void setStats(PlayerStats stats) {
		recruitMenu.checkRecruitOptions(stats);
		buildingMenu.setStats(stats);
	}

	public void setQueues(Map<Colony, List<BuildAble>> colonyQueues) {
		buildingMenu.setQueue(colonyQueues);
	}

	@Override
	public boolean mouseReleased(Point p) {
		/*
		 * Only handle mouse event if enabled
		 */
		if (recruitMenu.isVisible()) {
			recruitMenu.mouseReleased(p);
		} else if (buildingMenu.isVisible()) {
			buildingMenu.mouseReleased(p);
		} else if (isVisible()) {
			if (buildShipButton.mouseReleased(p)) {return true;}
			if (buildingsButton.mouseReleased(p)) {return true;}
		}
		return false;
	}

	@Override
	public void draw(Graphics g) {
		/*
		 * Only draw if enabled
		 */
		if (recruitMenu.isVisible()) {
			recruitMenu.draw(g);
		} else if (buildingMenu.isVisible()) {
			buildingMenu.draw(g);
		} else if (isVisible()) {
			super.draw(g);
			g.drawImage(colonyPicture, getX() + margin, getY() + 3*margin/2,null);
			drawColonyName(g);
			buildShipButton.draw(g);
			buildingsButton.draw(g);
		}
	}
	
	private void drawColonyName(Graphics g) {
		g.setColor(ownerColor);
		g.setFont(ViewResources.getFont().deriveFont((float) getMenuHeight()/20));
		int textX = getX() - (g.getFontMetrics().stringWidth(getMenuName()) / 2) + (getMenuWidth() / 2);
		int textY = getY() + (g.getFontMetrics().getHeight() / 2) + (2*margin + colonyPicture.getHeight(null));
		g.drawString(getMenuName(), textX, textY);
	}
	
	@Override
	public void setVisible(boolean enabled) {
		super.setVisible(enabled);
		recruitMenu.setVisible(false);
		buildingMenu.setVisible(false);
	}
	
	@Override
	public boolean isVisible() {
		return super.isVisible() || recruitMenu.isVisible() || buildingMenu.isVisible();
	}
}