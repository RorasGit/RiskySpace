package riskyspace.view.opengl.menu;


import javax.media.opengl.GLAutoDrawable;


import riskyspace.view.AbstractSideMenu;
import riskyspace.view.opengl.GLRenderAble;
import riskyspace.view.opengl.Rectangle;
import riskyspace.view.opengl.impl.GLSprite;
/**
 * 
 * @author Daniel Augurell
 *
 */

public abstract class GLAbstractSideMenu extends AbstractSideMenu implements GLRenderAble {
	
	private GLSprite background;
	private Rectangle renderRect;
	
	public GLAbstractSideMenu(int x, int y, int menuWidth, int menuHeight){
		this(x, y, menuWidth, menuHeight, "");
	}

	public GLAbstractSideMenu(int x, int y, int menuWidth, int menuHeight, String menuName) {
		super(x, y, menuWidth, menuHeight, menuName);
		renderRect = new Rectangle(x, 0, menuWidth, menuHeight);
		background = new GLSprite("menu/background", 256, 688);
	}
	@Override
	public void draw(GLAutoDrawable drawable, Rectangle objectRect, Rectangle targetArea, int zIndex) {
		background.draw(drawable, objectRect, targetArea, zIndex);
	}

	@Override
	public Rectangle getBounds() {
		return renderRect;
	}

}