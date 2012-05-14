package riskyspace.view.lobby;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import riskyspace.view.View;
import riskyspace.view.VolumeSlider;
import riskyspace.view.swing.SwingDrawable;

public class SettingsMenu extends AbstractPreGameMenu implements SwingDrawable {
	
	private VolumeSlider musicVolume = null;
	private VolumeSlider effectsVolume = null;

	public SettingsMenu(int x, int y, int menuWidth, int menuHeight) {
		super(x, y, menuWidth, menuHeight);
		musicVolume = new VolumeSlider(x + menuWidth/2 - 135, y + menuHeight/3, 270, 30, "Music volume");
		effectsVolume = new VolumeSlider(x + menuWidth/2 - 135, y + 2*menuHeight/3, 270, 30, "eFFect volume");	
	}
	
	@Override
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

