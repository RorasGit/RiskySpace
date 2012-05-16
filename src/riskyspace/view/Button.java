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
	private Image disabledImage = null;
	private Image scaledDisabledImage = null;
	private Action action = null;
	private String text = null;
	private Color textColor = null;
	private Boolean enabled = true;
	
	public Button(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void setAction(Action action) {
		this.action = action;
	}
	
	public void setImage(String location) {
		image = Toolkit.getDefaultToolkit().getImage(location);
		if (location.endsWith(View.res)) {
			location = location.substring(0, location.indexOf(View.res));
		}
		disabledImage = Toolkit.getDefaultToolkit().getImage(location + "_disabled" + View.res);
		scaledDisabledImage = disabledImage.getScaledInstance(width, height, Image.SCALE_DEFAULT);
		scaledImage = image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setTextColor(Color color) {
		this.textColor = color;
	}
	
	public String getText() {
		return text;
	}
	
	public void draw(Graphics g) {
		if (isEnabled()) {	
			g.drawImage(scaledImage, x, y, null);
			/*
			 * Draw centered text
			 */
			if (text != null) {
				drawButtonText(g);
			}
		} else {
			g.drawImage(scaledDisabledImage, x, y, null);
		}
	}
	
	private void drawButtonText(Graphics g) {
		g.setFont(ViewResources.getFont().deriveFont(18f));
		g.setColor(textColor != null ? textColor : ViewResources.WHITE);
		int textX = x - (g.getFontMetrics().stringWidth(text) / 2) + (width / 2);
		int textY = y + (g.getFontMetrics().getHeight() / 3) + (height / 2);
		g.drawString(text, textX, textY);
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
	
	public void setEnabled(Boolean b) {
		enabled = b;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public boolean contains(Point p) {
		boolean horizontal = p.x >= x && p.x <= x + width; 
		boolean vertical = p.y >= y && p.y <= y + height;
		return horizontal && vertical;
	}
	
	@Override
	public boolean mousePressed(Point p) {
		if (isEnabled()) {
			if (contains(p)) {
				if (action != null) {
					action.performAction();
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean mouseReleased(Point p) {
		if (isEnabled()) {
			if (contains(p)) {
				if (action != null) {
					action.performAction();
				}
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "[Button: x = " + x + ", y =" + y + ", w = " + width + ", h = " + height + "]";
	}
}
