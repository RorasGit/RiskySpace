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

import riskyspace.services.Event;


public class LobbyServer {
	
	private final String START_GAME = "start_game";
	
	private int maxNumberOfPlayers;
	private ServerSocket ss = null;

	private List<ConnectionHandler> connections = new ArrayList<ConnectionHandler>();
	
	private int port;
	private String ip;

	private boolean started = false;

	public LobbyServer(int maxNumberOfPlayers) {
		this.maxNumberOfPlayers = maxNumberOfPlayers;
		this.port = 6012;
		try {
			ss = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		new AcceptThread();
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
			System.out.println("Server started with IP: " + ip + ":" + port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public int getMaxNumberOfPlayers() {
		return maxNumberOfPlayers;
	}

	public String getIP() {
		return ip;
	}

	public int getPort() {
		return port;
	}
	
	public boolean start() {
		if(connections.size() == maxNumberOfPlayers - 1){
			started = true;
			InetAddress[] addresses = new InetAddress[maxNumberOfPlayers];
			for (int i = 0; i < addresses.length; i++) {
				addresses[i] = connections.get(i).socket.getInetAddress();
			}
			new GameServer(maxNumberOfPlayers, addresses);
			for (ConnectionHandler ch : connections) {
				try {
					ch.output.writeObject("connect_to_game");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return started;
	}
	
	private class AcceptThread implements Runnable {
		Thread t = null;

		public AcceptThread() {
			t = new Thread(this);
			t.start();
		}

		@Override
		public void run() {
			Socket cs = null;
			while (!started) {
				if (connections.size() < maxNumberOfPlayers) {
					try {
						cs = ss.accept();
						connections.add(new ConnectionHandler(cs));
						for (ConnectionHandler ch : connections) {
							ch.output.writeObject("players=" + (connections.size() + 1));
						}
					} catch (IOException e) {
						e.printStackTrace();
						System.exit(1);
					}
					System.out.println("IP Connected: " + cs.getInetAddress());
				}
			}
		}
	}
	
	private class ConnectionHandler implements Runnable {
		private Socket socket;
		private ObjectInputStream input = null;
		private ObjectOutputStream output = null;
		
		private boolean host = false;
		
		public ConnectionHandler(Socket socket) throws IOException {
			this.socket = socket;
			this.output = new ObjectOutputStream(socket.getOutputStream());
			this.input = new ObjectInputStream(socket.getInputStream());
			
			if (ss.getInetAddress().equals(socket.getInetAddress())){
				host = true;
			}
			
			output.writeObject("max_players=" + maxNumberOfPlayers);
			output.writeObject("game_mode=" + "Annihilation");
			output.writeObject("is_host=" + host);
			
			Thread t = new Thread(this);
			t.start();
		}

		@Override
		public void run() {
			while (!started) {
				if(!socket.isConnected()){
					disconnect();
					break;
				}
				try {
					Object o = input.readObject();
					if (host && o instanceof Event.EventTag) {
						if (START_GAME.equals(o) && connections.size() == maxNumberOfPlayers) {
							start();
						}
					}
				} catch (SocketException e){
					disconnect();
					break;
				} catch (EOFException e) {
					disconnect();
					break;
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
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
}