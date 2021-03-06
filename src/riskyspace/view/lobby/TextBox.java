package riskyspace.view.lobby;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import riskyspace.view.Button;
import riskyspace.view.swing.SwingRenderAble;

public class TextBox extends Button implements SwingRenderAble {
	
	private Image background;
	private Image hilightedBackground;
	
	public TextBox(int x, int y, int width, int height) {
		super(x, y, width, height);
		setText("");
		setTextColor(240, 240, 240);
		background = Toolkit.getDefaultToolkit().getImage("res/menu/lobby/text_item.png").
				getScaledInstance(width, height, Image.SCALE_DEFAULT);
		hilightedBackground = Toolkit.getDefaultToolkit().getImage("res/menu/lobby/text_item_glow.png").
		getScaledInstance(width, height, Image.SCALE_DEFAULT);
		
		
	}
	@Override
	public boolean mousePressed(Point p) {
		if (contains(p)) {
			setEnabled(true);
			return true;
		}
		return false;
	}
	@Override
	public boolean mouseReleased(Point p) {
		return mousePressed(p);
	}

	@Override
	public void draw(Graphics g) {
		if(isEnabled()){
			g.drawImage(hilightedBackground, getX(), getY(), null);
		}else{
			g.drawImage(background, getX(), getY(), null);
		}
		g.drawString(getText(), getX() + getWidth()/20, getY() + g.getFontMetrics().getHeight() + getHeight()/10);
	}
	
	
}
