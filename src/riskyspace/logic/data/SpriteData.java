package riskyspace.logic.data;

import riskyspace.model.Player;
import riskyspace.model.Position;

public abstract class SpriteData {
	
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
