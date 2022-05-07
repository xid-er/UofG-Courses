import java.util.LinkedList;

/**
 class to represent an undirected graph using adjacency lists
*/
public class Graph {

	private Vertex[] vertices; // the vertices

	private int numVertices = 0; // number of vertices

	// possibly other fields representing properties of the graph

	/**
	 creates a new instance of Graph with n vertices
	*/
	public Graph(int n) {
		numVertices = n;
		vertices = new Vertex[n];
		for (int i = 0; i < n; i++)
			vertices[i] = new Vertex(i);
	}

	public int size() {
		return numVertices;
	}

	public Vertex getVertex(int i) {
		return vertices[i];
	}

	public void setVertex(int i) {
		vertices[i] = new Vertex(i);
	}

	/**
	 find shortest weighted path between two vertices through
	 Dijkstra's algorithm
	*/
	public void findShortestPathDijkstra(int w1, int w2) {

		// initialize (no vertices in the set "S")
		for (Vertex v : vertices) {
			v.setS(false);
			v.setDistance(Integer.MAX_VALUE);
            // use MAX_VALUE to represent infinity (could use -1 but would need to check this)
		}
		
		// add vertex w2 to the set S and set distance to be 0
		// (as for bfs solution to simplify printing path start from w2 not w1)
		vertices[w2].setS(true);
		vertices[w2].setDistance(0);
		
		// set distance all vertices in j's adjacency list
		LinkedList<AdjListNode> list = vertices[w2].getAdjList();
		for (AdjListNode n : list){
			int k = n.getVertexIndex();
			int d = n.getWeight();
			vertices[k].setDistance(d);
			vertices[k].setPredecessor(w2);
		}
		
		boolean done = false;
		while(!done) {
			// find minimum distance
			int minDistance = Integer.MAX_VALUE; // min distance
			int i = -1; // find index of vertex with min distance
			for (Vertex v : vertices) {
				if (!v.getS() & v.getDistance() < minDistance) {
					minDistance = v.getDistance();
					i = v.getIndex();
				}
			}
			if (i>=0) vertices[i].setS(true); // m becomes a member of S if it exists

            // nothing to change or found target
			if (i<0 | i == w1) done = true;
			// update distances
			else {
				// update distance/perform relaxation
                // only have to consider vertices adjacent to vertex with index m
				for (AdjListNode n : vertices[i].getAdjList()){ // go through adjacency list
					int k = n.getVertexIndex();
                    // perform relaxation
					if ( !vertices[k].getS() & minDistance + n.getWeight() < vertices[k].getDistance()) {
						vertices[k].setDistance(minDistance + n.getWeight());
						vertices[k].setPredecessor(i);
					}
				}
			}

		}
        // return path if it exists
		if (getVertex(w1).getS()){
			System.out.println("minimum path distance " + vertices[w1].getDistance());
			// use "reverse" path as easier to print
			int i = w1;
			System.out.println("path with minimum distance:");
			System.out.println("    " + vertices[i].getWord());
			while (i != vertices[i].getPredecessor()){
				i = vertices[i].getPredecessor();
				System.out.println("    " + vertices[i].getWord());
			}
		}
		else System.out.println("no path exists");
	}
}
