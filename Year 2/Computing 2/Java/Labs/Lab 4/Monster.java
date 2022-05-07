import java.util.Arrays;

/**
 * Represents a monster in a monster battling game. A monster has a name, one or
 * two types, and up to four moves.
 * 
 * Lab 4, 2020
 * 
 * @author Mary Ellen Foster <MaryEllen.Foster@glasgow.ac.uk>
 * @author Karlis Siders <2467273s@student.gla.ac.uk>
 */
public class Monster implements TypedItem {

	/** The Monster's name */
	private String name;
	/** The Monster's types */
	private String[] types;
	/** The moves currently available to this Monster */
	private Move[] moves;

	/**
	 * Creates a new Monster with the given name and a single type.
	 * 
	 * @param name The name to use
	 * @param type The type to use
	 */
	public Monster(String name, String type) throws IllegalArgumentException {
		this(name, new String[] { type });
	}

	/**
	 * Creates a new Monster with the given name and the two (different) given
	 * types.
	 * 
	 * @param name  The name to use
	 * @param type1 The first type to use
	 * @param type2 The second type to use
	 */
	public Monster(String name, String type1, String type2) throws IllegalArgumentException {
		this(name, new String[] { type1, type2 });
	}

	/**
	 * Private constructor -- used to create a Monster with one or two types.
	 * 
	 * @param name  The name to use
	 * @param types The types to use
	 */
	private Monster(String name, String[] types) throws IllegalArgumentException{
		if (!TypedItem.isValidType(types[0])) {
			throw new IllegalArgumentException("Type " + types[0] + " not known.");
		} else if (types.length > 1 && !TypedItem.isValidType(types[1])) {
			throw new IllegalArgumentException("Type " + types[1] + " not known.");
		} else if (types.length > 1 && types[0].equals(types[1])) {
			throw new IllegalArgumentException("Types should not be the same.");
		} else {
			this.name = name;
			this.types = types;
			this.moves = new Move[4];
		}
	}

	/**
	 * @return The monster's name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Checks whether the given type is one of the types of this monster.
	 * 
	 * @param type The type to check
	 * @return True if the given type is one of this monster's types, and false if
	 *         not
	 */
	public boolean hasType(String type) {
		for (String t : types) {
			if (t == type) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return The monster's types
	 */
	public String[] getTypes() {
		return this.types;
	}

	/**
	 * Returns the Move at the given position in this monster's list.
	 * 
	 * @param index The index to use (must be between 0 and 3)
	 * @return The Move at that position, or null if there is no such move
	 */
	public Move getMove(int index) throws IllegalArgumentException {
		if (index >= 0 && index <= 3) {
			return moves[index];
		} else {
			throw new IllegalArgumentException("Index should be between 0 and 3.");
		}
	}

	/**
	 * Updates the Move at the given position in this monster's list.
	 * 
	 * @param index The index to use (must be between 0 and 3)
	 * @param move  The move to store in that position
	 */
	public void setMove(int index, Move move) throws IllegalArgumentException {
		if (index >= 0 && index <= 3) {
			moves[index] = move;
		} else {
			throw new IllegalArgumentException("Index should be between 0 and 3.");
		}
	}

	/**
	 * @return a well formatted string representing this Monster.
	 */
	public String toString() {
		return name + " " + Arrays.toString(types);
	}

	/**
	 * Calculates effective power based on original power, effectiveness multiplier
	 * and same-type multiplier.
	 * 
	 * @param The move being used
	 * @param The monster being attacked
	 * @return The effective power
	 */
	public double getEffectivePower(Move move, Monster defender) {
		double power = move.getPower();
		String[] defTypes = defender.getTypes();
		String moveType = move.getTypes()[0];
		for (String t : defTypes) {
			power *= TypedItem.getEffectiveness(moveType, t);
		}
		for (String t : this.types) {
			if (moveType.equals(t)) {
				power *= 1.5;
			}
		}
		return power;
	}

	/**
	 * Calculates most effective move by checking effectiveness of each move
	 * 
	 * @param The monster being attacked
	 * @return The attacking monster's strongest move against the defender
	 */
	public Move chooseMove(Monster defender) {
		int maxIx = 0;
		double maxEff = 0;
		for (int i = 0; i < this.moves.length; i++) {
			if (this.moves[i] != null) {
				double temp = this.getEffectivePower(this.moves[i], defender);
				if (temp > maxEff) {
					maxEff = temp;
					maxIx = i;
				}
			}
		}
		return this.moves[maxIx];
	}
}
