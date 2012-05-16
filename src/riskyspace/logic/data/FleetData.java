package riskyspace.logic.data;

import riskyspace.model.Player;
import riskyspace.model.Position;
import riskyspace.model.ShipType;

public class FleetData extends SpriteData {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7281617499767850994L;
	private ShipType flagship;
	
	public FleetData(Position pos, Player player, ShipType flagship) {
		super(pos, player);
		this.flagship = flagship;
	}
	public ShipType getFlagships(){
		return flagship;
	}
	public void setFlagShip(ShipType flagship) {
		this.flagship = flagship;
	}
}