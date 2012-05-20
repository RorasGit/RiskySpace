package riskyspace.view.opengl.menu;

import java.awt.Color;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GLAutoDrawable;

import riskyspace.PlayerColors;
import riskyspace.model.Colony;
import riskyspace.model.Player;
import riskyspace.model.PlayerStats;
import riskyspace.model.ShipType;
import riskyspace.services.Event;
import riskyspace.services.EventBus;
import riskyspace.view.Action;
import riskyspace.view.opengl.Rectangle;
import riskyspace.view.opengl.impl.GLButton;
import riskyspace.view.opengl.impl.GLSprite;

/**
 * 
 * @author Daniel Augurell
 *
 */
public class GLRecruitMenu extends GLAbstractSideMenu {
	
	private Color ownerColor = null;

	private int margin;

	private Colony colony = null;
	private PlayerStats stats = null;
	
	/*
	 * Build Buttons
	 */
	private GLButton buildScoutButton = null;
	private GLButton buildHunterButton = null;
	private GLButton buildDestroyerButton = null;
	private GLButton buildColonizerButton = null;
	
	/*
	 * Texture coordinates for different Players
	 */
	private Player[] players = new Player[]{
		Player.RED,
		Player.BLUE,
		Player.PINK,
		Player.GREEN,
	};
	private int[] y = new int[]{
		192,
		128,
		64,
		0
	};
	
	/*
	 * Back Button
	 */
	private GLButton backButton = null;
	
	/*
	 * Images
	 */
	private GLSprite colonyPicture = null;
	private Map<Player, GLSprite> cities = new HashMap<Player, GLSprite>();
	
	public GLRecruitMenu(int x, int y, int menuWidth, int menuHeight) {
		super(x, y, menuWidth, menuHeight);
		margin = menuHeight/20;
		setPicture();
		setButtons();
	}
	
	/*
	 * Create and set location for Buttons in this menu
	 */
	private void setButtons() {
		int x = getX() + getMenuWidth()/2 - margin/3;
		int y = getY() + cities.get(Player.BLUE).getBounds().getHeight() + 3*margin;
		
		buildScoutButton = new GLButton(x - 90, y, 90, 90);
		buildScoutButton.setAction(new Action() {
			@Override
			public void performAction() {
				Event evt = new Event(Event.EventTag.QUEUE_SHIP, ShipType.SCOUT);
				EventBus.CLIENT.publish(evt);
			}
		});
		buildHunterButton = new GLButton(x + 2 * margin / 3, y, 90, 90);
		buildHunterButton.setAction(new Action() {
			@Override
			public void performAction() {
				Event evt = new Event(Event.EventTag.QUEUE_SHIP, ShipType.HUNTER);
				EventBus.CLIENT.publish(evt);
			}
		});
		buildDestroyerButton = new GLButton(x - 90, y + 90 + margin/2, 90, 90);
		buildDestroyerButton.setAction(new Action() {
			@Override
			public void performAction() {
				Event evt = new Event(Event.EventTag.QUEUE_SHIP, ShipType.DESTROYER);
				EventBus.CLIENT.publish(evt);
			}
		});
		buildColonizerButton = new GLButton(x + 2 * margin / 3,  y + 90 + margin/2, 90, 90);
		buildColonizerButton.setAction(new Action() {
			@Override
			public void performAction() {
				Event evt = new Event(Event.EventTag.QUEUE_SHIP, ShipType.COLONIZER);
				EventBus.CLIENT.publish(evt);
			}
		});
		backButton = new GLButton(getX() + margin, 
				getY() + getMenuHeight() - 2*(getMenuWidth() - 2*margin)/4, 
				getMenuWidth() - 2 * margin,
				(getMenuWidth() - 2 * margin) / 4);
		backButton.setTexture("menu/back", 128, 32);
		backButton.setAction(new Action(){
			@Override
			public void performAction() { 
				setVisible(false);
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
	
	public GLRecruitMenu(int x, int y, int menuWidth, int menuHeight, Action backAction) {
		this(x, y, menuWidth, menuHeight);
		backButton.setAction(backAction);
	}
	
	public void setColony(Colony colony) {
		this.colony = colony;
		setMenuName(colony.getName());
		ownerColor = PlayerColors.getColor(colony.getOwner());
		colonyPicture = cities.get(colony.getOwner());
		checkRecruitOptions(stats);
		setImages(colony.getOwner());
	}
	
	private void setImages(Player player) {
		int i = 0;
		for (i = 0; i < players.length; i++) {
			if (players[i] == player) {break;};
		}
		buildScoutButton.setTexture(	"ship_icons",   0, y[i], 64, 64);
		buildHunterButton.setTexture(	"ship_icons",  64, y[i], 64, 64);
		buildColonizerButton.setTexture("ship_icons", 128, y[i], 64, 64);
		buildDestroyerButton.setTexture("ship_icons", 192, y[i], 64, 64);
	}
	
//	private void drawColonyName(Graphics g) {
//		g.setColor(ownerColor);
//		g.setFont(ViewResources.getFont().deriveFont((float) getMenuHeight()/20));
//		int textX = getX() - (g.getFontMetrics().stringWidth(getMenuName()) / 2) + (getMenuWidth() / 2);
//		int textY = getY() + (g.getFontMetrics().getHeight() / 2) + (2*margin + colonyPicture.getHeight(null));
//		g.drawString(getMenuName(), textX, textY);
//	}
	
	public void checkRecruitOptions(PlayerStats stats) {
		this.stats = stats;
		if (colony != null && stats != null){
//			buildScoutButton.setEnabled(stats.canAfford(ShipType.SCOUT) && colony.getHangar().canBuild(ShipType.SCOUT));
//			buildHunterButton.setEnabled(stats.canAfford(ShipType.HUNTER) && colony.getHangar().canBuild(ShipType.HUNTER));
//			buildColonizerButton.setEnabled(stats.canAfford(ShipType.COLONIZER) && colony.getHangar().canBuild(ShipType.COLONIZER));
//			buildDestroyerButton.setEnabled(stats.canAfford(ShipType.DESTROYER) && colony.getHangar().canBuild(ShipType.DESTROYER));
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
			else if (buildHunterButton.mouseReleased(p)) {return true;}
			else if (buildDestroyerButton.mouseReleased(p)) {return true;} 
			else if (buildColonizerButton.mouseReleased(p)) {return true;}
			else if (backButton.mouseReleased(p)) {return true;}
			if (this.contains(p)) {return true;}
			else {
				return false;
			}
		}
		return false;
	}
	
	@Override
	public void draw(GLAutoDrawable drawable, Rectangle objectRect, Rectangle targetArea, int zIndex) {
		super.draw(drawable, objectRect, targetArea, zIndex);
		zIndex++;
		colonyPicture.draw(drawable, colonyPicture.getBounds(), targetArea, zIndex);
		buildScoutButton.draw(drawable, null, targetArea, zIndex);
		buildHunterButton.draw(drawable, null, targetArea, zIndex);
		buildDestroyerButton.draw(drawable, null, targetArea, zIndex);
		buildColonizerButton.draw(drawable, null, targetArea, zIndex);
		backButton.draw(drawable, null, targetArea, zIndex);
	}
}