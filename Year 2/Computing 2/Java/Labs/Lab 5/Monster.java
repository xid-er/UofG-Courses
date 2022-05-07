package monster;
import java.util.Arrays;

/**
 * Represents a monster in a monster battling game. A monster has a name,
 * one or two types, and up to four moves.
 * 
 * JP2 Lab 5, 2020.
 * 
 * @author Mary Ellen Foster <MaryEllen.Foster@glasgow.ac.uk>
 * @author Karlis Siders <2467273S@student.gla.ac.uk>
 */
public class Monster implements TypedItem, Comparable<Monster> {
	
	/** The Monster's name */
	private String name;
	/** The Monster's types */
	private String[] types;
	/** The moves currently available to this Monster */
	private Move[] moves;

	private double hitPoints;
	/**
	 * Creates a new Monster with the given name and a single type.
	 * 
	 * @param name The name to use
	 * @param type The type to use
	 */
	public Monster(String name, String type, double hitPoints) throws IllegalArgumentException {
		this(name, new String[] { type }, hitPoints);
	}
	
	/**
	 * Creates a new Monster with the given name and the two given types.
	 * 
	 * @param name The name to use
	 * @param type1 The first type to use
	 * @param type2 The second type to use
	 */
	public Monster(String name, String type1, String type2, double hitPoints) throws IllegalArgumentException {
		this(name, new String[] { type1, type2 }, hitPoints);
	}
	
	/**
	 * Private constructor -- used to create a Monster with one or two types.
	 * 
	 * @param name The name to use
	 * @param types The types to use
	 */
	private Monster(String name, String[] types, double hitPoints) throws IllegalArgumentException {
		this.name = name;
		if (types.length == 2 && types[0].equals(types[1])) {
			throw new IllegalArgumentException("Duplicate type");
		}
		for (String type : types) {
			if (!TypedItem.isValidType(type)) {
				throw new IllegalArgumentException("Invalid type: " + type);
			}
		}
		if (hitPoints < 0) {
			throw new IllegalArgumentException("Hit points must be non-negative.");
		}
		this.types = types;
		this.hitPoints = hitPoints;
		this.moves = new Move[4];
	}
	
	/**
	 * @return The monster's name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * @return The monster's hit points
	 */
	public double getHP() {
		return this.hitPoints;
	}
	/**
	 * Checks whether the given type is one of the types of this monster.
	 * 
	 * @param type The type to check
	 * @return True if the given type is one of this monster's types, and false if not
	 */
	@Override
	public boolean hasType (String type) {
		for (String t : types) {
			if (t.equals(type)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the type(s) of this Monster.
	 * 
	 * @return The type(s)
	 */
	@Override
	public String[] getTypes() {
		return types;
	}

	
	/**
	 * Returns the Move at the given position in this monster's list.
	 * 
	 * @param index The index to use (must be between 0 and 3)
	 * @return The Move at that position, or null if there is no such move
	 */
	public Move getMove(int index) {
		try {
			return moves[index];
		} catch (ArrayIndexOutOfBoundsException ex) {
			throw new IllegalArgumentException("Index out of range: " + index);
		}
	}
	
	/**
	 * Updates the Move at the given position in this monster's list.
	 * 
	 * @param index The index to use (must be between 0 and 3)
	 * @param move The move to store in that position
	 */
	public void setMove (int index, Move move) {
		try {
			moves[index] = move;
		} catch (ArrayIndexOutOfBoundsException ex) {
			throw new IllegalArgumentException("Index out of range: " + index);
		}
	}
	
	/**
	 * Returns a well formatted string representing this Monster.
	 */
	public String toString() {
		return name + " " + Arrays.toString(types);
	}
	
	/**
	 * Computes the effective power of the given Move when used against the given Monster,
	 * incorporating type effectiveness computations.
	 * 
	 * @param move The move to consider
	 * @param defender The defending Monster
	 * @return The effective power
	 * @see TypedItem#getEffectiveness(String, String)
	 */
	public double getEffectivePower (Move move, Monster defender) {
		String moveType = move.getTypes()[0];
		
		// Incorporate effectiveness from all defender types
		double effectiveness = 1.0;
		for (String type : defender.getTypes()) {
			effectiveness *= TypedItem.getEffectiveness(moveType, type);
		}

		// Check for STAB
		if (this.hasType(moveType)) {
			effectiveness *= 1.5;
		}
		
		return effectiveness * move.getPower();
	}
	
	/**
	 * Chooses the most effective Move to use against the given defender.
	 * 
	 * @param defender The defending Monster
	 * @return The most effective Move to use, or null if this Monster has no Moves
	 */
	public Move chooseMove (Monster defender) {
		// Default values
		Move bestMove = null;
		double bestPower = 0;

		for (Move move : this.moves) {
			if (move != null) {
				double power = getEffectivePower(move, defender);
				if (bestMove == null || power > bestPower) {
					bestMove = move;
					bestPower = power;
				}
			}
		}
		return bestMove;
	}
	
	/**
	 * @return true if Monster's hit points have reached 0 and false otherwise
	 */

	public boolean isFainted() {
		return hitPoints == 0;
	}
	
	/**
	 * Removes hit points from the Monster under attack
	 * 
	 * @param damage The damage done to the Monster
	 */
	
	public void removeHP(double damage) {
		this.hitPoints -= damage;
		if (this.hitPoints < 0) {
			this.hitPoints = 0;
		}
	}
	
	/**
	 * Does damage to (takes away hit points from) defending Monster with the
	 * most effective Move 
	 * 
	 * @param defender The defending Monster
	 */
	public void attack(Monster defender) {
		Move atkMove = this.chooseMove(defender);
		double atkDMG = this.getEffectivePower(atkMove, defender);
		defender.removeHP(atkDMG);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(moves);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + Arrays.hashCode(types);
		return result;
	}

	/**
	 * Makes Monsters with same name and types equal
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Monster other = (Monster) obj;
		if (!Arrays.equals(moves, other.moves) ||
			(name == null && other.name != null) ||
			(name != null && !name.equals(other.name)) ||
			!Arrays.equals(types, other.types))
			return false;
		return true;
	}
	
	/**
	 * Compares Monsters with respect to hit points: more HP -> first
	 * 
	 * @param other The Monster being compared to
	 * @return negative if greater HP, 0 if equal HP, positive if less HP
	 */
	public int compareTo(Monster other) {
		return Double.compare(other.getHP(), this.hitPoints);
	}
}
