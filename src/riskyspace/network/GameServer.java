package riskyspace.network;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import riskyspace.GameManager;
import riskyspace.logic.SpriteMapData;
import riskyspace.model.Player;
import riskyspace.model.PlayerStats;
import riskyspace.model.World;
import riskyspace.services.Event;
import riskyspace.services.EventBus;
import riskyspace.services.EventHandler;

public class GameServer implements EventHandler {

	private final int numberOfPlayers;
	private final World world;
	
	private ServerSocket ss = null;
	private List<ConnectionHandler> connections = new ArrayList<ConnectionHandler>();

	/**
	 * MAIN METHOD
	 */
	public static void main(String[] args) throws IOException {
		new GameServer(2);
	}
	
	public GameServer(int numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
		this.world = new World();
		SpriteMapData.init(world);
		GameManager.INSTANCE.init(world);
		try {
			ss = new ServerSocket(6013);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		new AcceptThread();
		try {
			String ip = InetAddress.getLocalHost().getHostAddress();
			String port = "" + ss.getLocalPort();
			System.out.println("Server started with IP: " + ip + ":" + port);
		} catch (UnknownHostException e) {
			e.printStackTrace();	
		}
		EventBus.SERVER.addHandler(this);
	}
	
	public void sendObject(Object o) throws IOException{
		for (ConnectionHandler ch : connections) {
			ch.output.writeObject(o);
		}
	}
	
	public void sendObject(Object o, Player player) throws IOException{
		for (ConnectionHandler ch : connections) {
			System.out.println(GameManager.INSTANCE.getInfo(player));
			System.out.println(GameManager.INSTANCE.getInfo(player).getIP());
			System.out.println(ch.socket);
			System.out.println(ch.socket.getInetAddress());
			
			if (GameManager.INSTANCE.getInfo(player).getIP().equals(ch.socket.getInetAddress())){
				ch.output.writeObject(o);
			}
		}
	}

	@Override
	public void performEvent(Event evt) {
		try {
			if (evt.getTag() == Event.EventTag.STATS_CHANGED) {
				/*
				 * Assume only the current player's stats can be changed
				 * otherwise update all...
				 */
				sendObject(evt, GameManager.INSTANCE.getCurrentPlayer());
			} else if (evt.getTag() == Event.EventTag.UPDATE_SPRITEDATA) {
				for (Player player : GameManager.INSTANCE.getActivePlayers()) {
					try {
						Event event = new Event(Event.EventTag.UPDATE_SPRITEDATA, SpriteMapData.getData(player));
						sendObject(event, player);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} else if (evt.getTag() == Event.EventTag.ACTIVE_PLAYER_CHANGED) {
				sendObject(evt);
			} else if (evt.getTag() == Event.EventTag.SELECTION) {
				sendObject(evt, GameManager.INSTANCE.getCurrentPlayer());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	class ConnectionHandler implements Runnable {
		private Socket socket;
		private ObjectInputStream input = null;
		private ObjectOutputStream output = null;

		public ConnectionHandler(Socket socket) throws IOException {
			this.socket = socket;
			this.output = new ObjectOutputStream(socket.getOutputStream());
			this.input = new ObjectInputStream(socket.getInputStream());
			
			/*
			 * Set up Game Data
			 */
			Player player = GameManager.INSTANCE.addPlayer(socket.getInetAddress());
			SpriteMapData data = SpriteMapData.getData(player);
			PlayerStats stats = world.getStats(player);
			Integer rows = world.getRows();
			Integer cols = world.getCols();
			
			output.writeObject(new Event(Event.EventTag.INIT_COLS, cols));
			output.writeObject(new Event(Event.EventTag.INIT_ROWS, rows));
			output.writeObject(new Event(Event.EventTag.INIT_PLAYER, player));
			output.writeObject(new Event(Event.EventTag.STATS_CHANGED, stats));
			output.writeObject(new Event(Event.EventTag.UPDATE_SPRITEDATA, data));
			/*
			 * Start Thread
			 */
			Thread t = new Thread(this);
			t.start();
		}
		

		@Override
		public void run() {
			while (true) {
				try {
					Object o = input.readObject();
					if (o != null && o instanceof Event) {
						Event evt = (Event) o;
						Player p = null;
						for (Player player : GameManager.INSTANCE.getActivePlayers()) {
							if (GameManager.INSTANCE.getInfo(player).getIP().equals(socket.getInetAddress())) {
								p = player;
							}
						}
						GameManager.INSTANCE.handleEvent(evt, p);
					}
				} catch (EOFException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}

	}

	private class AcceptThread implements Runnable {

		public AcceptThread() {
			Thread t = new Thread(this);
			t.start();
		}

		@Override
		public void run() {
			Socket cs = null;
			while (connections.size() < numberOfPlayers) {
				try {
					cs = ss.accept();
					connections.add(new ConnectionHandler(cs));
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(1);
				}
				System.out.println("IP Connected: " + cs.getInetAddress());
			}
			GameManager.INSTANCE.start();
		}
	}
}