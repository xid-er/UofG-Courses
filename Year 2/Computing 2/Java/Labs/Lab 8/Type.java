package monster;

/**
 * Represents a type with 5 possible values and the effectiveness between them.
 * 
 * JP2 Lab 8, 2020.
 * 
 * @author Karlis Siders <2467273S@student.gla.ac.uk>
 *
 */

public enum Type {
	/** The 5 types */
	NORMAL, FIRE, WATER, ELECTRIC, GRASS;

	public static double NOT_EFFECTIVE = 0.5;
	public static double SUPER_EFFECTIVE = 2.0;
	public static double NORMAL_EFFECTIVE = 1.0;

	/**
	 * Returns the effectiveness of this Type given another Type.
	 * 
	 * @param otherType The Type being attacked
	 * @return The effectiveness
	 */
	public double getEffectiveness(Type otherType) {
		double effectiveness = NORMAL_EFFECTIVE;

		switch (this) {
		case FIRE:
			if (otherType == FIRE || otherType == WATER) {
				effectiveness = NOT_EFFECTIVE;
			} else if (otherType == GRASS) {
				effectiveness = SUPER_EFFECTIVE;
			}
			break;

		case WATER:
			if (otherType == WATER || otherType == GRASS) {
				effectiveness = NOT_EFFECTIVE;
			} else if (otherType == FIRE) {
				effectiveness = SUPER_EFFECTIVE;
			}
			break;

		case ELECTRIC:
			if (otherType == ELECTRIC || otherType == GRASS) {
				effectiveness = NOT_EFFECTIVE;
			} else if (otherType == WATER) {
				effectiveness = SUPER_EFFECTIVE;
			}
			break;

		case GRASS:
			if (otherType == GRASS || otherType == FIRE) {
				effectiveness = NOT_EFFECTIVE;
			} else if (otherType == WATER) {
				effectiveness = SUPER_EFFECTIVE;
			}
			break;
		}

		return effectiveness;
	}

}
