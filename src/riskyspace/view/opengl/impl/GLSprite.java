package riskyspace.view.opengl.impl;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import riskyspace.view.opengl.GLRenderAble;
import riskyspace.view.opengl.Rectangle;
import riskyspace.view.opengl.Textures;

import com.jogamp.opengl.util.texture.Texture;

public class GLSprite implements GLRenderAble {

	private Rectangle textureRect;
	private Rectangle renderRect;
	private String textureName;
	private double rotation = 0.0;
	
	public GLSprite(String textureName, int width, int height) {
		this(textureName, 0, 0, width, height);
	}
	
	public GLSprite(String textureName,int x, int y, int width, int height) {
		this.textureName = textureName;
		this.textureRect = new Rectangle(x, y, width, height);
		renderRect = new Rectangle(0, 0, 0, 0);
	}

	public void setLocationX(int x) {
		renderRect.setX(x);
	}
	
	public void setLocationY(int y) {
		renderRect.setY(y);
	}
	
	public void setSize(int width, int height) {
		setWidth(width);
		setHeight(height);
	}
	
	public void setWidth(int width) {
		renderRect.setWidth(width);
	}

	public void setHeight(int height) {
		renderRect.setHeight(height);
	}
	
	public void setBounds(Rectangle rectangle) {
		renderRect = new Rectangle(rectangle);
	}
	
	public void setRotation(double rotation) {
		this.rotation = rotation;
	}
	
	@Override
	public Rectangle getBounds() {
		return renderRect;
	}

	@Override
	public void draw(GLAutoDrawable drawable, Rectangle objectRect, Rectangle targetArea, int zIndex) {
		if (objectRect != null && objectRect.intersects(targetArea)) {
			GL2 gl = drawable.getGL().getGL2();
			Texture tex = Textures.bindTexture(textureName, drawable);
			
			gl.glBegin(GL2.GL_QUADS);
			
			/* Variables for texture position within larger texture*/
			float tX = (float) textureRect.getX() / tex.getWidth();
			float tY = (float) textureRect.getY() / tex.getHeight();
			float tX1 = tX + (float) textureRect.getWidth() / tex.getWidth();
			float tY1 = tY + (float) textureRect.getHeight() / tex.getHeight();
			
			/* Variables for drawing position */
			float x = ((float) (2*objectRect.getX()) - 2*targetArea.getX() - targetArea.getWidth())/targetArea.getWidth();
			float y = ((float) (2*objectRect.getY()) - 2*targetArea.getY() - targetArea.getHeight())/targetArea.getHeight();
			float x1 = ((float) (2*objectRect.getX() + 2*objectRect.getWidth() - 2*targetArea.getX() - targetArea.getWidth()))/targetArea.getWidth();
			float y1 = ((float) (2*objectRect.getY() + 2*objectRect.getHeight() - 2*targetArea.getY() - targetArea.getHeight()))/targetArea.getHeight();
			
			/*
			 * TODO: 
			 * Fix rotation
			 */
			float rX;
			float rY;
			float rX1;
			float rY1;
			
			float renderZ = 1f / zIndex;
			
			gl.glTexCoord2f(tX, tY);
			gl.glVertex3f(x, y, renderZ);

			gl.glTexCoord2f(tX, tY1);
			gl.glVertex3f(x, y1, renderZ);

			gl.glTexCoord2f(tX1, tY1);
			gl.glVertex3f(x1, y1, renderZ);

			gl.glTexCoord2f(tX1, tY);
			gl.glVertex3f(x1, y, renderZ);

			/*  Rotate 90d clockwise*/
//			gl.glTexCoord2f(tX, tY);
//			gl.glVertex3f(x, y1, renderZ);
//
//			gl.glTexCoord2f(tX, tY1);
//			gl.glVertex3f(x1, y1, renderZ);
//
//			gl.glTexCoord2f(tX1, tY1);
//			gl.glVertex3f(x1, y, renderZ);
//
//			gl.glTexCoord2f(tX1, tY);
//			gl.glVertex3f(x, y, renderZ);
			
			gl.glEnd();
		}
	}
}