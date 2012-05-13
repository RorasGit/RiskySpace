package riskyspace.view.openglImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.media.opengl.GLAutoDrawable;

import riskyspace.model.Player;
import riskyspace.view.camera.Camera;
import riskyspace.view.camera.CameraController;

public class GLRenderArea implements GLRenderAble {
	
	private Rectangle screenArea = null;
	private int squareSize;
	
	/**
	 * Game size
	 */
	private int rows, cols;
	
	/*
	 * Cameras
	 */
	private Camera currentCamera = null;
	private Map<Player, Camera> cameras = null;
	private CameraController cc = null;
	
	/**
	 * The player viewing this renderArea
	 */
	private Player viewer = null;
	private Sprite[] sprites = new Sprite[100000];
	
	public GLRenderArea(int width, int height, int rows, int cols) {
		screenArea = new Rectangle(0, 0, width, height);
		squareSize = Math.min(width/6,height/6);
		this.rows = rows;
		this.cols = cols;
		
		for (int i = 0; i < sprites.length; i++) {
			int x = Math.abs(r.nextInt() % (400-squareSize/20));
			int y = Math.abs(r.nextInt() % (400-squareSize/20));
			sprites[i] = new Sprite("res/icons/cloud", x, y, squareSize/10, squareSize/10);
		}
		initCameras();
	}
	
	Random r = new Random();
	
	private void initCameras() {
		cameras = new HashMap<Player, Camera>();
		cameras.put(Player.BLUE, new Camera(0.93f,0.92f));
		cameras.put(Player.RED, new Camera(0.07f,0.08f));
		cameras.put(Player.GREEN, new Camera(0.07f,0.92f));
		cameras.put(Player.PINK, new Camera(0.93f,0.08f));
		cc = new CameraController();
	}
	
	public void setViewer(Player player) {
		this.viewer = player;
		currentCamera = cameras.get(player);
		cc.setCamera(currentCamera);
		if (!cc.isAlive()) {
			cc.start();
		}
	}
	
	@Override
	public Rectangle getBounds() {
		return screenArea;
	}

	int times = 0;
	long sec;
	
	@Override
	public void draw(GLAutoDrawable drawable, Rectangle objectRect,
			Rectangle targetArea, int zIndex) {
		
		long b = System.currentTimeMillis();
		if (sec == 0 || b - sec > 1000) {
			System.out.println("FPS: " + times);
			sec = b;
			times = 0;
		}
		times++;
		for (int i = 0; i < sprites.length; i++) {
			int x = Math.abs(r.nextInt() % (targetArea.getWidth()-squareSize/20));
			int y = Math.abs(r.nextInt() % (targetArea.getHeight()-squareSize/20));
			sprites[i].getBounds().setX(x);
			sprites[i].getBounds().setY(y);
			sprites[i].draw(drawable, sprites[i].getBounds(), targetArea, zIndex);
		}
	}

	public void updateSize(int squareSize) {
		this.squareSize = squareSize;
		for (int i = 0; i < sprites.length; i++) {
			sprites[i].getBounds().setWidth(squareSize/10);
			sprites[i].getBounds().setHeight(squareSize/10);
		}
	}
}