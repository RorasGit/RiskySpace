package riskyspace.view.lobby;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import riskyspace.view.Action;
import riskyspace.view.Button;
import riskyspace.view.swing.SwingRenderAble;

public class TextBox extends Button implements SwingRenderAble, KeyListener{
	
	private Image background;
	private Image hilightedBackground;
	
	public TextBox(int x, int y, int width, int height) {
		super(x, y, width, height);
		setAction(new Action() {
			
			@Override
			public void performAction() {
				if(isEnabled()){
					setHilight();
				}
			}
		});
	}

	private void setHilight() {
		
	}
	public KeyListener getKeyListener(){
		return this;
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(background, getX(), getY(), null);
		g.drawString(getText(), getX(), getY());
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
			setText(getText().substring(0, getText().length()-1));
		}else{
			setText(getText() + e.getKeyChar());
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}
	
	
}
