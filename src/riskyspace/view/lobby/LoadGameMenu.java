package riskyspace.view.lobby;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import riskyspace.view.Action;
import riskyspace.view.lobby.Lobby.LocalGameClient;
import riskyspace.view.swing.SwingRenderAble;
import riskyspace.view.swing.impl.ScrollableList;
import riskyspace.view.swing.impl.SwingButton;

public class LoadGameMenu extends AbstractPreGameMenu implements SwingRenderAble {
	
	private ScrollableList<String> savedGames = null;
	
	private Image background = null;
	
	private SwingButton loadAsLocal = null;
	private SwingButton loadAsOnline = null;
	
	private int margin = 30;

	public LoadGameMenu(int x, int y, int menuWidth, int menuHeight, ArrayList<String> array) {
		super(x, y, menuWidth, menuHeight);
		savedGames = new ScrollableList<String>(x + menuWidth/2 - menuWidth/6 + margin  ,y + menuHeight/10 + 2*margin/3 ,menuWidth/3 - 2*margin, (3*menuHeight/4 - 4*margin/3)/12, array);
		background = Toolkit.getDefaultToolkit().getImage("res/menu/lobby/widest_background_menu.png").
				getScaledInstance(menuWidth/3, 3*menuHeight/4, Image.SCALE_DEFAULT);
		loadAsLocal = new SwingButton(x + menuWidth - 2*180 - 2*margin, y + menuHeight - 60, 180, 50);
		loadAsLocal.setImage("res/menu/lobby/load_local.png");
		loadAsLocal.setAction(new Action() {
			@Override
			public void performAction() {
				/*
				 * Load Local Game with selected save
				 */
				String saveName = savedGames.getSelectedSave();
				localClient.showLobby(saveName, true);
			}
		});
		loadAsOnline = new SwingButton(x + menuWidth - 180 - margin, y + menuHeight - 60, 180, 50);
		loadAsOnline.setImage("res/menu/lobby/load_online.png");
		loadAsOnline.setAction(new Action() {
			@Override
			public void performAction() {
				/*
				 * Load Online Game with selected save
				 */
				String saveName = savedGames.getSelectedSave();
				localClient.showLobby(saveName, false);
			}
		});
	}
	
	private LocalClient localClient;
	
	class LocalClient extends Observable {
		public void showLobby(String saveName, boolean local) {
			setChanged();
			notifyObservers("save=" + saveName + "?local=" + local);
		}
	}

	public void setObserver(final Observer o) {
		// Observer used in local game to dispose window
		localClient = new LocalClient();
		localClient.addObserver(o);
	}
	
	@Override
	public void draw(Graphics g) {
		g.drawImage(background, getX() + getMenuWidth()/2 - getMenuWidth()/6, getY() + getMenuHeight()/10, null);
		savedGames.draw(g);
		loadAsLocal.draw(g);
		loadAsOnline.draw(g);
	}

	@Override
	public boolean mousePressed(Point p) {
		if (isVisible()) {
			if (savedGames.mousePressed(p)) {return true;}
			if (loadAsLocal.mousePressed(p)) {return true;}
			if (loadAsOnline.mousePressed(p)) {return true;}
		}
		return false;
	}

	@Override
	public boolean mouseReleased(Point p) {
		if (isVisible()) {
			if (savedGames.mouseReleased(p)) {return true;}
		}
		return false;
	}
}