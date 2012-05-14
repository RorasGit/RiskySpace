package riskyspace.view.lobby;

import java.awt.Point;

import riskyspace.view.Clickable;
import riskyspace.view.IMenu;

public abstract class AbstractPreGameMenu implements IMenu, Clickable {
	
	private Boolean enabled;
	
	private int x,y;
	private int menuWidth,menuHeight;
	
	public AbstractPreGameMenu(int x, int y, int menuWidth, int menuHeight) {
		this.enabled = false;
		this.x = x;
		this.y = y;
		this.menuHeight = menuHeight;
		this.menuWidth = menuWidth;
	}
	
	@Override
	public boolean contains(Point p) {
		/*
		 * Only handle mouse event if enabled
		 */
		if (enabled) {
			boolean xLegal = p.x >= x && p.x <= x + menuWidth;
			boolean yLegal = p.y >= y && p.y <= y + menuHeight;
			return xLegal && yLegal;
		}
		return false;
	}

	@Override
	public void setVisible(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public boolean isVisible() {
		return enabled;
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
}