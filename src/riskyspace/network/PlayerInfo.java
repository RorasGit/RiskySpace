package riskyspace.network;

import java.net.InetAddress;

public class PlayerInfo {
	
	private InetAddress ip = null;
	
	public PlayerInfo() {}
	
	public void setIP(InetAddress ip) {
		this.ip = ip;
	}
	
	public InetAddress getIP() {
		return ip;
	}
}