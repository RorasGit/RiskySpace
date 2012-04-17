package riskyspace.logic.data;

import riskyspace.model.Player;
import riskyspace.model.Position;
import riskyspace.model.ShipType;

public class FleetData extends SpriteData {
	
	private ShipType flagship;
	
	public FleetData(Position pos, Player player, ShipType flagship) {
		super(pos, player);
		this.flagship = flagship;
	}
	public ShipType getFlagships(){
		return flagship;
	}
}