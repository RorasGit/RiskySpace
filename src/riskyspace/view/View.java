package riskyspace.view;

import riskyspace.model.Player;

public interface View {
	public static final String res = ".png"; // _lowres.jpg
	
	public void draw();
	public void setViewer(Player player);
	public void setVisible(boolean visible);
	public boolean isVisible();
}