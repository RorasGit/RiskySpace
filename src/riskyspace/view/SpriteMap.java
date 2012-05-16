package riskyspace.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import riskyspace.GameManager;
import riskyspace.PlayerColors;
import riskyspace.logic.SpriteMapData;
import riskyspace.logic.data.ColonizerData;
import riskyspace.logic.data.ColonyData;
import riskyspace.logic.data.FleetData;
import riskyspace.logic.data.PlanetData;
import riskyspace.model.Player;
import riskyspace.model.Position;
import riskyspace.model.Resource;

/**
 * 
 * @author flygarn
 * 
 */
public class SpriteMap {
	
	private static boolean initiated = false;
	
	/*
	 * Planet Sprites
	 */
	private static Map<Integer, Sprite> metalPlanets = new HashMap<Integer, Sprite>();
	private static Map<Integer, Sprite> gasPlanets = new HashMap<Integer, Sprite>();
	/*
	 * Path Sprites
	 */
	private static Map<String, Image> pathTextures = new HashMap<String, Image>();
	
	/*
	 * Ship Sprites
	 */
	private static Map<String, Sprite> shipSprites = new HashMap<String, Sprite>();
	
	/*
	 * All planets with randomized textures
	 */
	private static Map<Resource, Map<Position, Sprite>> allPlanets = new HashMap<Resource, Map<Position, Sprite>>();

	/*
	 * Colony Sprites for each Player
	 */
	private static Map<String, Sprite> colonySprites = new HashMap<String, Sprite>();
	
	/*
	 * Fog Sprite
	 */
	private static Sprite fogSprite = null;
	
	/*
	 * SpriteMapData
	 */
	private static SpriteMapData data = null;
	
	/*
	 * Sprites to be drawn.
	 */
	private List<Position> fogOfWar = new ArrayList<Position>();
	private Map<Position, Sprite> colonies = new HashMap<Position, Sprite>();
	private Map<Position, List<Sprite>> paths = new HashMap<Position, List<Sprite>>();
	private Map<Position, Sprite> fleets = new HashMap<Position, Sprite>();
	private Map<Position, Sprite> colonizers = new HashMap<Position, Sprite>();
	private Map<Position, Sprite> planets = new HashMap<Position, Sprite>();

	/*
	 * BAD SOLUTION TEMPORARY TODO
	 */
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
		Map<Position, Sprite> gas = new HashMap<Position, Sprite>();
		Map<Position, Sprite> metal = new HashMap<Position, Sprite>();
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
	public static SpriteMap getSprites(SpriteMapData data, int squareSize) {
		SpriteMap.data = data;
		if (!initiated) {
			init(squareSize);
		}
		SpriteMap map = new SpriteMap();
		
		for (ColonyData colonyData : SpriteMap.data.getColonyData()) {
			map.colonies.put(colonyData.getPosition(), colonySprites.get(colonyData.getPlayer().toString()));
		}
		for (PlanetData planetData : SpriteMap.data.getPlanetData()) {
			map.planets.put(planetData.getPosition(), allPlanets.get(planetData.getResource()).get(planetData.getPosition()));
		}
		for (ColonizerData colonizerData : SpriteMap.data.getColonizerData()) {
			map.colonizers.put(colonizerData.getPosition(), shipSprites.get("COLONIZER_" + colonizerData.getPlayer()));
			map.colonizerCount.put(colonizerData.getPosition(), data.getColonizerAmount(colonizerData.getPosition()));
		}
		for (FleetData fleetData : SpriteMap.data.getFleetData()) {
			map.fleets.put(fleetData.getPosition(), shipSprites.get(fleetData.getFlagships() + "_" + fleetData.getPlayer()));
			map.shipCount.put(fleetData.getPosition(), data.getFleetSize(fleetData.getPosition()));
		}
		for (Position pos : SpriteMap.data.getFog()) {
			map.fogOfWar.add(pos);
		}
		
		Position[][] paths = SpriteMap.data.getPaths();
		for (int i = 0; i < paths.length; i++) {
			if (paths[i].length > 1) {
				for (int j = 0; j < paths[i].length; j++) {
					if (!map.paths.containsKey(paths[i][j])) {
						map.paths.put(paths[i][j], new ArrayList<Sprite>());
					}
					Sprite sprite = null;
					/*
					 * TODO:
					 * Fix arrow colors thing
					 */
					if (j == 0) {
						sprite = new Sprite(SpriteMap.pathTextures.get("START_" + Player.BLUE), 0, 0);
						sprite.setRotation(getRotation(null, paths[i][j], paths[i][j+1]));
					} else if (j == paths[i].length - 1) {
						sprite = new Sprite(SpriteMap.pathTextures.get("HEAD_" + Player.BLUE), 0, 0);
						sprite.setRotation(getRotation(paths[i][j-1], paths[i][j], null));
					} else if (paths[i][j-1].getCol() != paths[i][j+1].getCol() && paths[i][j-1].getRow() != paths[i][j+1].getRow()) {
						sprite = new Sprite(SpriteMap.pathTextures.get("TURN_" + Player.BLUE), 0, 0);
						sprite.setRotation(getRotation(paths[i][j-1], paths[i][j], paths[i][j+1]));
					} else {
						sprite = new Sprite(SpriteMap.pathTextures.get("STRAIGHT_" + Player.BLUE), 0, 0);
						sprite.setRotation(getRotation(paths[i][j-1], paths[i][j], paths[i][j+1]));
					}
					map.paths.get(paths[i][j]).add(sprite);
				}
			}
		}
		return map;
	}
	
	/**
	 * Determine the Rotation in radians for a Path arrow part depending on path segments.
	 * @param previous The path segment previous to the current one.
	 * @param current The current path segment.
	 * @param next The next path segment.
	 * @return Rotation for the current segment in radians.
	 */
	private static double getRotation(Position previous, Position current, Position next) {
		/*
		 * Variables for determining where the previous and next positions
		 * are relative to the current position
		 */
		boolean prevAbove = false;
		boolean prevUnder = false;
		boolean prevLeft = false;
		boolean prevRight = false;
		
		boolean nextRight = false;
		boolean nextUnder = false;
		boolean nextLeft = false;
		boolean nextAbove = false;
		
		/*
		 * Only calculate values if there is a previous
		 * respective next position in.
		 */
		if (previous != null) {
			prevAbove = previous.getRow() < current.getRow();
			prevUnder = previous.getRow() > current.getRow();
			prevLeft = previous.getCol() < current.getCol();
			prevRight = previous.getCol() > current.getCol();
		}
		if (next != null) {
			nextRight = next.getCol() > current.getCol();
			nextUnder = next.getRow() > current.getRow();
			nextLeft = next.getCol() < current.getCol();
			nextAbove = next.getRow() < current.getRow();
		}
		/*
		 * If previous is null then only determine rotation based on 
		 * next position.
		 * >> Path is always of length 2 at least, therefore no point can
		 * have neither previous or next location.
		 */
		if (previous == null) {
			if (nextAbove) {
				return 3*Math.PI/2;
			} else if (nextUnder) {
				return Math.PI/2;
			} else if (nextLeft) {
				return Math.PI;
			} else if (nextRight) {
				return 0;
			}
		}
		/*
		 * If next is null then only determine rotation based on 
		 * previous position.
		 */
		if (next == null) {
			if (prevAbove) {
				return Math.PI/2;
			} else if (prevUnder) {
				return 3*Math.PI/2;
			} else if (prevLeft) {
				return 0;
			} else if (prevRight) {
				return Math.PI;
			}
		}
		/*
		 * Return rotation based on where the previous and next locations are.
		 */
		if (prevAbove) {
			if (nextUnder) {
				return Math.PI/2;
			} else if (nextLeft) {
				return Math.PI/2;
			} else if (nextRight) {
				return Math.PI;
			}
		} else if (nextAbove) {
			if (prevUnder) {
				return Math.PI/2;
			} else if (prevLeft) {
				return Math.PI/2;
			} else if (prevRight) {
				return Math.PI;
			}
		} else if (prevUnder) {
			if (nextAbove) {
				return Math.PI/2;
			} else if (nextLeft) {
				return 0;
			} else if (nextRight) {
				return 3*Math.PI/2;
			}
		} else if (nextUnder) {
			if (prevAbove) {
				return Math.PI/2;
			} else if (prevLeft) {
				return 0;
			} else if (prevRight) {
				return 3*Math.PI/2;
			}
		}
		/*
		 * Return 0 to make the compiler happy, will never run
		 * unless previous == current || current == next which
		 * is wrong usage.
		 */
		return 0;
	}
	
	/**
	 * Loads and scales Sprites and Textures used in the game.
	 */
	private static void loadSprites(int squareSize){
		/*
		 * Ships Blue Player
		 */
		shipSprites.put("SCOUT_BLUE", new Sprite(Toolkit.getDefaultToolkit().getImage("res/icons/blue/scout.png").getScaledInstance(squareSize/2, squareSize/2, Image.SCALE_DEFAULT), 0, 0.5f));
		shipSprites.put("HUNTER_BLUE", new Sprite(Toolkit.getDefaultToolkit().getImage("res/icons/blue/hunter.png").getScaledInstance(squareSize/2, squareSize/2, Image.SCALE_DEFAULT), 0, 0.5f));
		shipSprites.put("DESTROYER_BLUE", new Sprite(Toolkit.getDefaultToolkit().getImage("res/icons/blue/destroyer.png").getScaledInstance(squareSize/2, squareSize/2, Image.SCALE_DEFAULT), 0, 0.5f));
		shipSprites.put("COLONIZER_BLUE", new Sprite(Toolkit.getDefaultToolkit().getImage("res/icons/blue/colonizer.png").getScaledInstance(squareSize/2, squareSize/2, Image.SCALE_DEFAULT), 0.5f, 0.5f));
		
		/*
		 * Ships Red Player
		 */
		shipSprites.put("SCOUT_RED", new Sprite(Toolkit.getDefaultToolkit().getImage("res/icons/red/scout.png").getScaledInstance(squareSize/2, squareSize/2, Image.SCALE_DEFAULT), 0, 0.5f));
		shipSprites.put("HUNTER_RED", new Sprite(Toolkit.getDefaultToolkit().getImage("res/icons/red/hunter.png").getScaledInstance(squareSize/2, squareSize/2, Image.SCALE_DEFAULT), 0, 0.5f));
		shipSprites.put("DESTROYER_RED", new Sprite(Toolkit.getDefaultToolkit().getImage("res/icons/red/destroyer.png").getScaledInstance(squareSize/2, squareSize/2, Image.SCALE_DEFAULT), 0, 0.5f));
		shipSprites.put("COLONIZER_RED", new Sprite(Toolkit.getDefaultToolkit().getImage("res/icons/red/colonizer.png").getScaledInstance(squareSize/2, squareSize/2, Image.SCALE_DEFAULT), 0.5f, 0.5f));

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
		metalPlanets.put(0, new Sprite(Toolkit.getDefaultToolkit().getImage("res/icons/planets/metalplanet_0.png").getScaledInstance(squareSize/2, squareSize/2, Image.SCALE_DEFAULT), 0.5f, 0));
		metalPlanets.put(1, new Sprite(Toolkit.getDefaultToolkit().getImage("res/icons/planets/metalplanet_1.png").getScaledInstance(squareSize/2, squareSize/2, Image.SCALE_DEFAULT), 0.5f, 0));
		metalPlanets.put(2, new Sprite(Toolkit.getDefaultToolkit().getImage("res/icons/planets/metalplanet_2.png").getScaledInstance(squareSize/2, squareSize/2, Image.SCALE_DEFAULT), 0.5f, 0));
		metalPlanets.put(3, new Sprite(Toolkit.getDefaultToolkit().getImage("res/icons/planets/metalplanet_3.png").getScaledInstance(squareSize/2, squareSize/2, Image.SCALE_DEFAULT), 0.5f, 0));
		gasPlanets.put(0, new Sprite(Toolkit.getDefaultToolkit().getImage("res/icons/planets/gasplanet_0.png").getScaledInstance(squareSize/2, squareSize/2, Image.SCALE_DEFAULT), 0.5f, 0));
		gasPlanets.put(1, new Sprite(Toolkit.getDefaultToolkit().getImage("res/icons/planets/gasplanet_1.png").getScaledInstance(squareSize/2, squareSize/2, Image.SCALE_DEFAULT), 0.5f, 0));
		gasPlanets.put(2, new Sprite(Toolkit.getDefaultToolkit().getImage("res/icons/planets/gasplanet_2.png").getScaledInstance(squareSize/2, squareSize/2, Image.SCALE_DEFAULT), 0.5f, 0));
		
		/*
		 * Colony Sprite
		 */
		colonySprites.put("RED", createColonySprite(PlayerColors.getColor(Player.RED), squareSize));
		colonySprites.put("BLUE", createColonySprite(PlayerColors.getColor(Player.BLUE), squareSize));
		
		/*
		 * Fog
		 */
		fogSprite = new Sprite(Toolkit.getDefaultToolkit().getImage("res/icons/cloud.png").getScaledInstance(squareSize, squareSize, Image.SCALE_SMOOTH), 0, 0);
	}
	
	/**
	 * Creates a Circle Sprite of any Color used as Colony marker
	 * @param color The Color of the Circle
	 * @param squareSize Size of one Square in the game
	 */
	private static Sprite createColonySprite(Color color, int squareSize){
		BufferedImage img = new BufferedImage(squareSize/2, squareSize/2, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.createGraphics();
		g.setColor(color);
		g.fillOval(squareSize/60, squareSize/60, squareSize/2 + squareSize%2 - squareSize/30 - 1, squareSize/2 + squareSize%2 - squareSize/30 - 1);
		return new Sprite(img, 0.5f, 0);
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
			for (Sprite sprite : paths.get(pos)) {
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