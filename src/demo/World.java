package demo;

import java.util.HashMap;
import java.util.Map;

public class World {
	private int rows = 0;
	private int cols = 0;
	private Map<Position, Territory> territories = null;
	private Map<Player, PlayerStats> playerstats = null;
	
	public World(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		initTerritories();
		initPlayers();
		
		/*
		 * Set Planets TODO: Roras
		 * Set Starting Fleets
		 */
	}
	
	private void initTerritories() {
		territories = new HashMap<Position, Territory>();
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				territories.put(new Position(row, col), new Territory());
			}
		}
	}
	
	private void initPlayers() {
		playerstats = new HashMap<Player, PlayerStats>();
		playerstats.put(Player.BLUE, new PlayerStats());
		playerstats.put(Player.RED, new PlayerStats());
	}
	
	public World() {
		this(20,20);
	}
}