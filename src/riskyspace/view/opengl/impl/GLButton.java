package riskyspace.view.opengl.impl;

import java.awt.Toolkit;
import java.io.File;

import javax.media.opengl.GLAutoDrawable;

import riskyspace.view.Button;
import riskyspace.view.opengl.GLRenderAble;
import riskyspace.view.opengl.Rectangle;

/**
 * Simple Button object that can be drawn in an OpenGL context.
 * @author Daniel Augurell
 * @modified Alexander Hederstaf
 */
public class GLButton extends Button implements GLRenderAble {
	
	private GLSprite sprite;
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
		sprite = new GLSprite(textureName, width, height);
		canBeDisabled = false;
		if (new File("res/textures" + textureName + "_disabled").exists()) {
			canBeDisabled = true;
			disabledImage = new GLSprite(textureName + "_disabled", width, height);
		}
	}
	
	
	/**
	 * Set the Texture to be drawn as this button.
	 * <p>
	 * This version is for use with a sprite-sheet
	 * @param textureName The name of the Texture file
	 * @param x The x coordinate of the sprite in the sprite-sheet
	 * @param y The y coordinate of the sprite in the sprite-sheet
	 * @param width The width of the sprite in the sprite-sheet
	 * @param height The height of the sprite in the sprite-sheet
	 */
	public void setTexture(String textureName, int x, int y, int width, int height){
		sprite = new GLSprite(textureName, x, y, width, height);
		canBeDisabled = false;
		if (new File("res/textures" + textureName + "_disabled").exists()) {
			canBeDisabled = true;
			disabledImage = new GLSprite(textureName + "_disabled",0 , 0, width, height);
		}
	}
	
	/**
	 * Set the Sprite to be used for drawing this GLButton directly
	 * @param sprite A GLSprite that will be used by this GLButton
	 */
	public void setSprite(GLSprite sprite) {
		this.sprite = new GLSprite(sprite);
	}
	
	/**
	 * Set the Sprite that will be drawn when this button is disabled.
	 * @param sprite A GLSprite that will be used by this GLButton
	 * @see setEnabled(boolean b)
	 */
	public void setDisabledSprite(GLSprite sprite) {
		canBeDisabled = false;
		if (sprite != null) {
			canBeDisabled = true;
		}
		this.sprite = new GLSprite(sprite);
	}

	@Override
	public Rectangle getBounds() {
		return renderRect;
	}

	@Override
	public void draw(GLAutoDrawable drawable, Rectangle objectRect,	Rectangle targetArea, int zIndex) {
		if (isEnabled()) {
			sprite.draw(drawable, renderRect, targetArea, zIndex);
		} else if (canBeDisabled) {
			disabledImage.draw(drawable, renderRect, targetArea, zIndex);
		}
	}
}