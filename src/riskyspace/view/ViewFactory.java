package riskyspace.view;

import riskyspace.view.opengl.impl.OpenGLView;
import riskyspace.view.swing.impl.SwingView;

public class ViewFactory {
	
	public static String SWING_IMPL = "swing";
	public static String OPEN_GL_IMPL = "gl";
	
	public static View getView(String type, int rows, int cols) {
		if (SWING_IMPL.equals(type)) {
			return new SwingView(rows, cols);
		} else if (OPEN_GL_IMPL.equals(type)) {
			return new OpenGLView(rows, cols);
		} else {
			throw new IllegalArgumentException("Type does not exist");
		}
	}
}
