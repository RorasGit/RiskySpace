package riskyspace.view;

import riskyspace.model.Player;

public interface View {
	public static final String res = "_lowres";
	
	public void draw();
	public void setViewer(Player player);
}