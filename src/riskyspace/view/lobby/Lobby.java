package riskyspace.view.lobby;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;

import riskyspace.view.DropdownButton;
import riskyspace.view.View;
import riskyspace.view.swing.SwingDrawable;
import riskyspace.view.swing.impl.SwingButton;


public class Lobby extends AbstractPreGameMenu implements SwingDrawable {

	private SwingButton playerOne = null;
	private SwingButton playerTwo = null;
	private SwingButton playerThree = null;
	private SwingButton playerFour = null;
	
	private SwingButton startGame = null;
	
	private DropdownButton<String> numberOfPlayersButton = null;
	private DropdownButton<String> gameModesButton = null;
	
	private int margin = 10;
	
	private Image rightsideMenu = null;
	private Image gameModeImage = null;
	
	public Lobby(int x, int y, int menuWidth, int menuHeight) {
		super(x, y, menuWidth, menuHeight);
		createButtons();
		createBackground();
	}
	
	private void createButtons() {
		/*
		 * TODO: fix none absolute values
		 */
		playerOne = new SwingButton(getX() + margin, getY() + margin, 240, 50);
		playerOne.setImage("res/menu/lobby/wideButton" + View.res);
		playerTwo = new SwingButton(getX() + margin, getY() + 2*margin + 50, 240, 50);
		playerTwo.setImage("res/menu/lobby/wideButton" + View.res);
		playerThree = new SwingButton(getX() + margin, getY() + 3*margin + 100, 240, 50);
		playerThree.setImage("res/menu/lobby/wideButton" + View.res);
		playerThree.setEnabled(false);
		playerFour = new SwingButton(getX() + margin, getY() + 4*margin + 150, 240, 50);
		playerFour.setImage("res/menu/lobby/wideButton" + View.res);
		playerFour.setEnabled(false);
		
		startGame = new SwingButton(getX() + getMenuWidth() - getMenuWidth()/7 - 90, getY() + getMenuHeight() - 3*margin - 50, 180, 50);
		startGame.setImage("res/menu/lobby/startGameButton" + View.res);
		
		ArrayList<String> list = new ArrayList<String>();
		list.add("2 players");
		list.add("3 players");
		list.add("4 players");
		
		numberOfPlayersButton = new DropdownButton<String>(getX() + getMenuWidth() - getMenuWidth()/7 - 80, getY() + 3*getMenuHeight()/6, 160, 30, list);
		
		list.clear();
		list.add("Annihilation");
		
		gameModesButton = new DropdownButton<String>(getX() + getMenuWidth() - getMenuWidth()/7 - 80, getY() + 2*getMenuHeight()/6, 160, 30, list);
	}
	
	private void createBackground() {
		rightsideMenu = Toolkit.getDefaultToolkit().getImage("res/menu/lobby/multiplayer/widerMenubackground" + View.res).
				getScaledInstance(2*getMenuWidth()/7, getMenuHeight()-2*margin, Image.SCALE_DEFAULT);
		gameModeImage = Toolkit.getDefaultToolkit().getImage("res/menu/lobby/Annihilation" + View.res).
				getScaledInstance(2*getMenuWidth()/7 - 6*margin, (2*getMenuWidth()/7 - 3*margin)/2, Image.SCALE_DEFAULT);
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(rightsideMenu, getX() + getMenuWidth() - 2*getMenuWidth()/7, getY() + margin, null);
		g.drawImage(gameModeImage, getX() + getMenuWidth() - 2*getMenuWidth()/7 + 3*margin, getY() + 4*margin, null);
		numberOfPlayersButton.draw(g);
		gameModesButton.draw(g);
		
		startGame.draw(g);
		
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
		if (isVisible()) {
			if (numberOfPlayersButton.mousePressed(p)) {
				gameModesButton.setOpen(false);
				if (numberOfPlayersButton.getSelectedValue() == 3) {
					playerThree.setEnabled(true);
					playerFour.setEnabled(false);
				}
				else if (numberOfPlayersButton.getSelectedValue() == 4) {
					playerThree.setEnabled(true);
					playerFour.setEnabled(true);
				} else {
					playerThree.setEnabled(false);
					playerFour.setEnabled(false);
				}
				return true;
			}
			if (gameModesButton.mousePressed(p)) {
				numberOfPlayersButton.setOpen(false);
				return true;
			}
			numberOfPlayersButton.setOpen(false);
			gameModesButton.setOpen(false);
		}
		return false;
	}


	@Override
	public boolean mouseReleased(Point p) {
		if (numberOfPlayersButton.mouseReleased(p)) {return true;}
		if (gameModesButton.mouseReleased(p)) {return true;}
			return false;
	}
	

}
