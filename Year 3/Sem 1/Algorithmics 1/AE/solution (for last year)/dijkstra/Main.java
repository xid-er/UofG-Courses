import java.io.*;
import java.util.*;

/**
 program to find word ladder with shortest distance for two words in a dictionary
 distance between elements of the word ladder is the absolute difference in the
 positions of the alphabet of the non-matching letter
 */
public class Main {

	public static void main(String[] args) throws IOException {

		String inputFileName = args[0]; // dictionary
		String word1 = args[1]; // first word
		String word2 = args[2]; // second word
		
		// set timer
		long start = System.currentTimeMillis();

        // read in the dictionary here
        // store dictionary as an list as do not know size in advance
		ArrayList<String> dictionary = new ArrayList<String>();
		
		FileReader reader = new FileReader(inputFileName);
		Scanner in = new Scanner(reader);

		// find index of words to test while building
		int w1 = -1; // index of first word (initially unknown)
		int w2 = -1; // index of second word (initially unknown)
		int index = 0; // keep track of the index of the vertex corresponding to each word
		
		while (in.hasNextLine()) { // not reached edd of the file
			String line = in.nextLine();
			dictionary.add(line);
			if (line.equals(word1)) w1=index;
			if (line.equals(word2)) w2=index;
			index++;	
		}

		reader.close();

		// do the work here

		// first check the words are in the dictionary
		if (w1 == -1 && w2 == -1) {
			System.out.println("both " + word1 + " and " + word2 + " not in dictionary");
		}
		else if (w1 == -1) {
			System.out.println(word1 + " not in dictionary");
		}
		else if (w2 == -1) {
			System.out.println(word2 + " not in dictionary");
		}
		// then check words are not the same
		else if (w1 == w2) {
			System.out.println("size of dictionary = " + dictionary.size());
			System.out.println("word1 = " + dictionary.get(w1));
			System.out.println("word2 = " + dictionary.get(w2));
			System.out.println("shortest word ladder of length 0");
			System.out.println("path with minimum distance:");
			System.out.println(dictionary.get(w1));
		}
        // now do the actual work
		else {
			System.out.println("size of dictionary = " + dictionary.size());
			System.out.println("word1 = " + dictionary.get(w1));
			System.out.println("word2 = " + dictionary.get(w2));
			
			// create the word ladder graph
			int numVertices = index;
			Graph G = new Graph(numVertices);
			
			// add word values to verties
			// and create edges when charDiff equals 1
			
			index = 0; // index of current vertex
			int weight; // weight of edges
			
			for (String word : dictionary) { // go through adjacency list
				G.getVertex(index).setWord(word); // set word value
				// create edges
				for (int j = index-1; j >= 0; j--){
					if (charDiff(word,G.getVertex(j).getWord()) == 1) {
						weight = edgeWeight(word,G.getVertex(j).getWord());
						G.getVertex(index).addToAdjList(j,weight);
						G.getVertex(j).addToAdjList(index,weight);
					}
				}
				index++;
			}
			
			// find the shortest weighted path between words 
			// (w1 and w2 index of corresponding vertices)
			G.findShortestPathDijkstra(w1,w2);
			
			// end timer and print total time
			long end = System.currentTimeMillis();
			System.out.println("elapsed time: " + (end - start) + " milliseconds");
		}
	}

    /* could "optimise" my combining the functions below so only loop 
    through the words once and stop as soon as the difference is >1 */
	
	/* returns number of different characters in two words
	 assumes the words are of length five */
	public static int charDiff(String w1 , String w2 ) {
		
		int diff = 0;
		
		for (int i = 0; i < 5; i++)
			if ( w1.charAt(i) != w2.charAt(i) ) diff++;
		
		return diff;
	}
	/* returns the weight between two words */
	public static int edgeWeight(String w1 , String w2 ) {
		
		int weight = 0;
		
		for (int i = 0; i < 5; i++)
			weight += Math.abs( w1.charAt(i) - w2.charAt(i) );
		
		return weight;
	}
}
