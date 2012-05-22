package riskyspace.view.swing.impl;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Toolkit;
import java.io.File;

import riskyspace.view.Button;
import riskyspace.view.GlowableGraphic;
import riskyspace.view.ViewResources;
import riskyspace.view.swing.SwingRenderAble;

public class SwingButton extends Button implements GlowableGraphic, SwingRenderAble {

	private Image image = null;
	private Image scaledImage = null;
	private Image disabledImage = null;
	private Image scaledDisabledImage = null;
	private Image glowImage = null;
	private Image scaledGlowImage = null;
	private boolean canGlow;
	
	public SwingButton(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	public void setImage(String location) {
		image = Toolkit.getDefaultToolkit().getImage(location);
		scaledImage = image.getScaledInstance(getWidth(), getHeight(), Image.SCALE_DEFAULT);
		if (location.endsWith(".png")) {
			location = location.substring(0, location.indexOf(".png"));
		}
		disabledImage = Toolkit.getDefaultToolkit().getImage(location + "_disabled.png");
		scaledDisabledImage = disabledImage.getScaledInstance(getWidth(), getHeight(), Image.SCALE_DEFAULT);
		if (new File(location + "_glow.png").exists()) {
			glowImage = Toolkit.getDefaultToolkit().getImage(location + "_glow.png");
			scaledGlowImage = glowImage.getScaledInstance(getWidth(), getHeight(), Image.SCALE_DEFAULT);
			canGlow = true;
		} else {
			canGlow = false;
		}
	}

	@Override
	public void draw(Graphics g) {
		if (isEnabled()) {	
			if (cursorOver()) {
				g.drawImage(scaledGlowImage, getX(), getY(), null);
			} else {
				g.drawImage(scaledImage, getX(), getY(), null);
			}
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

	@Override
	public boolean cursorOver() {
		return canGlow() && contains(MouseInfo.getPointerInfo().getLocation());
	}

	@Override
	public boolean canGlow() {
		return canGlow;
	}
}