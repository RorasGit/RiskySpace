package riskyspace.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Utility class containing possible Colony Names
 * @author Alexander
 *
 */
public class ColonyNames {

	/*
	 * Colony Names for Players, First Name is the Home Planet
	 */
	private static final String[] BLUE_NAMES = {"Atlantis", "Kuati", "Dancha", "Lyna", "New Arkos", "Wolda", "Liosia",
		"Belga", "Dooine", "Arrell", "Tineria", "Arvon", "New Galia", "Ulan", "Bespia", "Old Atholl", "Lusia Valley",
		"Hephaestus", "Solovei", "Midgard", "Ahemait", "Kalia", "Jemhee", "Cellia", "Libuscha"};
	
	private static final String[] RED_NAMES = {"Jafnharr", "Raxahoone", "Traga", "Mara", "Musa", "Drisi", "Anziss",
		"Gandra", "Kora", "Cara", "Seoni", "Hydrie", "Parcaya", "Khorwirrn", "Membi", "Acrav Ama", "Sharrhi",
		"Arzehn", "Roreras", "Liluri", "Lachesis", "Stheno", "Alkonost", "Gorynich", "Charon"};
	
	private static final String[] GREEN_NAMES = {"Zuli City", "M'Voga", "N'Forma", "B'Miri", "A'Vorcia", "Jn'Horkyo",
		"Lani Mini", "Nera Thractu", "H'Ticia", "G'Byorme", "K'Trone", "C'Kani", "Lahmu", "Mule Xani", "D'Gnucta",
		"M'Essic", "Nepththys", "Volantis", "Zoidi", "Bgztlara"};
	
	private static final String[] PINK_NAMES = {"Hizo Station", "Vorta Terraform", "Lurash Complex", "Arnos City",
		"Keni VII",	"New Oberon", "Pictoris Prime",	"Tartarus", "Ancalagon IV", "Oceanus V", "New Sedna", "New Thyoph",
		"Tauri II", "Calou Prime", "Solarii V", "Alkonost", "New Triton", "Tenctai Station", "City of Idund", "Denaii Prime"};
	
	private static Map<Player, String[]> names = new HashMap<Player, String[]>();
	private static List<String> usedNames = new ArrayList<String>();
	
	private static Random random = new Random();
	
	public static String getName(Player player) {
		if (names.isEmpty()) {
			names.put(Player.BLUE, BLUE_NAMES);
			names.put(Player.RED, RED_NAMES);
			names.put(Player.GREEN, GREEN_NAMES);
			names.put(Player.PINK, PINK_NAMES);
		}
		String name = names.get(player)[0];
		while (usedNames.contains(name)) {
			int rand = Math.abs(random.nextInt() % names.get(player).length);
			name = names.get(player)[rand];
		}
		usedNames.add(name);
		return name;
	}

}