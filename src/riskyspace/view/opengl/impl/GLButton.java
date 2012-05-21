package riskyspace.view.opengl.impl;

import java.awt.MouseInfo;
import java.awt.Toolkit;
import java.io.File;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import riskyspace.view.Button;
import riskyspace.view.GlowableGraphic;
import riskyspace.view.opengl.GLRenderAble;
import riskyspace.view.opengl.Rectangle;

/**
 * Simple Button object that can be drawn in an OpenGL context.
 * @author Daniel Augurell
 * @modified Alexander Hederstaf
 */
public class GLButton extends Button implements GLRenderAble, GlowableGraphic {
	
	private GLSprite sprite;
	private GLSprite disabledSprite;
	
	private boolean canBeDisabled = false;
	private boolean canGlow = true;
	
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
		if (new File("res/textures" + textureName + "_disabled").exists()) {
			canBeDisabled = true;
			disabledSprite = new GLSprite(textureName + "_disabled", width, height);
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
		if (new File("res/textures" + textureName + "_disabled").exists()) {
			canBeDisabled = true;
			disabledSprite = new GLSprite(textureName + "_disabled",0 , 0, width, height);
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
		this.disabledSprite = new GLSprite(sprite);
	}

	@Override
	public Rectangle getBounds() {
		return renderRect;
	}

	@Override
	public void draw(GLAutoDrawable drawable, Rectangle objectRect,	Rectangle targetArea, int zIndex) {
		GL2 gl = drawable.getGL().getGL2();
//		System.out.println("Can: " + canBeDisabled + " " + sprite.toString());
		if (isEnabled()) {
			if (canGlow() && selected()) {
				gl.glColor4f(0.8f, 0.8f, 1.0f, 1.0f);
			}
			sprite.draw(drawable, renderRect, targetArea, zIndex);
		} else if (canBeDisabled) {
			disabledSprite.draw(drawable, renderRect, targetArea, zIndex);
		}
		gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	}

	@Override
	public boolean selected() {
		return this.contains(MouseInfo.getPointerInfo().getLocation());
	}

	/**
	 * Set this button to glow on mouseover
	 * @param enable <code>true</code> for glow effect.
	 */
	public void enableGlow(boolean enable) {
		canGlow = enable;
	}
	
	@Override
	public boolean canGlow() {
		return canGlow;
	}
}