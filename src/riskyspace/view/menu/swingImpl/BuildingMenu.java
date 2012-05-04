package riskyspace.view.menu.swingImpl;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import riskyspace.model.Colony;
import riskyspace.services.Event;
import riskyspace.view.Button;
import riskyspace.view.RankIndicator;
import riskyspace.view.menu.AbstractSideMenu;

/**
 * 
 * @author flygarn
 *
 */
public class BuildingMenu extends AbstractSideMenu {

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
	}
	
	public void setColony(Colony colony) {
		/*
		 * TODO:
		 * Assign values to variables based on colony info.
		 * make sure the colony supplies sufficient information
		 * Immutable?
		 */
	}
	
	@Override
	public void draw(Graphics g) {
		super.draw(g);
		//TODO Draw all stuff
	}
	
	@Override
	public boolean mousePressed(Point p) {
		if (isVisible()) {
			if (upgradeMine.mousePressed(p)) {return true;}
			else if (upgradeTurret.mousePressed(p)) {return true;}
			else if (upgradeRadar.mousePressed(p)) {return true;}
			else if (upgradeHangar.mousePressed(p)) {return true;}
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
		
	}
}