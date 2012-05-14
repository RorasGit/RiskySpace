package riskyspace.view;

import java.awt.Color;
import java.awt.Point;

/**
 * Abstract class for a Button that uses an Action when clicked
 * @author Daniel Augurell, Alexander Hederstaf
 *
 */
public abstract class Button implements Clickable {

	private Action action = null;
	private String text = null;
	private boolean enabled = true;
	
	public void setAction(Action action) {
		this.action = action;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}

	public abstract void setTextColor(Color color);
	public abstract void setTextColor(int r, int g, int b);
	
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
				return true;
			}
		}
		return false;
	}
}