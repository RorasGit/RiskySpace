package riskyspace.view.opengl.menu;

import java.awt.Color;
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
import riskyspace.view.Action;
import riskyspace.view.opengl.Rectangle;
import riskyspace.view.opengl.impl.GLButton;
import riskyspace.view.opengl.impl.GLSprite;
/**
 * 
 * @author Daniel Augurell
 * Menu showing information and options for a Colony
 */
public class GLColonyMenu extends GLAbstractSideMenu{

	/*
	 * Strings to be printed on the menu
	 */
	
	private Color ownerColor = null;
	private Colony colony;
	
	private int margin;
	
	/*
	 * Buttons
	 */
	private GLButton buildShipButton;
	private GLButton buildingsButton;
	
	/*
	 * Sub Menus
	 */
	private GLRecruitMenu recruitMenu;
	private GLBuildingMenu buildingMenu;
	
	/*
	 * Sprites
	 */
	private GLSprite colonyPicture = null;
	
	private Map<Player, GLSprite> cities = new HashMap<Player, GLSprite>();
	
	public GLColonyMenu(int x, int y, int menuWidth, int menuHeight) {
		super(x, y, menuWidth, menuHeight);
		margin = menuHeight/20;
		recruitMenu = new GLRecruitMenu(x, y, menuWidth, menuHeight);
		buildingMenu = new GLBuildingMenu(x, y, menuWidth, menuHeight);
		setPicture();
		setButtons();
	}
	private void setButtons() {
		buildShipButton = new GLButton(getX() + margin, 
				(getBounds().getWidth() - 2*margin)/4 , 
				getBounds().getWidth()-2*margin, (getBounds().getWidth() - 2*margin)/4);
		buildShipButton.setTexture("menu/recruit", 128, 32);
		buildShipButton.setAction(new Action(){
			@Override
			public void performAction() {
				setVisible(false);
				recruitMenu.setVisible(true);
			}
		});
		buildingsButton = new GLButton(getX() + margin, 
				2*(getBounds().getWidth() - margin)/4, 
				getBounds().getWidth()-2*margin, (getBounds().getWidth() - 2*margin)/4);
		buildingsButton.setTexture("menu/build", 128, 32);
		buildingsButton.setAction(new Action(){
			@Override
			public void performAction() {
				setVisible(false);
				buildingMenu.setVisible(true);
			}
		});
	}
	private void setPicture(){
		Rectangle renderRect = new Rectangle(getBounds().getX() + margin, 
				getBounds().getHeight() - ((getBounds().getWidth() - 2*margin)*3)/4 - 3*margin/2, 
				getBounds().getWidth() - 2*margin, ((getBounds().getWidth() - 2*margin)*3)/4);

		GLSprite GLtmp = new GLSprite("menu/city_red", 1280, 700);
		GLtmp.setBounds(renderRect);
		cities.put(Player.RED, GLtmp);
		GLtmp = new GLSprite("menu/city_blue", 900, 486);
		GLtmp.setBounds(renderRect);
		cities.put(Player.BLUE, GLtmp);
		colonyPicture = GLtmp;
	
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
	
	public void setStats(PlayerStats stats) {
		recruitMenu.checkRecruitOptions(stats);
		buildingMenu.setStats(stats);
	}
	
	public void setQueues(Map<Colony, List<BuildAble>> colonyQueues) {
		buildingMenu.setQueue(colonyQueues);
	}
    /*
	private void drawColonyName(Graphics g) {
		g.setColor(ownerColor);
		g.setFont(ViewResources.getFont().deriveFont((float) getMenuHeight()/20));
		int textX = getX() - (g.getFontMetrics().stringWidth(getMenuName()) / 2) + (getMenuWidth() / 2);
		int textY = getY() + (g.getFontMetrics().getHeight() / 2) + (2*margin + colonyPicture.getHeight(null));
		g.drawString(getMenuName(), textX, textY);
	}
	*/

	@Override
	public boolean mousePressed(Point p) {
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

	@Override
	public boolean mouseReleased(Point p) {
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
	public void draw(GLAutoDrawable drawable, Rectangle objectRect, Rectangle targetArea, int zIndex) {
//		buildingMenu.setVisible(true);
		if(isVisible()){
			super.draw(drawable, objectRect, targetArea, zIndex);
			colonyPicture.draw(drawable, colonyPicture.getBounds(), targetArea, zIndex+1);
			buildingsButton.draw(drawable, null, targetArea, zIndex+1);
			buildShipButton.draw(drawable, null, targetArea, zIndex+1);
		} else if(recruitMenu.isVisible()){
			recruitMenu.draw(drawable, objectRect, targetArea, zIndex);
		} else if(buildingMenu.isVisible()){
			buildingMenu.draw(drawable, objectRect, targetArea, zIndex);
		}
	}
}