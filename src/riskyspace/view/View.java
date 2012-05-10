package riskyspace.view;

import riskyspace.logic.SpriteMapData;
import riskyspace.model.Colony;
import riskyspace.model.Fleet;
import riskyspace.model.Planet;
import riskyspace.model.Player;
import riskyspace.model.PlayerStats;

public interface View {
	public static final String res = ".png"; // _lowres.jpg
	
	public void draw();
	public void setViewer(Player player);
	public void setActivePlayer(Player player);
	public void setVisible(boolean visible);
	public boolean isVisible();
	
	public void updateData(SpriteMapData data);
	public void setPlayerStats(PlayerStats stats);
	
	public void showPlanet(Planet selection);
	public void showColony(Colony selection);
	public void showFleet(Fleet selection);
	public void hideMenus();
}