package riskyspace.view.opengl;

import javax.media.opengl.GLAutoDrawable;

public interface GLRenderAble {
	public Rectangle getBounds();
	public void draw(GLAutoDrawable drawable, Rectangle objectRect, Rectangle targetArea, int zIndex);
}