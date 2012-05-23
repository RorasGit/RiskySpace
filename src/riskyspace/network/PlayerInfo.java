package riskyspace.network;

import java.net.InetAddress;

public class PlayerInfo {
	
	private String ip = null;
	
	public PlayerInfo() {}
	
	public void setIP(InetAddress ip) {
		this.ip = ip.getHostAddress();
	}
	
	public void setIP(String ip) {
		this.ip = ip;
	}
	
	public String getIP() {
		return ip;
	}
}