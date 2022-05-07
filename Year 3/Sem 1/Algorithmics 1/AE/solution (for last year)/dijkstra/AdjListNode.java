
/**
 class to represent an entry in the adjacency list of a vertex
 in a graph
 */
public class AdjListNode {

	private int vertexIndex;
	
	// could be other fields, for example representing
	// properties of the edge - weight, capacity, . . .

    private int weight; // weight of the edge

	public AdjListNode(int n, int w){
		vertexIndex = n;
		weight = w;
	}
	
	public int getVertexIndex(){
		return vertexIndex;
	}
	public void setVertexIndex(int n){
		vertexIndex = n;
	}

	public int getWeight(){
		return weight;
	}
	public void setWeight(int w){
		weight = w;
	}
	
	
}
