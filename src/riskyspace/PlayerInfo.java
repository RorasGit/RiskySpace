package riskyspace;

import java.awt.Color;
import java.net.InetAddress;

import riskyspace.model.Player;

public class PlayerInfo {
	
	private InetAddress ip = null;
	private Color playerColor;
	
	public PlayerInfo(Player player) {
		if (player == Player.BLUE) {
			playerColor = new Color(0x31579d);
		} else if (player == Player.RED) {
			playerColor = Color.RED;
		} else {
			playerColor = Color.GRAY;
		}
	}
	
	public Color getColor() {
		return playerColor;
	}

	public void setIP(InetAddress ip) {
		this.ip = ip;
	}
	
	public InetAddress getIP() {
		return ip;
	}
}