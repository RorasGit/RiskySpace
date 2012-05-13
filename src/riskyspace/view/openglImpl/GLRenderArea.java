package riskyspace.view.openglImpl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLProfile;

import riskyspace.model.Player;
import riskyspace.view.camera.Camera;
import riskyspace.view.camera.CameraController;

import com.jogamp.opengl.util.GLBuffers;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

public class GLRenderArea implements GLRenderAble {
	
	/**
	 * Extra space at the Right and Left sides
	 * of the screen.
	 */
	private static final int EXTRA_SPACE_HORIZONTAL = 3;
	
	/**
	 * Extra space at the Top and Bottom sides
	 * of the screen.
	 */
	private static final int EXTRA_SPACE_VERTICAL = 2;
	
	private Rectangle screenArea = null;
	private int squareSize;
	private int totalWidth;
	private int totalHeight;
	private BufferedImage backgroundImage;
	private Texture backgroundTexture;
	
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

	/**
	 * Layers of GLRenderAble objects
	 */
	private GLRenderAble[][] renderAbles;
	
	public GLRenderArea(int width, int height, int rows, int cols) {
		screenArea = new Rectangle(0, 0, width, height);
		squareSize = Math.min(width/6,height/6);
		totalWidth = (cols + 2*EXTRA_SPACE_HORIZONTAL)*squareSize;
		totalHeight = (rows + 2*EXTRA_SPACE_VERTICAL)*squareSize;
		this.rows = rows;
		this.cols = cols;
		initCameras();
		createBackground();
		add(new Sprite("res/icons/cloud", 0, 0, squareSize, squareSize), 2);
		add(new Sprite("res/menu/blue/city", 0, 0, squareSize*5, squareSize*5), 1);
	}

	
	private void createBackground() {
//		WritableRaster raster = 
//				Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
//			                                    totalWidth,
//			                                    totalHeight,
//			                                    4,
//			                                    null);
//		ComponentColorModel colorModel=
//			new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
//		                             new int[] {8,8,8,8},
//		                             true,
//		                             false,
//		                             ComponentColorModel.TRANSLUCENT,
//		                             DataBuffer.TYPE_BYTE);
//		BufferedImage backgroundImage = new BufferedImage(colorModel, raster, false, null);
		backgroundImage = new BufferedImage(totalWidth, totalHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = backgroundImage.createGraphics();
		// Transform image from awt format to openGL format
//		AffineTransform gt = new AffineTransform();
//		gt.translate (0, totalHeight);
//		gt.scale (1, -1d);
//		g.transform (gt);
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, totalWidth, totalHeight);
		
		g.setColor(Color.LIGHT_GRAY);
		/*
		 * Draw Horizontal lines
		 */
		for (int row = 0; row <= rows; row++) {
			int x1 = (EXTRA_SPACE_HORIZONTAL) * squareSize;
			int x2 = (EXTRA_SPACE_HORIZONTAL + cols) * squareSize;
			int y = (EXTRA_SPACE_VERTICAL + row) * squareSize;
			g.drawLine(x1, y, x2, y);
		}
		/*
		 * Draw Vertical lines
		 */
		for (int col = 0; col <= rows; col++) {
			int x = (EXTRA_SPACE_HORIZONTAL + col) * squareSize;
			int y1 = (EXTRA_SPACE_VERTICAL) * squareSize;
			int y2 = (EXTRA_SPACE_VERTICAL + rows) * squareSize;
			g.drawLine(x, y1, x, y2);
		}
		
		/*
		 * Draw stars
		 */
		g.setColor(Color.WHITE);
		for (int i = 0; i < 5000; i++) {
			int x = (int) (Math.random()*(cols+2*EXTRA_SPACE_HORIZONTAL)*squareSize);
			int y = (int) (Math.random()*(rows+2*EXTRA_SPACE_VERTICAL)*squareSize);
			g.fillRect(x, y, 1, 1);
		}
	}
	
	/**
	 * Add GLRenderAble to a zIndex for this RenderArea to draw.
	 * @param renderAble The GLRenderAble
	 * @param zIndex 
	 */
	public void add(GLRenderAble renderAble, int zIndex){
		if (renderAble == null) {
			return;
		}
		
		if (renderAbles == null) {
			renderAbles = new GLRenderAble[1 + zIndex][];
		}
		
		if (zIndex >= renderAbles.length) {
			renderAbles = getIncreasedArray(renderAbles, 1 + zIndex);
		}
		
		if (renderAbles[zIndex] == null) {
			// Index is empty, create new Array with place for 3 object
			renderAbles[zIndex] = new GLRenderAble[3];
		}
		
		if (renderAbles[zIndex][renderAbles[zIndex].length-1] != null){
			// Index is full, add place for another 3 objects
			GLRenderAble[] newArray = new GLRenderAble[3 + renderAbles[zIndex].length];
			for (int i = 0; i < renderAbles[zIndex].length; i++) {
				newArray[i] = renderAbles[zIndex][i];
			}
			renderAbles[zIndex] = newArray;
		}
		
		for (int i = 0; i < renderAbles[zIndex].length; i++) {
			// Find first null and add the GLRenderAble there
			if (renderAbles[zIndex][i] == null) {
				renderAbles[zIndex][i] = renderAble;
				break;
			}
		}
	}
	
	/**
	 * Remove the GLRenderAble from being drawn
	 * @param renderAble The GLRenderAble to remove
	 */
	public void remove(GLRenderAble renderAble) {
		if (renderAble == null || renderAbles == null) {
			return;
		}
		for (int i = 0; i < renderAbles.length; i++) {
			for (int j = 0; j < renderAbles[i].length; j++) {
				if (renderAbles[i][j] == renderAble) {
					renderAbles[i][j] = null;
				}
			}
		}
	}
	
	private GLRenderAble[][] getIncreasedArray(GLRenderAble[][] ra, int newLength) {
		// Create new tmp array
		GLRenderAble[][] tmp = new GLRenderAble[newLength][];
		for (int i = 0; i < ra.length; i++) {
			// Transfer all data from current GLRenderAble array
			tmp[i] = ra[i];
		}
		return tmp;
	}
	
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

	@Override
	public void draw(GLAutoDrawable drawable, Rectangle objectRect, Rectangle targetArea, int zIndex) {
		drawBackground(drawable);
		
		if (renderAbles == null)
			return;
		for (int i = 0; i < renderAbles.length; i++) {
			if (renderAbles[i] == null)
				continue;
			for (GLRenderAble glra : renderAbles[i]) {
				if (glra == null)
					continue;
				glra.draw(drawable, glra.getBounds(), targetArea, i);
			}
		}
	}

	private void drawBackground(GLAutoDrawable drawable) {
		if (backgroundTexture == null) {
			backgroundTexture = AWTTextureIO.newTexture(drawable.getGLProfile(), backgroundImage, false);
		}
		backgroundTexture.bind(drawable.getGL());
		GL2 gl = drawable.getGL().getGL2();
		
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
		gl.glAlphaFunc(GL2.GL_GREATER, 0.10f);
		
		gl.glBegin(GL2.GL_QUADS);
		
		gl.glTexCoord2f(0.5f, 0.5f);
		gl.glVertex3f(-1, -1, 0);

		gl.glTexCoord2f(0.5f, 0.8f);
		gl.glVertex3f(-1, 1, 0);

		gl.glTexCoord2f(0.8f, 0.8f);
		gl.glVertex3f(1, 1, 0);

		gl.glTexCoord2f(0.8f, 0.5f);
		gl.glVertex3f(1, -1, 0);

		gl.glEnd();
	}


	public void updateSize(int width, int height) {
		this.squareSize = Math.min(width/6,height/6);
		screenArea.setHeight(height);
		screenArea.setWidth(width);
	}
}