package riskyspace.view.swing.impl;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

public class SwingSprite {
	private double rotation = 0;
	private float dx;
	private float dy;
	private Image image = null;
	
	/**
	 * Creates a new Sprite object
	 * @param image The texture to be drawn.
	 * @param dx The offset from 0 to 1 of the left side 
	 * inside the position
	 * @param dy The offset from 0 to 1 of the top side 
	 * inside the position
	 */
	public SwingSprite(Image image, float dx, float dy) {
		this.image = image;
		this.dx = dx;
		this.dy = dy;
	}
	
	/**
	 * Set the rotation of this Sprite in radians
	 * @param rotation Set rotation to this angle.
	 */
	public void setRotation(double rotation) {
			this.rotation = rotation;
	}
	
	/**
	 * Rotates the Sprite
	 * @param rotation Rotate this amount.
	 */
	public void rotate(double rotation) {
			this.rotation += rotation;
	}
	
	public void draw(Graphics g, int x, int y, int squareSize) {
		Graphics2D g2D = (Graphics2D) g;
		g2D.rotate(rotation, x + dx + image.getWidth(null)/2, y + dy + image.getHeight(null)/2);
		g2D.drawImage(image, (int) (x + dx*squareSize), (int) (y + dy*squareSize), null);
		g2D.rotate(-rotation, x + dx + image.getWidth(null)/2, y + dy + image.getHeight(null)/2);
	}
}