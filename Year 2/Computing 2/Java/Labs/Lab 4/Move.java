/**
 * Represents a single move in a monster battling game.
 * 
 * Lab 4, 2020
 * 
 * @author Mary Ellen Foster <MaryEllen.Foster@glasgow.ac.uk>
 * @author Karlis Siders <2467273s@student.gla.ac.uk>
 */

public class Move implements TypedItem {

	/** The move type */
	private String type;
	/** The move's name */
	private String name;
	/** The move's power */
	private int power;

	/**
	 * Creates a new Move with the given parameters
	 * 
	 * @param name  The name to use
	 * @param type  The type to use
	 * @param power The power to use
	 */
	public Move(String name, String type, int power) throws IllegalArgumentException {
		if (!TypedItem.isValidType(type)) {
			throw new IllegalArgumentException("Type " + type + " not known.");
		}
		if (power >= 0 && power <= 180) {
			this.name = name;
			this.power = power;
			this.type = type;
		} else {
			throw new IllegalArgumentException("Power must be between 0 and 180.");
		}
	}

	/**
	 * @return The type
	 */
	public String[] getTypes() {
		return new String[] { type };
	}

	/**
	 * @return The name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return The power
	 */
	public int getPower() {
		return this.power;
	}

	public boolean hasType(String type) {
		return this.type.equals(type);
	}

	/**
	 * Returns a well formatted string representing this Move.
	 */
	public String toString() {
		return name + " (" + type + "): " + power;
	}

}
