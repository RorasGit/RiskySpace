package riskyspace;

import java.awt.Color;

public class PlayerInfo {
	
	private String IP;
	private Color playerColor;
	
	/*
	 * TODO: add IP to constructor
	 */
	public PlayerInfo(Color pColor) {
		playerColor = pColor;
	}
	
	public Color getColor() {
		return playerColor;
	}
	
	public String getIP() {
		return IP;
	}

}
