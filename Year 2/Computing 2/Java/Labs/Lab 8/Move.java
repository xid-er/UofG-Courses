package monster;

import java.util.Objects;

/**
 * Represents a single move in a monster battling game.
 * 
 * JP2 Lab 8, 2020.
 * 
 * @author Mary Ellen Foster <MaryEllen.Foster@glasgow.ac.uk>
 * @author Karlis Siders <2467273S@student.gla.ac.uk>
 */

public class Move implements TypedItem {

	/** The move type */
	private Type type;
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
	public Move(String name, Type type, int power) throws IllegalArgumentException {
		if (power < 0 || power > 180) {
			throw new IllegalArgumentException("Power out of range: " + power);
		}
		this.type = type;
		this.name = name;
		this.power = power;
	}

	/**
	 * @return The type (will always be a one-element array)
	 */
	@Override
	public Type[] getTypes() {
		return new Type[] { this.type };
	}

	/**
	 * Checks whether this Move has the given type.
	 */
	@Override
	public boolean hasType(Type type) {
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

	/**
	 * Computes a hash code based on name, power, and type
	 */
	@Override
	public int hashCode() {
		return Objects.hash(name, power, type);
	}

	/**
	 * Computes equality based on name, power, and type
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Move)) {
			return false;
		}
		Move other = (Move) obj;
		return Objects.equals(name, other.name) && power == other.power && Objects.equals(type, other.type);
	}

}
