package riskyspace.logic.data;

import java.io.Serializable;
import java.util.List;

import riskyspace.model.Fleet;
import riskyspace.model.Player;

/**
 * A class for reporting the results of a battle - the model is not altered.
 * 
 */
public class BattleStats implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5632954553067736337L;
	
	private Player[] participants = new Player[2];
	private Player winner = null;
	private boolean colonyDestroyed = false;
	private List<Fleet> destroyedFleets = null;
	
	public void setDestroyedFleets(List<Fleet> destroyedFleets) {
		this.destroyedFleets = destroyedFleets;
	}

	public List<Fleet> getDestroyedFleets() {
		return destroyedFleets;
	}

	public Player getWinner() {
		return winner;
	}
	
	public Player getLoser() {
		return winner == participants[0] ? participants[1] : participants[0];
	}
	
	public void setWinner(Player winner) {
		this.winner = winner;
	}

	public boolean isColonyDestroyed() {
		return colonyDestroyed;
	}

	public void setColonyDestroyed(boolean colonyDestroyed) {
		this.colonyDestroyed = colonyDestroyed;
	}

	public String getWinnerString() {
		return getWinner().toString().substring(0, 1) + getWinner().toString().substring(1).toLowerCase() + " has won the battle!";
	}

	public void setParticipants(Player p1, Player p2) {
		participants[0] = p1;
		participants[1] = p2;
	}

	public Player[] getParticipants() {
		return participants;
	}
}