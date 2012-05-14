package riskyspace.view;

import riskyspace.view.swing.impl.SwingView;

public class ViewFactory {
	
	public static String SWING_IMPL = "swing";
	
	public static View getView(String type, int rows, int cols) {
		if (SWING_IMPL.equals(type)) {
			return new SwingView(rows, cols);
		} else {
			throw new IllegalArgumentException("Type does not exist");
		}
	}
}
