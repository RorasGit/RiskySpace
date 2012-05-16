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
	 * Current, next and the pos after that for rotation data.
	 */
	private Position[] steps;
	
	/**
	 * How long time in millisecons the animation should be
	 */
	private int time;
	
	public AnimationData(Position pos, Player player, ShipType flagship, int time, Position[] steps) {
		super(pos, player, flagship);
		if (steps.length < 3) {
			throw new IllegalArgumentException("There must be at least 3 Positions including current");
		}
		this.time = time;
		this.steps = new Position[]{steps[0], steps[1], steps[2]};
	}
	
	public Position[] getSteps() {
		return steps;
	}
	
	public int getTime() {
		return time;
	}
}