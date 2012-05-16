package riskyspace.view.swing.menu;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import riskyspace.view.AbstractSideMenu;
import riskyspace.view.View;
import riskyspace.view.swing.SwingRenderAble;

/**
 * 
 * @author Daniel Augurell
 *
 */
public abstract class SwingAbstractSideMenu extends AbstractSideMenu implements SwingRenderAble {

	private Image background = null;
	
	public SwingAbstractSideMenu(int x, int y, int menuWidth, int menuHeight){
		this(x, y, menuWidth, menuHeight, "");
	}

	public SwingAbstractSideMenu(int x, int y, int menuWidth, int menuHeight, String menuName) {
		super(x, y, menuWidth, menuHeight, menuName);
		background = Toolkit.getDefaultToolkit().getImage("res/menu/menubackground" + View.res)
				.getScaledInstance(menuWidth, menuHeight, Image.SCALE_DEFAULT);
	}
	@Override
	public void draw(Graphics g) {
		if (isVisible()) {
			g.drawImage(background, getX(), getY(), null);
		}
	}
}