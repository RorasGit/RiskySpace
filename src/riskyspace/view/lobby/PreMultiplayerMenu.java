package riskyspace.view.lobby;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyListener;

import riskyspace.view.swing.SwingRenderAble;
import riskyspace.view.swing.impl.SwingButton;

public class PreMultiplayerMenu extends AbstractPreGameMenu implements SwingRenderAble {
	
	private int margin = 30;
	
	private Image background = null;
	
	private TextBox textbox;
	
	private SwingButton joinGame;
	private SwingButton hostGame;

	public PreMultiplayerMenu(int x, int y, int menuWidth, int menuHeight) {
		super(x, y, menuWidth, menuHeight);
		background = Toolkit.getDefaultToolkit().getImage("res/menu/lobby/widerMenubackground.png").
				getScaledInstance(menuWidth, menuHeight, Image.SCALE_DEFAULT);	
		textbox = new TextBox(x + margin, y + margin, menuWidth - 2*margin, 30);
		
		joinGame = new SwingButton(x + menuWidth/2 - 90, y + margin + menuHeight/10, 180, 50);
		joinGame.setImage("res/menu/lobby/joingame.png");
		
		hostGame = new SwingButton(x + menuWidth/2 - 90, y + margin + 3*menuHeight/10, 180, 50);
		hostGame.setImage("res/menu/lobby/hostgame.png");
	}
	
	public KeyListener getKeyListener() {
		return textbox.getKeyListener();
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
		if (isVisible()) {
			g.drawImage(background, getX(), getY(), null);
			textbox.draw(g);
			joinGame.draw(g);
			hostGame.draw(g);
		}
	}
}
