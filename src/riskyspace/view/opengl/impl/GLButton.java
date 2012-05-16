package riskyspace.view.opengl.impl;

import javax.media.opengl.GLAutoDrawable;

import riskyspace.view.Button;
import riskyspace.view.opengl.GLRenderAble;
import riskyspace.view.opengl.Rectangle;

/**
 * 
 * @author Daniel Augurell
 *
 */
public class GLButton extends Button implements GLRenderAble{
	
	private GLSprite image;
	private GLSprite disabledImage;
	
	private Rectangle renderRect;

	public GLButton(int x, int y, int width, int height) {
		super(x, y, width, height);
		renderRect = new Rectangle(x, y, width, height);
	}
	public void setImage(String textureName, int width, int height){
		image = new GLSprite(textureName, width, height);
		disabledImage = new GLSprite(textureName + "_disabled", width, height);
	}

	@Override
	public Rectangle getBounds() {
		return renderRect;
	}

	@Override
	public void draw(GLAutoDrawable drawable, Rectangle objectRect,
			Rectangle targetArea, int zIndex) {
		if (isEnabled()) {
			image.draw(drawable, renderRect, targetArea, zIndex);
		} else {
			disabledImage.draw(drawable, renderRect, targetArea, zIndex);
		}
	}

}
