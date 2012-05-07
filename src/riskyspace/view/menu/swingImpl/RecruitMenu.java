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
import riskyspace.model.PlayerStats;
import riskyspace.model.Resource;
import riskyspace.model.ShipType;
import riskyspace.services.Event;
import riskyspace.services.EventBus;
import riskyspace.services.EventHandler;
import riskyspace.view.Action;
import riskyspace.view.Button;
import riskyspace.view.ViewResources;
import riskyspace.view.View;
import riskyspace.view.menu.AbstractSideMenu;

public class RecruitMenu extends AbstractSideMenu {
	
	private Color ownerColor = null;

	private int margin;

	private Colony colony = null;
	
	/*
	 * Build Buttons
	 */
	private Button buildScoutButton = null;
	private Button buildHunterButton = null;
	private Button buildDestroyerButton = null;
	private Button buildColonizerButton = null;
	
	/*
	 * Back Button
	 */
	private Button backButton = null;
	
	/*
	 * Images
	 */
	private Image colonyPicture = null;
	private Map<Player, Image> cities = new HashMap<Player, Image>();
	
	public RecruitMenu(int x, int y, int menuWidth, int menuHeight) {
		super(x, y, menuWidth, menuHeight);
		margin = menuHeight/20;
		cities.put(Player.BLUE, Toolkit.getDefaultToolkit().getImage("res/menu/blue/city" + View.res).
				getScaledInstance(menuWidth - 2*margin, ((menuWidth - 2*margin)*3)/4, Image.SCALE_DEFAULT));
		cities.put(Player.RED, Toolkit.getDefaultToolkit().getImage("res/menu/red/city" + View.res).
				getScaledInstance(menuWidth - 2*margin, ((menuWidth - 2*margin)*3)/4, Image.SCALE_DEFAULT));
		
		buildScoutButton = new Button(x + menuWidth/2 - margin/3 - 90, 2*y + menuHeight - (2*menuWidth), 90, 90);
		buildScoutButton.setAction(new Action() {
			@Override
			public void performAction() {
				Event evt = new Event(Event.EventTag.QUEUE_SHIP, ShipType.SCOUT);
				EventBus.INSTANCE.publish(evt);
			}
		});
		buildHunterButton = new Button(x + menuWidth/2 + margin/3, 2*y + menuHeight - (2*menuWidth), 90, 90);
		buildHunterButton.setAction(new Action() {
			@Override
			public void performAction() {
				Event evt = new Event(Event.EventTag.QUEUE_SHIP, ShipType.HUNTER);
				EventBus.INSTANCE.publish(evt);
			}
		});
		buildDestroyerButton = new Button(x + menuWidth/2 - margin/3 - 90, 2*y + menuHeight - (2*menuWidth) + 90 + margin/2, 90, 90);
		buildDestroyerButton.setAction(new Action() {
			@Override
			public void performAction() {
				Event evt = new Event(Event.EventTag.QUEUE_SHIP, ShipType.DESTROYER);
				EventBus.INSTANCE.publish(evt);
			}
		});
		buildColonizerButton = new Button(x + menuWidth/2 + margin/3, 2*y + menuHeight - (2*menuWidth) + 90 + margin/2, 90, 90);
		buildColonizerButton.setAction(new Action() {
			@Override
			public void performAction() {
				Event evt = new Event(Event.EventTag.QUEUE_SHIP, ShipType.COLONIZER);
				EventBus.INSTANCE.publish(evt);
			}
		});
		backButton = new Button(x + margin, y + menuHeight - 2*(menuWidth - 2*margin)/4, menuWidth-2*margin, (menuWidth - 2*margin)/4);
		backButton.setAction(new Action(){
			@Override
			public void performAction() {
				Event evt = new Event(Event.EventTag.SHOW_MENU, colony);
				EventBus.INSTANCE.publish(evt);
				setVisible(false);
			}
		});
		EventBus.INSTANCE.addHandler(this);
	}
	
	public void setColony(Colony colony) {
		this.colony = colony;
		setMenuName(colony.getName());
		ownerColor = GameManager.INSTANCE.getInfo(colony.getOwner()).getColor();
		colonyPicture = cities.get(colony.getOwner());
		
		String playerString = colony.getOwner().toString().toLowerCase();
		backButton.setImage("res/menu/" + playerString + "/backButton" + View.res);
		buildColonizerButton.setImage("res/menu/" + playerString + "/button/colonizerButton" + View.res);
		buildScoutButton.setImage("res/menu/" + playerString + "/button/scoutButton" + View.res);
		buildHunterButton.setImage("res/menu/" + playerString + "/button/hunterButton" + View.res);
		buildDestroyerButton.setImage("res/menu/" + playerString + "/button/destroyerButton" + View.res);
	}
	
	private void drawColonyName(Graphics g) {
		g.setColor(ownerColor);
		g.setFont(ViewResources.getFont().deriveFont((float) getMenuHeight()/20));
		int textX = getX() - (g.getFontMetrics().stringWidth(getMenuName()) / 2) + (getMenuWidth() / 2);
		int textY = getY() + (g.getFontMetrics().getHeight() / 2) + (2*margin + colonyPicture.getHeight(null));
		g.drawString(getMenuName(), textX, textY);
	}

	private void checkRecruitOptions(PlayerStats stats) {
		buildScoutButton.setEnabled(stats.getResource(Resource.METAL) >= ShipType.SCOUT.getMetalCost());
		buildHunterButton.setEnabled(stats.getResource(Resource.METAL) >= ShipType.HUNTER.getMetalCost() && stats.getResource(Resource.GAS) >= ShipType.HUNTER.getGasCost());
		buildColonizerButton.setEnabled(stats.getResource(Resource.METAL) >= ShipType.COLONIZER.getMetalCost());
		buildDestroyerButton.setEnabled(stats.getResource(Resource.METAL) >= ShipType.DESTROYER.getMetalCost() && stats.getResource(Resource.GAS) >= ShipType.DESTROYER.getGasCost());
	}

	@Override
	public void performEvent(Event evt) {
		if (evt.getTag() == Event.EventTag.HIDE_MENU) {
				setVisible(false);
		} else if (evt.getTag() == Event.EventTag.RESOURCES_CHANGED) {
			PlayerStats stats = (PlayerStats) evt.getObjectValue();
			checkRecruitOptions(stats);
		}
	}

	@Override
	public boolean mousePressed(Point p) {
		/*
		 * Only handle mouse event if enabled
		 */
		if (isVisible()) {
			if (buildScoutButton.mousePressed(p)) {return true;}
			else if (buildHunterButton.mousePressed(p)) {return true;}
			else if (buildDestroyerButton.mousePressed(p)) {return true;} 
			else if (buildColonizerButton.mousePressed(p)) {return true;}
			else if (backButton.mousePressed(p)) {return true;}
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
			if (buildScoutButton.mouseReleased(p)) {return true;}
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
			buildScoutButton.draw(g);
			buildHunterButton.draw(g);
			buildDestroyerButton.draw(g);
			buildColonizerButton.draw(g);
			backButton.draw(g);
		}
	}
}
