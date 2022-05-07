package monster;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a collection of monsters in a monster battling game.
 * 
 * JP2 Lab 8, 2020.
 * 
 * @author Karlis Siders <2467273S@student.gla.ac.uk>
 */

public final class MonsterCollection {
	/** The Monsters */
	private final Set<Monster> monsters;

	/**
	 * Creates a new MonsterCollection with the given Set of Monsters.
	 * 
	 * @param monsters The Set of Monsters
	 */
	public MonsterCollection(Set<Monster> monsters) {
		this.monsters = new HashSet<>(monsters);
	}

	/**
	 * Returns a copy of the Set of Monsters.
	 * 
	 * @return The Set of Monsters
	 */
	public Set<Monster> getMonsters() {
		return new HashSet<>(this.monsters);
	}

	/**
	 * Returns the first Monster that is not fainted.
	 * 
	 * @return The Monster
	 */
	public final Monster chooseBattleMonster() {
		return monsters.stream().filter(m -> !m.isFainted()).findAny().orElseGet(() -> null);
	}

	/**
	 * Returns the Monster with the highest HP.
	 * 
	 * @return The strongest Monster
	 */
	public final Monster getStrongestMonster() {
		return monsters.stream().sorted().findFirst().orElseGet(() -> null);
	}

	/**
	 * Returns the average HP of all Monsters in a Set.
	 * 
	 * @return The average HP
	 */
	public final double getAverageHP() {
		return monsters.stream().mapToDouble(m -> m.getHP()).average().orElseGet(() -> 0);
	}

	/**
	 * Returns a Set of Monsters of a given Type.
	 * 
	 * @param type Type
	 * @return The Set of Monsters
	 */
	public final Set<Monster> getMonstersOfType(Type type) {
		return new HashSet<Monster>(monsters.stream().filter(m -> m.hasType(type)).collect(Collectors.toSet()));
	}
}