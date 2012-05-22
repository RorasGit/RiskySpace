package riskyspace.network;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
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
	
	private ServerSocket ss = null;
	private List<ConnectionHandler> connections = new ArrayList<ConnectionHandler>();
	private String ip;
	private int port = 6013;

	public GameServer(int numberOfPlayers, String[] ips) {
		this.numberOfPlayers = numberOfPlayers;
		World world = new World(20, 20, numberOfPlayers);
		SpriteMapData.init(world);
		GameManager.INSTANCE.init(world);
		try {
			ss = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		new AcceptThread(ips);
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
			System.out.println("Server started with IP: " + ip + ":" + port);
		} catch (UnknownHostException e) {
			e.printStackTrace();	
		}
		EventBus.SERVER.addHandler(this);
	}
	
	public void sendObject(Object o) throws IOException{
		for (ConnectionHandler ch : connections) {
			ch.output.writeObject(o);
			ch.output.reset();
		}
	}
	
	public void sendObject(Object o, Player player) throws IOException{
		for (ConnectionHandler ch : connections) {
			if (GameManager.INSTANCE.getInfo(player).getIP().equals(ch.socket.getInetAddress())){
				ch.output.writeObject(o);
				ch.output.reset();
				break;
			}
		}
	}

	@Override
	public void performEvent(Event evt) {
		try {
			if (evt.getTag() == Event.EventTag.STATS_CHANGED) {
				sendObject(evt, evt.getPlayer());
			} else if (evt.getTag() == Event.EventTag.UPDATE_SPRITEDATA) {
				if (evt.getPlayer() != null){
					Event event = new Event(Event.EventTag.UPDATE_SPRITEDATA, SpriteMapData.getData(evt.getPlayer()));
					sendObject(event, evt.getPlayer());
				} else {
					for (Player player : GameManager.INSTANCE.getActivePlayers()) {
						Event event = new Event(Event.EventTag.UPDATE_SPRITEDATA, SpriteMapData.getData(player));
						sendObject(event, player);
					}
				}
			} else if (evt.getTag() == Event.EventTag.ACTIVE_PLAYER_CHANGED) {
				sendObject(evt);
			} else if (evt.getTag() == Event.EventTag.SELECTION) {
				sendObject(evt, evt.getPlayer());
			} else if(evt.getTag() == Event.EventTag.BUILDQUEUE_CHANGED){
				sendObject(evt, evt.getPlayer());
			} else if (evt.getTag() == Event.EventTag.HOME_LOST) {
				sendObject(evt, evt.getPlayer());
			} else if (evt.getTag() == Event.EventTag.GAME_OVER) {
				sendObject(evt, evt.getPlayer());
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class ConnectionHandler implements Runnable {
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
			PlayerStats stats = GameManager.INSTANCE.getStats(player);
			Integer rows = GameManager.INSTANCE.getWorldRows();
			Integer cols = GameManager.INSTANCE.getWorldCols();
			
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
					if(!socket.isConnected()){
						disconnect();
						break;
					}

					Object o = input.readObject();
					if (o != null && o instanceof Event) {
						Event evt = (Event) o;
						Player p = null;
						for (Player player : GameManager.INSTANCE.getActivePlayers()) {
							if (GameManager.INSTANCE.getInfo(player).getIP().equals(socket.getInetAddress())) {
								p = player;
							}
						}
						if (p != null) {
							GameManager.INSTANCE.handleEvent(evt, p);
						}
					}
				} catch(SocketException e){
					disconnect();
					break;
				} catch (EOFException e) {
					disconnect();
					break;
				} catch (IOException e) {
					e.printStackTrace();
					try {
						Thread.sleep(1999);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				} catch (ClassNotFoundException e) {
					/*
					 * Server got an nonserializable object, nothing to do here.
					 */
				} 
			}
		}

		private void disconnect() {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			connections.remove(this);
			System.out.println("Connection to :" + socket.getInetAddress() + " closed.");
		}
	}

	private class AcceptThread implements Runnable {
		Thread t = null;
		String[] addresses;
		
		public AcceptThread(String[] addresses) {
			this.addresses = addresses;
			for (int i = 0; i < addresses.length; i++) {
				System.out.println("adr: " + addresses[i]);
			}
			t = new Thread(this);
			t.start();
		}

		@Override
		public void run() {
			Socket cs = null;
			while (connections.size() < numberOfPlayers) {
				try {
					System.out.println("try!");
					cs = ss.accept();
					for (int i = 0; i < addresses.length; i++) {
						if (cs.getInetAddress().getHostAddress().equals(addresses[i])) {
							connections.add(new ConnectionHandler(cs));		
							break;
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
			GameManager.INSTANCE.start();
		}
	}
	public String getIP(){
		return ip;
	}
	public int getPort(){
		return port;
	}
}
