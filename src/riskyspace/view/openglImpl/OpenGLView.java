package riskyspace.view.openglImpl;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Map;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;

import riskyspace.logic.SpriteMapData;
import riskyspace.model.BuildAble;
import riskyspace.model.Colony;
import riskyspace.model.Fleet;
import riskyspace.model.Player;
import riskyspace.model.PlayerStats;
import riskyspace.model.Territory;
import riskyspace.services.Event;
import riskyspace.services.EventBus;
import riskyspace.view.View;

public class OpenGLView implements View, GLEventListener {

	public static void main(String[] args) {
		View v = new OpenGLView(20, 20);
	}
	
	private GLRenderArea renderArea = null;
	
	public OpenGLView (int rows, int cols) {
		GLProfile glProfile = GLProfile.getDefault();
		GLCapabilities glCapabilities = new GLCapabilities(glProfile);
		GLCanvas canvas = new GLCanvas(glCapabilities);
		
		int width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;
		
		renderArea = new GLRenderArea(width, height, rows, cols);
		
		JFrame frame = new JFrame("RiskySpace");
		canvas.setFocusable(true);
		canvas.requestFocusInWindow();
		canvas.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
					System.exit(0);
				} else if (event.getKeyCode() == KeyEvent.VK_SPACE) {
					Event evt = new Event(Event.EventTag.MOVE, null);
					EventBus.CLIENT.publish(evt);
				} else if (event.getKeyCode() == KeyEvent.VK_ENTER) {
					Event evt = new Event(Event.EventTag.NEXT_TURN, null);
					EventBus.CLIENT.publish(evt);
				}
			}
			@Override public void keyReleased(KeyEvent arg0) {}
			@Override public void keyTyped(KeyEvent arg0) {}
		});
		canvas.addGLEventListener(this);
		
		frame.add(canvas);
		frame.setPreferredSize(new Dimension(400, 400));
		frame.setUndecorated(true);
		if (GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().isFullScreenSupported()) {
			GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(frame);
		} else {
			System.err.println("Fullscreen not supported");
		}
		
		FPSAnimator anim = new FPSAnimator(canvas, 100);
		anim.start();
	}
	
	//*****GLEventListener Methods*****
	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		drawable.getGL().glClearColor(0, 0, 0, 1f);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
		gl.glClear(GL2.GL_ALPHA_BITS);
		
		renderArea.draw(drawable, renderArea.getBounds(), renderArea.getBounds(), 0);
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthFunc(GL.GL_LESS);
		gl.glEnable(GL2.GL_ALPHA_TEST);
		
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		renderArea.updateSize(width, height);
	}
	
	//*****View Methods*****
	@Override
	public void draw() {
		
	}

	@Override
	public void setViewer(Player player) {
		
	}

	@Override
	public void setActivePlayer(Player player) {
		
	}

	@Override
	public void setVisible(boolean visible) {
		
	}

	@Override
	public boolean isVisible() {
		return false;
	}

	@Override
	public void updateData(SpriteMapData data) {
		
	}

	@Override
	public void setPlayerStats(PlayerStats stats) {
		
	}

	@Override
	public void setQueue(Map<Colony, List<BuildAble>> colonyQueues) {
		
	}

	@Override
	public void showPlanet(Territory selection) {
		
	}

	@Override
	public void showColony(Colony selection) {
		
	}

	@Override
	public void showFleet(Fleet selection) {
		
	}

	@Override
	public void hideMenus() {
		
	}
}