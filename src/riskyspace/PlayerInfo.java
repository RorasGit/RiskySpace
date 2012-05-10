package riskyspace;

import java.net.InetAddress;

import riskyspace.model.Player;

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