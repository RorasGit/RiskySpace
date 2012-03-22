package riskyspace.view.swingImpl;

import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;

import riskyspace.model.World;
import riskyspace.view.View;

public class SwingView implements View {

	private JFrame frame = null;
	private RenderArea renderArea = null;
	
	public SwingView(World world) {
		setFrame();
		renderArea = new RenderArea(world);
		frame.add(renderArea);
	}
	
	private void setFrame() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setIgnoreRepaint(true);
		frame.setUndecorated(true);
		if (GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().isFullScreenSupported()) {
			GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(frame);
		}
	}

	
	@Override
	public void draw() {
		renderArea.render();
	}
}
