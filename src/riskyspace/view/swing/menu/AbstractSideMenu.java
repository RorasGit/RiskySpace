package riskyspace.view.swing.menu;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;

import riskyspace.services.EventHandler;
import riskyspace.view.Clickable;
import riskyspace.view.IMenu;
import riskyspace.view.View;
import riskyspace.view.swing.SwingDrawable;
import riskyspace.model.Player;

public abstract class AbstractSideMenu implements IMenu, Clickable, SwingDrawable {

	private boolean visible;

	private int x, y;
	private int menuHeight;
	private int menuWidth;

	private String menuName;
	
	private Image background = null;
	
	public AbstractSideMenu(int x, int y, int menuWidth, int menuHeight){
		this(x, y, menuWidth, menuHeight, "");
	}

	public AbstractSideMenu(int x, int y, int menuWidth, int menuHeight, String menuName) {
		this.visible = false;
		this.x = x;
		this.y = y;
		this.menuHeight = menuHeight;
		this.menuWidth = menuWidth;
		this.menuName = menuName;
		background = Toolkit.getDefaultToolkit().getImage("res/menu/menubackground" + View.res)
				.getScaledInstance(menuWidth, menuHeight, Image.SCALE_DEFAULT);
	}

	@Override
	public boolean contains(Point p) {
		/*
		 * Only handle mouse event if enabled
		 */
		if (visible) {
			boolean xLegal = p.x >= x && p.x <= x + menuWidth;
			boolean yLegal = p.y >= y && p.y <= y + menuHeight;
			return xLegal && yLegal;
		}
		return false;
	}

	@Override
	public void draw(Graphics g) {
		if (visible) {
			g.drawImage(background, x, y, null);
		}
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getMenuHeight() {
		return menuHeight;
	}

	public int getMenuWidth() {
		return menuWidth;
	}
	
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuName() {
		return menuName;
	}
}
