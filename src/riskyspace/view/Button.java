package riskyspace.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;

public class Button implements Clickable, GlowableGraphic {

	private int x, y, width, height;
	private Image image = null;
	private Image scaledImage = null;
	private Image disabledImage = null;
	private Image scaledDisabledImage = null;
	private Image glowImage = null;
	private Image scaledGlowImage = null;
	private Action action = null;
	private String text = null;
	private String imageLocation = null;
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
			imageLocation = location.substring(0, location.indexOf(View.res));
		}
		disabledImage = Toolkit.getDefaultToolkit().getImage(imageLocation + "_disabled" + View.res);
		scaledDisabledImage = disabledImage.getScaledInstance(width, height, Image.SCALE_DEFAULT);
		scaledImage = image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
		glowImage = Toolkit.getDefaultToolkit().getImage(imageLocation + "_glow" + View.res);
		scaledGlowImage = glowImage.getScaledInstance(width, height, Image.SCALE_DEFAULT);
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
			if (cursorOver()) {
				g.drawImage(scaledGlowImage, x, y, null);
			} else {
				g.drawImage(scaledImage, x, y, null);
			}
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
	public boolean cursorOver() {
		if(this.contains(MouseInfo.getPointerInfo().getLocation())) {
			if (hasGlowImage()) {
			return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean hasGlowImage() {
		File file = new File(imageLocation + "_glow" + View.res);
		if (file.exists()) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return "[Button: x = " + x + ", y =" + y + ", w = " + width + ", h = " + height + "]";
	}
}
