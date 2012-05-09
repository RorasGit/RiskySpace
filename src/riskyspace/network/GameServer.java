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

import riskyspace.model.Territory;
import riskyspace.services.Event;

public class GameServer {

	private ServerSocket ss = null;
	private List<ConnectionHandler> connections = new ArrayList<ConnectionHandler>();

	public GameServer() {
		try {
			ss = new ServerSocket(6013);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		new AcceptThread();
		try {
			System.out.println("Server started with IP: "
					+ InetAddress.getLocalHost().getHostAddress() + ":"
					+ ss.getLocalPort());
		} catch (UnknownHostException e) {
		}

	}

	public static void main(String[] args) throws IOException {
		new GameServer();

	}

	class ConnectionHandler implements Runnable {
		private Socket socket;
		private ObjectInputStream input = null;
		private ObjectOutputStream output = null;

		public ConnectionHandler(Socket socket) throws IOException {
			this.socket = socket;
			this.output = new ObjectOutputStream(socket.getOutputStream());
			this.input = new ObjectInputStream(socket.getInputStream());
			Thread t = new Thread(this);
			t.start();
		}

		@Override
		public void run() {
			while (true) {
				try {
					Object o = input.readObject();
					if (o != null) {
						Territory e = (Territory) o;
						System.out.println(e.getColony().getMine().getRank());
					}
				} catch (EOFException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();

				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
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
			while (connections.size() < 2) {
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