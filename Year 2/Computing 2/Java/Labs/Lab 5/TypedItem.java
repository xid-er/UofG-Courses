package monster;
/**
 * Represents an item that may have one or more Types in the battling game. Also provides
 * static methods for checking for type validity and effectiveness.
 * 
 * Model solution to JP2 Lab 4, 2020.
 * 
 * @author Mary Ellen Foster <MaryEllen.Foster@glasgow.ac.uk>
 */

public interface TypedItem {
	/**
	 * Checks whether this TypedItem has a given type.
	 * 
	 * @param type The type to check
	 * @return True if this item has the given type, and false if not
	 */
	public boolean hasType (String type);
	
	/**
	 * Returns all types that this TypedItem has.
	 * 
	 * @return A list of all types of this item
	 */
	public String[] getTypes();
	
	/**
	 * Checks whether a given String represents a valid type.
	 * @param type The type to check
	 * @return True if it is valid, and false if it is not
	 */
	public static boolean isValidType (String type) {
		// The switch will crash on a null string
		if (type == null) return false;
		
		switch (type) {
		case "Normal":
		case "Fire":
		case "Water":
		case "Electric":
		case "Grass":
			return true;
		}
		return false;
	}
	
	/** Constant indicating not-very effectiveness */
	public static final double NOT_EFFECTIVE = 0.5;
	/** Constant indicating super effectiveness */
	public static final double SUPER_EFFECTIVE = 2.0;
	/** Constant indicating regular effectiveness */
	public static final double NORMAL_EFFECTIVE = 1.0;
	
	/**
	 * Checks the effectiveness of the given attacking type against the given defending type.
	 * 
	 * @param attackType The attacking type
	 * @param defendType The defending type
	 * @return One of NOT_EFFECTIVE, SUPER_EFFECTIVE, NORMAL_EFFECTIVE
	 */
	public static double getEffectiveness (String attackType, String defendType) {
		// Default value
		double effectiveness = NORMAL_EFFECTIVE;
		
		// Only check the ones that differ from normal
		switch (attackType) {
		case "Fire":
		 	if (defendType.equals("Fire") || defendType.equals("Water")) {
		 		effectiveness = NOT_EFFECTIVE;
			} else if (defendType.equals("Grass")) {
				effectiveness = SUPER_EFFECTIVE;
			}
		 	break;
			
		case "Water":
		 	if (defendType.equals("Water") || defendType.equals("Grass")) {
				effectiveness = NOT_EFFECTIVE;
			} else if (defendType.equals("Fire")) {
				effectiveness = SUPER_EFFECTIVE;
			}
		 	break;
			
		case "Electric":
		 	if (defendType.equals("Electric") || defendType.equals("Grass")) {
				effectiveness = NOT_EFFECTIVE;
			} else if (defendType.equals("Water")) {
				effectiveness = SUPER_EFFECTIVE;
			}
		 	break;
			
		case "Grass":
		 	if (defendType.equals("Fire") || defendType.equals("Grass")) {
				effectiveness = NOT_EFFECTIVE;
			} else if (defendType.equals("Water")) {
				effectiveness = SUPER_EFFECTIVE;
			}
		 	break;
		}

		return effectiveness;
	}
}
