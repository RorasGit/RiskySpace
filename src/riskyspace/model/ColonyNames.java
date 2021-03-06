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
	 * Colony Names For Players, First Name is the Home Planet
	 */
	private static final String[] BLUE_NAMES = {"Atlania", "Kuati", "Dancha", "Lyna", "New Arkos", "Wolda", "Liosia",
		"Belga", "Dooine", "Arrell", "Tineria", "Arvon", "New Galia", "Ulan", "Bespia", "Old Atholl", "Lusia Valley",
		"Hephasteus", "Old Solovei", "Midgard", "Ghazoi Central", "Kalia", "Jemhee", "Cellia", "Libuscha", 
		"Rappia", "Flygha"};
	
	private static final String[] RED_NAMES = {"Zagra","JaFnharr", "New Raxam", "Traga", "Mara", "Muzah", "Drisi", "Aziss",
		"Gandra", "Korai", "Caranis", "Seoni", "Hydrea", "Paraya", "Khorwin", "Membi", "Acrav Ama", "Sharrhi",
		"Narzeya", "Roreras", "Liluri", "Lachesis", "Zheno", "Z3-B0", "Gorynich", "Charon", "Fuuh"};
	
	private static final String[] GREEN_NAMES = {"Zuli City", "Ma'Voga", "Ni'Forma", "Ba'Miri", "Ai'Vorka", "Jin'Horyo",
		"Lani Mini", "Nera Thractu", "He'Ticia", "Gia Borme", "Kitrone", "Ce'Kani", "Lahmu", "Molo Xani", "Digimonis",
		"Essia Province", "Nethys", "Volana", "Zoidi", "New X3"};
	
	private static final String[] YELLOW_NAMES = {"Hizou Station", "Vorta TerraForm", "Lurash Complex", "Arnos City",
		"Keni VII",	"New Oberon", "Pictoris Prime",	"Taurarus", "Ancalagon IV", "Oceanus V", "New Sedna", "New Thyoph",
		"Tauri II", "Calou Prime", "Solarii V", "Greater Wara", "New Triton", "Tenctai Station", "City oF Idund", "Denaii Prime"};
	
	private static Map<Player, String[]> names = new HashMap<Player, String[]>();
	private static List<String> usedNames = new ArrayList<String>();
	
	private static Random random = new Random();
	
	public static String getName(Player player) {
		if (names.isEmpty()) {
			names.put(Player.BLUE, BLUE_NAMES);
			names.put(Player.RED, RED_NAMES);
			names.put(Player.GREEN, GREEN_NAMES);
			names.put(Player.YELLOW, YELLOW_NAMES);
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