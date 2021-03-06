package huffman;

public class Node {
	char character;
	int weight;
	Node leftChild;
	Node rightChild;
	
	public Node(char c, int w) {
		this.character = c;
		this.weight = w;
		this.leftChild = null;
		this.rightChild = null;
	}
	
	public Node(char c, int w, Node leftChild, Node rightChild) {
		this.character = c;
		this.weight = w;
		this.leftChild = leftChild;
		this.rightChild = rightChild;
	}
	
	public int getWeight() {
		return this.weight;
	}
}
