package demo;

public class Demo {
	
	private Player[] players = {Player.BLUE, Player.RED};
	private Player currentPlayer = null;
	private World world = null;
	
	public Demo () {
		world = new World();
	}
}
