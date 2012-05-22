package riskyspace.view.swing.impl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import riskyspace.PlayerColors;
import riskyspace.logic.Path;
import riskyspace.logic.SpriteMapData;
import riskyspace.logic.data.ColonizerData;
import riskyspace.logic.data.ColonyData;
import riskyspace.logic.data.FleetData;
import riskyspace.logic.data.PlanetData;
import riskyspace.model.Player;
import riskyspace.model.Position;
import riskyspace.model.Resource;
import riskyspace.view.ViewResources;

/**
 * Drawing all game Sprites in a awt context
 * @author Alexander Hederstaf
 * 
 */
public class SwingSpriteMap {
	
	private static boolean initiated = false;
	
	/*
	 * Planet Sprites
	 */
	private static Map<Integer, SwingSprite> metalPlanets = new HashMap<Integer, SwingSprite>();
	private static Map<Integer, SwingSprite> gasPlanets = new HashMap<Integer, SwingSprite>();
	/*
	 * Path Sprites
	 */
	private static Map<String, Image> pathTextures = new HashMap<String, Image>();
	
	/*
	 * Ship Sprites
	 */
	private static Map<String, SwingSprite> shipSprites = new HashMap<String, SwingSprite>();
	
	/*
	 * All planets with randomized textures
	 */
	private static Map<Resource, Map<Position, SwingSprite>> allPlanets = new HashMap<Resource, Map<Position, SwingSprite>>();

	/*
	 * Colony Sprites for each Player
	 */
	private static Map<String, SwingSprite> colonySprites = new HashMap<String, SwingSprite>();
	
	/*
	 * Fog Sprite
	 */
	private static SwingSprite fogSprite = null;
	
	/*
	 * SpriteMapData
	 */
	private static SpriteMapData data = null;
	
	/*
	 * Sprites to be drawn.
	 */
	private List<Position> fogOfWar = new ArrayList<Position>();
	private Map<Position, SwingSprite> colonies = new HashMap<Position, SwingSprite>();
	private Map<Position, List<SwingSprite>> paths = new HashMap<Position, List<SwingSprite>>();
	private Map<Position, SwingSprite> fleets = new HashMap<Position, SwingSprite>();
	private Map<Position, SwingSprite> colonizers = new HashMap<Position, SwingSprite>();
	private Map<Position, SwingSprite> planets = new HashMap<Position, SwingSprite>();

	private Map<Position, Integer> shipCount = new HashMap<Position, Integer>();
	private Map<Position, Integer> colonizerCount = new HashMap<Position, Integer>();
	
	/**
	 * Initiate this SpriteMap object so that it is set up for creating
	 * SpriteMaps.
	 * 
	 * @param squareSize The size of a square.
	 */
	private static void init(int squareSize) {
		loadSprites(squareSize);
		setPlanetSprites();
		initiated = true;
	}
	
	private static void setPlanetSprites() {
		Map<Position, SwingSprite> gas = new HashMap<Position, SwingSprite>();
		Map<Position, SwingSprite> metal = new HashMap<Position, SwingSprite>();
		for (Position pos : data.getAllPositions()) {
			gas.put(pos, gasPlanets.get((int) (Math.random()*3)));
			metal.put(pos, metalPlanets.get((int) (Math.random()*4)));
		}
		allPlanets.put(Resource.GAS, gas);
		allPlanets.put(Resource.METAL, metal);
	}
	
	/**
	 * Creates a SpriteMap containing Sprites that can be drawn using the
	 * <code>draw(Graphics g)</code> method.
	 * @param smd The Player's view this SpriteMap should contain.
	 * @return A SpriteMap with graphic info for this Player.
	 */
	public static SwingSpriteMap getSprites(SpriteMapData data, int squareSize) {
		SwingSpriteMap.data = data;
		if (!initiated) {
			init(squareSize);
		}
		SwingSpriteMap map = new SwingSpriteMap();
		
		for (ColonyData colonyData : SwingSpriteMap.data.getColonyData()) {
			map.colonies.put(colonyData.getPosition(), colonySprites.get(colonyData.getPlayer().toString()));
		}
		for (PlanetData planetData : SwingSpriteMap.data.getPlanetData()) {
			map.planets.put(planetData.getPosition(), allPlanets.get(planetData.getResource()).get(planetData.getPosition()));
		}
		for (ColonizerData colonizerData : SwingSpriteMap.data.getColonizerData()) {
			map.colonizers.put(colonizerData.getPosition(), shipSprites.get("COLONIZER_" + colonizerData.getPlayer()));
			map.colonizerCount.put(colonizerData.getPosition(), data.getColonizerAmount(colonizerData.getPosition()));
		}
		for (FleetData fleetData : SwingSpriteMap.data.getFleetData()) {
			map.fleets.put(fleetData.getPosition(), shipSprites.get(fleetData.getFlagships() + "_" + fleetData.getPlayer()));
			map.shipCount.put(fleetData.getPosition(), data.getFleetSize(fleetData.getPosition()));
		}
		for (Position pos : SwingSpriteMap.data.getFog()) {
			map.fogOfWar.add(pos);
		}
		
		Position[][] paths = SwingSpriteMap.data.getPaths();
		for (int i = 0; i < paths.length; i++) {
			if (paths[i].length > 1) {
				for (int j = 0; j < paths[i].length; j++) {
					if (!map.paths.containsKey(paths[i][j])) {
						map.paths.put(paths[i][j], new ArrayList<SwingSprite>());
					}
					SwingSprite sprite = null;
					if (j == 0) {
						sprite = new SwingSprite(SwingSpriteMap.pathTextures.get("START_" + Player.BLUE), 0, 0);
						sprite.setRotation(Path.getRotation(null, paths[i][j], paths[i][j+1]));
					} else if (j == paths[i].length - 1) {
						sprite = new SwingSprite(SwingSpriteMap.pathTextures.get("HEAD_" + Player.BLUE), 0, 0);
						sprite.setRotation(Path.getRotation(paths[i][j-1], paths[i][j], null));
					} else if (paths[i][j-1].getCol() != paths[i][j+1].getCol() && paths[i][j-1].getRow() != paths[i][j+1].getRow()) {
						sprite = new SwingSprite(SwingSpriteMap.pathTextures.get("TURN_" + Player.BLUE), 0, 0);
						sprite.setRotation(Path.getRotation(paths[i][j-1], paths[i][j], paths[i][j+1]));
					} else {
						sprite = new SwingSprite(SwingSpriteMap.pathTextures.get("STRAIGHT_" + Player.BLUE), 0, 0);
						sprite.setRotation(Path.getRotation(paths[i][j-1], paths[i][j], paths[i][j+1]));
					}
					map.paths.get(paths[i][j]).add(sprite);
				}
			}
		}
		return map;
	}
	
	/**
	 * Loads and scales Sprites and Textures used in the game.
	 */
	private static void loadSprites(int squareSize){
		/*
		 * Ships Blue Player
		 */
		shipSprites.put("SCOUT_BLUE", new SwingSprite(Toolkit.getDefaultToolkit().getImage("res/icons/blue/scout.png").getScaledInstance(squareSize/2, squareSize/2, Image.SCALE_DEFAULT), 0, 0.5f));
		shipSprites.put("HUNTER_BLUE", new SwingSprite(Toolkit.getDefaultToolkit().getImage("res/icons/blue/hunter.png").getScaledInstance(squareSize/2, squareSize/2, Image.SCALE_DEFAULT), 0, 0.5f));
		shipSprites.put("DESTROYER_BLUE", new SwingSprite(Toolkit.getDefaultToolkit().getImage("res/icons/blue/destroyer.png").getScaledInstance(squareSize/2, squareSize/2, Image.SCALE_DEFAULT), 0, 0.5f));
		shipSprites.put("COLONIZER_BLUE", new SwingSprite(Toolkit.getDefaultToolkit().getImage("res/icons/blue/colonizer.png").getScaledInstance(squareSize/2, squareSize/2, Image.SCALE_DEFAULT), 0.5f, 0.5f));
		
		/*
		 * Ships Red Player
		 */
		shipSprites.put("SCOUT_RED", new SwingSprite(Toolkit.getDefaultToolkit().getImage("res/icons/red/scout.png").getScaledInstance(squareSize/2, squareSize/2, Image.SCALE_DEFAULT), 0, 0.5f));
		shipSprites.put("HUNTER_RED", new SwingSprite(Toolkit.getDefaultToolkit().getImage("res/icons/red/hunter.png").getScaledInstance(squareSize/2, squareSize/2, Image.SCALE_DEFAULT), 0, 0.5f));
		shipSprites.put("DESTROYER_RED", new SwingSprite(Toolkit.getDefaultToolkit().getImage("res/icons/red/destroyer.png").getScaledInstance(squareSize/2, squareSize/2, Image.SCALE_DEFAULT), 0, 0.5f));
		shipSprites.put("COLONIZER_RED", new SwingSprite(Toolkit.getDefaultToolkit().getImage("res/icons/red/colonizer.png").getScaledInstance(squareSize/2, squareSize/2, Image.SCALE_DEFAULT), 0.5f, 0.5f));

		/*
		 * Path Arrows
		 */
		pathTextures.put("HEAD_BLUE", Toolkit.getDefaultToolkit().getImage("res/path/blue/head.png").getScaledInstance(squareSize, squareSize, Image.SCALE_DEFAULT));
		pathTextures.put("START_BLUE", Toolkit.getDefaultToolkit().getImage("res/path/blue/start.png").getScaledInstance(squareSize, squareSize, Image.SCALE_DEFAULT));
		pathTextures.put("STRAIGHT_BLUE", Toolkit.getDefaultToolkit().getImage("res/path/blue/straight.png").getScaledInstance(squareSize, squareSize, Image.SCALE_DEFAULT));
		pathTextures.put("TURN_BLUE", Toolkit.getDefaultToolkit().getImage("res/path/blue/turn.png").getScaledInstance(squareSize, squareSize, Image.SCALE_DEFAULT));
		pathTextures.put("HEAD_RED", Toolkit.getDefaultToolkit().getImage("res/path/red/head.png").getScaledInstance(squareSize, squareSize, Image.SCALE_DEFAULT));
		pathTextures.put("START_RED", Toolkit.getDefaultToolkit().getImage("res/path/red/start.png").getScaledInstance(squareSize, squareSize, Image.SCALE_DEFAULT));
		pathTextures.put("STRAIGHT_RED", Toolkit.getDefaultToolkit().getImage("res/path/red/straight.png").getScaledInstance(squareSize, squareSize, Image.SCALE_DEFAULT));
		pathTextures.put("TURN_RED", Toolkit.getDefaultToolkit().getImage("res/path/red/turn.png").getScaledInstance(squareSize, squareSize, Image.SCALE_DEFAULT));
			
		/*
		 * Planets
		 */
		metalPlanets.put(0, new SwingSprite(Toolkit.getDefaultToolkit().getImage("res/icons/planets/metalplanet_0.png").getScaledInstance(squareSize/2, squareSize/2, Image.SCALE_DEFAULT), 0.5f, 0));
		metalPlanets.put(1, new SwingSprite(Toolkit.getDefaultToolkit().getImage("res/icons/planets/metalplanet_1.png").getScaledInstance(squareSize/2, squareSize/2, Image.SCALE_DEFAULT), 0.5f, 0));
		metalPlanets.put(2, new SwingSprite(Toolkit.getDefaultToolkit().getImage("res/icons/planets/metalplanet_2.png").getScaledInstance(squareSize/2, squareSize/2, Image.SCALE_DEFAULT), 0.5f, 0));
		metalPlanets.put(3, new SwingSprite(Toolkit.getDefaultToolkit().getImage("res/icons/planets/metalplanet_3.png").getScaledInstance(squareSize/2, squareSize/2, Image.SCALE_DEFAULT), 0.5f, 0));
		gasPlanets.put(0, new SwingSprite(Toolkit.getDefaultToolkit().getImage("res/icons/planets/gasplanet_0.png").getScaledInstance(squareSize/2, squareSize/2, Image.SCALE_DEFAULT), 0.5f, 0));
		gasPlanets.put(1, new SwingSprite(Toolkit.getDefaultToolkit().getImage("res/icons/planets/gasplanet_1.png").getScaledInstance(squareSize/2, squareSize/2, Image.SCALE_DEFAULT), 0.5f, 0));
		gasPlanets.put(2, new SwingSprite(Toolkit.getDefaultToolkit().getImage("res/icons/planets/gasplanet_2.png").getScaledInstance(squareSize/2, squareSize/2, Image.SCALE_DEFAULT), 0.5f, 0));
		
		/*
		 * Colony Sprite
		 */
		colonySprites.put("RED", createColonySprite(PlayerColors.getColor(Player.RED), squareSize));
		colonySprites.put("BLUE", createColonySprite(PlayerColors.getColor(Player.BLUE), squareSize));
		
		/*
		 * Fog
		 */
		fogSprite = new SwingSprite(Toolkit.getDefaultToolkit().getImage("res/icons/cloud.png").getScaledInstance(squareSize, squareSize, Image.SCALE_SMOOTH), 0, 0);
	}
	
	/**
	 * Creates a Circle Sprite of any Color used as Colony marker
	 * @param color The Color of the Circle
	 * @param squareSize Size of one Square in the game
	 */
	private static SwingSprite createColonySprite(Color color, int squareSize){
		BufferedImage img = new BufferedImage(squareSize/2, squareSize/2, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.createGraphics();
		g.setColor(color);
		g.fillOval(squareSize/60, squareSize/60, squareSize/2 + squareSize%2 - squareSize/30 - 1, squareSize/2 + squareSize%2 - squareSize/30 - 1);
		return new SwingSprite(img, 0.5f, 0);
	}
	
	/**
	 * Draw this sprite map
	 * @param g The graphics context to draw on.
	 * @param squareSize Size of one square in pixels
	 * @param offsetX The number of squares on the left and right
	 * sides of the world.
	 * @param offsetY The number of squares on the top and bottom
	 * sides of the world.
	 */
	public void draw(Graphics g, int squareSize, int offsetX, int offsetY) {
		for (Position pos : colonies.keySet()) {
			colonies.get(pos).draw(g, calcX(pos, offsetX, squareSize), calcY(pos, offsetY, squareSize), squareSize);
		}
		for (Position pos : planets.keySet()) {
			planets.get(pos).draw(g, calcX(pos, offsetX, squareSize), calcY(pos, offsetY, squareSize), squareSize);
		}
		for (Position pos : paths.keySet()) {
			for (SwingSprite sprite : paths.get(pos)) {
				sprite.draw(g, calcX(pos, offsetX, squareSize), calcY(pos, offsetY, squareSize), squareSize);
			}
		}
		for (Position pos : fogOfWar) {
			fogSprite.draw(g, calcX(pos, offsetX, squareSize), calcY(pos, offsetY, squareSize), squareSize);
		}
		g.setColor(ViewResources.WHITE);
		g.setFont(ViewResources.getFont().deriveFont(12f));
		for (Position pos : fleets.keySet()) {
			fleets.get(pos).draw(g, calcX(pos, offsetX, squareSize), calcY(pos, offsetY, squareSize), squareSize);
			g.drawString("" + shipCount.get(pos), calcX(pos, offsetX, squareSize) + squareSize/2 - squareSize/8, calcY(pos, offsetY, squareSize) + squareSize - 5);
		}
		for (Position pos : colonizers.keySet()) {
			colonizers.get(pos).draw(g, calcX(pos, offsetX, squareSize), calcY(pos, offsetY, squareSize), squareSize);
			g.drawString("" + colonizerCount.get(pos), calcX(pos, offsetX, squareSize) + squareSize - squareSize/8, calcY(pos, offsetY, squareSize) + squareSize - 5);
		}
	}

	private int calcX(Position pos, int offsetX, int squareSize) {
		return (pos.getCol()-1 + offsetX)*squareSize;
	}

	private int calcY(Position pos, int offsetY, int squareSize) {
		return (pos.getRow()-1 + offsetY)*squareSize;
	}
}