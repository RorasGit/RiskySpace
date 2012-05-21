package riskyspace.network;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import riskyspace.services.Event;
import riskyspace.services.EventBus;
import riskyspace.services.EventHandler;
import riskyspace.view.swingImpl.LobbyView;

/**
 * 
 * @author Daniel Augurell
 *
 */
public class LobbyClient {

	private ObjectInputStream input = null;
	private ObjectOutputStream output = null;
	private Socket socket = null;
	
	public static final String IS_HOST = "is_host=";
	public static final String MAX_PLAYERS = "max_players=";
	public static final String CURRENT_PLAYER = "players=";
	public static final String GAME_MODE = "game_mode=";
	public static final String CONNECT_TO_GAME = "connect_to_game";
	
	private boolean host = false;
	private int maxPlayers;
	private int players;
	private String gameMode = "";
	
	public LobbyClient() {
		new ServerListener();		
	}

	public boolean connectToLobby(String hostIP) {
		int tries = 0;
		while (socket == null) {
			if (tries == 5) {
				System.err.println("Couldn't Connect");
				return false;
			}
			System.out.println("Connecting. Test #" + (tries+1));
			/*
			 * Loop until Connected
			 */
			connectToHost(hostIP, 6012);
			tries++;
		}
		System.out.println("Connected");
		return true;
		
	}

	public void startGame() {
		try {
			output.writeObject("start_game");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void connectToHost(String hostIP, int hostPort) {
		try {
			socket = new Socket(hostIP, hostPort);
			input = new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());
		} catch (UnknownHostException e) {
			System.err.println("Dont know about host: " + hostIP);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @author Alexander Hederstaf
	 *
	 * Listens to events from the server and updates the 
	 * View accordingly.
	 */
	private class ServerListener implements Runnable {
		
		public ServerListener() {
			Thread t = new Thread(this);
			t.start();
		}
		
		@Override
		public void run() {
			boolean started = false;
			while (!started) {
				try {
					Object o = input.readObject();
					String input = null;
					if (o instanceof String) {
						input = (String) o;
					}
					if (input != null) {
						System.out.println(input);
						if (input.contains(CONNECT_TO_GAME)) {
							// Create GameClient
							String ip = socket.getInetAddress().getHostAddress();
							int port = socket.getPort();
							new GameClient(ip, port);
							// Dispose window
							
							// Set boolean to cancel this thread
							started = true;
						} else {
							String value = input.split("=")[1];
							if (input.contains(IS_HOST)){
								host = Boolean.parseBoolean(value);
							} else if (input.contains(MAX_PLAYERS)){
								maxPlayers = Integer.parseInt(value);
							} else if (input.contains(CURRENT_PLAYER)){
								players = Integer.parseInt(value);
							} else if (input.contains(GAME_MODE)){
								gameMode = value;
							}	
						}
					}
				} catch (EOFException e){
					System.out.println("Server shutdown!");
					System.exit(0);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}
}