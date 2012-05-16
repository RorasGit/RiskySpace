package riskyspace.view;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;

public class Slider implements Clickable {
	
	private Image slider0 = null;
	private Image slider25 = null;
	private Image slider50 = null;
	private Image slider75 = null;
	private Image slider100 = null;
	
	private Map<Integer, Image> sliderValues = new HashMap<Integer, Image>();
	
	private int selectedValue;
	
	private int x,y;
	private int width, height;
	
	public Slider(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
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
		// TODO Auto-generated method stub
		return false;
	}

}
