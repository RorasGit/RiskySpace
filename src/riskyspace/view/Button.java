package riskyspace.view;

import java.awt.Color;
import java.awt.Point;

/**
 * Abstract class for a Button that uses an Action when clicked
 * @author Daniel Augurell, Alexander Hederstaf
 *
 */
public abstract class Button implements Clickable {

	private int x,y;
	private int width, height;
	
	private Action action = null;
	private String text = null;
	private boolean enabled = true;
	private Color textColor = null;
	
	public Button(int x, int y, int width, int height) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void setAction(Action action) {
		this.action = action;
	}
	
	public Action getAction(){
		return action;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setTextColor(Color color) {
		this.textColor = color;
	}
	
	public void setTextColor(int r, int g, int b) {
		this.textColor = new Color(r, g, b);
	}
	
	public Color getTextColor() {
		return textColor;
	}
	
	public void setEnabled(Boolean b) {
		enabled = b;
	}
	
	public boolean isEnabled() {
		return enabled;
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
	public boolean contains(Point p) {
		boolean horizontal = p.x >= x && p.x <= x + width; 
		boolean vertical = p.y >= y && p.y <= y + height;
		return horizontal && vertical;
	}
}