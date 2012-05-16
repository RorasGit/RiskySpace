package riskyspace.logic.data;

import riskyspace.model.Player;
import riskyspace.model.Position;
import riskyspace.model.ShipType;

public class AnimationData extends FleetData {

	/**
	 * Serializeable interface serial
	 */
	private static final long serialVersionUID = 6046373189838191418L;
	
	/**
	 * How long time in millisecons the animation should be
	 */
	private int time;
	
	public AnimationData(Position pos, Player player, ShipType flagship, int time, Position[] steps) {
		super(pos, player, flagship, steps);
		this.time = time;
	}
	
	public int getTime() {
		return time;
	}
}