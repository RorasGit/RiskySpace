package riskyspace.view.lobby;

import java.awt.Graphics;
import java.awt.Point;

import riskyspace.services.Event;
import riskyspace.view.Button;

public class LocalLobby extends AbstractPreGameMenu {
	
	private Button twoPlayers = null;
	private Button threePlayers = null;
	private Button fourPlayers = null;
	
	public LocalLobby(int x, int y, int menuWidth, int menuHeight) {
		super(x, y, menuWidth, menuHeight);
		
	}
	
	private void createButtons() {
		twoPlayers = new Button(getX()/2 - getMenuWidth()/2  , getY()/2 - getMenuHeight()/2, getMenuWidth(), getMenuHeight());
	}
	
	@Override
	public void draw(Graphics g) {
		
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

}
