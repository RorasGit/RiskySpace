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
import riskyspace.services.Event;
import riskyspace.services.EventBus;
import riskyspace.view.Action;
import riskyspace.view.Button;
import riskyspace.view.Fonts;
import riskyspace.view.RankIndicator;
import riskyspace.view.View;
import riskyspace.view.menu.AbstractSideMenu;

/**
 * 
 * @author flygarn
 *
 */
public class BuildingMenu extends AbstractSideMenu {

	private Colony colony = null;

	private Color ownerColor = null;
	private int margin;
	
	/*
	 * Upgrade Buttons.
	 */
	private Button upgradeMine;
	private Button upgradeTurret;
	private Button upgradeRadar;
	private Button upgradeHangar;
	
	/*
	 * Back button
	 */
	private Button backButton;
	
	/*
	 * Images for different Buildings
	 */
	private Image mineImage;
	private Image turretImage;
	private Image radarImage;
	private Image hangarImage;
	
	/*
	 * City Images
	 */
	private Image cityImage = null;
	private Map<Player, Image> cities = new HashMap<Player, Image>();
	
	/*
	 * Rank indicators
	 */
	private RankIndicator mineRank;
	private RankIndicator turretRank;
	private RankIndicator radarRank;
	private RankIndicator hangarRank;
	
	/*
	 * Strings with information to be drawn.
	 */
	private String currentTurretDamage;
	private String currentTurretShield;
	private String nextTurretDamage;
	private String nextTurretShield;
	private String nextTurretMetal;
	private String nextTurretGas;
	
	private String currentMineIncome;
	private String nextMineIncome;
	private String nextMineMetal;
	private String nextMineGas;
	
	private String currentRadarRange;
	private String nextRadarRange;
	private String nextRadarMetal;
	private String nextRadarGas;
	
	private String currentHangarPerk;
	private String nextHangarPerk;
	private String nextHangarMetal;
	private String nextHangarGas;
	
	public BuildingMenu(int x, int y, int menuWidth, int menuHeight) {
		super(x, y, menuWidth, menuHeight);
		//TODO Create buttons and Load Images
		margin = menuHeight/20;
		
		/*
		 * Load City Images for every Player
		 */
		int imageWidth = menuWidth - 2*margin;
		int imageHeight = ((menuWidth - 2*margin)*3)/4;
		cities.put(Player.BLUE, Toolkit.getDefaultToolkit().getImage("res/menu/blue/city" + View.res).
				getScaledInstance(imageWidth, imageHeight, Image.SCALE_DEFAULT));
		cities.put(Player.RED, Toolkit.getDefaultToolkit().getImage("res/menu/red/city" + View.res).
				getScaledInstance(imageWidth, imageHeight, Image.SCALE_DEFAULT));
		
		/*
		 * Create Buttons
		 */
		backButton = new Button(x + margin, y + menuHeight - 2*(menuWidth - 2*margin)/4, menuWidth-2*margin, (menuWidth - 2*margin)/4);
		backButton.setAction(new Action(){
			@Override
			public void performAction() {
				Event evt = new Event(Event.EventTag.SHOW_MENU, colony);
				EventBus.INSTANCE.publish(evt);
				setVisible(false);
			}
		});
		
		/*
		 * Create Rank Indicators
		 */
		// Set Rank
		mineRank = new RankIndicator(3);
		mineRank.setRank(2);
		turretRank = new RankIndicator(2);
		radarRank = new RankIndicator(3);
		radarRank.setRank(1);
		hangarRank = new RankIndicator(2);
		
		// Set Size
		int width = menuHeight/35;
		int height = 3*width;
		
		mineRank.setSize(width, height);
		turretRank.setSize(width, height);
		radarRank.setSize(width, height);
		hangarRank.setSize(width, height);
		
		// Set Location
		mineRank.setLocation(x + margin/2, y + 6*margin/2 + imageHeight);
		turretRank.setLocation(x + margin/2, y + height + 7*margin/2 + imageHeight);
		radarRank.setLocation(x + margin/2, y + height*2 + 8*margin/2 + imageHeight);
		hangarRank.setLocation(x + margin/2, y + height*3 + 9*margin/2 + imageHeight);
		
		/*
		 * Load Images
		 */
		turretImage = Toolkit.getDefaultToolkit().getImage("res/menu/turret_laser" + View.res).getScaledInstance(height, height, Image.SCALE_DEFAULT);
		mineImage = Toolkit.getDefaultToolkit().getImage("res/menu/mine" + View.res).getScaledInstance(height, height, Image.SCALE_DEFAULT);
		
		EventBus.INSTANCE.addHandler(this);
	}
	
	public void setColony(Colony colony) {
		/*
		 * TODO:
		 * Assign values to variables based on colony info.
		 * make sure the colony supplies sufficient information
		 * Immutable?
		 */
		this.colony = colony;
		setPlayer(colony.getOwner());
		setMenuName(colony.getName());
		ownerColor = GameManager.INSTANCE.getInfo(colony.getOwner()).getColor();
		cityImage = cities.get(colony.getOwner());
		
		String playerString = colony.getOwner().toString().toLowerCase();
		backButton.setImage("res/menu/" + playerString + "/backButton" + View.res);
	}
	
	@Override
	public void draw(Graphics g) {
		super.draw(g);
		//TODO Draw all stuff
		if (isVisible()) {
			g.drawImage(cityImage, getX() + margin, getY() + margin + 15,null);
			drawColonyName(g);
			backButton.draw(g);
			
			/*
			 * Draw Rank Indicators
			 */
			mineRank.draw(g);
			turretRank.draw(g);
			radarRank.draw(g);
			hangarRank.draw(g);
			
			/*
			 * Draw Images
			 */
			g.drawImage(turretImage, turretRank.getX() + turretRank.getWidth(), turretRank.getY(), null);
			g.drawImage(mineImage, mineRank.getX() + mineRank.getWidth(), mineRank.getY(), null);
		}
	}
	
	private void drawColonyName(Graphics g) {
		g.setColor(ownerColor);
		g.setFont(Fonts.getFont().deriveFont((float) getMenuHeight()/20));
		int textX = getX() - (g.getFontMetrics().stringWidth(getMenuName()) / 2) + (getMenuWidth() / 2);
		int textY = getY() + (g.getFontMetrics().getHeight() / 2) + (2*margin + cityImage.getHeight(null));
		g.drawString(getMenuName(), textX, textY);
	}
	
	@Override
	public boolean mousePressed(Point p) {
		if (isVisible()) {
			if (backButton.mousePressed(p)) {return true;}
//			else if (upgradeMine.mousePressed(p)) {return true;}
//			else if (upgradeTurret.mousePressed(p)) {return true;}
//			else if (upgradeRadar.mousePressed(p)) {return true;}
//			else if (upgradeHangar.mousePressed(p)) {return true;}
			else if (this.contains(p)) {
				return true;
			} else {
				return false;
			}
			} else {
				return false;
		}
	}

	@Override
	public boolean mouseReleased(Point p) {
		if (isVisible()) {
			if (this.contains(p)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public void performEvent(Event evt) {
		if (evt.getTag() == Event.EventTag.HIDE_MENU) {
			setVisible(false);
	} else if (evt.getTag() == Event.EventTag.RESOURCES_CHANGED) {
		PlayerStats stats = (PlayerStats) evt.getObjectValue();
//		checkRecruitOptions(stats); //CHECK 
	}
	}
}