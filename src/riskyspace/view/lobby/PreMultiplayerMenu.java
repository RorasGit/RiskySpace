package riskyspace.view.lobby;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import riskyspace.network.LobbyClient;
import riskyspace.view.Action;
import riskyspace.view.Clickable;
import riskyspace.view.IMenu;
import riskyspace.view.ViewResources;
import riskyspace.view.swing.SwingRenderAble;
import riskyspace.view.swing.impl.SwingButton;

public class PreMultiplayerMenu extends AbstractPreGameMenu implements SwingRenderAble {
	
	private int margin = 30;

	private IMenu multiplayerLobby = null;
	
	private Image background = null;
	
	private TextBox textbox;
	
	private SwingButton joinGame;
	private SwingButton hostGame;
	
	private TextBoxListener textListener;
	
	private LobbyClient client;
	
	

	public PreMultiplayerMenu(int x, int y, int menuWidth, int menuHeight) {
		super(x, y, menuWidth, menuHeight);
		background = Toolkit.getDefaultToolkit().getImage("res/menu/lobby/widerMenubackground.png").
				getScaledInstance(menuWidth, menuHeight, Image.SCALE_DEFAULT);	
		textbox = new TextBox(x + margin, y + margin + menuHeight/10, menuWidth - 2*margin, 30);
		textbox.setEnabled(false);
		
		joinGame = new SwingButton(x + menuWidth/2 - 90, y + margin + 2*menuHeight/10, 180, 50);
		joinGame.setImage("res/menu/lobby/joingame.png");
		joinGame.setAction(new Action() {
			@Override
			public void performAction() {
				client = new LobbyClient();
				if(client.connectToLobby(textbox.getText())){
					//TODO: SET MULTIPLAYERLOBBY
					// multiplayerLobby.setNumberOfPlayers(client.getNbrOfPlayers());
					setVisible(false);
					multiplayerLobby.setVisible(true);
				}
			}
		});
		
		hostGame = new SwingButton(x + menuWidth/2 - 90, y + margin + 5*menuHeight/10, 180, 50);
		hostGame.setImage("res/menu/lobby/hostgame.png");
		hostGame.setAction(new Action() {
			
			@Override
			public void performAction() {
				setVisible(false);
				multiplayerLobby.setVisible(true);
			}
		});

		multiplayerLobby = new Lobby(5*x/12, y/2, 10*menuWidth/3, 2*menuHeight);
		
		textListener = new TextBoxListener();
	}
	
	public KeyListener getKeyListener() {
		return textListener;
	}
	@Override
	public void setVisible(boolean enabled) {
		super.setVisible(enabled);
		multiplayerLobby.setVisible(false);
	}
	@Override
	public boolean mousePressed(Point p) {
		if(this.isVisible()) {
			if(this.contains(p)) {
				if(joinGame.mousePressed(p)) {
					return true;
				}
				if(textbox.mousePressed(p)) {
					return true;
				}
				textbox.setEnabled(false);
				if(hostGame.mousePressed(p)) {
					return true;
				}
				return true;
			}
		}
		if (multiplayerLobby instanceof Clickable) {
			if (((Clickable) multiplayerLobby).mousePressed(p)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean mouseReleased(Point p) {
		return mousePressed(p);
	}

	@Override
	public void draw(Graphics g) {
		if (multiplayerLobby.isVisible()) {
			((SwingRenderAble) multiplayerLobby).draw(g);
		}
		if (isVisible()) {
			g.drawImage(background, getX(), getY(), null);
			g.setFont(ViewResources.getFont().deriveFont(g.getClipBounds().height/40.0f));
			g.setColor(textbox.getTextColor());
			g.drawString("IP:", getX() + getMenuWidth()/2 - g.getFontMetrics().stringWidth("IP:")/2, getY() + 2*margin/3 + getMenuHeight()/10);
			textbox.draw(g);
			joinGame.draw(g);
			hostGame.draw(g);
		}
	}
	private class TextBoxListener extends KeyAdapter {
		String allowedChars = "1234567890.";
		
		public void keyPressed(KeyEvent e) {
			if(textbox.isEnabled()){
				if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
					if(textbox.getText().length() > 0){
						textbox.setText(textbox.getText().substring(0, textbox.getText().length()-1));
					}
				}else if(allowedChars.contains(""+e.getKeyChar()) && textbox.getText().length() < 15){
					textbox.setText(textbox.getText() + e.getKeyChar());
				}
			}
		}
	}
}
