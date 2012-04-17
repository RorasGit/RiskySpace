package riskyspace.view.swingImpl;

import java.awt.GraphicsEnvironment;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import riskyspace.model.Player;
import riskyspace.model.World;
import riskyspace.view.View;

public class SwingView implements View {

	private JFrame frame = null;
	private RenderArea renderArea = null;
	
	public SwingView(int rows, int cols, KeyListener keyListener) {
		setFrame();
		renderArea = new RenderArea(rows, cols);
		frame.add(renderArea);
		frame.setVisible(true);
		frame.addKeyListener(keyListener);
	}
	
	private void setFrame() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setIgnoreRepaint(true);
		frame.setUndecorated(true);
		System.out.println("Mem: " + GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getAvailableAcceleratedMemory());
		/*
		 * If Mem is small set non transparent textures!
		 */
		if (GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().isFullScreenSupported()) {
			GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(frame);
		} else {
			System.err.println("Fullscreen not supported");
		}
	}

	
	@Override
	public void draw() {
		renderArea.repaint();
	}

	@Override
	public void setViewer(Player player) {
		renderArea.setPlayer(player);
	}
}
