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

	/**
	 * Copy constructor
	 * @param sprite The GLSprite to copy.
	 */
	public GLSprite(GLSprite sprite) {
		this.textureName = sprite.textureName;
		this.textureRect = new Rectangle(sprite.textureRect);
		this.renderRect = new Rectangle(sprite.renderRect);
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
	
	/**
	 * Set clockwise rotation of this Sprite
	 * @param rotation A rotation angle in degrees
	 * @see Math.toDegrees(double radian)
	 */
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
			gl.glEnable(GL2.GL_TEXTURE_2D);
			Texture tex = Textures.bindTexture(textureName, drawable);
			
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
			
			float texCenterX = tX + (0.5f * textureRect.getWidth()) / tex.getWidth();
			float texCenterY = tY + (0.5f * textureRect.getHeight()) / tex.getHeight();
			
			float renderZ = 1f / zIndex;
			
			if (rotation != 0) {
				gl.glMatrixMode( GL2.GL_TEXTURE );
				gl.glPushMatrix();
				gl.glLoadIdentity();
				gl.glTranslatef(texCenterX, texCenterY, 0);
				gl.glRotated(rotation, 0, 0, 1);
				gl.glTranslatef(-texCenterX, -texCenterY, 0);
			}

			gl.glBegin(GL2.GL_QUADS);
			
			gl.glTexCoord2f(tX, tY);
			gl.glVertex3f(x, y, renderZ);

			gl.glTexCoord2f(tX, tY1);
			gl.glVertex3f(x, y1, renderZ);

			gl.glTexCoord2f(tX1, tY1);
			gl.glVertex3f(x1, y1, renderZ);

			gl.glTexCoord2f(tX1, tY);
			gl.glVertex3f(x1, y, renderZ);

			gl.glEnd();
			
			if (rotation != 0) {
				gl.glLoadIdentity();
				gl.glPopMatrix();
			}
		}
	}
	
	@Override
	public String toString() {
		return "GLSprite: " + textureName + " " + textureRect ;
	}
}