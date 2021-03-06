package huffman;

import java.io.*;
import java.util.*;

/** program to find compression ratio using Huffman coding
 */

class WeightComparator implements Comparator<Node> {
    public int compare(Node first, Node second)
    {
        return first.weight - second.weight;
    }
}
public class Main {

	public static void main(String[] args) throws IOException {

		long start = System.currentTimeMillis();
		String inputFileName = args[0];
		FileReader reader = new FileReader(inputFileName);
		Scanner in = new Scanner(reader);
		
		// read in the data
		HashMap<Character, Integer> frequencies = new HashMap<Character, Integer>();
		
		int ogLength = 0;
		while(in.hasNextLine()) {
			// Get whole line and newline character
			String line = in.nextLine() + '\n';
			//Go char by char
			for(int i = 0; i < line.length(); i++) {
				char character = line.charAt(i);
				if (frequencies.get(character) == null) frequencies.put(character, 0);
				frequencies.put(character, frequencies.get(character) + 1);
				ogLength++;
			}
		}
		
	// Build Huffman tree
		
		// Add all parentless nodes
		PriorityQueue<Node> parentless_nodes = new PriorityQueue<Node>(frequencies.size(), new WeightComparator());
		for(Map.Entry<Character, Integer> entry : frequencies.entrySet()) {
			Node node = new Node(entry.getKey(), entry.getValue());
			parentless_nodes.add(node);
		}
		
		// add internal nodes, keeping in mind the weighted path length (WPL)
		int wpl = 0;
		while (parentless_nodes.size() > 1) {
			Node x = parentless_nodes.poll();
			Node y = parentless_nodes.poll();
			// Parent node's weight is sum of both children's weights
			int sum = x.getWeight() + y.getWeight();
			Node parent = new Node(' ', sum, x, y);
			parentless_nodes.add(parent);
			wpl += sum;
		}
		
		int compressedLength = wpl;
		
		int ogLengthInBits = ogLength * 8;
		float ratio = (float) compressedLength / ogLengthInBits;

		reader.close();
		in.close();
		
		// output the results here
		System.out.println("Original file length in bits = " + ogLengthInBits);
		System.out.println("Compressed file length in bits = " + compressedLength);
		System.out.println("Compression ratio = " + ratio);
		// end timer and print elapsed time as last line of output
		long end = System.currentTimeMillis();
		System.out.println("Elapsed time: " + (end - start) + " milliseconds");
	}

}
