package monster;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a monster in a monster battling game. A monster has a name, one or
 * two types, and up to four moves.
 * 
 * JP2 Lab 8, 2020.
 * 
 * @author Mary Ellen Foster <MaryEllen.Foster@glasgow.ac.uk>
 * @author Karlis Siders <2467273S@student.gla.ac.uk>
 */
public class Monster implements TypedItem, Comparable<Monster> {

	/** The Monster's name */
	private String name;
	/** The Monster's types */
	private Type[] types;
	/** The moves currently available to this Monster */
	private Move[] moves;
	/** The Monster's current HP */
	private double hp;

	/**
	 * Creates a new Monster with the given name and a single type.
	 * 
	 * @param name The name to use
	 * @param type The type to use
	 */
	public Monster(String name, Type type, double hp) throws IllegalArgumentException {
		this(name, new Type[] { type }, hp);
	}

	/**
	 * Creates a new Monster with the given name and the two given types.
	 * 
	 * @param name  The name to use
	 * @param type1 The first type to use
	 * @param type2 The second type to use
	 */
	public Monster(String name, Type type1, Type type2, double hp) throws IllegalArgumentException {
		this(name, new Type[] { type1, type2 }, hp);
	}

	/**
	 * Private constructor -- used to create a Monster with one or two types.
	 * 
	 * @param name  The name to use
	 * @param types The types to use
	 */
	private Monster(String name, Type[] types, double hp) throws IllegalArgumentException {
		this.name = name;
		this.hp = hp;
		if (types.length == 2 && types[0].equals(types[1])) {
			throw new IllegalArgumentException("Duplicate type");
		}
		this.types = types;
		this.moves = new Move[4];
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
	public boolean hasType(Type type) {
		for (Type t : types) {
			if (t.equals(type)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the full set of Types of this Monster.
	 */
	@Override
	public Type[] getTypes() {
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
	 * @param move  The move to store in that position
	 */
	public void setMove(int index, Move move) {
		try {
			moves[index] = move;
		} catch (ArrayIndexOutOfBoundsException ex) {
			throw new IllegalArgumentException("Index out of range: " + index);
		}
	}

	/**
	 * Computes the effective power of the given Move when used against the given
	 * Monster, incorporating type effectiveness computations.
	 * 
	 * @param move     The move to consider
	 * @param defender The defending Monster
	 * @return The effective power
	 * @see TypedItem#getEffectiveness(String, String)
	 */
	public double getEffectivePower(Move move, Monster defender) {
		Type moveType = move.getTypes()[0];

		// Incorporate effectiveness from all defender types
		double effectiveness = 1.0;
		for (Type type : defender.getTypes()) {
			effectiveness *= moveType.getEffectiveness(type);
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
	public Move chooseMove(Monster defender) {
		// Default values
		Move bestMove = null;
		double bestPower = 0;

		for (Move move : moves) {
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
	 * Removes the given amount of HP from this Monster.
	 * 
	 * @param damage The HP to remove
	 */
	public void removeHP(double damage) {
		this.hp -= damage;
		if (this.hp < 0)
			this.hp = 0;
	}

	/**
	 * Checks whether this Monster can still battle -- that is, whether its HP is
	 * non-negative.
	 * 
	 * @return Whether this Monster can battle
	 */
	public boolean isFainted() {
		return this.hp <= 0;
	}

	/**
	 * Returns the current HP of this Monster.
	 * 
	 * @return The HP
	 */
	public double getHP() {
		return this.hp;
	}

	/**
	 * Simulates an attack by this Monster on the other one. Does nothing if either
	 * Monster is fainted; otherwise, removes the appropriate amount of HP from the
	 * defender.
	 * 
	 * @param defender The monster to attack
	 * @see #isFainted()
	 * @see #removeHP(double)
	 * @see #getEffectivePower(Move, Monster)
	 */
	public void attack(Monster defender) {
		if (this.isFainted() || defender.isFainted()) {
			// Invalid
			return;
		}
		Move bestMove = chooseMove(defender);
		if (bestMove != null) {
			defender.removeHP(getEffectivePower(bestMove, defender));
		}
	}

	/**
	 * Returns a well formatted string representing this Monster.
	 */
	public String toString() {
		return name + " " + Arrays.toString(types);
	}

	/**
	 * Computes a hash code based on the moves, type, and name.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(moves);
		result = prime * result + Arrays.hashCode(types);
		result = prime * result + Objects.hash(name);
		return result;
	}

	/**
	 * Computes equality based on the moves, type, and name.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Monster)) {
			return false;
		}
		Monster other = (Monster) obj;
		return Arrays.equals(moves, other.moves) && Objects.equals(name, other.name)
				&& Arrays.equals(types, other.types);
	}

	/**
	 * Creates a sort order based on decreasing HP.
	 */
	@Override
	public int compareTo(Monster o) {
		return Double.compare(o.hp, this.hp);
	}

}
