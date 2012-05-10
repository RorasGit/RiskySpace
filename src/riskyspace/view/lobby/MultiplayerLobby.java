package riskyspace.view.lobby;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import riskyspace.view.Button;
import riskyspace.view.View;


public class MultiplayerLobby extends AbstractPreGameMenu {

	private Button playerOne = null;
	private Button playerTwo = null;
	private Button playerThree = null;
	private Button playerFour = null;
	
	private int margin = 10;
	
	private Image background = null;
	private Image rightsideMenu = null;
	
	public MultiplayerLobby(int x, int y, int menuWidth, int menuHeight) {
		super(x, y, menuWidth, menuHeight);
		createButtons();
		createBackground();
	}
	
	private void createButtons() {
		/*
		 * TODO: fix none absolute values
		 */
		playerOne = new Button(getX() + margin, getY() + margin, 240, 50);
		playerOne.setImage("res/menu/lobby/multiplayer/playerButton.png");
		playerTwo = new Button(getX() + margin, getY() + 2*margin + 50, 240, 50);
		playerTwo.setImage("res/menu/lobby/multiplayer/playerButton.png");
		playerThree = new Button(getX() + margin, getY() + 3*margin + 100, 240, 50);
		playerThree.setImage("res/menu/lobby/multiplayer/playerButton.png");
		playerFour = new Button(getX() + margin, getY() + 4*margin + 150, 240, 50);
		playerFour.setImage("res/menu/lobby/multiplayer/playerButton.png");
	}
	
	private void createBackground() {
		rightsideMenu = Toolkit.getDefaultToolkit().getImage("res/menu/lobby/multiplayer/menubackground" + View.res).
				getScaledInstance(2*getMenuWidth()/7, getMenuHeight()-2*margin, Image.SCALE_DEFAULT);
	}

	@Override
	public void draw(Graphics g) {
		super.draw(g);
		
		g.drawImage(rightsideMenu, getX() + getMenuWidth() - 2*getMenuWidth()/7, getY() + margin, null);
		
		/*
		 * Only draw if visible
		 */
		if (isVisible()) {
			playerOne.draw(g);
			playerTwo.draw(g);
			playerThree.draw(g);
			playerFour.draw(g);
		}
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
