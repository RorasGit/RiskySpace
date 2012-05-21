package riskyspace.view.lobby;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.sun.media.sound.Toolkit;

import riskyspace.view.Action;
import riskyspace.view.Button;
import riskyspace.view.ViewResources;
import riskyspace.view.swing.SwingRenderAble;

public class TextBox extends Button implements SwingRenderAble{
	
	private Image background;
	private Image hilightedBackground;
	private TextBoxListener textListener;
	
	public TextBox(int x, int y, int width, int height) {
		super(x, y, width, height);
		setText("");
		setTextColor(240, 240, 240);
		textListener = new TextBoxListener();
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
		return textListener;
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(background, getX(), getY(), null);
		g.setFont(ViewResources.getFont().deriveFont(g.getClipBounds().height/40.0f));
		g.setColor(getTextColor());
		g.drawString(getText(), getX(), getY());
	}
	
	private class TextBoxListener extends KeyAdapter {
		String allowedChars = "1234567890.";
		
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
				if(getText().length() > 0){
					setText(getText().substring(0, getText().length()-1));
				}
			}else if(allowedChars.contains(""+e.getKeyChar())){
				setText(getText() + e.getKeyChar());
			}
			System.out.println("Text: "+ getText());
		}
	}

}
