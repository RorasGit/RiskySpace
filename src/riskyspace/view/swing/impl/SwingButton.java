package riskyspace.view.swing.impl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import riskyspace.view.Action;
import riskyspace.view.Button;
import riskyspace.view.Clickable;
import riskyspace.view.View;
import riskyspace.view.ViewResources;
import riskyspace.view.swing.SwingRenderAble;

public class SwingButton extends Button implements SwingRenderAble{

	private Image image = null;
	private Image scaledImage = null;
	private Image disabledImage = null;
	private Image scaledDisabledImage = null;
	
	public SwingButton(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	public void setImage(String location) {
		image = Toolkit.getDefaultToolkit().getImage(location);
		if (location.endsWith(View.res)) {
			location = location.substring(0, location.indexOf(View.res));
		}
		disabledImage = Toolkit.getDefaultToolkit().getImage(location + "_disabled" + View.res);
		scaledDisabledImage = disabledImage.getScaledInstance(getWidth(), getHeight(), Image.SCALE_DEFAULT);
		scaledImage = image.getScaledInstance(getWidth(), getHeight(), Image.SCALE_DEFAULT);
	}
	
	public void draw(Graphics g) {
		if (isEnabled()) {	
			g.drawImage(scaledImage, getX(), getY(), null);
			/*
			 * Draw centered text
			 */
			if (getText() != null) {
				drawButtonText(g);
			}
		} else {
			g.drawImage(scaledDisabledImage, getX(), getY(), null);
		}
	}
	
	private void drawButtonText(Graphics g) {
		g.setFont(ViewResources.getFont().deriveFont(18f));
		g.setColor(getTextColor() != null ? getTextColor() : ViewResources.WHITE);
		int textX = getX() - (g.getFontMetrics().stringWidth(getText()) / 2) + (getWidth() / 2);
		int textY = getY() + (g.getFontMetrics().getHeight() / 3) + (getHeight() / 2);
		g.drawString(getText(), textX, textY);
	}
	
	@Override
	public String toString() {
		return "[Button: x = " + getX() + ", y =" + getY() + ", w = " + getWidth() + ", h = " + getHeight() + "]";
	}
}