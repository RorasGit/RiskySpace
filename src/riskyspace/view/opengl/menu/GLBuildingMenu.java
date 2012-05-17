package riskyspace.view.opengl.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
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
	private GLSprite mineImage;
	private GLSprite turretImage;
	private GLSprite radarImage;
	private GLSprite hangarImage;
	
	/*
	 * City Images
	 */
	private GLSprite cityImage = null;
	private Map<Player, GLSprite> cities = new HashMap<Player, GLSprite>();
	
	/*
	 * Split Image
	 */
	private GLSprite split = null;
	
	/*
	 * 
	 */
	private GLSprite inProgress = null;
	
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
		//TODO Create buttons and Load Images
		margin = menuHeight/20;
		
		/*
		 * Load City Images for every Player
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

		// Set Location
		mineRank.setLocation(x + margin/2, menuHeight - (7*margin/2 + imageHeight));
		turretRank.setLocation(x + margin/2, menuHeight - (height + 9*margin/2 + imageHeight));
		radarRank.setLocation(x + margin/2, menuHeight - (height*2 + 11*margin/2 + imageHeight));
		hangarRank.setLocation(x + margin/2, menuHeight - (height*3 + 13*margin/2 + imageHeight));
		
		mineRank.setSize(width, height);
		turretRank.setSize(width, height);
		radarRank.setSize(width, height);
		hangarRank.setSize(width, height);
		
		
		setButtons(menuWidth, menuHeight, height);
	}

	private void setButtons(int menuWidth, int menuHeight, int height) {
		/*
		 * Button Sizes
		 */
		int upgradeButtonWidth = height;
		int upgradeButtonHeight = height/4;
		
		/*
		 * Load Images
		 */
		mineImage = new GLSprite("menu/mine", 72, 72);
		mineImage.setBounds(new Rectangle(mineRank.getX() + mineRank.getWidth(), mineRank.getY(), height, height));
		turretImage = new GLSprite("menu/turret_laser", 72, 72);
		turretImage.setBounds(new Rectangle(turretRank.getX() + turretRank.getWidth(), turretRank.getY(), height, height));
		radarImage = new GLSprite("menu/radar", 72, 72);
		radarImage.setBounds(new Rectangle(radarRank.getX() + radarRank.getWidth(), radarRank.getY(), height, height));
		hangarImage = new GLSprite("menu/hangar", 72, 72);
		hangarImage.setBounds(new Rectangle(radarRank.getX() + radarRank.getWidth(), radarRank.getY(), height, height));
		split = new GLSprite("menu/split", 15, 72);
		split.setBounds(new Rectangle(0, 0, height/5, height));
		inProgress = new GLSprite("menu/progress", 128, 32);
		inProgress.setBounds(new Rectangle(0, 0, upgradeButtonWidth, upgradeButtonHeight));
				
		/*
		 * Create Buttons
		 */
		int x = getX() + getMenuWidth() - 3*margin/5 - upgradeButtonWidth;
		
		upgradeMine = new GLButton(x, mineRank.getY() + upgradeButtonHeight
				, upgradeButtonWidth, upgradeButtonHeight);
		upgradeMine.setImage("menu/upgrade", 128, 32);
		upgradeMine.setAction(new Action(){
			@Override
			public void performAction() {
				Event evt = new Event(Event.EventTag.QUEUE_BUILDING, "MINE");
				EventBus.CLIENT.publish(evt);
			}
		});
		
		upgradeTurret = new GLButton(x, turretRank.getY() + upgradeButtonHeight
				, upgradeButtonWidth, upgradeButtonHeight);
		upgradeTurret.setImage("menu/upgrade", 128, 32);
		upgradeTurret.setAction(new Action(){
			@Override
			public void performAction() {
				Event evt = new Event(Event.EventTag.QUEUE_BUILDING, "TURRET");
				EventBus.CLIENT.publish(evt);
			}
		});
		
		
		upgradeRadar = new GLButton(x, radarRank.getY() + upgradeButtonHeight
				, upgradeButtonWidth, upgradeButtonHeight);
		upgradeRadar.setImage("menu/upgrade", 128, 32);
		upgradeRadar.setAction(new Action(){
			@Override
			public void performAction() {
				Event evt = new Event(Event.EventTag.QUEUE_BUILDING, "RADAR");
				EventBus.CLIENT.publish(evt);
			}
		});
		
		upgradeHangar = new GLButton(x, hangarRank.getY() + upgradeButtonHeight
				, upgradeButtonWidth, upgradeButtonHeight);
		upgradeHangar.setImage("menu/upgrade", 128, 32);
		upgradeHangar.setAction(new Action(){
			@Override
			public void performAction() {
				Event evt = new Event(Event.EventTag.QUEUE_BUILDING, "HANGAR");
				EventBus.CLIENT.publish(evt);
			}
		});
		
		backButton = new GLButton(getX() + margin, 
				(getBounds().getWidth() - 2*margin)/4, 
				getBounds().getWidth()-2*margin, (getBounds().getWidth() - 2*margin)/4);
		backButton.setImage("menu/back", 128, 32);
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
		Rectangle renderRect = new Rectangle(getBounds().getX() + margin, 
				getBounds().getHeight() - imageHeight - 3*margin/2, 
				imageWidth, imageHeight);

		GLSprite GLtmp = new GLSprite("menu/city_red", 1280, 700);
		GLtmp.setBounds(renderRect);
		cities.put(Player.RED, GLtmp);
		GLtmp = new GLSprite("menu/city_blue", 900, 486);
		GLtmp.setBounds(renderRect);
		cities.put(Player.BLUE, GLtmp);
		cityImage = GLtmp;
	
	}
	
	public void setColony(Colony colony) {
		/*
		 * TODO:
		 * Assign values to variables based on colony info.
		 * make sure the colony supplies sufficient information
		 * Immutable?
		 */
		this.colony = colony;
		checkBuildOptions();
		checkQueue();
		setMenuName(colony.getName());
		ownerColor = PlayerColors.getColor(colony.getOwner());
		cityImage = cities.get(colony.getOwner());
		
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
		zIndex++;
		cityImage.draw(drawable, cityImage.getBounds(), targetArea, zIndex);
		
		((GLRenderAble)mineRank).draw(drawable, mineRank.getBounds(), targetArea, zIndex);
		((GLRenderAble)turretRank).draw(drawable, turretRank.getBounds(), targetArea, zIndex);
		((GLRenderAble)radarRank).draw(drawable, radarRank.getBounds(), targetArea, zIndex);
		((GLRenderAble)hangarRank).draw(drawable, hangarRank.getBounds(), targetArea, zIndex);
		
		turretImage.draw(drawable, turretImage.getBounds(), targetArea, zIndex);
		mineImage.draw(drawable, mineImage.getBounds(), targetArea, zIndex);
		hangarImage.draw(drawable, hangarImage.getBounds(), targetArea, zIndex);
		radarImage.draw(drawable, radarImage.getBounds(), targetArea, zIndex);
		
		/*
		 * The X coordinate of the right side of building images.
		 */
		int imageRightX = mineRank.getX() + mineRank.getWidth() + mineImage.getBounds().getWidth();
		
		/*
		 * Draw Split Image between levels
		 */
		int splitX = (getMenuWidth() - margin - mineRank.getWidth() - mineImage.getBounds().getWidth())/2 - split.getBounds().getWidth()/2;
		splitX += imageRightX;
		
		split.draw(drawable, new Rectangle(splitX, hangarRank.getY(), split.getBounds().getWidth(), split.getBounds().getWidth()), targetArea, zIndex);
		split.draw(drawable, new Rectangle(splitX, radarRank.getY(), split.getBounds().getWidth(), split.getBounds().getWidth()), targetArea, zIndex);
		split.draw(drawable, new Rectangle(splitX, mineRank.getY(), split.getBounds().getWidth(), split.getBounds().getWidth()), targetArea, zIndex);
		split.draw(drawable, new Rectangle(splitX, turretRank.getY(), split.getBounds().getWidth(), split.getBounds().getWidth()), targetArea, zIndex);
		
		/*
		 * Draw Buttons
		 */
		upgradeMine.draw(drawable, upgradeMine.getBounds(), targetArea, zIndex);
		upgradeTurret.draw(drawable, upgradeTurret.getBounds(), targetArea, zIndex);
		upgradeRadar.draw(drawable, upgradeRadar.getBounds(), targetArea, zIndex);
		upgradeHangar.draw(drawable, upgradeHangar.getBounds(), targetArea, zIndex);
		backButton.draw(drawable, backButton.getBounds(), targetArea, zIndex);
		
		drawProgressIndicators(drawable, objectRect, targetArea, zIndex);
	}
	/*
	public void draw(Graphics g) {
		//super.draw(g);
		//TODO Draw all stuff
		if (isVisible()) {
			
			g.drawImage(cityImage, getX() + margin, getY() + 3*margin/2,null);
			drawColonyName(g);
			
			((SwingRenderAble)mineRank).draw(g);
			((SwingRenderAble)turretRank).draw(g);
			((SwingRenderAble)radarRank).draw(g);
			((SwingRenderAble)hangarRank).draw(g);
			
			g.drawImage(turretImage, turretRank.getX() + turretRank.getWidth(), turretRank.getY(), null);
			g.drawImage(mineImage, mineRank.getX() + mineRank.getWidth(), mineRank.getY(), null);
			g.drawImage(radarImage, radarRank.getX() + radarRank.getWidth(), radarRank.getY(), null);
			g.drawImage(hangarImage, hangarRank.getX() + hangarRank.getWidth(), hangarRank.getY(), null);
			
			int imageRightX = mineRank.getX() + mineRank.getWidth() + mineImage.getBounds().getWidth();
			
			int splitX = (getMenuWidth() - margin - mineRank.getWidth() - mineImage.getWidth(null))/2 - split.getWidth(null)/2;
			splitX += imageRightX;
			
			g.drawImage(split, splitX, hangarRank.getY(), null);
			g.drawImage(split, splitX, turretRank.getY(), null);
			g.drawImage(split, splitX, mineRank.getY(), null);
			g.drawImage(split, splitX, radarRank.getY(), null);
			
			
			 * Draw Title Strings
			 
			g.setFont(titleFont);
			g.setColor(ViewResources.WHITE);
			FontMetrics fm = g.getFontMetrics();
			g.drawString(mine, getX() + getMenuWidth()/2 - fm.stringWidth(mine)/2, mineRank.getY() - 5);
			g.drawString(turret, getX() + getMenuWidth()/2 - fm.stringWidth(turret)/2, turretRank.getY() - 5);
			g.drawString(radar, getX() + getMenuWidth()/2 - fm.stringWidth(radar)/2, radarRank.getY() - 5);
			g.drawString(hangar, getX() + getMenuWidth()/2 - fm.stringWidth(hangar)/2, hangarRank.getY() - 5);
			
			/*
			 * Draw info strings
			 
			g.setFont(infoFont);
			fm = g.getFontMetrics();
			
			int height = fm.getHeight();
			int infoWidth = splitX - imageRightX + split.getWidth(null)/2;
			
			g.drawString(currentMineLevel, imageRightX + infoWidth/2 - fm.stringWidth(currentMineLevel)/2, mineRank.getY() + 5 + height/2);
			g.drawString(nextMineLevel, imageRightX + 3*infoWidth/2 - fm.stringWidth(nextMineLevel)/2, mineRank.getY() + 5 + height/2);
			
			g.drawString(currentTurretLevel, imageRightX + infoWidth/2 - fm.stringWidth(currentTurretLevel)/2, turretRank.getY() + 5 + height/2);
			g.drawString(nextTurretLevel, imageRightX + 3*infoWidth/2 - fm.stringWidth(nextTurretLevel)/2, turretRank.getY() + 5 + height/2);
			
			g.drawString(currentRadarLevel, imageRightX + infoWidth/2 - fm.stringWidth(currentTurretLevel)/2, radarRank.getY() + 5 + height/2);
			g.drawString(nextRadarLevel, imageRightX + 3*infoWidth/2 - fm.stringWidth(nextTurretLevel)/2, radarRank.getY() + 5 + height/2);
			
			g.drawString(currentHangarLevel, imageRightX + infoWidth/2 - fm.stringWidth(currentHangarLevel)/2, hangarRank.getY() + 5 + height/2);
			g.drawString(nextHangarLevel, imageRightX + 3*infoWidth/2 - fm.stringWidth(nextHangarLevel)/2, hangarRank.getY() + 5 + height/2);
			
			// Mining Information
			g.drawString(currentMineIncome, mineRank.getX() + mineRank.getWidth() + mineImage.getWidth(null) + 5, mineRank.getY() + margin/3 + 5 + height/2);
			g.drawString(nextMineIncome, mineRank.getX() + mineRank.getWidth() + mineImage.getWidth(null) + 8 + infoWidth, mineRank.getY() + margin/3 + 5 + height/2);
			
			// Defense System Information
			g.drawString(currentTurretDamage, turretRank.getX() + turretRank.getWidth() + turretImage.getWidth(null) + 5, turretRank.getY() + margin/3 + 5 + height/2);
			g.drawString(currentTurretShield, turretRank.getX() + turretRank.getWidth() + turretImage.getWidth(null) + 5, turretRank.getY() + margin/3 + 5 + 3*height/2);
			g.drawString(nextTurretDamage, turretRank.getX() + turretRank.getWidth() + turretImage.getWidth(null) + 8 + infoWidth, turretRank.getY() + margin/3 + 5 + height/2);
			g.drawString(nextTurretShield, turretRank.getX() + turretRank.getWidth() + turretImage.getWidth(null) + 8 + infoWidth, turretRank.getY() + margin/3 + 5 + 3*height/2);
			
			// Radar Information
			g.drawString(currentRadarRange, radarRank.getX() + radarRank.getWidth() + radarImage.getWidth(null) + 5, radarRank.getY() + margin/3 + 5 + height/2);
			g.drawString(nextRadarRange, radarRank.getX() + radarRank.getWidth() + radarImage.getWidth(null) + 8 + infoWidth, radarRank.getY() + margin/3 + 5 + height/2);
			g.drawString(currentRadarRange2, radarRank.getX() + radarRank.getWidth() + radarImage.getWidth(null) + 5, radarRank.getY() + margin/3 + 5 + 3*height/2);
			g.drawString(nextRadarRange2, radarRank.getX() + radarRank.getWidth() + radarImage.getWidth(null) + 8 + infoWidth, radarRank.getY() + margin/3 + 5 + 3*height/2);
			
			// Hangar Information
			g.drawString(currentHangarPerk, hangarRank.getX() + hangarRank.getWidth() + hangarImage.getWidth(null) + 5, hangarRank.getY() + margin/3 + 5 + height/2);
			g.drawString(currentHangarPerk2, hangarRank.getX() + hangarRank.getWidth() + hangarImage.getWidth(null) + 5, hangarRank.getY() + margin/3 + 5 + 3*height/2);
			g.drawString(nextHangarPerk, hangarRank.getX() + hangarRank.getWidth() + hangarImage.getWidth(null) + 8 + infoWidth, hangarRank.getY() + margin/3 + 5 + height/2);
			g.drawString(nextHangarPerk2, hangarRank.getX() + hangarRank.getWidth() + hangarImage.getWidth(null) + 8 + infoWidth, hangarRank.getY() + margin/3 + 5 + 3*height/2);
			
			/*
			 * Draw next Level costs if not maxed
			 
			if (!colony.getMine().isMaxRank()) {
				g.drawString(nextMineMetal + nextMineGas, mineRank.getX() + mineRank.getWidth() + mineImage.getWidth(null) + 3*infoWidth/2 - fm.stringWidth(nextTurretMetal + nextMineGas)/2, mineRank.getY() + margin/3 + 5 + 5*height/2);
			}
			if (!colony.getTurret().isMaxRank()) {
				g.drawString(nextTurretMetal + nextTurretGas, turretRank.getX() + turretRank.getWidth() + turretImage.getWidth(null) + 3*infoWidth/2 - fm.stringWidth(nextTurretMetal + nextTurretGas)/2, turretRank.getY() + margin/3 + 5 + 5*height/2);
			}
			if (!colony.getRadar().isMaxRank()) {
				g.drawString(nextRadarMetal + nextRadarGas, radarRank.getX() + radarRank.getWidth() + radarImage.getWidth(null) + 3*infoWidth/2 - fm.stringWidth(nextRadarMetal + nextRadarGas)/2, radarRank.getY() + margin/3 + 5 + 5*height/2);
			}
			if (!colony.getHangar().isMaxRank()) {
				g.drawString(nextHangarMetal + nextHangarGas, hangarRank.getX() + hangarRank.getWidth() + hangarImage.getWidth(null) + 3*infoWidth/2 - fm.stringWidth(nextHangarMetal + nextHangarGas)/2, hangarRank.getY() + margin/3 + 5 + 5*height/2);
			}
			
			upgradeMine.draw(g);
			upgradeTurret.draw(g);
			upgradeRadar.draw(g);
			upgradeHangar.draw(g);
			backButton.draw(g);
			
			/*
			 * Draw progress indication
			 
			
		}
	}
	*/
	
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
	/*
	private void drawColonyName(Graphics g) {
		g.setColor(ownerColor);
		g.setFont(ViewResources.getFont().deriveFont((float) getMenuHeight()/20));
		int textX = getX() - (g.getFontMetrics().stringWidth(getMenuName()) / 2) + (getMenuWidth() / 2);
		int textY = getY() + (g.getFontMetrics().getHeight() / 2) + (2*margin + cityImage.getHeight(null));
		g.drawString(getMenuName(), textX, textY);
	}
	*/
	
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