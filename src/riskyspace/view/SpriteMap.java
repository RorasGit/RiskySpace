package riskyspace.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import riskyspace.model.Planet;
import riskyspace.model.Player;
import riskyspace.model.Position;
import riskyspace.model.Resource;
import riskyspace.model.ShipType;
import riskyspace.model.Territory;
import riskyspace.model.World;

/**
 * 
 * @author flygarn
 * 
 */
public class SpriteMap {
	/*
	 * Planet Textures
	 */
	private static Map<Integer, Image> metalplanets = new HashMap<Integer, Image>();
	private static Map<Integer, Image> gasplanets = new HashMap<Integer, Image>();
	/*
	 * Ship Textures
	 */
	private static Map<String, Image> shipTextures = new HashMap<String, Image>();
	
	private Map<Position, Sprite> fogOfWar = new HashMap<Position, Sprite>();
	private Map<Position, Sprite> colonies = new HashMap<Position, Sprite>();
	private Map<Position, Sprite> paths = new HashMap<Position, Sprite>();
	private Map<Position, Sprite> fleets = new HashMap<Position, Sprite>();
	private Map<Position, Sprite> colonizers = new HashMap<Position, Sprite>();
	
	private Map<Position, Sprite> planets = new HashMap<Position, Sprite>();
	private static Map<Position, Sprite> allPlanets = new HashMap<Position, Sprite>();
	
	private static World world = null;
	
	public static void init(World world) {
		SpriteMap.world = world;
		setTextures();
		setPlanetSprites();
	}
	
	public static void setPlanetSprites() {
		for (Position pos : world.getContentPositions()) {
			if (world.getTerritory(pos).hasPlanet()) {
				Planet p = world.getTerritory(pos).getPlanet();
				if (p.getType() == Resource.METAL) {
					int index = (int) (Math.random()*4);
					allPlanets.put(pos, new Sprite(metalplanets.get(index), 0.5f, 0, 0.5f));
				} else if (p.getType() == Resource.GAS) {
					int index = (int) (Math.random()*3);
					allPlanets.put(pos, new Sprite(gasplanets.get(index), 0.5f, 0, 0.5f));
				}
			}
		}
	}
	
	public static SpriteMap getSprites(Player player) {
		SpriteMap map = new SpriteMap();
		for (Position pos : world.getContentPositions()) {
			Territory terr = world.getTerritory(pos);
			if (terr.hasColony()) {
				BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
				Graphics g = img.createGraphics();
				g.setColor(terr.getColony().getOwner() == Player.BLUE ? Color.BLUE : Color.RED);
				g.fillOval(5, 5, 90, 90);
				map.colonies.put(pos, new Sprite(img, 0.5f, 0, 0.5f));
			}
			if (terr.hasPlanet()) {
				map.planets.put(pos, allPlanets.get(pos));
			}
			if (terr.hasColonizer()) {
				map.colonizers.put(pos, new Sprite(shipTextures.get("COLONIZER_" + terr.controlledBy()), 0.5f, 0.5f, 0.5f));
			}
			if (terr.hasFleet()) {
				if (terr.getFleetsFlagships() != ShipType.COLONIZER) {
					map.fleets.put(pos, new Sprite(shipTextures.get(terr.getFleetsFlagships() + "_" + terr.controlledBy()), 0, 0.5f, 0.5f));
				}
			}
		}
		return map;
	}
	
	private static void setTextures() {
		/*
		 * Planets
		 */
		metalplanets.put(0, Toolkit.getDefaultToolkit().getImage("res/icons/planets/metalplanet_0.png"));
		metalplanets.put(1, Toolkit.getDefaultToolkit().getImage("res/icons/planets/metalplanet_1.png"));
		metalplanets.put(2, Toolkit.getDefaultToolkit().getImage("res/icons/planets/metalplanet_2.png"));
		metalplanets.put(3, Toolkit.getDefaultToolkit().getImage("res/icons/planets/metalplanet_3.png"));
		gasplanets.put(0, Toolkit.getDefaultToolkit().getImage("res/icons/planets/gasplanet_0.png"));
		gasplanets.put(1, Toolkit.getDefaultToolkit().getImage("res/icons/planets/gasplanet_1.png"));
		gasplanets.put(2, Toolkit.getDefaultToolkit().getImage("res/icons/planets/gasplanet_2.png"));
		
		/*
		 * Ships Blue Player
		 */
		shipTextures.put("SCOUT_BLUE", Toolkit.getDefaultToolkit().getImage("res/icons/blue/scout.png"));
		shipTextures.put("HUNTER_BLUE", Toolkit.getDefaultToolkit().getImage("res/icons/blue/hunter.png"));
		shipTextures.put("DESTROYER_BLUE", Toolkit.getDefaultToolkit().getImage("res/icons/blue/destroyer.png"));
		shipTextures.put("COLONIZER_BLUE", Toolkit.getDefaultToolkit().getImage("res/icons/blue/colonizer.png"));
		
		/*
		 * Ships Red Player
		 */
		shipTextures.put("SCOUT_RED", Toolkit.getDefaultToolkit().getImage("res/icons/red/scout.png"));
		shipTextures.put("HUNTER_RED", Toolkit.getDefaultToolkit().getImage("res/icons/red/hunter.png"));
		shipTextures.put("DESTROYER_RED", Toolkit.getDefaultToolkit().getImage("res/icons/red/destroyer.png"));
		shipTextures.put("COLONIZER_RED", Toolkit.getDefaultToolkit().getImage("res/icons/red/colonizer.png"));
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
		for (Position pos : fleets.keySet()) {
			fleets.get(pos).draw(g, calcX(pos, offsetX, squareSize), calcY(pos, offsetY, squareSize), squareSize);
		}
		for (Position pos : colonizers.keySet()) {
			colonizers.get(pos).draw(g, calcX(pos, offsetX, squareSize), calcY(pos, offsetY, squareSize), squareSize);
		}
	}

	private int calcX(Position pos, int offsetX, int squareSize) {
		return (pos.getCol()-1 + offsetX)*squareSize;
	}

	private int calcY(Position pos, int offsetY, int squareSize) {
		return (pos.getRow()-1 + offsetY)*squareSize;
	}
}