package riskyspace.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

public class Button implements Clickable {

	private int x, y, width, height;
	private Image image = null;
	private Image scaledImage = null;
	
	private String text = null;
	
	public Button(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void setImage(String location) {
		image = Toolkit.getDefaultToolkit().getImage(location);
		scaledImage = image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
	}
	
	public void draw(Graphics g) {
		g.drawImage(scaledImage, x, y, null);
		
		/*
		 * Draw centered text
		 */
		if (text != null) {
			drawButtonText(g);
		}
	}
	
	private void drawButtonText(Graphics g) {
		Color prev = g.getColor();
		g.setColor(Color.LIGHT_GRAY);
		Font saveFont = g.getFont();
		g.setFont(new Font("Comic Sanc MS", Font.ITALIC, 40));
		int textX = x - (g.getFontMetrics().stringWidth(text) / 2) + (width / 2);
		int textY = y + (g.getFontMetrics().getHeight() / 2) + (height / 2);
		g.drawString(text, textX, textY);
		g.setFont(saveFont);
		g.setColor(prev);
	}
	
	public void setLocation(Point p) {
		this.x = p.x;
		this.y = p.y;
	}

	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void setSize(Dimension d) {
		this.width = d.width;
		this.height = d.height;
	}
	
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public boolean contains(Point p) {
		boolean horizontal = p.x >= x && p.x <= x + width; 
		boolean vertical = p.y >= y && p.y <= y + height;
		return horizontal && vertical;
	}
	
	@Override
	public boolean mousePressed(Point p) {
		if (contains(p)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean mouseReleased(Point p) {
		if (contains(p)) {
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "[Button: x = " + x + ", y =" + y + ", w = " + width + ", h = " + height + "]";
	}
}