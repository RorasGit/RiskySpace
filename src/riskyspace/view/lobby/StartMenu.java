package riskyspace.view.lobby;

import java.awt.Graphics;
import java.awt.Point;

import riskyspace.services.Event;
import riskyspace.view.Button;

public class StartMenu extends AbstractPreGameMenu {
	
	private Button localGame = null;
	private Button multiplayer = null;
	private Button loadGame = null;
	private Button settings = null;

	public StartMenu(int x, int y, int menuWidth, int menuHeight) {
		super(x, y, menuWidth, menuHeight);
		createButtons();
		
	}
	
	public void createButtons() {
		localGame = new Button(getX(), getY(), getMenuWidth(), getMenuHeight()/5);
		localGame.setImage("res/menu/lobby/localGameButton.png");
		multiplayer = new Button(getX(), getY() + getMenuHeight()/5 + getMenuHeight()/10, getMenuWidth(), getMenuHeight()/5);
		multiplayer.setImage("res/menu/lobby/multiplayerButton.png");
		loadGame = new Button(getX(), getY() + 3*getMenuHeight()/5, getMenuWidth(), getMenuHeight()/5);
		loadGame.setImage("res/menu/lobby/loadGameButton.png");
		settings = new Button(getX(), getY() + 4*getMenuHeight()/5 + getMenuHeight()/10, getMenuWidth(), getMenuHeight()/5);
		settings.setImage("res/menu/lobby/settingsButton.png");
	}

	@Override
	public void draw(Graphics g) {
		if (isVisible()) {
			localGame.draw(g);
			multiplayer.draw(g);
			loadGame.draw(g);
			settings.draw(g);
		}
	}
	
	@Override
	public boolean mousePressed(Point p) {
		/*
		 * Only handle mouse event if enabled
		 */
		if (isVisible()) {
			if (localGame.mousePressed(p)) {return true;}
			if (multiplayer.mousePressed(p)) {return true;}
			if (loadGame.mousePressed(p)) {return true;}
			if (settings.mousePressed(p)) {return true;}
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
			if (localGame.mouseReleased(p)) {return true;}
			if (multiplayer.mouseReleased(p)) {return true;}
			if (loadGame.mouseReleased(p)) {return true;}
			if (settings.mouseReleased(p)) {return true;}
			else {
				return false;
			}
		}
		return false;
	}

	@Override
	public void performEvent(Event evt) {
//		if(isVisible()) {
//			if (evt.getTag() == Event.EventTag.NEW_MENU) {
//				setVisible(false);
//			}
//		}
	}

}
