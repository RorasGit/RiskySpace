package riskyspace.view.lobby;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import riskyspace.view.Action;
import riskyspace.view.Clickable;
import riskyspace.view.IMenu;
import riskyspace.view.View;
import riskyspace.view.swing.SwingRenderAble;
import riskyspace.view.swing.impl.SwingButton;

public class StartScreen extends JPanel {
	
	private int width;
	private int height;
	
	private boolean startScreenVisible = true;
	
	private IMenu localLobby = null;
	private IMenu loadGameMenu = null;
	private IMenu multiplayerLobby = null;
	private IMenu settingsMenu = null;
	
	private SwingButton localGame = null;
	private SwingButton settings = null;
	private SwingButton multiplayer = null;
	private SwingButton loadGame = null;
	private SwingButton exit = null;
	private SwingButton backButton = null;
	
	
	private ClickHandler clickHandler = null;
	
	/*
	 * Background
	 */
	private Image backGround = null;

	/*
	 * MultiplayerMenu
	 */
	private Image rightsideMenu = null;
	private boolean connectToMultiplayer = false;
	

	public StartScreen() {
		measureScreen();
		backGround = Toolkit.getDefaultToolkit().getImage("res/menu/lobby/background.png").
				getScaledInstance(width, height, Image.SCALE_DEFAULT);
		createButtons();
		createMenus();
		createJoinMenu();
		
		clickHandler = new ClickHandler();
		addMouseListener(clickHandler);
	}

	private void createJoinMenu() {
		rightsideMenu = Toolkit.getDefaultToolkit().getImage("res/menu/lobby/multiplayer/widerMenubackground.png").
				getScaledInstance((4*width/3)/7, 2*height/3-2*10, Image.SCALE_DEFAULT);	
	}

	public void measureScreen() {
		width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	}
	
	public void createMenus() {
		localLobby = new Lobby(width/6, height/6, 2*width/3, 2*height/3);
		multiplayerLobby = new Lobby(width/6, height/6, 2*width/3, 2*height/3);
		settingsMenu = new SettingsMenu(width/3, height/4, width/3, height/3);
		
		ArrayList<String> list = new ArrayList<String>();
		for (int i=0; i<20; i++) {
			list.add("" + i);
		}
		
		loadGameMenu = new LoadGameMenu<String>(width/2 - width/4, height/2 - height/4, width/2, height/2, list);
	}
	
	private void hideMenus() {
		localLobby.setVisible(false);
		multiplayerLobby.setVisible(false);
		loadGameMenu.setVisible(false);
		settingsMenu.setVisible(false);
	}
	
	public void createButtons() {
		localGame = new SwingButton(width/2 - 125, height/2 - 200, 250, 50);
		localGame.setImage("res/menu/lobby/localGameButton.png");
		localGame.setAction(new Action(){
			@Override
			public void performAction() {
				localLobby.setVisible(true);
				startScreenVisible = false;
			}
		});
		multiplayer = new SwingButton(width/2 - 125, height/2 - 200 + 100, 250, 50);
		multiplayer.setImage("res/menu/lobby/multiplayerButton.png");
		multiplayer.setAction(new Action(){
			@Override
			public void performAction() {
				multiplayerLobby.setVisible(true);
				startScreenVisible = false;
			}
		});
		loadGame = new SwingButton(width/2 - 125, height/2 - 200 + 200, 250, 50);
		loadGame.setImage("res/menu/lobby/loadGameButton.png");
		loadGame.setAction(new Action(){
			@Override
			public void performAction() {
				loadGameMenu.setVisible(true);
				startScreenVisible = false;
			}
		});
		settings = new SwingButton(width/2 - 125, height/2 - 200 + 300, 250, 50);
		settings.setImage("res/menu/lobby/settingsButton.png");
		settings.setAction(new Action(){
			@Override
			public void performAction() {
				settingsMenu.setVisible(true);
				startScreenVisible = false;
			}
		});
		exit = new SwingButton(width/2 - 125, height/2 - 200 + 400, 250, 50);
		exit.setImage("res/menu/lobby/exitGameButton.png");
		exit.setAction(new Action(){
			@Override
			public void performAction() {
				System.exit(0);
			}
		});
		backButton = new SwingButton(width/2 - width/3 + 10, height/2 + height/3 - 60, 180, 50);
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
			if (localLobby.isVisible()) {
				((SwingRenderAble) localLobby).draw(g);
			}
			if (loadGameMenu.isVisible()) {
				((SwingRenderAble) loadGameMenu).draw(g);
			}
			if (multiplayerLobby.isVisible()) {
				((SwingRenderAble) multiplayerLobby).draw(g);
			}
			if (settingsMenu.isVisible()) {
				((SwingRenderAble) settingsMenu).draw(g);
			}
		}
		else if (startScreenVisible) {
			localGame.draw(g);
			multiplayer.draw(g);
			loadGame.draw(g);
			settings.draw(g);
			exit.draw(g);
			if(connectToMultiplayer){
				drawJoinMenu(g);
			}
		}
	}
	
	private void drawJoinMenu(Graphics g) {	
		g.drawImage(rightsideMenu, width/2 - width/3 + 2*width/3 - 2*(2*width/3)/7, height/2 - height/3 + 10, null);
	}

	private class ClickHandler implements MouseListener {
		/*
		 * Click handling for different lobby menus
		 */
		public boolean menuClick(Point point) {
			if (startScreenVisible) {
				if (localGame.mousePressed(point)) {return true;}
				if (multiplayer.mousePressed(point)) {return true;}
				if (loadGame.mousePressed(point)) {return true;}
				if (settings.mousePressed(point)) {return true;}
				if (exit.mousePressed(point)) {return true;}
			}
			if (backButton.mousePressed(point)) {
				hideMenus();
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
			if (loadGameMenu instanceof Clickable) {
				if (((Clickable) loadGameMenu).mousePressed(point)) {
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

		@Override public void mouseClicked(MouseEvent me) {}
		@Override public void mouseReleased(MouseEvent arg0) {}
		@Override public void mouseEntered(MouseEvent arg0) {}
		@Override public void mouseExited(MouseEvent arg0) {}
		@Override public void mousePressed(MouseEvent me) {
			if (me.getButton() == MouseEvent.BUTTON1) {
				if (menuClick(me.getPoint())) {return;}
			}
		}
	}
}
