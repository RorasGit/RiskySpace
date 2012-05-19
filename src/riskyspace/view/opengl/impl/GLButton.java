package riskyspace.view.opengl.impl;

import java.awt.Toolkit;
import java.io.File;

import javax.media.opengl.GLAutoDrawable;

import riskyspace.view.Button;
import riskyspace.view.opengl.GLRenderAble;
import riskyspace.view.opengl.Rectangle;

/**
 * 
 * @author Daniel Augurell
 *
 */
public class GLButton extends Button implements GLRenderAble {
	
	private GLSprite image;
	private GLSprite disabledImage;
	
	private boolean canBeDisabled = false;
	
	private Rectangle renderRect;

	public GLButton(int x, int y, int width, int height) {
		super(x, y, width, height);
		int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
		renderRect = new Rectangle(x, screenHeight - y - height, width, height);
	}
	
	/**
	 * Set the Texture to be drawn as this button.
	 * @param textureName The name of the texture
	 * @param width Width of texture 
	 * @param height Height of texture
	 */
	public void setTexture(String textureName, int width, int height){
		image = new GLSprite(textureName, width, height);
		canBeDisabled = false;
		if (new File("res/textures" + textureName + "_disabled").exists()) {
			canBeDisabled = true;
			disabledImage = new GLSprite(textureName + "_disabled", width, height);
		}
	}

	@Override
	public Rectangle getBounds() {
		return renderRect;
	}

	@Override
	public void draw(GLAutoDrawable drawable, Rectangle objectRect,	Rectangle targetArea, int zIndex) {
		if (isEnabled()) {
			image.draw(drawable, renderRect, targetArea, zIndex);
		} else if (canBeDisabled) {
			disabledImage.draw(drawable, renderRect, targetArea, zIndex);
		}
	}
}