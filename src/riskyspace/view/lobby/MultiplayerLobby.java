package riskyspace.view.lobby;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import riskyspace.view.Button;
import riskyspace.view.DropdownButton;
import riskyspace.view.View;


public class MultiplayerLobby extends AbstractPreGameMenu {

	private Button playerOne = null;
	private Button playerTwo = null;
	private Button playerThree = null;
	private Button playerFour = null;
	
	private Button startGame = null;
	
	private DropdownButton numberOfPlayersButton = null;
	private DropdownButton gameModesButton = null;
	
	private int margin = 10;
	
	private int numberOfPlayers = 2;
	
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
		
		startGame = new Button(getX() + getMenuWidth() - getMenuWidth()/7 - 90, getY() + getMenuHeight() - 3*margin - 50, 180, 50);
		startGame.setImage("res/menu/lobby/startButton.png");
		
		ArrayList<String> list = new ArrayList<String>();
		list.add("2 players");
		list.add("3 players");
		list.add("4 players");
		
		numberOfPlayersButton = new DropdownButton(getX() + getMenuWidth() - getMenuWidth()/7 - 80, getY() + 3*getMenuHeight()/6, 160, 30, list);
		
		list.clear();
		list.add("Anihilation");
		
		gameModesButton = new DropdownButton(getX() + getMenuWidth() - getMenuWidth()/7 - 80, getY() + 2*getMenuHeight()/6, 160, 30, list);
		
		
	}
	
	private void createBackground() {
		rightsideMenu = Toolkit.getDefaultToolkit().getImage("res/menu/lobby/multiplayer/menubackground" + View.res).
				getScaledInstance(2*getMenuWidth()/7, getMenuHeight()-2*margin, Image.SCALE_DEFAULT);
	}

	@Override
	public void draw(Graphics g) {
		super.draw(g);
		
		g.drawImage(rightsideMenu, getX() + getMenuWidth() - 2*getMenuWidth()/7, getY() + margin, null);
		
		numberOfPlayersButton.draw(g);
		gameModesButton.draw(g);
		
		startGame.draw(g);
		
		/*
		 * Only draw if visible
		 */
		if (isVisible()) {
			playerOne.draw(g);
			playerTwo.draw(g);
			if (numberOfPlayers > 2) {
				playerThree.draw(g);
			}
			if (numberOfPlayers > 3) {
				playerFour.draw(g);
			}
		}
	}
	
	@Override
	public boolean mousePressed(Point p) {
		if (numberOfPlayersButton.mousePressed(p)) {
			numberOfPlayers = numberOfPlayersButton.getSelectedValue();
			return true;
			}
		if (gameModesButton.mousePressed(p)) {return true;}
		else {
			return false;
		}
	}


	@Override
	public boolean mouseReleased(Point p) {
		if (numberOfPlayersButton.mouseReleased(p)) {return true;}
		if (gameModesButton.mouseReleased(p)) {return true;}
		else {
			return false;
		}
	}
	

}
