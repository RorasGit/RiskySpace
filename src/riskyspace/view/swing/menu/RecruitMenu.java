package riskyspace.view.swing.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;

import riskyspace.GameManager;
import riskyspace.PlayerColors;
import riskyspace.model.Colony;
import riskyspace.model.Player;
import riskyspace.model.PlayerStats;
import riskyspace.model.Resource;
import riskyspace.model.ShipType;
import riskyspace.services.Event;
import riskyspace.services.EventBus;
import riskyspace.services.EventHandler;
import riskyspace.view.Action;
import riskyspace.view.ViewResources;
import riskyspace.view.View;
import riskyspace.view.swing.impl.SwingButton;

public class RecruitMenu extends AbstractSideMenu {
	
	private Color ownerColor = null;

	private int margin;

	private Colony colony = null;
	private PlayerStats stats = null;
	
	/*
	 * Build Buttons
	 */
	private SwingButton buildScoutButton = null;
	private SwingButton buildHunterButton = null;
	private SwingButton buildDestroyerButton = null;
	private SwingButton buildColonizerButton = null;
	
	/*
	 * Back Button
	 */
	private SwingButton backButton = null;
	
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
		
		buildScoutButton = new SwingButton(x + menuWidth/2 - margin/3 - 90, 2*y + menuHeight - (2*menuWidth), 90, 90);
		buildScoutButton.setAction(new Action() {
			@Override
			public void performAction() {
				Event evt = new Event(Event.EventTag.QUEUE_SHIP, ShipType.SCOUT);
				EventBus.CLIENT.publish(evt);
			}
		});
		buildHunterButton = new SwingButton(x + menuWidth/2 + margin/3, 2*y + menuHeight - (2*menuWidth), 90, 90);
		buildHunterButton.setAction(new Action() {
			@Override
			public void performAction() {
				Event evt = new Event(Event.EventTag.QUEUE_SHIP, ShipType.HUNTER);
				EventBus.CLIENT.publish(evt);
			}
		});
		buildDestroyerButton = new SwingButton(x + menuWidth/2 - margin/3 - 90, 2*y + menuHeight - (2*menuWidth) + 90 + margin/2, 90, 90);
		buildDestroyerButton.setAction(new Action() {
			@Override
			public void performAction() {
				Event evt = new Event(Event.EventTag.QUEUE_SHIP, ShipType.DESTROYER);
				EventBus.CLIENT.publish(evt);
			}
		});
		buildColonizerButton = new SwingButton(x + menuWidth/2 + margin/3, 2*y + menuHeight - (2*menuWidth) + 90 + margin/2, 90, 90);
		buildColonizerButton.setAction(new Action() {
			@Override
			public void performAction() {
				Event evt = new Event(Event.EventTag.QUEUE_SHIP, ShipType.COLONIZER);
				EventBus.CLIENT.publish(evt);
			}
		});
		backButton = new SwingButton(x + margin, y + menuHeight - 2*(menuWidth - 2*margin)/4, menuWidth-2*margin, (menuWidth - 2*margin)/4);
		backButton.setAction(new Action(){
			@Override
			public void performAction() { 
				setVisible(false);
			}
		});
	}
	
	public RecruitMenu(int x, int y, int menuWidth, int menuHeight, Action backAction) {
		this(x, y, menuWidth, menuHeight);
		backButton.setAction(backAction);
	}
	
	public void setColony(Colony colony) {
		this.colony = colony;
		setMenuName(colony.getName());
		ownerColor = PlayerColors.getColor(colony.getOwner());
		colonyPicture = cities.get(colony.getOwner());
		checkRecruitOptions(stats);
		
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

	public void checkRecruitOptions(PlayerStats stats) {
		this.stats = stats;
		if (colony != null && stats != null){
			buildScoutButton.setEnabled(stats.canAfford(ShipType.SCOUT) && colony.getHangar().canBuild(ShipType.SCOUT));
			buildHunterButton.setEnabled(stats.canAfford(ShipType.HUNTER) && colony.getHangar().canBuild(ShipType.HUNTER));
			buildColonizerButton.setEnabled(stats.canAfford(ShipType.COLONIZER) && colony.getHangar().canBuild(ShipType.COLONIZER));
			buildDestroyerButton.setEnabled(stats.canAfford(ShipType.DESTROYER) && colony.getHangar().canBuild(ShipType.DESTROYER));
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
