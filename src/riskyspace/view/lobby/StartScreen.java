package riskyspace.view.lobby;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import riskyspace.view.Action;
import riskyspace.view.Button;
import riskyspace.view.Clickable;
import riskyspace.view.View;
import riskyspace.view.menu.IMenu;

public class StartScreen extends JPanel{
	
	private int width;
	private int height;
	
	private boolean startScreenVisible = true;
	
	private IMenu localLobby = null;
	private IMenu loadGameMenu = null;
	private IMenu multiplayerLobby = null;
	private IMenu settingsMenu = null;
	
	private Button localGame = null;
	private Button settings = null;
	private Button multiplayer = null;
	private Button loadGame = null;
	private Button backButton = null;
	
	
	
	private ClickHandler clickHandler = null;
	
	/*
	 * Background
	 */
	private Image backGround = null;
	

	public StartScreen() {
		measureScreen();
		backGround = Toolkit.getDefaultToolkit().getImage("res/menu/lobby/background" + View.res).
				getScaledInstance(width, height, Image.SCALE_DEFAULT);
		createButtons();
		createMenus();

		
		clickHandler = new ClickHandler();
		addMouseListener(clickHandler);
	}

	public void measureScreen() {
		width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		System.out.println(width);
		height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		System.out.println(height);
	}
	
	public void createMenus() {
		multiplayerLobby = new MultiplayerLobby(width/2 - width/3, height/2 - height/3, 2*width/3, 2*height/3);
		settingsMenu = new SettingsMenu(width/2 - width/6, height/2 - height/4, width/3, height/3);
	}
	
	private void hideMenus() {
		multiplayerLobby.setVisible(false);
		settingsMenu.setVisible(false);
	}
	
	public void createButtons() {
		localGame = new Button(width/2 - 130, height/2 - 130, 240, 50);
		localGame.setImage("res/menu/lobby/localGameButton.png");
		localGame.setAction(new Action(){
			@Override
			public void performAction() {
				localLobby.setVisible(true);
				startScreenVisible = false;
			}
		});
		multiplayer = new Button(width/2 - 130, height/2 - 130 + 75, 240, 50);
		multiplayer.setImage("res/menu/lobby/multiplayerButton.png");
		multiplayer.setAction(new Action(){
			@Override
			public void performAction() {
				multiplayerLobby.setVisible(true);
				startScreenVisible = false;
			}
		});
		loadGame = new Button(width/2 - 130, height/2 - 130 + 150, 240, 50);
		loadGame.setImage("res/menu/lobby/loadGameButton.png");
		settings = new Button(width/2 - 130, height/2 - 130 + 225, 240, 50);
		settings.setImage("res/menu/lobby/settingsButton.png");
		settings.setAction(new Action(){
			@Override
			public void performAction() {
				settingsMenu.setVisible(true);
				startScreenVisible = false;
			}
		});
		backButton = new Button(width/2 - width/3 + 10, height/2 + height/3 - 60, 180, 50);
		backButton.setImage("res/menu/lobby/backButton.png");
		backButton.setAction(new Action(){
			@Override
			public void performAction() {
				startScreenVisible = true;
			}
		});
	}
	
	public void paintComponent(Graphics g) {
		
		g.drawImage(backGround, 0, 0, null);
		
		if (!startScreenVisible) {
			backButton.draw(g);
	//		if (localLobby.isVisible()) {
	//			localLobby.draw(g);
	//		}
	//		if (loadGame.isVisible()) {
	//			loadGame.draw(g);
	//		}
			if (multiplayerLobby.isVisible()) {
				multiplayerLobby.draw(g);
			}
			if (settingsMenu.isVisible()) {
				settingsMenu.draw(g);
			}
		}
		if (startScreenVisible) {
			localGame.draw(g);
			multiplayer.draw(g);
			loadGame.draw(g);
			settings.draw(g);
		}
	}
	
	private class ClickHandler implements MouseListener {
		/*
		 * Click handling for different lobby menus
		 */
		public boolean menuClick(Point point) {
			if (startScreenVisible) {
				if (multiplayer.mousePressed(point)) {return true;}
			}
			
			if (backButton.mousePressed(point)) {
				hideMenus();
				return true;
			}
			if (settings.mousePressed(point)) {
				return true;
			}
			if (localLobby instanceof Clickable) {
				if (((Clickable) localLobby).mousePressed(point)) {
					return true;
				}
			}
			if (multiplayerLobby instanceof Clickable) {
				if (((Clickable) multiplayerLobby).mousePressed(point)) {
					return true;
				}
			}
			if (settingsMenu instanceof Clickable) {
				if (((Clickable) settingsMenu).mousePressed(point)) {
					return true;
				}
			}
			return false;
		}

		@Override
		public void mouseClicked(MouseEvent me) {
			if (me.getButton() == MouseEvent.BUTTON1) {
				if (menuClick(me.getPoint())) {return;}
			}
			
		}

		@Override public void mouseReleased(MouseEvent arg0) {}
		@Override public void mouseEntered(MouseEvent arg0) {}
		@Override public void mouseExited(MouseEvent arg0) {}
		@Override public void mousePressed(MouseEvent arg0) {}
	}
	





	public void draw(Graphics g) {
		if (isVisible()) {
			localGame.draw(g);
			multiplayer.draw(g);
			loadGame.draw(g);
			settings.draw(g);
		}
	}

	
}
