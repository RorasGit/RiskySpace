package riskyspace.view.lobby;

import java.awt.Graphics;
import java.awt.Point;

import riskyspace.view.Slider;

public class SettingsMenu extends AbstractPreGameMenu {
	
	private Slider musicVolume = null;
	private Slider effectsVolume = null;

	public SettingsMenu(int x, int y, int menuWidth, int menuHeight) {
		super(x, y, menuWidth, menuHeight);
		musicVolume = new Slider(x + menuWidth/2 - 135, y + menuHeight/3, 270, 30, "Music volume");
		effectsVolume = new Slider(x + menuWidth/2 - 135, y + 2*menuHeight/3, 270, 30, "eFFect volume");
	}
	
	public void draw(Graphics g) {
		musicVolume.draw(g);
		effectsVolume.draw(g);
	}

	@Override
	public boolean mousePressed(Point p) {
		if (musicVolume.mousePressed(p)) {return true;}
		if (effectsVolume.mousePressed(p)) {return true;}
		return false;
	}

	@Override
	public boolean mouseReleased(Point p) {
		if (musicVolume.mouseReleased(p)) {return true;}
		if (effectsVolume.mouseReleased(p)) {return true;}
			return false;
	}

}

