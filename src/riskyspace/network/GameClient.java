package riskyspace.network;

import java.awt.HeadlessException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import riskyspace.model.Player;
import riskyspace.model.Resource;
import riskyspace.model.Territory;
import riskyspace.view.View;

public class GameClient {

	private View mainView = null;
	private ObjectInputStream input = null;
	private ObjectOutputStream output = null;
	private Socket socket = null;

	public GameClient(String hostIP, int hostPort) throws IOException {
		connectToHost(hostIP, hostPort);
		while (true) {
			Territory t = new Territory();
			t.setPlanet(Resource.METAL);
			t.getPlanet().buildColony(Player.WORLD);
			t.getColony().getMine().upgrade();
			
			output.writeObject(t);
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void connectToHost(String hostIP, int hostPort) throws IOException {

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

	public static void main(String[] args) throws HeadlessException,
			IOException {

		new GameClient("localhost", 6013);
	}

}
