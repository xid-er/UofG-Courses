package monster;

/**
 * Interface for items with types.
 * 
 * JP2 Lab 8, 2020.
 * 
 * @author Mary Ellen Foster <MaryEllen.Foster@glasgow.ac.uk>
 * @author Karlis Siders <2467273S@student.gla.ac.uk>
 *
 */

public interface TypedItem {
	public boolean hasType(Type type);

	public Type[] getTypes();

}
