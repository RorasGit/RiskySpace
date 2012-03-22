package riskyspace;

import riskyspace.model.Player;
import riskyspace.model.World;

public class Demo {
	
	/*
	 * Players
	 */
	private Player[] players = {Player.BLUE, Player.RED};
	private Player currentPlayer = null;
//	private Map<Player, PlayerInfo> = null;
	
	/*
	 * Models
	 */
	private World world = null;
//	private Camera[] cameras = null;
	
	/*
	 * Controllers
	 */
//	private GameThread gameThread = null;
//	private CameraUpdater = null;
//	TODO: Separated View and Model threads? 
//	no threads and listeners instead somewhere?
	
	/*
	 * Views
	 */
//	private View mainView = null;
	
	public Demo () {
		world = new World();
	}
}
