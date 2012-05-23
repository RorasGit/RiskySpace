package riskyspace.view.lobby;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;

import riskyspace.view.swing.SwingRenderAble;
import riskyspace.view.swing.impl.ScrollableList;
import riskyspace.view.swing.impl.SwingButton;

public class LoadGameMenu<E> extends AbstractPreGameMenu implements SwingRenderAble {
	
	private ScrollableList<E> savedGames = null;
	
	private Image background = null;
	
	private SwingButton loadAsLocal = null;
	private SwingButton loadAsOnline = null;
	
	private int margin = 30;

	public LoadGameMenu(int x, int y, int menuWidth, int menuHeight, ArrayList<E> array) {
		super(x, y, menuWidth, menuHeight);
		savedGames = new ScrollableList<E>(x + menuWidth/2 - menuWidth/6 + margin  ,y + menuHeight/10 + 2*margin/3 ,menuWidth/3 - 2*margin, (3*menuHeight/4 - 4*margin/3)/12, array);
		background = Toolkit.getDefaultToolkit().getImage("res/menu/lobby/widest_background_menu.png").
				getScaledInstance(menuWidth/3, 3*menuHeight/4, Image.SCALE_DEFAULT);
		loadAsLocal = new SwingButton(x + menuWidth - 2*180 - 2*margin, y + menuHeight - 60, 180, 50);
		loadAsLocal.setImage("res/menu/lobby/load_local.png");
		loadAsOnline = new SwingButton(x + menuWidth - 180 - margin, y + menuHeight - 60, 180, 50);
		loadAsOnline.setImage("res/menu/lobby/load_online.png");
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