package network;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class GameClient {
	public static void main(String[] args) throws IOException{
		Socket cs = null;
		String ip = "localhost";
		try{
			cs = new Socket(ip, 6013);
		}catch (UnknownHostException e) {
			System.err.println("Dont know about host: localhost");
			System.exit(1);
		}catch (IOException e) {
			System.err.println("Couldnt get I/O: localhost");
			System.exit(1);
		}
		cs.close();
	}
}
