package riskyspace.network;

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

public class GameServer {

	private final int numberOfPlayers;
	private final World world;
	
	private ServerSocket ss = null;
	private List<ConnectionHandler> connections = new ArrayList<ConnectionHandler>();

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
	}
	
	public void sendObject(Object o) throws IOException{
		for (ConnectionHandler ch : connections) {
			ch.output.writeObject(o);
		}
	}
	
	public void sendObject(Object o, Player p) throws IOException{
		for (ConnectionHandler ch : connections) {
			if(GameManager.INSTANCE.getInfo(p).getIP().equals(ch.socket.getInetAddress())){
				ch.output.writeObject(o);
			}
		}
	}

	public static void main(String[] args) throws IOException {
		new GameServer(2);
	}

	class ConnectionHandler implements Runnable {
		private Socket socket;
		private ObjectInputStream input = null;
		private ObjectOutputStream output = null;

		public ConnectionHandler(Socket socket) throws IOException {
			this.socket = socket;
			this.output = new ObjectOutputStream(socket.getOutputStream());
			this.input = new ObjectInputStream(socket.getInputStream());
			
			System.out.println("start handle");
			
			/*
			 * Set up Game Data
			 */
			Player player = GameManager.INSTANCE.addPlayer(socket.getInetAddress());
			
			/*
			 * TODO:
			 * Send SpriteMapData
			 * Send world size
			 * Send Player
			 * 
			 */
			SpriteMapData data = SpriteMapData.getData(player);
			PlayerStats stats = world.getStats(player);
			Integer rows = 20;
			Integer cols = 20;
			
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
//				try {
					
//					Object o = input.readObject();
//					if (o != null) {
//						//TODO
//					}
//				} catch (EOFException e) {
//					e.printStackTrace();
//				} catch (IOException e) {
//					e.printStackTrace();
//				} catch (ClassNotFoundException e) {
//					e.printStackTrace();
//				}
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
		}
	}
}