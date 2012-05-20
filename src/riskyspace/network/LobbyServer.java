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


public class LobbyServer {
	
	private int maxNumberOfPlayers;
	private ServerSocket ss = null;

	private List<ConnectionHandler> connections = new ArrayList<ConnectionHandler>();
	
	private int port;
	private String ip;

	private AcceptThread at;
	private boolean started = false;

	public LobbyServer() {
		this.maxNumberOfPlayers = 2;
		this.port = 6013;
		try {
			ss = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		at = new AcceptThread();
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

	public void setMaxNumberOfPlayers(int maxNumberOfPlayers) {
		this.maxNumberOfPlayers = maxNumberOfPlayers;
	}

	public String getIP() {
		return ip;
	}

	public int getPort() {
		return port;
	}
	public boolean start(){
		if(connections.size() == maxNumberOfPlayers){
			started = true;
		}
		return started;
	}
	public void forceStart(){
		started = true;
	}
	public boolean isStarted(){
		return started;
	}

	private class AcceptThread implements Runnable {
		Thread t = null;

		public AcceptThread() {
			t = new Thread(this);
			t.start();
		}

		private Thread getThread() {
			return t;
		}

		@Override
		public void run() {
			Socket cs = null;
			while (!started) {
				if (connections.size() <= maxNumberOfPlayers) {
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
	private class ConnectionHandler implements Runnable {
		private Socket socket;
		private ObjectInputStream input = null;
		private ObjectOutputStream output = null;
		
		private boolean host = false;
		
		public ConnectionHandler(Socket socket) throws IOException {
			this.socket = socket;
			this.output = new ObjectOutputStream(socket.getOutputStream());
			this.input = new ObjectInputStream(socket.getInputStream());
			
			if(ss.getInetAddress().equals(socket.getInetAddress())){
				host = true;
			}
			
			/*
			 * Start Thread
			 */
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
					
				}
			
			}


		private void disconnect() {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			connections.remove(this);
			System.out.println("Connection to :"+socket.getInetAddress()+" closed.");
		
		}
	}
	
}
