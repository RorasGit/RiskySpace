package riskyspace.view;

import java.awt.event.KeyListener;

import riskyspace.model.World;
import riskyspace.view.swingImpl.SwingView;

public class ViewFactory {
	
	public static String SWING_IMPL = "swing";
	
	public static View getView(String type, World world, KeyListener keyListener) {
		if (SWING_IMPL.equals(type)) {
			return new SwingView(world, keyListener);
		} else {
			return null;
		}
	}
}
