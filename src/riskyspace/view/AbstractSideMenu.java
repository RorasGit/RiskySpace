package riskyspace.view;

import java.awt.Point;

import riskyspace.view.Clickable;
import riskyspace.view.IMenu;

/**
 * 
 * @author Daniel Augurell
 *
 */
public abstract class AbstractSideMenu implements IMenu, Clickable{

	private boolean visible;

	private int x, y;
	private int menuHeight;
	private int menuWidth;

	private String menuName;
		
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
