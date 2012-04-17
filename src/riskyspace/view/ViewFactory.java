package riskyspace.view;

import java.awt.event.KeyListener;

import riskyspace.model.World;
import riskyspace.view.swingImpl.SwingView;

public class ViewFactory {
	
	public static String SWING_IMPL = "swing";
	
	public static View getView(String type, int rows, int cols, KeyListener keyListener) {
		if (SWING_IMPL.equals(type)) {
			return new SwingView(rows, cols, keyListener);
		} else {
			return null;
		}
	}
}
