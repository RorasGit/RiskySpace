package riskyspace.view.opengl.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.media.opengl.GLAutoDrawable;


import riskyspace.PlayerColors;
import riskyspace.model.BuildAble;
import riskyspace.model.Colony;
import riskyspace.model.Player;
import riskyspace.model.PlayerStats;
import riskyspace.model.building.Hangar;
import riskyspace.model.building.Mine;
import riskyspace.model.building.Radar;
import riskyspace.model.building.Turret;
import riskyspace.services.Event;
import riskyspace.services.EventBus;
import riskyspace.view.Action;
import riskyspace.view.ViewResources;
import riskyspace.view.opengl.GLRenderAble;
import riskyspace.view.opengl.Rectangle;
import riskyspace.view.opengl.impl.GLButton;
import riskyspace.view.opengl.impl.GLRankIndicator;
import riskyspace.view.opengl.impl.GLSprite;

/**
 * 
 * @author Daniel Augurell
 *
 */
public class GLBuildingMenu extends GLAbstractSideMenu {

	private Colony colony = null;
	private PlayerStats stats = null;
	private Map<Colony, List<BuildAble>> colonyQueues = new HashMap<Colony, List<BuildAble>>();
	
	private boolean mineUpgrading = false;
	private boolean turretUpgrading = false;
	private boolean radarUpgrading = false;
	private boolean hangarUpgrading = false;
	
	private Color ownerColor = null;
	private int margin;
	
	/*
	 * Upgrade Buttons.
	 */
	private GLButton upgradeMine;
	private GLButton upgradeTurret;
	private GLButton upgradeRadar;
	private GLButton upgradeHangar;
	
	/*
	 * Back button
	 */
	private GLButton backButton;
	
	/*
	 * Images for different Buildings
	 */
	private GLSprite mineSprite;
	private GLSprite turretSprite;
	private GLSprite radarSprite;
	private GLSprite hangarSprite;
	
	/*
	 * City Sprites
	 */
	private GLSprite citySprite = null;
	private Map<Player, GLSprite> cities = new HashMap<Player, GLSprite>();
	
	/*
	 * Split Sprite and Rectangle location/size
	 */
	private GLSprite split = null;
	private Rectangle[] splits = null;
	
	/*
	 * In Progress Sprite and Rectangle location/size
	 */
	private GLSprite inProgress = null;
	private Rectangle[] progressRects = null;
	
	/*
	 * Rank indicators
	 */
	private GLRankIndicator mineRank;
	private GLRankIndicator turretRank;
	private GLRankIndicator radarRank;
	private GLRankIndicator hangarRank;
	
	/*
	 * Building name Strings
	 */
	private String mine = "MINING OPERATIONS";
	private String turret = "DEFENSE SYSTEM";
	private String radar = "SPACE RADAR";
	private String hangar = "FLEET HANGAR";
	
	/*
	 * Strings with information to be drawn.
	 */
	private String level = "Level ";
	
	private String currentMineLevel = "";
	private String nextMineLevel = "";
	private String currentMineIncome = "";
	private String nextMineIncome = "";
	private String nextMineMetal = "";
	private String nextMineGas = "";
	
	private String currentTurretLevel = "";
	private String nextTurretLevel = "";
	private String currentTurretDamage = "";
	private String currentTurretShield = "";
	private String nextTurretDamage = "";
	private String nextTurretShield = "";
	private String nextTurretMetal = "";
	private String nextTurretGas = "";
	
	private String currentRadarLevel = "";
	private String nextRadarLevel = "";
	private String currentRadarRange = "";
	private String nextRadarRange = "";
	private String currentRadarRange2 = "";
	private String nextRadarRange2 = "";
	private String nextRadarMetal = "";
	private String nextRadarGas = "";
	
	private String currentHangarLevel = "";
	private String nextHangarLevel = "";
	private String currentHangarPerk = "";
	private String currentHangarPerk2 = "";
	private String nextHangarPerk = "";
	private String nextHangarPerk2 = "";
	private String nextHangarMetal = "";
	private String nextHangarGas = "";
	
	private Font titleFont;
	private Font infoFont;
	
	public GLBuildingMenu(int x, int y, int menuWidth, int menuHeight) {
		super(x, y, menuWidth, menuHeight);
		//TODO Create buttons and Load Sprites
		margin = menuHeight/20;
		
		/*
		 * Load City Sprites for every Player
		 */
		int imageWidth = menuWidth - 2*margin;
		int imageHeight = ((menuWidth - 2*margin)*3)/4;
		setPicture(imageWidth, imageHeight);
		
		/*
		 * Create Rank Indicators
		 */
		// Set Rank
		mineRank = new GLRankIndicator(3);
		turretRank = new GLRankIndicator(3);
		radarRank = new GLRankIndicator(3);
		hangarRank = new GLRankIndicator(3);
		
		// Set Size for Rank Indicators
		int width = menuHeight/35;
		int height = 3*width;

		mineRank.setSize(width, height);
		turretRank.setSize(width, height);
		radarRank.setSize(width, height);
		hangarRank.setSize(width, height);
		
		// Set Location
		mineRank.setLocation(x + margin/2, 7*margin/2 + imageHeight);
		turretRank.setLocation(x + margin/2, height + 9*margin/2 + imageHeight);
		radarRank.setLocation(x + margin/2, height*2 + 11*margin/2 + imageHeight);
		hangarRank.setLocation(x + margin/2, height*3 + 13*margin/2 + imageHeight);
		
		setButtons(menuWidth, menuHeight, height);
	}

	private void setButtons(int menuWidth, int menuHeight, int height) {
		/*
		 * Button Sizes
		 */
		int upgradeButtonWidth = height;
		int upgradeButtonHeight = height/4;
		
		/*
		 * Load Sprites
		 */
		mineSprite = 	new GLSprite("menu/mine", 			 72, 72);
		turretSprite = 	new GLSprite("menu/turret_laser", 	 72, 72);
		radarSprite = 	new GLSprite("menu/radar", 			 72, 72);
		hangarSprite = 	new GLSprite("menu/hangar", 		 72, 72);
		split =			new GLSprite("menu/split",			 15, 72);
		inProgress = 	new GLSprite("menu/progress", 		128, 32);
		
		/*
		 * Set location and height of Sprites
		 */
		int sHeight = Toolkit.getDefaultToolkit().getScreenSize().height; // Screen Height
		int yDiff = sHeight - height; // (yDiff - y) will give correct openGL Y value
		mineSprite.setBounds(new Rectangle(mineRank.getX() + mineRank.getWidth(),
				yDiff - mineRank.getY(), height, height));
		turretSprite.setBounds(new Rectangle(turretRank.getX() + turretRank.getWidth(),
				yDiff - turretRank.getY(), height, height));
		radarSprite.setBounds(new Rectangle(radarRank.getX() + radarRank.getWidth(),
				yDiff - radarRank.getY(), height, height));
		hangarSprite.setBounds(new Rectangle(hangarRank.getX() + hangarRank.getWidth(),
				yDiff - hangarRank.getY(), height, height));
		
		int splitWidth = height/5; //Width of the "split" Image
		/*
		 * X location of "split" image
		 */
		int imageRightX = mineRank.getX() + mineRank.getWidth() + height; // Right side of Building Icon
		int splitX = (menuWidth - margin - mineRank.getWidth() - height)/2 - splitWidth/2;
		splitX += imageRightX;
		
		splits = new Rectangle[]{
				new Rectangle(splitX, yDiff - mineRank.getY(), splitWidth, height),
				new Rectangle(splitX, yDiff - turretRank.getY(), splitWidth, height),
				new Rectangle(splitX, yDiff - radarRank.getY(), splitWidth, height),
				new Rectangle(splitX, yDiff - hangarRank.getY(), splitWidth, height),
		};
		
		/*
		 * X location of the "inProgress" image and upgrade buttons
		 */
		int upgradeX = getX() + getMenuWidth() - 3*margin/5 - upgradeButtonWidth;
		
		progressRects = new Rectangle[]{
			new Rectangle(upgradeX, yDiff - mineRank.getY(), 	upgradeButtonWidth, upgradeButtonHeight),
			new Rectangle(upgradeX, yDiff - turretRank.getY(), upgradeButtonWidth, upgradeButtonHeight),
			new Rectangle(upgradeX, yDiff - radarRank.getY(), 	upgradeButtonWidth, upgradeButtonHeight),
			new Rectangle(upgradeX, yDiff - hangarRank.getY(), upgradeButtonWidth, upgradeButtonHeight),
		};
		
		/*
		 * Create Buttons
		 * Does not use yDiff as we want the y Coordinate in awt coordinate space for clicks
		 * The Button will take care of correct drawing itself
		 */
		
		upgradeMine = new GLButton(upgradeX 
				, mineRank.getY() + mineRank.getHeight() - upgradeButtonHeight
				, upgradeButtonWidth, upgradeButtonHeight);
		upgradeMine.setTexture("menu/upgrade", 128, 32);
		upgradeMine.setAction(new Action(){
			@Override
			public void performAction() {
				Event evt = new Event(Event.EventTag.QUEUE_BUILDING, "MINE");
				EventBus.CLIENT.publish(evt);
			}
		});
		
		upgradeTurret = new GLButton(upgradeX 
				, turretRank.getY() + turretRank.getHeight() - upgradeButtonHeight
				, upgradeButtonWidth, upgradeButtonHeight);
		upgradeTurret.setTexture("menu/upgrade", 128, 32);
		upgradeTurret.setAction(new Action(){
			@Override
			public void performAction() {
				Event evt = new Event(Event.EventTag.QUEUE_BUILDING, "TURRET");
				EventBus.CLIENT.publish(evt);
			}
		});
		
		
		upgradeRadar = new GLButton(upgradeX 
				, radarRank.getY() + radarRank.getHeight() - upgradeButtonHeight
				, upgradeButtonWidth, upgradeButtonHeight);
		upgradeRadar.setTexture("menu/upgrade", 128, 32);
		upgradeRadar.setAction(new Action(){
			@Override
			public void performAction() {
				Event evt = new Event(Event.EventTag.QUEUE_BUILDING, "RADAR");
				EventBus.CLIENT.publish(evt);
			}
		});
		
		upgradeHangar = new GLButton(upgradeX 
				, hangarRank.getY() + hangarRank.getHeight() - upgradeButtonHeight
				, upgradeButtonWidth, upgradeButtonHeight);
		upgradeHangar.setTexture("menu/upgrade", 128, 32);
		upgradeHangar.setAction(new Action(){
			@Override
			public void performAction() {
				Event evt = new Event(Event.EventTag.QUEUE_BUILDING, "HANGAR");
				EventBus.CLIENT.publish(evt);
			}
		});
		
		backButton = new GLButton(getX() + margin, 
				 getY() + getMenuHeight() - margin - (menuWidth - 2*margin)/4, 
				getBounds().getWidth()-2*margin, (getBounds().getWidth() - 2*margin)/4);
		backButton.setTexture("menu/back", 128, 32);
		backButton.setAction(new Action(){
			@Override
			public void performAction() {
				setVisible(false);
			}
		});
	}

	public GLBuildingMenu(int x, int y, int menuWidth, int menuHeight, Action backAction) {
		this(x, y, menuWidth, menuHeight);
		backButton.setAction(backAction);
		titleFont = ViewResources.getFont().deriveFont(menuHeight/40f);
		infoFont = new Font("Tahoma", Font.PLAIN, menuHeight/80);
	}
	
	private void setPicture(int imageWidth, int imageHeight){
		int sHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
		Rectangle imageRenderRect = new Rectangle(getX() + margin, 
				sHeight - (getY() + imageHeight + 3*margin/2), 
				imageWidth, imageHeight);

		GLSprite GLtmp = new GLSprite("menu/city_red", 1280, 700);
		GLtmp.setBounds(imageRenderRect);
		cities.put(Player.RED, GLtmp);
		GLtmp = new GLSprite("menu/city_blue", 900, 486);
		GLtmp.setBounds(imageRenderRect);
		cities.put(Player.BLUE, GLtmp);
		citySprite = GLtmp;
		// TODO: YELLOW / GREEN
//		GLSprite GLtmp = new GLSprite("menu/city_red", 1280, 700);
//		GLtmp.setBounds(imageRenderRect);
//		cities.put(Player.RED, GLtmp);
//		GLtmp = new GLSprite("menu/city_blue", 900, 486);
//		GLtmp.setBounds(imageRenderRect);
//		cities.put(Player.BLUE, GLtmp);
//		citySprite = GLtmp;
	}
	
	public void setColony(Colony colony) {
		this.colony = colony;
		checkBuildOptions();
		checkQueue();
		setMenuName(colony.getName());
		ownerColor = PlayerColors.getColor(colony.getOwner());
		citySprite = cities.get(colony.getOwner());
		
		/*
		 * Set Rank Indicators
		 */
		mineRank.setRank(colony.getMine().getRank());
		turretRank.setRank(colony.getTurret().getRank());
		radarRank.setRank(colony.getRadar().getRank());
		hangarRank.setRank(colony.getHangar().getRank());
		
		/*
		 * Get Level Strings
		 */
		currentMineLevel = colony.getMine().getRank() > 0 ? level + colony.getMine().getRank() : "No Mine";
		if (colony.getMine().isMaxRank()) {
			nextMineLevel = "Maxed";
		} else {
			nextMineLevel = level + (colony.getMine().getRank() + 1);
		}
		currentTurretLevel = colony.getTurret().getRank() > 0 ? level + colony.getTurret().getRank() : "No Turret";
		if (colony.getTurret().isMaxRank()) {
			nextTurretLevel = "Maxed";
		} else {
			nextTurretLevel = level + (colony.getTurret().getRank() + 1);
		}
		currentRadarLevel = colony.getRadar().getRank() > 0 ? level + colony.getRadar().getRank() : "No Radar";
		if (colony.getRadar().isMaxRank()) {
			nextRadarLevel = "Maxed";
		} else {
			nextRadarLevel = level + (colony.getRadar().getRank() + 1);
		}
		currentHangarLevel = colony.getHangar().getRank() > 0 ? level + colony.getHangar().getRank() : "No Hangar";
		if (colony.getHangar().isMaxRank()) {
			nextHangarLevel = "Maxed";
		} else {
			nextHangarLevel = level + (colony.getHangar().getRank() + 1);
		}
		
		/*
		 * Get Info Strings
		 */
		currentMineIncome = colony.getMine().getDescriptiveString(colony.getMine().getRank());
		nextMineIncome = colony.getMine().getDescriptiveString(colony.getMine().getRank() + 1);
		
		String[] currentTurretInfo = colony.getTurret().getDescriptiveString(colony.getTurret().getRank()).split("\n");
		String[] nextTurretInfo = colony.getTurret().getDescriptiveString(colony.getTurret().getRank() + 1).split("\n");
		currentTurretDamage = currentTurretInfo[0];
		currentTurretShield = currentTurretInfo[1];
		if (nextTurretInfo.length > 1) {
			nextTurretDamage = nextTurretInfo[0];
			nextTurretShield = nextTurretInfo[1];
		} else {
			nextTurretDamage = "";
			nextTurretShield = "";
		}
		
		String[] currentRadarInfo = colony.getRadar().getDescriptiveString(colony.getRadar().getRank()).split("\n");
		String[] nextRadarInfo = colony.getRadar().getDescriptiveString(colony.getRadar().getRank() + 1).split("\n");
		currentRadarRange = currentRadarInfo[0];
		if (currentRadarInfo.length > 1) {
			currentRadarRange2 = currentRadarInfo[1];
		} else {
			currentRadarRange2 = "";
		}
		nextRadarRange = nextRadarInfo[0];
		if (nextRadarInfo.length > 1) {
			nextRadarRange2 = nextRadarInfo[1];
		} else {
			nextRadarRange2 = "";
		}
		
		String[] currentHangarInfo = colony.getHangar().getDescriptiveString(colony.getHangar().getRank()).split("\n");
		String[] nextHangarInfo = colony.getHangar().getDescriptiveString(colony.getHangar().getRank() + 1).split("\n");
		currentHangarPerk = currentHangarInfo[0];
		if (currentHangarInfo.length > 1) {
			currentHangarPerk2 = currentHangarInfo[1];
		} else {
			currentHangarPerk2 = "";
		}
		nextHangarPerk = nextHangarInfo[0];
		if (nextHangarInfo.length > 1) {
			nextHangarPerk2 = nextHangarInfo[1];
		} else {
			nextHangarPerk2 = "";
		}
		
		/*
		 * Get Next Cost
		 */
		nextMineMetal = colony.getMine().getMetalCost() + " M | ";
		nextMineGas = colony.getMine().getGasCost() + " G";
		
		nextTurretMetal = colony.getTurret().getMetalCost() + " M | ";
		nextTurretGas = colony.getTurret().getGasCost()  + " G";
		
		nextRadarMetal = colony.getRadar().getMetalCost() + " M | ";
		nextRadarGas = colony.getRadar().getGasCost()  + " G";
		
		nextHangarMetal = colony.getHangar().getMetalCost()  + " M | ";
		nextHangarGas = colony.getHangar().getGasCost()  + " G";
	}
	
	@Override
	public void draw(GLAutoDrawable drawable, Rectangle objectRect, Rectangle targetArea, int zIndex) {
		super.draw(drawable, objectRect, targetArea, zIndex);
		if (!isVisible()) {
			return; // Only draw if visible
		}
		zIndex++;
		citySprite.draw(drawable, citySprite.getBounds(), targetArea, zIndex);
		
		((GLRenderAble)mineRank).draw(drawable, mineRank.getBounds(), targetArea, zIndex);
		((GLRenderAble)turretRank).draw(drawable, turretRank.getBounds(), targetArea, zIndex);
		((GLRenderAble)radarRank).draw(drawable, radarRank.getBounds(), targetArea, zIndex);
		((GLRenderAble)hangarRank).draw(drawable, hangarRank.getBounds(), targetArea, zIndex);
		
		turretSprite.draw(drawable, turretSprite.getBounds(), targetArea, zIndex);
		mineSprite.draw(drawable, mineSprite.getBounds(), targetArea, zIndex);
		hangarSprite.draw(drawable, hangarSprite.getBounds(), targetArea, zIndex);
		radarSprite.draw(drawable, radarSprite.getBounds(), targetArea, zIndex);
		
		split.draw(drawable, splits[0], targetArea, zIndex);
		split.draw(drawable, splits[1], targetArea, zIndex);
		split.draw(drawable, splits[2], targetArea, zIndex);
		split.draw(drawable, splits[3], targetArea, zIndex);
		
		/*
		 * Draw Buttons
		 * Buttons know their own location
		 */
		upgradeMine.draw(drawable, null, targetArea, zIndex);
		upgradeTurret.draw(drawable, null, targetArea, zIndex);
		upgradeRadar.draw(drawable, null, targetArea, zIndex);
		upgradeHangar.draw(drawable, null, targetArea, zIndex);
		backButton.draw(drawable, null, targetArea, zIndex);
		
		drawProgressIndicators(drawable, objectRect, targetArea, zIndex);
	}
	
	private void drawProgressIndicators(GLAutoDrawable drawable, Rectangle objectRect, Rectangle targetArea, int zIndex) {
		int x = getX() + getMenuWidth() - 3*margin/5 - inProgress.getBounds().getWidth();
		if (mineUpgrading) {
			inProgress.draw(drawable, new Rectangle(x, mineRank.getY() + inProgress.getBounds().getHeight(), inProgress.getBounds().getWidth(), inProgress.getBounds().getHeight()), targetArea, zIndex);
		}
		if (turretUpgrading) {
			inProgress.draw(drawable, new Rectangle(x, turretRank.getY() + inProgress.getBounds().getHeight(), inProgress.getBounds().getWidth(), inProgress.getBounds().getHeight()), targetArea, zIndex);

		}
		if (radarUpgrading) {
			inProgress.draw(drawable, new Rectangle(x, radarRank.getY() + inProgress.getBounds().getHeight(), inProgress.getBounds().getWidth(), inProgress.getBounds().getHeight()), targetArea, zIndex);

		}
		if (hangarUpgrading) {
			inProgress.draw(drawable, new Rectangle(x, hangarRank.getY() + inProgress.getBounds().getHeight(), inProgress.getBounds().getWidth(), inProgress.getBounds().getHeight()), targetArea, zIndex);

		}
	}
	
//	private void drawColonyName(Graphics g) {
//		g.setColor(ownerColor);
//		g.setFont(ViewResources.getFont().deriveFont((float) getMenuHeight()/20));
//		int textX = getX() - (g.getFontMetrics().stringWidth(getMenuName()) / 2) + (getMenuWidth() / 2);
//		int textY = getY() + (g.getFontMetrics().getHeight() / 2) + (2*margin + cityImage.getHeight(null));
//		g.drawString(getMenuName(), textX, textY);
//	}
	
	@Override
	public boolean mousePressed(Point p) {
		if (isVisible()) {
			if (backButton.mousePressed(p)) {return true;}
			else if (upgradeMine.mousePressed(p)) {return true;}
			else if (upgradeTurret.mousePressed(p)) {return true;}
			else if (upgradeRadar.mousePressed(p)) {return true;}
			else if (upgradeHangar.mousePressed(p)) {return true;}
			else if (this.contains(p)) {
				return true;
			}
		}
		return false;
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

	private void checkBuildOptions() {
		if (colony != null && stats != null) {
			upgradeMine.setEnabled(!mineUpgrading && !colony.getMine().isMaxRank() && stats.canAfford(colony.getMine()));
			upgradeTurret.setEnabled(!turretUpgrading && !colony.getTurret().isMaxRank() && stats.canAfford(colony.getTurret()));
			upgradeRadar.setEnabled(!radarUpgrading && !colony.getRadar().isMaxRank() && stats.canAfford(colony.getRadar()));
			upgradeHangar.setEnabled(!hangarUpgrading && !colony.getHangar().isMaxRank() && stats.canAfford(colony.getHangar()));
		}
	}

	public void setStats(PlayerStats stats) {
		this.stats = stats;
		checkBuildOptions();
	}

	public void setQueue(Map<Colony, List<BuildAble>> colonyQueues) {
		this.colonyQueues = colonyQueues;
		checkQueue();
	}
	
	private void checkQueue() {
		mineUpgrading = false;
		turretUpgrading = false;
		radarUpgrading = false;
		hangarUpgrading = false;
		if (colonyQueues != null) {
			for (Colony colony : colonyQueues.keySet()) {
				if (colony.equals(this.colony)) {
					for (int i = 0; i < colonyQueues.get(colony).size(); i++) {
						if (colonyQueues.get(colony).get(i) instanceof Mine) {
							mineUpgrading = true;
						} else if (colonyQueues.get(colony).get(i) instanceof Turret) {
							turretUpgrading = true;
						} else if (colonyQueues.get(colony).get(i) instanceof Radar) {
							radarUpgrading = true;
						} else if (colonyQueues.get(colony).get(i) instanceof Hangar) {
							hangarUpgrading = true;
						}
					}
				}
			}
		}
	}
}