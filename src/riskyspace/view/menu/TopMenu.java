package riskyspace.view.menu;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import riskyspace.model.Colony;
import riskyspace.services.Event;
import riskyspace.services.EventHandler;
import riskyspace.view.Button;
import riskyspace.view.Clickable;
import riskyspace.view.View;

public class TopMenu implements IMenu, Clickable, EventHandler {
	
	private boolean enabled;
	
	private Image metal = null;
	private Image gas = null;
	private Image supply = null;
	private Image currentRound;
	
	private Button endTurn = null;
	private Button performMoves = null;
	private Button menuButton = null;
	private Button buildQueueButton = null;
	
	private int x, y;
	private int menuHeight = 0;
	private int menuWidth = 0;
	private int margin = 5;
	
	public TopMenu (int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		
		menuButton = new Button(margin, margin, 90, 50);
		menuButton.setImage("res/menu/menuButton3" + View.res);
		
		buildQueueButton = new Button(85, margin, 95, 50);
		buildQueueButton.setImage("res/menu/menuButton2" + View.res);
		
		endTurn = new Button(width-100, margin, 80, 80);
		endTurn.setImage("res/menu/endTurn" + View.res);
		
		performMoves = new Button(width-190, margin, 80, 80);
		performMoves.setImage("res/menu/moves" + View.res);
	}

	@Override
	public void performEvent(Event evt) {
		if (evt.getTag() == Event.EventTag.ACTIVE_PLAYER_CHANGED) {
				setVisible(true);
			}
	}

	@Override
	public boolean contains(Point p) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mousePressed(Point p) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseReleased(Point p) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(metal, x, y, null);
		g.drawImage(gas, x, y, null);
		g.drawImage(supply, x, y, null);
		menuButton.draw(g);
		buildQueueButton.draw(g);
		endTurn.draw(g);
		performMoves.draw(g);
	}

	@Override
	public boolean isVisible() {
		return enabled;
	}

	@Override
	public void setVisible(boolean set) {
		enabled = set;
	}

}