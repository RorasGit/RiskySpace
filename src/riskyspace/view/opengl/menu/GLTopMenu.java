package riskyspace.view.opengl.menu;

import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;

import javax.media.opengl.GLAutoDrawable;

import riskyspace.model.PlayerStats;
import riskyspace.model.Resource;
import riskyspace.model.Supply;
import riskyspace.services.Event;
import riskyspace.services.EventBus;
import riskyspace.view.Action;
import riskyspace.view.Clickable;
import riskyspace.view.IMenu;
import riskyspace.view.ViewResources;
import riskyspace.view.opengl.GLRenderAble;
import riskyspace.view.opengl.Rectangle;
import riskyspace.view.opengl.impl.GLButton;
import riskyspace.view.opengl.impl.GLSprite;

public class GLTopMenu implements IMenu, Clickable, GLRenderAble {
	
	private boolean enabled;
	
	private GLSprite supplySprite = null;
	private GLSprite metalSprite = null;
	private GLSprite gasSprite = null;
	
	/*
	 * Rectangles for position and size of Sprites
	 */
	private Rectangle supplyRect;
	private Rectangle metalRect;
	private Rectangle gasRect;
	
	private int metal;
	private int gas;
	private Supply supply;
	
	private GLButton endTurnButton = null;
	private GLButton performMovesButton = null;
	private GLButton menuButton = null;
	private GLButton buildQueueButton = null;
	
	private int x, y;
	private int menuHeight = 0;
	private int menuWidth = 0;
	private int margin = 5;
	
	private Font resourceFont = null;
	
	public GLTopMenu(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		menuHeight = height;
		menuWidth = width;
		
		supplySprite = new GLSprite("supply_square", 32, 32);
		metalSprite = new GLSprite("metal_square", 32, 32);
		gasSprite = new GLSprite("gas_square", 32, 32);
		
		int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
		int w = menuWidth/10;

		supplyRect = new Rectangle(w*5, screenHeight - margin - 32, 32, 32);
		metalRect = new Rectangle(w*6, screenHeight - margin - 32, 32, 32);
		gasRect = new Rectangle(w*7, screenHeight - margin - 32, 32, 32);
		
		menuButton = new GLButton(x, screenHeight - y, screenHeight/6, height);
		menuButton.setTexture("menu", 128, 80);
		menuButton.setAction(new Action() {
			@Override
			public void performAction() {
				System.exit(0);
			}
		});
		
		buildQueueButton = new GLButton(x + screenHeight/6, screenHeight - y, screenHeight/6, height);
		buildQueueButton.setTexture("build_queue", 128, 80);
		
		endTurnButton = new GLButton(width - screenHeight/6, screenHeight - y, screenHeight/6, height);
		endTurnButton.setTexture("end_turn", 128, 80);
		endTurnButton.setAction(new Action() {
			@Override
			public void performAction() {
				Event evt = new Event(Event.EventTag.NEXT_TURN, null);
				EventBus.CLIENT.publish(evt);
			}
		});
		
		performMovesButton = new GLButton(width - screenHeight/3, screenHeight - y, screenHeight/6, height);
		performMovesButton.setTexture("perform_moves", 128, 80);
		performMovesButton.setAction(new Action() {
			@Override
			public void performAction() {
				Event evt = new Event(Event.EventTag.MOVE, null);
				EventBus.CLIENT.publish(evt);
			}
		});
		resourceFont = ViewResources.getFont().deriveFont(17f);
		setVisible(true);
	}

	@Override
	public boolean contains(Point p) {
		return false;
	}

	@Override
	public boolean mousePressed(Point p) {
		if (menuButton.mousePressed(p)) {return true;}
		else if (buildQueueButton.mousePressed(p)) {return true;}
		else if (endTurnButton.mousePressed(p)) {return true;}
		else if (performMovesButton.mousePressed(p)) {return true;}
		else if (this.contains(p)) {return true;}
		return false;
	}

	@Override
	public boolean mouseReleased(Point p) {
		if (menuButton.mouseReleased(p)) {return true;}
		else if (buildQueueButton.mouseReleased(p)) {return true;}
		else if (endTurnButton.mouseReleased(p)) {return true;}
		else if (performMovesButton.mouseReleased(p)) {return true;}
		else if (this.contains(p)) {return true;}
		return false;
	}

//	@Override
//	public void draw(Graphics g) {
//		int a = menuWidth/10;
//		g.setColor(ViewResources.WHITE);
//		g.setFont(resourceFont);
//		
//		int fontHeight = g.getFontMetrics(resourceFont).getHeight();
//		/*
//		 * Draw the player's metal
//		 */
//		g.drawImage(metalImage, a*6, margin, null);
//		g.drawString("" + metal, a*6 + metalImage.getWidth(null) + 5, margin + metalImage.getHeight(null)/2 + fontHeight/2);
//		/*
//		 * Draw the player's gas
//		 */
//		g.drawImage(gasImage, a*7, margin, null);
//		g.drawString("" + gas, a*7 + gasImage.getWidth(null) + 5, margin + gasImage.getHeight(null)/2 + fontHeight/2);
//		/*
//		 * Draw the player's supply
//		 */
//		if (supply.isCapped())
//			g.setColor(Color.RED);
//		g.drawImage(supplyImage, a*5, margin, null);
//		g.drawString(supply.getUsed() + "/" + supply.getMax(), a*5 + supplyImage.getWidth(null) + 5, margin + supplyImage.getHeight(null)/2 + fontHeight/2);
//		
//		menuButton.draw(g);
//		buildQueueButton.draw(g);
//		endTurnButton.draw(g);
//		performMovesButton.draw(g);
//	}

	public void setStats(PlayerStats stats) {
		gas = stats.getResource(Resource.GAS);
		metal = stats.getResource(Resource.METAL);
		supply = stats.getSupply();
	}
	
	@Override
	public boolean isVisible() {
		return enabled;
	}

	@Override
	public void setVisible(boolean set) {
		enabled = set;
	}

	@Override
	public Rectangle getBounds() {
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;
		return new Rectangle(x, height - y, menuWidth, menuHeight);
	}

	@Override
	public void draw(GLAutoDrawable drawable, Rectangle objectRect,	Rectangle targetArea, int zIndex) {
		
		metalSprite.draw(drawable, metalRect, targetArea, zIndex);
		gasSprite.draw(drawable, gasRect, targetArea, zIndex);
		supplySprite.draw(drawable, supplyRect, targetArea, zIndex);
		
		menuButton.draw(drawable, null, targetArea, zIndex);
		buildQueueButton.draw(drawable, null, targetArea, zIndex);
		endTurnButton.draw(drawable, null, targetArea, zIndex);
		performMovesButton.draw(drawable, null, targetArea, zIndex);
	}
}