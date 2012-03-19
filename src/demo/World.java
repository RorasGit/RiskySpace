package demo;

import java.util.Map;

public class World {
	private int rows = 0;
	private int cols = 0;
	private Map<Position, Territory> territories = null;
	private Map<Player, PlayerStats> playerstats = null;
}