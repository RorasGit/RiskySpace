package riskyspace.view.lobby;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;

import riskyspace.view.swing.SwingRenderAble;
import riskyspace.view.swing.impl.ScrollableList;

public class LoadGameMenu<E> extends AbstractPreGameMenu implements SwingRenderAble {
	
	private ScrollableList<E> savedGames = null;
	
	private Image background = null;
	
	private int margin = 30;

	public LoadGameMenu(int x, int y, int menuWidth, int menuHeight, ArrayList<E> array) {
		super(x, y, menuWidth, menuHeight);
		savedGames = new ScrollableList<E>(x + margin ,y + 2*margin/3 ,menuWidth - 2*margin, (menuHeight - 4*margin/3)/12, array);
		background = Toolkit.getDefaultToolkit().getImage("res/menu/lobby/widest_background_menu.png").
				getScaledInstance(menuWidth, menuHeight, Image.SCALE_DEFAULT);
	}
	
	@Override
	public void draw(Graphics g) {
		g.drawImage(background, getX(), getY(), null);
		savedGames.draw(g);
	}

	@Override
	public boolean mousePressed(Point p) {
		if (isVisible()) {
			if (savedGames.mousePressed(p)) {return true;}
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