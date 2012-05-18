package riskyspace.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;

public class VolumeSlider implements Clickable {
	
	private Map<Integer, Image> sliderValues = new HashMap<Integer, Image>();
	
	private int selectedValue;
	
	private int x,y;
	private int width, height;
	private Color textColor;
	private String text;
	
	public VolumeSlider(int x, int y, int width, int height, String text) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = text;
		mapImages();
		selectedValue = 50;
	}
	
	public void mapImages() {
		Image slider = Toolkit.getDefaultToolkit().getImage("res/menu/lobby/slider0%" + View.res).
				getScaledInstance(width, height, Image.SCALE_DEFAULT);
		sliderValues.put(0, slider);
		slider = Toolkit.getDefaultToolkit().getImage("res/menu/lobby/slider25%" + View.res).
				getScaledInstance(width, height, Image.SCALE_DEFAULT);
		sliderValues.put(25, slider);
		slider = Toolkit.getDefaultToolkit().getImage("res/menu/lobby/slider50%" + View.res).
				getScaledInstance(width, height, Image.SCALE_DEFAULT);
		sliderValues.put(50, slider);
		slider = Toolkit.getDefaultToolkit().getImage("res/menu/lobby/slider75%" + View.res).
				getScaledInstance(width, height, Image.SCALE_DEFAULT);
		sliderValues.put(75, slider);
		slider = Toolkit.getDefaultToolkit().getImage("res/menu/lobby/slider100%" + View.res).
				getScaledInstance(width, height, Image.SCALE_DEFAULT);
		sliderValues.put(100, slider);
	}
	
	public void draw(Graphics g) {
		g.drawImage(sliderValues.get(selectedValue), x, y, null);
		drawSliderText(g);
	}
	
	private void drawSliderText(Graphics g) {
		g.setFont(ViewResources.getFont().deriveFont(18f));
		g.setColor(textColor != null ? textColor : ViewResources.WHITE);
		if (selectedValue == 0) {
			String offtext = text + " OFF";
			int textX = x - (g.getFontMetrics().stringWidth(offtext) / 2) + (width / 2);
			int textY = y + (g.getFontMetrics().getHeight() / 3) + (height / 2) - height;
			g.drawString(offtext, textX, textY);
		} else {
			int textX = x - (g.getFontMetrics().stringWidth(text) / 2) + (width / 2);
			int textY = y + (g.getFontMetrics().getHeight() / 3) + (height / 2) - height;
			g.drawString(text, textX, textY);
		}
	}
	
	public boolean mouseEntered(Point p) {
		return false;
	}
	
	public int getSelectedValue() {
		return selectedValue;
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
			if (p.getX() < x + 2*width/10) {
				selectedValue = 0;
				return true;
			} else if (p.getX() > x + 2*width/10 && p.getX() < x + 4*width/10) {
				selectedValue = 25;
				return true;
			} else if (p.getX() > x + 4*width/10 && p.getX() < x + 6*width/10) {
				selectedValue = 50;
				return true;
			} else if (p.getX() > x + 6*width/10 && p.getX() < x + 8*width/10) {
				selectedValue = 75;
				return true;
			} else if (p.getX() > x + 8*width/10) {
				selectedValue = 100;
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean mouseReleased(Point p) {
		if (contains(p)) {return true;}
		return false;
	}

}
