package riskyspace.view.openglImpl;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

public class Sprite implements GLRenderAble {

	private Rectangle rect;
	private String textureName;
	
	public Sprite(String textureName, int width, int height) {
		this(textureName, 0, 0, width, height);
	}
	
	public Sprite(String textureName,int x, int y, int width, int height) {
		this.textureName = textureName;
		this.rect = new Rectangle(x, y, width, height);
	}

	public int getX() {
		return rect.getX();
	}
	
	public int getY() {
		return rect.getY();
	}

	public int getWidth() {
		return rect.getWidth();
	}
	
	public int getHeight() {
		return rect.getHeight();
	}
	
	@Override
	public Rectangle getBounds() {
		return rect;
	}

	@Override
	public void draw(GLAutoDrawable drawable, Rectangle objectRect, Rectangle targetArea, int zIndex) {
		if (objectRect != null && objectRect.intersects(targetArea)) {
			GL2 gl = drawable.getGL().getGL2();
			Textures.bindTexture(textureName, drawable);
			
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
			gl.glAlphaFunc(GL2.GL_GREATER, 0.10f);
			
			gl.glBegin(GL2.GL_QUADS);
			
			float x = (float) 2*getX() / targetArea.getWidth() - 1f;
			float y = (float) 2*getY() / targetArea.getHeight() - 1f;
			float x1 = x + (float) getWidth() / targetArea.getWidth();
			float y1 = y + (float) getHeight() / targetArea.getHeight();
			
			gl.glTexCoord2f(0, 0);
			gl.glVertex2f(x, y);

			gl.glTexCoord2f(0, 1);
			gl.glVertex2f(x, y1);

			gl.glTexCoord2f(1, 1);
			gl.glVertex2f(x1, y1);

			gl.glTexCoord2f(1, 0);
			gl.glVertex2f(x1, y);

			gl.glEnd();
		}
	}
}