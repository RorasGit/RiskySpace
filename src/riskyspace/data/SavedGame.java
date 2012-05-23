package riskyspace.data;

import java.io.Serializable;
import java.util.List;

import riskyspace.model.Player;
import riskyspace.model.World;

public class SavedGame implements Serializable{
	/**
	 * Serializable ID
	 */
	private static final long serialVersionUID = -1714063672495857194L;
	private World world;
	private List<Player> players;
	private Player currentPlayer;
	private int turn;
	private String gameMode;
	
	public SavedGame(World world, List<Player> players, Player currentPlayer, int turn, String gameMode, String gameName) {
		this.world = world;
		this.players = players;
		this.currentPlayer = currentPlayer;
		this.turn = turn;
		this.gameMode = gameMode;
	}
	
	public World getWorld() {
		return world;
	}
	
	public List<Player> getPlayers() {
		return players;
	}
	
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	public int getTurn() {
		return turn;
	}
	
	public String getGameMode() {
		return gameMode;
	}
}