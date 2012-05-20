package riskyspace.view.opengl.impl;

import java.awt.Toolkit;

import javax.media.opengl.GLAutoDrawable;

import riskyspace.view.RankIndicator;
import riskyspace.view.opengl.GLRenderAble;
import riskyspace.view.opengl.Rectangle;

/**
 * 
 * @author Daniel Augurell
 * @modified Alexander Hederstaf
 */
public class GLRankIndicator extends RankIndicator implements GLRenderAble {

	private GLSprite redLight;
	private GLSprite greenLight;
	private GLSprite offLight;
	
	private Rectangle[] lightRects;
	
	public GLRankIndicator(int maxRank) {
		super(maxRank);
		redLight = new GLSprite("menu/light_red", 24, 24);
		greenLight = new GLSprite("menu/light_green", 24, 24);
		offLight = new GLSprite("menu/light_off", 24, 24);
	}

	private void updateLightRects() {
		int sHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
		int itemHeight = getHeight()/3;
		lightRects = new Rectangle[]{
			new Rectangle(getX(), sHeight - getY() - getHeight(), getWidth(), itemHeight),
			new Rectangle(getX(), sHeight - getY() - getHeight() + itemHeight * 1, getWidth(), itemHeight),
			new Rectangle(getX(), sHeight - getY() - getHeight() + itemHeight * 2, getWidth(), itemHeight),
		};
	}
	
	@Override	
	public void setSize(int width, int height) {
		super.setSize(width, height);
		updateLightRects();
	}
	
	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
		updateLightRects();
	}

	@Override
	public Rectangle getBounds() {
		int sHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
		return new Rectangle(getX(), sHeight - getHeight() - getY(), getWidth(), getHeight());
	}
	
	@Override
	public void draw(GLAutoDrawable drawable, Rectangle objectRect, Rectangle targetArea, int zIndex) {
		if (getWidth() != 0 && getHeight() != 0) {
			/*
			 * Draw lights
			 */
			for (int i = 0; i < lightRects.length; i++) {
				int rank = i + 1;
				if (rank <= getMaxRank()) {
					if (rank <= getRank()) {
						greenLight.draw(drawable, lightRects[i], targetArea, zIndex);
					} else {
						redLight.draw(drawable, lightRects[i], targetArea, zIndex);
					}
				} else {
					offLight.draw(drawable, lightRects[i], targetArea, zIndex);
				}
			}
		}
	}
}