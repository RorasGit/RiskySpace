package riskyspace.view.menu.swingImpl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import riskyspace.logic.FleetMove;
import riskyspace.model.Supply;
import riskyspace.services.Event;
import riskyspace.services.EventBus;
import riskyspace.services.EventHandler;
import riskyspace.view.Action;
import riskyspace.view.Button;
import riskyspace.view.Clickable;
import riskyspace.view.View;
import riskyspace.view.menu.IMenu;

public class TopMenu implements IMenu, Clickable, EventHandler {
	
	private boolean enabled;
	
	private Image metalImage = null;
	private Image gasImage = null;
	private Image supplyImage = null;
	private int metal;
	private int gas;
	private Supply supply;
	
	private Button endTurnButton = null;
	private Button performMovesButton = null;
	private Button menuButton = null;
	private Button buildQueueButton = null;
	
	private int x, y;
	private int menuHeight = 0;
	private int menuWidth = 0;
	private int margin = 5;
	
	public TopMenu (int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		menuHeight = height;
		menuWidth = width;
		
		supplyImage = Toolkit.getDefaultToolkit().getImage("res/menu/supply.png");
		metalImage = Toolkit.getDefaultToolkit().getImage("res/menu/resource_metal.png");
		gasImage = Toolkit.getDefaultToolkit().getImage("res/menu/resource_gas.png");
		
		menuButton = new Button(margin, margin, 79, 30);
		menuButton.setImage("res/menu/menuButton3" + View.res);
		menuButton.setAction(new Action() {
			@Override
			public void performAction() {
				System.exit(0);
			}
		});
		
		buildQueueButton = new Button(margin + 79 + 4, margin, 79, 30);
		buildQueueButton.setImage("res/menu/menuButton2" + View.res);
		
		endTurnButton = new Button(width - margin - 79, margin, 79, 66);
		endTurnButton.setImage("res/menu/endTurn" + View.res);
		endTurnButton.setAction(new Action() {
			@Override
			public void performAction() {
				Event evt = new Event(Event.EventTag.NEXT_TURN, null);
				EventBus.INSTANCE.publish(evt);
			}
		});
		
		performMovesButton = new Button(width - margin - 2*79 - 5, margin, 79, 66);
		performMovesButton.setImage("res/menu/moves" + View.res);
		performMovesButton.setAction(new Action() {
			@Override
			public void performAction() {
				Event evt = new Event(FleetMove.isMoving() ? Event.EventTag.INTERRUPT_MOVES : Event.EventTag.PERFORM_MOVES, null);
				EventBus.INSTANCE.publish(evt);
			}
		});
		EventBus.INSTANCE.addHandler(this);
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

	@Override
	public void draw(Graphics g) {
		int a = menuWidth/10;
		g.setColor(Color.BLUE);
		
		/*
		 * Draw the player's metal
		 */
		g.drawImage(metalImage, a*6, margin, null);
		g.drawString("" + metal, a*6 + metalImage.getWidth(null) + 5, margin + metalImage.getHeight(null)/2);
		/*
		 * Draw the player's gas
		 */
		g.drawImage(gasImage, a*7, margin, null);
		g.drawString("" + gas, a*7 + gasImage.getWidth(null) + 5, margin + gasImage.getHeight(null)/2);
		/*
		 * Draw the player's supply
		 */
		if (supply.isCapped())
			g.setColor(Color.RED);
		g.drawImage(supplyImage, a*5, margin, null);
		g.drawString(supply.getUsed() + "/" + supply.getMax(), a*5 + supplyImage.getWidth(null) + 5, margin + supplyImage.getHeight(null)/2);
		
		menuButton.draw(g);
		buildQueueButton.draw(g);
		endTurnButton.draw(g);
		performMovesButton.draw(g);
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
	public void performEvent(Event evt) {
		if (evt.getTag() == Event.EventTag.METAL_CHANGED) {
				metal = (Integer) evt.getObjectValue();
		}
		if (evt.getTag() == Event.EventTag.GAS_CHANGED) {
			gas = (Integer) evt.getObjectValue();
		}
		if (evt.getTag() == Event.EventTag.SUPPLY_CHANGED) {
			Supply supply = (Supply) evt.getObjectValue();
			this.supply = supply;
		}
	}
}