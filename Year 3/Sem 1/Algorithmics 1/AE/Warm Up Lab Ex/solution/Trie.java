import java.util.LinkedList;

public class Trie {
	
	// create root of the trie
	private Node root; 
	
	public Trie() {
		// null character in the root
		root = new Node(Character.MIN_VALUE); 
	}
    
	// list to store the words in the trie when extract is called
    private LinkedList<String> words = new LinkedList<String>();

    // possible outcomes of a search
	private enum Outcomes {PRESENT, ABSENT, UNKNOWN}
	
	/** search trie for word w */
	public boolean search(String w) {
		
		// initially outcome unknown
		Outcomes outcome = Outcomes.UNKNOWN;
		
		// position in word so far searched (start at beginning)
		int i = 0;

		// start with first child of the root
		Node current = root.getChild();
		
		// start search
		while (outcome == Outcomes.UNKNOWN) {
			
			if (current == null) outcome = Outcomes.ABSENT; // dead-end
			else if (current.getLetter() == w.charAt(i)) { // positions match				
				if (i == w.length() - 1) {
					outcome = Outcomes.PRESENT; // matched word
				}
				else { // descend one level...
					current = current.getChild(); // in trie
					i++; // in word being searched
				}
			}	
			else { // positions not matched so try next sibling
				current = current.getSibling();
			}
		}
		// return answer
		if (outcome != Outcomes.PRESENT) return false;
		else return current.getIsWord();
	}
		
	/* now changed so insertion is performed in a lexicographical order (task 3) */
		
    /** inserting a word w into trie */
    public void insert(String w){
        
        int i = 0; // position in word (start at beginning)
        Node current = root; // current node in trie (start at root)
        Node next = current.getChild(); // first child of current node we are testing
        
        while (i < w.length()) { // not reached end of word
            if (next !=null && next.getLetter() == w.charAt(i)) { // chars match: decend a level
                current = next; // update node to the child node
                next = current.getChild(); // update child node
                i++; // next position in the word
            }
            else if (next != null) { // try next sibling
                next = next.getSibling();
            }
            else { // no more siblings: need new node
                Node x = new Node(w.charAt(i)); // label with ith element of the word
                x.setSibling(current.getChild()); // sibling: first child of current node
                current.setChild(x); // make first child of current node
                current = x; // move to the new node
                next = current.getChild(); // update child node
                i++; // next position in word
            }
        }
        current.setIsWord(true); // current represents word w

    }

    /** inserting a word w into trie in lexicographic order (task 3) */
    public void insertLex(String w){
        
        int i = 0; // position in word (start at beginning)
        Node current = root; // current node in trie (start at root)
        Node next = current.getChild(); // next child of current node we are testing
        Node previous = null; // previous child we tested
        
        while (i < w.length()) { // not reached end of word
            if (next !=null && next.getLetter() == w.charAt(i)) { // chars match: decend a level
                i++; // next position in the word
                current = next; // update node to the child node
                next = current.getChild(); // update next child node
                previous = null; // update previous child node
            }
            else if (next != null) { // not reached the end
                if (next.getLetter() < w.charAt(i)) { // move to next child
                    previous = next;
                    next = next.getSibling();
                }
                else { // insert here
                    Node x = new Node(w.charAt(i)); // label with ith element of the word
                    if (previous != null) { // insert between children
                        previous.setSibling(x);
                        x.setSibling(next);
                    }
                    else { // insert as first child
                        current.setChild(x);
                        x.setSibling(next);
                    }
                    i++; // next position in word
                    current = x; // move to the new node
                    next = null; // update next child node
                    previous = null; // update previous child node
                }
            }
            else { // reach final child so insert
                Node x = new Node(w.charAt(i)); // label with ith element of the word
                if (previous != null) previous.setSibling(x); // insert as last child
                else current.setChild(x); // insert as first (and only child)
                i++; // next position in word
                current = x; // move to the new node
                next = null; // update next child node
                previous = null; // update previous child node
            }
        }
        current.setIsWord(true); // current represents word w

    }

	// traverse method for extracting words - added below (task 1)

	/* traverse trie from node t adding words to list a assuming path to t yields the string s */
	private void traverse(Node t, String s){
		if (t != null){ // not at a leaf
			
			if (t.getIsWord()) words.add(s + t.getLetter()); // if node represents a word: add word to list
			
			// continue the traversal
			traverse(t.getChild(), s + t.getLetter()); // first look at children (note string changes)
			traverse(t.getSibling(), s); // then look at siblings
		}
	}

	/** returns an List containing all the distinct words in the trie */
	public LinkedList<String> extract(){
        words.clear(); // clear the list
		String s = ""; // path to root yields empty string
		traverse(root.getChild(), s); // start traversal
		return words;
	}
	
}
