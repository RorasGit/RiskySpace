package riskyspace.logic.data;

import riskyspace.model.Player;
import riskyspace.model.Position;
import riskyspace.model.Resource;

public class PlanetData extends SpriteData {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3901890636821860492L;
	private Resource res;
	
	public PlanetData(Position pos, Player player, Resource res) {
		super(pos, player);
		this.res = res;
	}
	public int getIndex(){
		return (int)(Math.random()* ((res == Resource.GAS) ? 3 : 4)); 
	}
	public Resource getResource(){
		return res;
	}
}