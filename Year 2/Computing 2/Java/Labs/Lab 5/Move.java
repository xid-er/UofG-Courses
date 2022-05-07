package monster;

/**
 * Represents a single move in a monster battling game.
 * 
 * JP2 Lab 5, 2020.
 * 
 * @author Mary Ellen Foster <MaryEllen.Foster@glasgow.ac.uk>
 * @author Karlis Siders <2467273S@student.gla.ac.uk>
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
	 * @param name The name to use
	 * @param type The type to use
	 * @param power The power to use
	 * @throws IllegalArgumentException if the type is invalid or the power is out of range
	 */
	public Move(String name, String type, int power) throws IllegalArgumentException {
		if (power < 0 || power > 180) {
			throw new IllegalArgumentException("Power out of range: " + power);
		}
		if (!TypedItem.isValidType(type)) {
			throw new IllegalArgumentException("Unknown type: " + type);
		}
		this.type = type;
		this.name = name;
		this.power = power;
	}

	/**
	 * @return The type (will always be a one-element array)
	 */
	@Override
	public String[] getTypes() {
		return new String[] { this.type };
	}
	
	/**
	 * Checks whether this Move has the given type.
	 */
	@Override
	public boolean hasType(String type) {
		return this.type.equals(type);
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
	
	/**
	 * Returns a well formatted string representing this Move.
	 */
	public String toString() {
		return name + " (" + type + "): " + power;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + power;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	/**
	 * Makes Moves with same name, type and power equal
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Move other = (Move) obj;
		if ((name == null && other.name != null) ||
			(name != null && !name.equals(other.name)) ||
			power != other.power ||
			(type == null && other.type != null) ||
			(type != null && !type.equals(other.type))) {
			return false;
		}
		return true;
	}

	
}
