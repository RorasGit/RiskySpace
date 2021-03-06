package riskyspace.view.lobby;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import riskyspace.LocalGame;
import riskyspace.data.GameDataHandler;
import riskyspace.data.SavedGame;
import riskyspace.network.LobbyClient;
import riskyspace.network.LobbyServer;
import riskyspace.view.Action;
import riskyspace.view.swing.SwingRenderAble;
import riskyspace.view.swing.impl.DropdownButton;
import riskyspace.view.swing.impl.SwingButton;


public class Lobby extends AbstractPreGameMenu implements SwingRenderAble, Observer {

	private SwingButton playerOne = null;
	private SwingButton playerTwo = null;
	private SwingButton playerThree = null;
	private SwingButton playerFour = null;
	
	private SwingButton startGame = null;
	private SwingButton createServer = null;
	
	private DropdownButton<String> numberOfPlayersButton = null;
	private DropdownButton<String> gameModesButton = null;
	
	private int margin = 10;
	
	private Image rightsideMenu = null;
	private Image gameModeImage = null;

	private boolean host = false;
	private int players;
	
	private LobbyClient client;
	private LobbyServer ls;
	
	private String game;
	
	private String ipString = "";
	
	public Lobby(int x, int y, int menuWidth, int menuHeight) {
		super(x, y, menuWidth, menuHeight);
		createButtons();
		createBackground();
	}

	private LocalGameClient localClient;
	
	private void createButtons() {
		playerOne = new SwingButton(getX() + margin, getY() + margin, 240, 50);
		playerOne.setImage("res/menu/lobby/wide_button.png");
		playerOne.setText("BLUE PLAYER");
		playerTwo = new SwingButton(getX() + margin, getY() + 2*margin + 50, 240, 50);
		playerTwo.setImage("res/menu/lobby/wide_button.png");
		playerTwo.setText("RED PLAYER");
		playerThree = new SwingButton(getX() + margin, getY() + 3*margin + 100, 240, 50);
		playerThree.setImage("res/menu/lobby/wide_button.png");
		playerThree.setText("GREEN PLAYER");
		playerThree.setEnabled(false);
		playerFour = new SwingButton(getX() + margin, getY() + 4*margin + 150, 240, 50);
		playerFour.setImage("res/menu/lobby/wide_button.png");
		playerFour.setText("YELLOW PLAYER");
		playerFour.setEnabled(false);
		
		startGame = new SwingButton(getX() + getMenuWidth() - getMenuWidth()/7 - 90, getY() + getMenuHeight() - 3*margin - 50, 180, 50);
		startGame.setImage("res/menu/lobby/start_game_button.png");
		startGame.setAction(new Action() {
			@Override
			public void performAction() {
				if (client != null) {
					client.startGame();
				} else {
					localClient.disposeLobby();
					if (game != null) {
						try {
							new LocalGame(GameDataHandler.loadGame(game));
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						new LocalGame(Integer.parseInt(numberOfPlayersButton.getSelectedValue().split(" ")[0]));
					}
				}
			}
		});
		
		createServer = new SwingButton(getX() + getMenuWidth() - getMenuWidth()/7 - 90, getY() + getMenuHeight() - 3*margin - 50, 180, 50);
		createServer.setImage("res/menu/lobby/create_server_button.png");
		createServer.setAction(new Action() {
			@Override
			public void performAction() {
				ls = new LobbyServer(Integer.parseInt(numberOfPlayersButton.getSelectedValue().split(" ")[0]));
				ipString = ls.getIP();
				setClient(client);
				client.connectToLobby(ls.getIP());
				createServer.setEnabled(false);
			}
		});
		
		ArrayList<String> list = new ArrayList<String>();
		list.add("2 players");
		list.add("3 players");
		list.add("4 players");
		
		numberOfPlayersButton = new DropdownButton<String>(getX() + getMenuWidth() - getMenuWidth()/7 - 80, getY() + 3*getMenuHeight()/6, 160, 30, list);
		
		list.clear();
		list.add("Annihilation");
		
		gameModesButton = new DropdownButton<String>(getX() + getMenuWidth() - getMenuWidth()/7 - 80, getY() + 2*getMenuHeight()/6, 160, 30, list);
	}
	
	/**
	 * Client substitute used for Local Game to close the Lobby 
	 */
	class LocalGameClient extends Observable {
		public void disposeLobby() {
			setChanged();
			notifyObservers("dispose");
		}
	}

	/**
	 * Set Observer to observe a localClient and listen for event to close the Lobby
	 */
	public void setObserver(final Observer o) {
		// Observer used in local game to dispose window
		localClient = new LocalGameClient();
		localClient.addObserver(o);
	}
	
	public void close() {
		if (ls != null) {
			ls.close();
			ls = null;
			ipString = "";
			playerOne.setText("BLUE PLAYER");
			playerTwo.setText("RED PLAYER");
			playerThree.setText("GREEN PLAYER");
			playerFour.setText("YELLOW PLAYER");
		}
		if(client != null){
			client.close();
			client = null;
			ipString = "";
			playerOne.setText("BLUE PLAYER");
			playerTwo.setText("RED PLAYER");
			playerThree.setText("GREEN PLAYER");
			playerFour.setText("YELLOW PLAYER");
		}
	}
	
	public void setNumberOfPlayers(int nbrOfPlayers) {
		numberOfPlayersButton.setSelectedValue("" +nbrOfPlayers);
	}
	
	private void createBackground() {
		rightsideMenu = Toolkit.getDefaultToolkit().getImage("res/menu/lobby/wider_menu_background.png").
				getScaledInstance(2*getMenuWidth()/7, getMenuHeight()-2*margin, Image.SCALE_DEFAULT);
		gameModeImage = Toolkit.getDefaultToolkit().getImage("res/menu/lobby/annihilation.png").
				getScaledInstance(2*getMenuWidth()/7 - 6*margin, (2*getMenuWidth()/7 - 3*margin)/2, Image.SCALE_DEFAULT);
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(rightsideMenu, getX() + getMenuWidth() - 2*getMenuWidth()/7, getY() + margin, null);
		g.drawImage(gameModeImage, getX() + getMenuWidth() - 2*getMenuWidth()/7 + 3*margin, getY() + 4*margin, null);
		numberOfPlayersButton.draw(g);
		gameModesButton.draw(g);
		
		startGame.draw(g);
		createServer.draw(g);

		g.drawString(ipString, 20, 20);
		
		/*
		 * Only draw if visible
		 */
		if (isVisible()) {
			playerOne.draw(g);
			playerTwo.draw(g);
			playerThree.draw(g);
			playerFour.draw(g);
		}
	}
	
	private void setPlayerButtons() {
		if (Integer.parseInt(numberOfPlayersButton.getSelectedValue().split(" ")[0]) == 3) {
			playerThree.setEnabled(true);
			playerFour.setEnabled(false);
		}
		else if (Integer.parseInt(numberOfPlayersButton.getSelectedValue().split(" ")[0]) == 4) {
			playerThree.setEnabled(true);
			playerFour.setEnabled(true);
		} else {
			playerThree.setEnabled(false);
			playerFour.setEnabled(false);
		}
	}
	
	@Override
	public boolean mousePressed(Point p) {
		if (isVisible()) {
			if (startGame.mousePressed(p)) {
				return true;
			}
			if (createServer.mousePressed(p)) {
				return true;
			}
			if (numberOfPlayersButton.mousePressed(p)) {
				gameModesButton.setOpen(false);
				setPlayerButtons();
				return true;
			}
			if (gameModesButton.mousePressed(p)) {
				numberOfPlayersButton.setOpen(false);
				return true;
			}
			numberOfPlayersButton.setOpen(false);
			gameModesButton.setOpen(false);
		}
		return false;
	}

	@Override
	public boolean mouseReleased(Point p) {
		if (numberOfPlayersButton.mouseReleased(p)) {return true;}
		if (createServer.mouseReleased(p)) {return true;}
		if (gameModesButton.mouseReleased(p)) {return true;}
			return false;
	}

	public void setLoadGame(LobbyClient client, String saveName) {
		if (client == null) {
			game = saveName;
			gameModesButton.setEnabled(false);
			numberOfPlayersButton.setEnabled(false);
			createServer.setEnabled(false);
			startGame.setEnabled(true);
			setVisible(true);
		} else {
			this.client = client;
			game = saveName;
			client.addObserver(this);
			gameModesButton.setEnabled(false);
			numberOfPlayersButton.setEnabled(false);
			createServer.setEnabled(false);
			startGame.setEnabled(false);
			try {
				ls = new LobbyServer(GameDataHandler.loadGame(saveName));
			} catch (IOException e) {
				e.printStackTrace();
			}
			ipString = ls.getIP();
			setClient(client);
			client.connectToLobby(ls.getIP());
			setVisible(true);
		}
	}
	
	public void setGameCreate(LobbyClient client) {
		if (client == null) {
			// Local
			gameModesButton.setEnabled(true);
			numberOfPlayersButton.setEnabled(true);
			createServer.setEnabled(false);
			startGame.setEnabled(true);
			setVisible(true);
		} else {
			this.client = client;
			client.addObserver(this);
			gameModesButton.setEnabled(true);
			numberOfPlayersButton.setEnabled(true);
			createServer.setEnabled(true);
			startGame.setEnabled(false);
			setVisible(true);
		}
	}

	public void setClient(LobbyClient client) {
		this.client = client;
		client.addObserver(this);
		gameModesButton.setEnabled(false);
		numberOfPlayersButton.setEnabled(false);
		createServer.setEnabled(false);
		startGame.setEnabled(false);
		setVisible(true);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		String input = (String) arg;
		if (input.contains("=")) {
			String value = input.split("=")[1];
			if (input.contains(LobbyClient.IS_HOST)){
				host = Boolean.parseBoolean(value);
				startGame.setEnabled(host && players == Integer.parseInt(numberOfPlayersButton.getSelectedValue()));
			} else if (input.contains(LobbyClient.MAX_PLAYERS)){
				numberOfPlayersButton.setSelectedValue(value);
				setPlayerButtons();
			} else if (input.contains(LobbyClient.CURRENT_PLAYER)){
				players = Integer.parseInt(value);
				playerOne.setText(	players >= 1 ? "Connected" : "Empty");
				playerTwo.setText(	players >= 2 ? "Connected" : "Empty");
				playerThree.setText(players >= 3 ? "Connected" : "Empty");
				playerFour.setText(	players >= 4 ? "Connected" : "Empty");
				startGame.setEnabled(host && players == Integer.parseInt(numberOfPlayersButton.getSelectedValue()));
			} else if (input.contains(LobbyClient.GAME_MODE)){
				gameModesButton.setSelectedValue(value);
			}	
		}
	}
}
