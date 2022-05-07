
/**
 class to represent an entry in the adjacency list of a vertex
 in a graph
 */
public class AdjListNode {

	private int vertexIndex;
	
	// could be other fields, for example representing
	// properties of the edge - weight, capacity, . . .
	
	public AdjListNode(int n){
		vertexIndex = n;
	}
	
	public int getVertexIndex(){
		return vertexIndex;
	}
	
	public void setVertexIndex(int n){
		vertexIndex = n;
	}
	
}
