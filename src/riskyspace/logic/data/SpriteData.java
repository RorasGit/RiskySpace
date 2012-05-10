package riskyspace.logic.data;

import java.io.Serializable;

import riskyspace.model.Player;
import riskyspace.model.Position;

public abstract class SpriteData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1233990190804340445L;
	private Position pos;
	private Player player;
	
	public SpriteData(Position pos, Player player){
		this.pos = pos;
		this.player = player;
	}
	public Position getPosition(){
		return pos;
	}
	public Player getPlayer(){
		return player;
	}
}
