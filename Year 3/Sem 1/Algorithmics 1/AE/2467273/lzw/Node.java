package lzw;


public class Node {
	
	private int codewordLen; // length of codeword
	private char letter; // label on incoming branch
	private Node sibling; // next sibling (when it exists)
	private Node child; // first child (when it exists)
	
	/** create a new node with letter c */
	public Node(char c, int l){
		letter = c;
		sibling = null;
		child = null;
		this.codewordLen = l;
	}
	
	// include accessors and mutators for
	// the various components of the class
	public Node getChild() {
		return this.child;
	}
	
	public char getLetter() {
		return this.letter;
	}
	
	public Node getSibling() {
		return this.sibling;
	}
	
	public int getCodewordLen() {
		return this.codewordLen;
	}
	
	public void setSibling(Node x) {
		this.sibling = x;
	}
	
	public void setChild(Node x) {
		this.child = x;
	}
	
}
