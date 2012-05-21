package riskyspace.view.lobby;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import riskyspace.view.Action;
import riskyspace.view.Button;
import riskyspace.view.GlowableGraphic;
import riskyspace.view.ViewResources;
import riskyspace.view.swing.SwingRenderAble;

public class TextBox extends Button implements SwingRenderAble {
	
	private Image background;
	private Image hilightedBackground;
	private Image displayedImage;
	
	private boolean canGlow;
	
	public TextBox(int x, int y, int width, int height) {
		super(x, y, width, height);
		setText("");
		setTextColor(240, 240, 240);
		background = Toolkit.getDefaultToolkit().getImage("res/menu/lobby/textItem.png").
				getScaledInstance(width, height, Image.SCALE_DEFAULT);
		hilightedBackground = Toolkit.getDefaultToolkit().getImage("res/menu/lobby/textItem_glow.png").
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
