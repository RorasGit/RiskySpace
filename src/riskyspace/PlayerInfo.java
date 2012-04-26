package riskyspace;

import java.awt.Color;

import riskyspace.model.Player;

public class PlayerInfo {
	
	private String ip = "127.0.0.1";
	private Color playerColor;
	
	public PlayerInfo(Player player) {
		if (player == Player.BLUE) {
			playerColor = Color.BLUE;
		} else if (player == Player.RED) {
			playerColor = Color.RED;
		} else {
			playerColor = Color.GRAY;
		}
	}
	
	public Color getColor() {
		return playerColor;
	}

	public void setIP(String ip) {
		this.ip = ip;
	}
	
	public String getIP() {
		return ip;
	}
}