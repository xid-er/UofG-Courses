package lzw;

import java.io.*;
import java.util.*;

/** program to find compression ratio using LZW compression
 */
public class Main {

	public static void main(String[] args) throws IOException {

		long start = System.currentTimeMillis();
		String inputFileName = args[0];
		FileReader reader = new FileReader(inputFileName);
		Scanner in = new Scanner(reader);
		
		// read in the data and do the work here
        // read a line at a time to enable newlines to be detected and included in the compression
		Trie trieDic = new Trie();
		int dicSize = 0;
		int codewordLength = 8;
		int maxDicSize = (int) Math.pow(2, codewordLength);
		// Initialise with first 128 ASCII chars
		for (int i = 0; i < 128; i++) {
			trieDic.insert("" + (char) i, 8);
			dicSize++;
		}
		
		int ogLength = 0;
		int compressedLength = 0;
		
		String word = "";
		Node node = trieDic.getRoot();
		boolean inserted = false;
		// Go char by char
		in.useDelimiter("");
		while(in.hasNext()) {
			char current = in.next().charAt(0);
			word += current;
			
			if (!inserted) {
				// Search char from given node
				node = trieDic.search("" + current, node);
			} else {
				// Search word from root
				node = trieDic.search(word);
			}
			
			// If word not in dictionary
			if (node == null) {
				if (dicSize == maxDicSize) {
					codewordLength++;
					maxDicSize *= 2;
				}
				// TODO: Insert char as child of Node, don't search from root
				trieDic.insert(word, codewordLength);
				dicSize++;
				
				compressedLength += codewordLength;
				word = "" + current;
				inserted = true;
			} else {
				inserted = false;
			}
			ogLength++;
		}
		// Last word usually already in the dictionary
		if (word.length() > 0) {
			codewordLength = trieDic.search(word).getCodewordLen();
			compressedLength += codewordLength;
		}

		reader.close();
		in.close();
		
		int ogLengthInBits = ogLength * 8;
		float ratio = (float) compressedLength / ogLengthInBits;

		// output the results here
		System.out.println("Original file length in bits = " + ogLengthInBits);
		System.out.println("Compressed file length in bits = " + compressedLength);
		System.out.println("Compression ratio = " + ratio);
		// end timer and print elapsed time as last line of output
		long end = System.currentTimeMillis();
		System.out.println("Elapsed time: " + (end - start) + " milliseconds");
	}

}
