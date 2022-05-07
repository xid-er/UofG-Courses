package monster;

/**
 * Represents a trainer in a monster battling game. A trainer has a name and a
 * set of monsters.
 * 
 * JP2 Lab 5, 2020.
 * 
 * @author Karlis Siders <2467273S@student.gla.ac.uk>
 */
import java.util.HashSet;
import java.util.Set;

public class Trainer {
	private String name;
	private Set<Monster> monsters = new HashSet<>();
	
	/**
	 * Creates a new Trainer with the given parameter.
	 * @param name The name to use
	 */
	public Trainer(String name) {
		this.name = name;
	}

	/**
	 * @return the Trainer's name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Returns a well formatted string representing this Trainer.
	 */
	@Override
	public String toString() {
		return "Rocket grunt " + this.name;
	}
	
	/**
	 * Adds a new Monster to the Trainer's set of Monsters.
	 * 
	 * @param monster The Monster to add
	 * @return true if the Monster is new to the set, false otherwise
	 */
	public boolean addMonster(Monster monster) {
		return this.monsters.add(monster);
	}
	
	/**
	 * Removes a Monster from the Trainer's set of Monsters.
	 * 
	 * @param monster The Monster to remove
	 * @return true if the Monster was a monster to remove, false otherwise
	 */
	public boolean removeMonster(Monster monster) {
		return this.monsters.remove(monster);
	}
	
	/**
	 * Chooses a Monster from the Trainer's set with the most HP.
	 * 
	 * @return a non-fainted Monster with the most HP, or null if all have fainted
	 */
	public Monster chooseBattleMonster() {
		for(Monster mon : this.monsters) {
			if(!mon.isFainted()) {
				return mon;
			}
		}
		return null;
	}
	
	/**
	 * Simulates a battle between 2 Trainers.
	 * 
	 * @param trainer1 The first Trainer (attacker)
	 * @param trainer2 The second Trainer (defender)
	 * @return winner
	 */
	public static Trainer doBattle(Trainer trainer1, Trainer trainer2) {
		Trainer attacker = trainer1;
		Trainer defender = trainer2;
		
		boolean end = false;
		while (! end) {
			Monster atkMon = attacker.chooseBattleMonster();
			if (atkMon == null) {
				return defender;
			}
			
			Monster defMon = defender.chooseBattleMonster();
			if (defMon == null) {
				return attacker;
			}
			
			atkMon.attack(defMon);
			
			Trainer temp = attacker;
			attacker = defender;
			defender = temp;
		}
		return null;
	}
}
