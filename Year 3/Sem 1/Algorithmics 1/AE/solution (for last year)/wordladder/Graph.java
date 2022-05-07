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
	 find shortest path between two vertices through
	 a breadth first search/traversal of the graph
	*/
	public void findShortestPathBfs(int w1, int w2) {
		
		// initialize (no vertices visted)
		for (Vertex v : vertices) v.setVisited(false);
		
		// create queue for breadth first search
		LinkedList<Vertex> queue = new LinkedList<Vertex>();
		
		// if we do search in the reverse direction can easily
		// (using predecessor) print path in the 'right direction'
		Vertex v = getVertex(w2); // initial vertex
		v.setDistance(0); // set distance of source vertex
		v.setVisited(true); // set vertex to be visted
		v.setPredecessor(w2); // set predecessor (which is itself as initial vertex)
		queue.add(v); // add to the queue
		
		outerloop: while (!queue.isEmpty()) { // main loop
			Vertex u = queue.remove(); // remove vertex from queue
			LinkedList<AdjListNode> list = u.getAdjList(); // get its adjacency
			for (AdjListNode node : list) { // go through adjacency list
				Vertex w = vertices[node.getVertexIndex()];
				if (!w.getVisited()) { // if it has not been visited...
					w.setVisited(true); // it has now
					w.setPredecessor(u.getIndex()); // set its predecessor
					queue.add(w); // add to queue to be processed
					w.setDistance(u.getDistance()+1); // set distance
				}
				if (w.getIndex() == w1) break outerloop; // stop when we find target
			}
		}
		
		if (getVertex(w1).getVisited()){
			System.out.println("shortest word ladder of length " + getVertex(w1).getDistance());
			// use "reverse" path as easier to print
            int i = w1;
			System.out.println("example shortest word ladder:");
			System.out.println("    " + vertices[i].getWord());
			while (i != getVertex(i).getPredecessor()){
				i = getVertex(i).getPredecessor();
				System.out.println("    " + getVertex(i).getWord());
			}
		}
		else System.out.println("no word ladder exists");
	}
}
