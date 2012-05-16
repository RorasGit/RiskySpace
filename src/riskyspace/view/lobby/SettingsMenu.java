package riskyspace.view.lobby;

import java.awt.Graphics;
import java.awt.Point;

import riskyspace.view.Slider;

public class SettingsMenu extends AbstractPreGameMenu {
	
	private Slider volume = null;

	public SettingsMenu(int x, int y, int menuWidth, int menuHeight) {
		super(x, y, menuWidth, menuHeight);
		volume = new Slider(x,y + menuHeight/2, menuWidth, menuHeight/6);
	}
	
	public void draw(Graphics g) {
		volume.draw(g);
	}

	@Override
	public boolean mousePressed(Point p) {
		if (volume.mousePressed(p)) {return true;}
		return false;
	}

	@Override
	public boolean mouseReleased(Point p) {
		if (volume.mouseReleased(p)) {return true;}
			return false;
	}

}

