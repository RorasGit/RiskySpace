package riskyspace.logic.data;

import riskyspace.model.Player;
import riskyspace.model.Position;
import riskyspace.model.ShipType;

public class FleetData extends SpriteData {
	
	private ShipType flagships;
	public FleetData(Position pos, Player player, ShipType flagships) {
		super(pos, player);
		this.flagships = flagships;
	}
	public ShipType getFlagships(){
		return flagships;
	}

}
