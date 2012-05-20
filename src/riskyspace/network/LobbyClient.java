package riskyspace.network;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
public class LobbyClient implements EventHandler {

	private LobbyView mainView = null;
	private ObjectInputStream input = null;
	private ObjectOutputStream output = null;
	private Socket socket = null;
	
	public static void main(String[] args) {
		new LobbyClient();
	}
	
	public LobbyClient() {
		EventBus.CLIENT.addHandler(this);
		initiateGameView();
		new ServerListener();		
		Thread renderThread = new Thread(new Runnable() {
			@Override public void run() {
				while(true) {
					mainView.draw();
					try {
						Thread.sleep(1000/60);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
		});
		renderThread.start();
	}

	private void connectToLobby(String hostIP, int hostPort) {
		int tries = 0;
		while (socket == null) {
			if (tries == 5) {
				System.err.println("Couldn't Connect");
				System.exit(1);
			}
			System.out.println("Connecting. Test #" + (tries+1));
			/*
			 * Loop until Connected
			 */
			connectToHost(hostIP, hostPort);
			tries++;
		}
		System.out.println("Connected");
		
		
	}

	private void initiateGameView() {
		mainView = new LobbyView();
				
	}

	private void connectToHost(String hostIP, int hostPort) {
		try {
			socket = new Socket(hostIP, hostPort);
			input = new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());
		} catch (UnknownHostException e) {
			System.err.println("Dont know about host: " + hostIP);
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void performEvent(Event evt) {
		try {
			output.writeObject(evt);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
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
			while(true) {
				try {
					Object o = input.readObject();
					Event event = null;
					if (o instanceof Event) {
						event = (Event) o;
					}
					if (event != null) {
						System.out.println(event);
						
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