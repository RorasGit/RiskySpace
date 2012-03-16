package menudemo;

import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.Timer;

public class ViewDemoMenuTest {
	public static void main(String[] args) {
		new ViewDemoMenuTest();
	}

	JFrame testFrame = null;

	public ViewDemoMenuTest() {
		testFrame = new JFrame();
		TestPanelMenuTest testPanel = new TestPanelMenuTest();
		testFrame.add(testPanel);
		testFrame.setUndecorated(true);
		testFrame.setVisible(true);
		testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		testFrame.setIgnoreRepaint(true);
		
		if (GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().isFullScreenSupported()) {
			GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(testFrame);
		}
		
		Timer timer = new Timer(1000/60, new MoveListener());
		timer.start();
	}

	private class MoveListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			Point loc = MouseInfo.getPointerInfo().getLocation();
			if (loc.x == Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 1) {
				TestPanelMenuTest.camera.x = Math.min(TestPanelMenuTest.squareSize* 26 - (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), TestPanelMenuTest.camera.x + 10);
			} else if (loc.x == 0) {
				TestPanelMenuTest.camera.x = Math.max(0, TestPanelMenuTest.camera.x - 10);
			}
			if (loc.y == Toolkit.getDefaultToolkit().getScreenSize()
					.getHeight() - 1) {
				TestPanelMenuTest.camera.y = Math.min(TestPanelMenuTest.squareSize * 24 - (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight(),
						TestPanelMenuTest.camera.y + 10);
			} else if (loc.y == 0) {
				TestPanelMenuTest.camera.y = Math.max(0, TestPanelMenuTest.camera.y - 10);
			}
			testFrame.repaint();
		}
	}
}