package riskyspace.view;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import riskyspace.model.Player;

public class PlayerColors {

	private static Map<Player, Color> playerColors = new HashMap<Player, Color>();
	
	public static Color getColor(Player player) {
		if (playerColors.isEmpty()) {
			playerColors.put(Player.BLUE, new Color(0x31579d));
			playerColors.put(Player.RED, Color.RED);
			playerColors.put(Player.GREEN, Color.GREEN);
			playerColors.put(Player.YELLOW, new Color(0xFF, 0xD7, 0x00));
			playerColors.put(Player.WORLD, new Color(0x7d7d7d));
		}
		return playerColors.get(player);
	}
}
