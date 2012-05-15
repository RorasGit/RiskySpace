package riskyspace.view.opengl.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import riskyspace.logic.Path;
import riskyspace.logic.SpriteMapData;
import riskyspace.logic.data.ColonizerData;
import riskyspace.logic.data.ColonyData;
import riskyspace.logic.data.FleetData;
import riskyspace.logic.data.PlanetData;
import riskyspace.model.Player;
import riskyspace.model.Position;
import riskyspace.model.Resource;
import riskyspace.model.ShipType;
import riskyspace.view.opengl.GLRenderAble;
import riskyspace.view.opengl.Rectangle;

/**
 * Drawing all game Sprites in openGL
 * @author Alexander Hederstaf
 *
 */
/*
 * Saves all data as rectangles mapped to GLSprite keys
 * in such a way that re-binding of textures will be
 * used as little as possible during drawing.
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
	private static Map<Player, GLSprite> colonyMarkerSprites = new HashMap<Player, GLSprite>();
	
	/*
	 * Path Sprites
	 */
	private static Map<String, GLSprite> pathSprites = new HashMap<String, GLSprite>();
	
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
	
	private List<Rectangle> fogOfWar = new ArrayList<Rectangle>();
	private Map<Resource, Map<Position, Rectangle>> planets = new HashMap<Resource, Map<Position, Rectangle>>();
	private Map<Player, List<Rectangle>> colonies = new HashMap<Player, List<Rectangle>>();
	private Map<Player, Map<ShipType , List<Rectangle>>> fleets = new HashMap<Player, Map<ShipType , List<Rectangle>>>();
	private Map<String, Map<Rectangle, Double>> paths = new HashMap<String, Map<Rectangle, Double>>();
	
	/* Integers for display, draw text how? */
//	private Map<Position, Integer> shipCount = new HashMap<Position, Integer>();
//	private Map<Position, Integer> colonizerCount = new HashMap<Position, Integer>();
	
	/**
	 * Private constructor, create instances with getSprites()
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
		shipSprites.put("SCOUT_GREEN", 		new GLSprite("ships",   0,   0, 64, 64));
		shipSprites.put("HUNTER_GREEN", 	new GLSprite("ships",  64,   0, 64, 64));
		shipSprites.put("COLONIZER_GREEN", 	new GLSprite("ships", 128,   0, 64, 64));
		shipSprites.put("DESTROYER_GREEN", 	new GLSprite("ships", 192,   0, 64, 64));
		/* Load BLUE ships*/
		shipSprites.put("SCOUT_PINK", 		new GLSprite("ships",  0,   64, 64, 64));
		shipSprites.put("HUNTER_PINK", 		new GLSprite("ships",  64,  64, 64, 64));
		shipSprites.put("COLONIZER_PINK", 	new GLSprite("ships", 128,  64, 64, 64));
		shipSprites.put("DESTROYER_PINK", 	new GLSprite("ships", 192,  64, 64, 64));
		/* Load PINK ships*/
		shipSprites.put("SCOUT_BLUE", 		new GLSprite("ships",   0, 128, 64, 64));
		shipSprites.put("HUNTER_BLUE", 		new GLSprite("ships",  64, 128, 64, 64));
		shipSprites.put("COLONIZER_BLUE", 	new GLSprite("ships", 128, 128, 64, 64));
		shipSprites.put("DESTROYER_BLUE", 	new GLSprite("ships", 192, 128, 64, 64));
		/* Load GREEN ships*/
		shipSprites.put("SCOUT_RED", 		new GLSprite("ships",   0, 192, 64, 64));
		shipSprites.put("HUNTER_RED", 		new GLSprite("ships",  64, 192, 64, 64));
		shipSprites.put("COLONIZER_RED",	new GLSprite("ships", 128, 192, 64, 64));
		shipSprites.put("DESTROYER_RED", 	new GLSprite("ships", 192, 192, 64, 64));
		
		metalPlanets.put(0, new GLSprite("planets/metalplanet_0", 64, 64));
		metalPlanets.put(1, new GLSprite("planets/metalplanet_1", 64, 64));
		metalPlanets.put(2, new GLSprite("planets/metalplanet_2", 64, 64));
		metalPlanets.put(3, new GLSprite("planets/metalplanet_3", 64, 64));
		gasPlanets.put(0, 	new GLSprite("planets/gasplanet_0",   64, 64));
		gasPlanets.put(1,  	new GLSprite("planets/gasplanet_1",   64, 64));
		gasPlanets.put(2,  	new GLSprite("planets/gasplanet_2",   64, 64));
		
		colonyMarkerSprites.put(Player.RED, 	new GLSprite("colonymarker:RED",   64, 64));
		colonyMarkerSprites.put(Player.BLUE, 	new GLSprite("colonymarker:BLUE",  64, 64));
		colonyMarkerSprites.put(Player.PINK, 	new GLSprite("colonymarker:PINK",  64, 64));
		colonyMarkerSprites.put(Player.GREEN, 	new GLSprite("colonymarker:GREEN", 64, 64));
		
		pathSprites.put(HEAD, new GLSprite("path/head", 128, 128));
		pathSprites.put(START, new GLSprite("path/start", 128, 128));
		pathSprites.put(STRAIGHT, new GLSprite("path/straight", 128, 128));
		pathSprites.put(TURN, new GLSprite("path/turn", 128, 128));
		
		fogSprite = new GLSprite("cloud", 256, 256);
	}
	
	public static GLSpriteMap getSprites(SpriteMapData data, int squareSize) {
		GLSpriteMap.data = data;
		if (!initiated) {
			init();
		}
		GLSpriteMap map = new GLSpriteMap();
		int rows = 2*GLRenderArea.EXTRA_SPACE_VERTICAL + GLSpriteMap.data.getRows();
		int cols = 2*GLRenderArea.EXTRA_SPACE_HORIZONTAL + GLSpriteMap.data.getCols();
		map.bounds = new Rectangle(0, 0, cols*squareSize, rows*squareSize);
		/* Add Colony data */
		for (ColonyData colonyData : GLSpriteMap.data.getColonyData()) {
			if (map.colonies.get(colonyData.getPlayer()) == null)
				map.colonies.put(colonyData.getPlayer(), new ArrayList<Rectangle>());
			Rectangle r = calculateRect(colonyData.getPosition(), 0.5f, 0.5f, squareSize, 0.5f);
			map.colonies.get(colonyData.getPlayer()).add(r);
		}
		/* Add Planet data */
		for (PlanetData planetData : GLSpriteMap.data.getPlanetData()) {
			if (map.planets.get(planetData.getResource()) == null)
				map.planets.put(planetData.getResource(), new HashMap<Position, Rectangle>());
			Map<Position, Rectangle> m = map.planets.get(planetData.getResource());
			Rectangle r = calculateRect(planetData.getPosition(), 0.5f, 0.5f, squareSize, 0.5f);
			m.put(planetData.getPosition(), r);
		}
		/* Add Fleet Data */
		for (FleetData fleetData : GLSpriteMap.data.getFleetData()) {
			if (map.fleets.get(fleetData.getPlayer()) == null)
				map.fleets.put(fleetData.getPlayer(), new HashMap<ShipType, List<Rectangle>>());
			if (map.fleets.get(fleetData.getPlayer()).get(fleetData.getFlagships()) == null)
				map.fleets.get(fleetData.getPlayer()).put(fleetData.getFlagships(), new ArrayList<Rectangle>());
			List<Rectangle> list = map.fleets.get(fleetData.getPlayer()).get(fleetData.getFlagships());
			list.add(calculateRect(fleetData.getPosition(), 0, 0, squareSize, 0.5f));
		}
		/* Add Colonizer Data */
		for (ColonizerData colonizerData : GLSpriteMap.data.getColonizerData()) {
			if (map.fleets.get(colonizerData.getPlayer()) == null)
				map.fleets.put(colonizerData.getPlayer(), new HashMap<ShipType, List<Rectangle>>());
			if (map.fleets.get(colonizerData.getPlayer()).get(ShipType.COLONIZER) == null)
				map.fleets.get(colonizerData.getPlayer()).put(ShipType.COLONIZER, new ArrayList<Rectangle>());
			List<Rectangle> list = map.fleets.get(colonizerData.getPlayer()).get(ShipType.COLONIZER);
			list.add(calculateRect(colonizerData.getPosition(), 0.5f, 0, squareSize, 0.5f));
		}
		/* Add Fog Data */
		for (Position pos : GLSpriteMap.data.getFog()) {
			map.fogOfWar.add(calculateRect(pos, 0, 0, squareSize, 1));
		}
		
		/* Add Path data */
		Position[][] paths = GLSpriteMap.data.getPaths();
		double rotation = 0;
		for (int i = 0; i < paths.length; i++) {
			if (paths[i].length > 1) {
				for (int j = 0; j < paths[i].length; j++) {
					if (j == 0) {
						if (map.paths.get(START) == null) {
							map.paths.put(START, new HashMap<Rectangle, Double>());
						}
						rotation = Path.getRotation(null, paths[i][j], paths[i][j+1]);
						map.paths.get(START).put(calculateRect(paths[i][j], 0, 0, squareSize, 1), rotation);
					} else if (j == paths[i].length - 1) {
						if (map.paths.get(HEAD) == null) {
							map.paths.put(HEAD, new HashMap<Rectangle, Double>());
						}
						rotation = Path.getRotation(paths[i][j-1], paths[i][j], null);
						map.paths.get(HEAD).put(calculateRect(paths[i][j], 0, 0, squareSize, 1), rotation);
					} else if (paths[i][j-1].getCol() != paths[i][j+1].getCol() && paths[i][j-1].getRow() != paths[i][j+1].getRow()) {
						if (map.paths.get(TURN) == null) {
							map.paths.put(TURN, new HashMap<Rectangle, Double>());
						}
						rotation = Path.getRotation(paths[i][j-1], paths[i][j], paths[i][j+1]);
						map.paths.get(TURN).put(calculateRect(paths[i][j], 0, 0, squareSize, 1), rotation);
					} else {
						if (map.paths.get(STRAIGHT) == null) {
							map.paths.put(STRAIGHT, new HashMap<Rectangle, Double>());
						}
						rotation = Path.getRotation(paths[i][j-1], paths[i][j], paths[i][j+1]);
						map.paths.get(STRAIGHT).put(calculateRect(paths[i][j], 0, 0, squareSize, 1), rotation);
					}
				}
			}
		}
		return map;
	}
	
	/**
	 * Calculates a Rectangle for the area to draw a GLSprite
	 * @param pos The Position of the GLSprite
	 * @param dX distance in <code>dX * squareSize</code> from the Positions x coordinate
	 * @param dY distance in <code>dY * squareSize</code> from the Positions y coordinate
	 * @param squareSize the size of a square in the game in pixels.
	 * @param dS the size of the Rectangle relative to a square.
	 * @return
	 */
	private static Rectangle calculateRect(Position pos, float dX, float dY, int squareSize, float dS) {
		int rows = 2*GLRenderArea.EXTRA_SPACE_VERTICAL + GLSpriteMap.data.getRows();
		System.out.println("rows = " + rows);
		int x = (GLRenderArea.EXTRA_SPACE_HORIZONTAL + pos.getCol() - 1) * squareSize;
		int y = (rows - GLRenderArea.EXTRA_SPACE_VERTICAL - pos.getRow()) * squareSize;
		x += (int) (dX * squareSize);
		y += (int) (dY * squareSize);
		return new Rectangle(x, y, (int) (squareSize*dS), (int) (squareSize*dS));
	}

	@Override
	public Rectangle getBounds() {
		return bounds;
	}

	@Override
	public void draw(GLAutoDrawable drawable, Rectangle objectRect,	Rectangle targetArea, int zIndex) {
		/* Draw Colonies */
		for (Player player : colonies.keySet()) {
			for (Rectangle r : colonies.get(player)) {
				if (r.intersects(targetArea)) {
					colonyMarkerSprites.get(player).draw(drawable, r, targetArea, zIndex + 1);
				}
			}
		}
		/* Draw Planets */
		for (Resource res : planets.keySet()) {
			for (Position pos : planets.get(res).keySet()) {
				Rectangle r = planets.get(res).get(pos);
				allPlanets.get(res).get(pos).draw(drawable, r, targetArea, zIndex + 2);
			}
		}
		/* Draw Fleets */
		for (Player player : fleets.keySet()) {
			for (ShipType type : fleets.get(player).keySet()) {
				List<Rectangle> list = fleets.get(player).get(type);
				for (Rectangle r : list) {
					shipSprites.get(type + "_" + player).draw(drawable, r, targetArea, zIndex + 3);
				}
			}
		}
		/* Draw Paths */
		for (String s : paths.keySet()) {
			for (Rectangle r : paths.get(s).keySet()) {
				GL2 gl = drawable.getGL().getGL2();
				double rotation = Math.toDegrees(paths.get(s).get(r));
//				gl.glLoadIdentity();
//				float xTrans = ((float) r.getX() + r.getWidth()/2 - targetArea.getX())/targetArea.getWidth();
//				float yTrans = ((float) r.getY() +r.getHeight()/2 - targetArea.getY())/targetArea.getHeight();
//				System.out.println(s);
//				System.out.println("xT: " + xTrans);
//				System.out.println("yT: " + yTrans);
//				System.out.println("rot: " + rotation);
//				System.out.println("--------------");
//				gl.glTranslatef(-xTrans, -yTrans, 0);
//				gl.glRotated(-rotation, 0, 0, 1);
//				gl.glTranslatef(xTrans, yTrans, 0);
				pathSprites.get(s).draw(drawable, r, targetArea, zIndex + 4);
//				gl.glLoadIdentity();
			}
		}
		
		/* Draw Fog */
		for (Rectangle r : fogOfWar) {
			fogSprite.draw(drawable, r, targetArea, zIndex + 5);
		}
	}
}