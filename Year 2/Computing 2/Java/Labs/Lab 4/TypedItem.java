/**
 * Interface for 5 types and their effectiveness
 * 
 * Lab 4, 2020
 * 
 * @author Karlis Siders <2467273s@student.gla.ac.uk>
 */
public interface TypedItem {
	String[] types = { "Normal", "Fire", "Water", "Electric", "Grass" };
	double[][] effects = { { 1.0, 1.0, 1.0, 1.0, 1.0 }, { 1.0, 0.5, 0.5, 1.0, 2.0 }, { 1.0, 2.0, 0.5, 1.0, 0.5 },
			{ 1.0, 1.0, 2.0, 0.5, 0.5 }, { 1.0, 0.5, 2.0, 1.0, 0.5 } };

	boolean hasType(String type);

	String[] getTypes();

	/**
	 * Checks validity of a given type.
	 * 
	 * @param A type
	 * @return True for 5 types and false for anything else.
	 */
	static boolean isValidType(String type) {
		return (type != null && (type.equals("Normal") || type.equals("Fire") || type.equals("Water")
				|| type.equals("Grass") || type.equals("Electric")));
	}

	/**
	 * Finds type effectiveness for a given attacking move type against a given
	 * defending type.
	 * 
	 * @param Attacking type
	 * @param The       defending type
	 * @return The effectiveness of the attacking type
	 */
	static double getEffectiveness(String attackType, String defendType) {
		int attackInd = -1;
		int defendInd = -1;
		for (int i = 0; i < types.length; i++) {
			if (types[i].equals(attackType)) {
				attackInd = i;
			}
			if (types[i].equals(defendType)) {
				defendInd = i;
			}
		}
		return effects[attackInd][defendInd];
	}
}
