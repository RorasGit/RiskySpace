package riskyspace.view.menu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import riskyspace.services.Event;
import riskyspace.services.EventHandler;
import riskyspace.view.Button;
import riskyspace.view.Clickable;

public class RecruitMenu implements IMenu, Clickable, EventHandler {
	
private Color ownerColor = null;
	
	private boolean enabled;
	
	private int x, y;
	private int menuHeight = 0;
	private int menuWidth = 0;
	private int margin = 8;
	
	private Image background = null;
	private Button buildScoutButton = null;
	private Button buildSHunterButton = null;
	private Button buildDestroyerButton = null;
	
	public RecruitMenu(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		menuHeight = height;
		menuWidth = width;
		
	}

	@Override
	public void performEvent(Event evt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean contains(Point p) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mousePressed(Point p) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseReleased(Point p) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setVisible(boolean set) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isVisible() {
		// TODO Auto-generated method stub
		return false;
	}

}
