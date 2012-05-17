package riskyspace.view.opengl.impl;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;

import javax.media.opengl.GLAutoDrawable;

import riskyspace.view.RankIndicator;
import riskyspace.view.View;
import riskyspace.view.opengl.GLRenderAble;
import riskyspace.view.opengl.Rectangle;

/**
 * 
 * @author Daniel Augurell
 *
 */
public class GLRankIndicator extends RankIndicator implements GLRenderAble {

	private GLSprite redLight;
	private GLSprite greenLight;
	private GLSprite offLight;
	
	private Rectangle renderRect;
	
	
	public GLRankIndicator(int maxRank) {
		super(maxRank);
		redLight = new GLSprite("menu/light_red", 24, 24);
		greenLight = new GLSprite("menu/light_green", 24, 24);
		offLight = new GLSprite("menu/light_off", 24, 24);
	}
	
	public void setSize(int width, int height) {
		super.setSize(width, height);
		renderRect = new Rectangle(getX(), getY(), width, height);

	}
	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
		renderRect = new Rectangle(x, y, getWidth(), getHeight());
	}

	@Override
	public Rectangle getBounds() {
		return renderRect;
	}
	
	@Override
	public void draw(GLAutoDrawable drawable, Rectangle objectRect, Rectangle targetArea, int zIndex) {
		if (getWidth() != 0 && getHeight() != 0) {
			int dHeight = getHeight()/3;
			/*
			 * Draw lights
			 */
			//TODO: FIX!
			/*
			for (int i = 1; i <= 3; i++) {
				if (i <= getMaxRank()) {
					if (i <= getRank()) {
						greenLight.draw(drawable, new Rectangle(getX(), getY() + (i-1)*dHeight - getHeight(), getWidth(), getHeight()), targetArea, zIndex);
					} else {
						redLight.draw(drawable, new Rectangle(getX(), getY() + (i-1)*dHeight - getHeight(), getWidth(), getHeight()), targetArea, zIndex);
					}
				} else {
					offLight.draw(drawable, new Rectangle(getX(), getY() + (i-1)*dHeight - getHeight(), getWidth(), getHeight()), targetArea, zIndex);
				}
			}
			*/
		}
	}
}