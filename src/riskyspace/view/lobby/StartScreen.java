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

import riskyspace.services.Event;
import riskyspace.services.EventBus;
import riskyspace.services.EventHandler;
import riskyspace.view.Clickable;
import riskyspace.view.View;
import riskyspace.view.menu.IMenu;

public class StartScreen extends JPanel implements EventHandler {
	
	private int width;
	private int height;
	
	private IMenu startMenu = null;
	private IMenu localLobby = null;
	private IMenu loadGame = null;
	private IMenu multiplayerLobby = null;
	
	private ClickHandler clickHandler = null;
	
	/*
	 * Background
	 */
	private Image backGround = null;
	

	public StartScreen() {
		measureScreen();
		backGround = Toolkit.getDefaultToolkit().getImage("res/menu/lobby/background" + View.res).
				getScaledInstance(width, height, Image.SCALE_DEFAULT);
		createMenus();
		startMenu.setVisible(true);
		
		EventBus.INSTANCE.addHandler(this);
		clickHandler = new ClickHandler();
		addMouseListener(clickHandler);
	}

	public void measureScreen() {
		width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	}
	
	public void createMenus() {
		startMenu = new StartMenu(width/2 - 90, height/2 - 200, 180, 400);
	}
	
	public void paintComponent(Graphics g) {
		
		g.drawImage(backGround, 0, 0, null);
		
		/*
		 * TODO: Do not check isVisible, the menus should handle that part
		 */
//		if (localLobby.isVisible()) {
//			localLobby.draw(g);
//		}
//		if (loadGame.isVisible()) {
//			loadGame.draw(g);
//		}
//		if (multiplayerLobby.isVisible()) {
//			multiplayerLobby.draw(g);
//		}
		if (startMenu.isVisible()) {
			startMenu.draw(g);
		}
	}
	
	@Override
	public void performEvent(Event evt) {
		// TODO Auto-generated method stub
		
	}
	
	private class ClickHandler implements MouseListener {

		public Point pressedPoint;
		/*
		 * Click handling for different parts
		 */
		public boolean menuClick(Point point) {
			if (startMenu instanceof Clickable) {
				if (((Clickable) startMenu).mousePressed(point)) {
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
		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		@Override public void mouseExited(MouseEvent arg0) {}
		@Override public void mousePressed(MouseEvent arg0) {}
		@Override public void mouseReleased(MouseEvent arg0) {}		}
	
}
