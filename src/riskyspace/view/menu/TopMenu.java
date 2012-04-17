package riskyspace.view.menu;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import riskyspace.logic.FleetMove;
import riskyspace.model.Colony;
import riskyspace.services.Event;
import riskyspace.services.EventBus;
import riskyspace.services.EventHandler;
import riskyspace.view.Action;
import riskyspace.view.Button;
import riskyspace.view.Clickable;
import riskyspace.view.View;

public class TopMenu implements IMenu, Clickable, EventHandler {
	
	private boolean enabled;
	
	private Image metal = null;
	private Image gas = null;
	private Image supply = null;
	private Image currentRound;
	
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
		
		menuButton = new Button(margin, margin, 79, 30);
		menuButton.setImage("res/menu/menuButton3" + View.res);
		menuButton.setAction(new Action() {
			@Override
			public void performAction() {
				System.exit(0);
			}
		});
		
		buildQueueButton = new Button(margin + 84, margin, 79, 30);
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
	}

	@Override
	public void performEvent(Event evt) {
		if (evt.getTag() == Event.EventTag.ACTIVE_PLAYER_CHANGED) {
				setVisible(true);
			}
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
		g.drawImage(metal, x, y, null);
		g.drawImage(gas, x, y, null);
		g.drawImage(supply, x, y, null);
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

}