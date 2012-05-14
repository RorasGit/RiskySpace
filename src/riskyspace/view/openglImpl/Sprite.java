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
			
			gl.glBegin(GL2.GL_QUADS);
			
			float x = ((float) (2*getX()) - 2*targetArea.getX() - targetArea.getWidth())/targetArea.getWidth();
			float y = ((float) (2*getY()) - 2*targetArea.getY() - targetArea.getHeight())/targetArea.getHeight();
			float x1 = ((float) (2*getX() + 2*getWidth() - 2*targetArea.getX() - targetArea.getWidth()))/targetArea.getWidth();
			float y1 = ((float) (2*getY() + 2*getHeight() - 2*targetArea.getY() - targetArea.getHeight()))/targetArea.getHeight();
			
			float renderZ = zIndex / 10000000f;
			
			gl.glTexCoord2f(0, 0);
			gl.glVertex3f(x, y, renderZ);

			gl.glTexCoord2f(0, 1);
			gl.glVertex3f(x, y1, renderZ);

			gl.glTexCoord2f(1, 1);
			gl.glVertex3f(x1, y1, renderZ);

			gl.glTexCoord2f(1, 0);
			gl.glVertex3f(x1, y, renderZ);

			gl.glEnd();
		}
	}
}