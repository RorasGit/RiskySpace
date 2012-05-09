package network;
import java.io.IOException;
import java.net.*;

public class GameServer {
	public static void main(String[] args) throws IOException {
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(6013);
		} catch (IOException e) {
			System.err.println("TROLL");
			System.exit(123);
		}
		Socket cs = null;
		
		try {
			cs = ss.accept();
		} catch (IOException e) {
			System.err.println("TROLL2");
			System.exit(123);
		}
		System.out.println("ip="+cs.getInetAddress());
		cs.close();
		ss.close();
		
	}
}
