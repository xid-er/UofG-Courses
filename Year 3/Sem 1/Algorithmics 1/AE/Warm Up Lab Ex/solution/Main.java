import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {	
	
	/**
	 * @param args
	 * args[0] is the name of the input file
	 */
	public static void main(String[] args) throws IOException {
		
		Trie t = new Trie(); // trie to be constructed
		
		// open the file to read
		FileReader reader = new FileReader(args[0]);
		Scanner in = new Scanner(reader); 
		
		// read words in the file
		while (in.hasNext()) {
			
			// read one line at a time
			String nl = in.nextLine();
			Scanner stringIn = new Scanner(nl);
			Pattern p = Pattern.compile("[^a-zA-Z]"); // pattern for words
			stringIn.useDelimiter(p);
			while (stringIn.hasNext()) {
				// add word to trie
				//t.insert(stringIn.next().toLowerCase());
				t.insertLex(stringIn.next().toLowerCase());
			}
		}
		
		// now use your generated function from task 1 to display the words
		display(t.extract());
	
	}

	/**
	 * displays the elements of an linked list
	 * followed by the size of the list
	 */
	public static void display(LinkedList<String> a){
		for (String s : a) System.out.println(s); // prints list of words in array
		System.out.println("Total number of words = " + a.size() + "\n"); // prints number of words in the array
	}
	
}
