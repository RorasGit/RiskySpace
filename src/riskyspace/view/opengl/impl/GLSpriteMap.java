package riskyspace.view.opengl.impl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLProfile;

import riskyspace.PlayerColors;
import riskyspace.logic.SpriteMapData;
import riskyspace.logic.data.ColonyData;
import riskyspace.model.Player;
import riskyspace.model.Position;
import riskyspace.model.Resource;
import riskyspace.view.opengl.GLRenderAble;
import riskyspace.view.opengl.Rectangle;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

/**
 * Drawing all game Sprites in openGL
 * @author Alexander Hederstaf
 *
 */
public class GLSpriteMap implements GLRenderAble {
	private static boolean initiated = false;

	/*
	 * Path Texture names
	 */
	private static final String HEAD = "head";
	private static final String START = "start";
	private static final String STRAIGHT = "straight";
	private static final String TURN = "turn";
	
	/*
	 * Planet Sprites
	 */
	private static Map<Integer, GLSprite> metalPlanets = new HashMap<Integer, GLSprite>();
	private static Map<Integer, GLSprite> gasPlanets = new HashMap<Integer, GLSprite>();

	/*
	 * Ship Sprites
	 */
	private static Map<String, GLSprite> shipSprites = new HashMap<String, GLSprite>();
	
	/*
	 * All planets with randomized textures
	 */
	private static Map<Resource, Map<Position, GLSprite>> allPlanets = new HashMap<Resource, Map<Position, GLSprite>>();

	/*
	 * Colony Textures for each Player
	 */
	private static Map<String, Texture> colonyTextures = new HashMap<String, Texture>();
	
	/*
	 * Fog Sprite
	 */
	private static GLSprite fogSprite = null;
	
	/*
	 * SpriteMapData
	 */
	private static SpriteMapData data = null;
	
	/*
	 * Bounds
	 */
	private Rectangle bounds;
	
	private Map<Player, List<Rectangle>> colonies = new HashMap<Player, List<Rectangle>>();
	/*
	 * Add more data to draw!
	 * TODO:
	 */
	
	/**
	 * Private constructor, create instances
	 * with getSprites()
	 */
	private GLSpriteMap() {};
	
	public static void init() {
		loadSprites();
		setPlanetSprites();
		initiated = true;
	}

	private static void setPlanetSprites() {
		Map<Position, GLSprite> gas = new HashMap<Position, GLSprite>();
		Map<Position, GLSprite> metal = new HashMap<Position, GLSprite>();
		for (Position pos : data.getAllPositions()) {
			gas.put(pos, gasPlanets.get((int) (Math.random()*3)));
			metal.put(pos, metalPlanets.get((int) (Math.random()*4)));
		}
		allPlanets.put(Resource.GAS, gas);
		allPlanets.put(Resource.METAL, metal);
	}

	private static void loadSprites() {
		/*
		 * Ship Textures of all colors are saved in one class to
		 * reduce the amount of calls to Texture.bind() which is 
		 * quite expensive.
		 */
		/* Load RED ships*/
		shipSprites.put("SCOUT_RED", new GLSprite("ships", 0, 0, 64, 64));
		shipSprites.put("HUNTER_RED", new GLSprite("ships", 64, 0, 64, 64));
		shipSprites.put("COLONIZER_RED", new GLSprite("ships", 128, 0, 64, 64));
		shipSprites.put("DESTROYER_RED", new GLSprite("ships", 192, 0, 64, 64));
		/* Load BLUE ships*/
		shipSprites.put("SCOUT_BLUE", new GLSprite("ships", 0, 64, 64, 64));
		shipSprites.put("HUNTER_BLUE", new GLSprite("ships", 64, 64, 64, 64));
		shipSprites.put("COLONIZER_BLUE", new GLSprite("ships", 128, 64, 64, 64));
		shipSprites.put("DESTROYER_BLUE", new GLSprite("ships", 192, 64, 64, 64));
		/* Load PINK ships*/
		shipSprites.put("SCOUT_PINK", new GLSprite("ships", 0, 64, 128, 64));
		shipSprites.put("HUNTER_PINK", new GLSprite("ships", 64, 64, 128, 64));
		shipSprites.put("COLONIZER_PINK", new GLSprite("ships", 128, 128, 64, 64));
		shipSprites.put("DESTROYER_PINK", new GLSprite("ships", 192, 128, 64, 64));
		/* Load GREEN ships*/
		shipSprites.put("SCOUT_GREEN", new GLSprite("ships", 0, 64, 192, 64));
		shipSprites.put("HUNTER_GREEN", new GLSprite("ships", 64, 64, 192, 64));
		shipSprites.put("COLONIZER_GREEN", new GLSprite("ships", 128, 192, 64, 64));
		shipSprites.put("DESTROYER_GREEN", new GLSprite("ships", 192, 192, 64, 64));
		
		metalPlanets.put(0, new GLSprite("planets/metal_planet_0", 64, 64));
		metalPlanets.put(1, new GLSprite("planets/metal_planet_1", 64, 64));
		metalPlanets.put(2, new GLSprite("planets/metal_planet_2", 64, 64));
		metalPlanets.put(3, new GLSprite("planets/metal_planet_3", 64, 64));
		gasPlanets.put(0,  new GLSprite("planets/gas_planet_0", 64, 64));
		gasPlanets.put(1,  new GLSprite("planets/gas_planet_1", 64, 64));
		gasPlanets.put(2,  new GLSprite("planets/gas_planet_2", 64, 64));
		
		colonyTextures.put("RED", createColonySprite(PlayerColors.getColor(Player.RED)));
		colonyTextures.put("BLUE", createColonySprite(PlayerColors.getColor(Player.BLUE)));
		colonyTextures.put("PINK", createColonySprite(PlayerColors.getColor(Player.PINK)));
		colonyTextures.put("GREEN", createColonySprite(PlayerColors.getColor(Player.GREEN)));
		
		fogSprite = new GLSprite("cloud", 256, 256);
	}
	
	/**
	 * Creates a Circle Texture of any Color used as Colony marker
	 * @param color The Color of the Circle
	 */
	private static Texture createColonySprite(Color color){
		BufferedImage img = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.createGraphics();
		g.setColor(color);
		g.fillOval(2, 2, 60, 60);
		return AWTTextureIO.newTexture(GLProfile.getDefault(), img, false);
	}

	public static GLSpriteMap getSprites(SpriteMapData data) {
		GLSpriteMap.data = data;
		if (!initiated) {
			init();
		}
		GLSpriteMap map = new GLSpriteMap();
		
		for (ColonyData colonyData : GLSpriteMap.data.getColonyData()) {
			
		}
		return null;
	}
	
	@Override
	public Rectangle getBounds() {
		return bounds;
	}

	@Override
	public void draw(GLAutoDrawable drawable, Rectangle objectRect,
			Rectangle targetArea, int zIndex) {
		
	}
}