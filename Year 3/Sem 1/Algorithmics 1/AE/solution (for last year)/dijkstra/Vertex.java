import java.util.LinkedList;


/**
 class to represent a  vertex in a graph
 */
public class Vertex {
   
    private LinkedList<AdjListNode> adjList ; // the adjacency list 
    private int index; // the index of this vertex in the graph
	private String word; // word associated with the vertex
	private boolean visited; // whether visited in a traversal
    private int predecessor; // index of predecessor vertex in a traversal
	private int distance; // distance from some starting vertex
	private boolean S; // if it is a member of S (used in Dijkstra's algorithm)

    /**
	 creates a new instance of Vertex
	*/
    public Vertex(int n) {
    	adjList = new LinkedList<AdjListNode>();
    	index = n;
		word = null;
    	visited = false;
		distance = Integer.MAX_VALUE; // use max integer value as infinity
		S = false;
		predecessor = n; // set to itself when not known
    }
    
    /**
	 copy constructor
     */
    public Vertex(Vertex v){
    	adjList = v.getAdjList();
    	index = v.getIndex();
    	visited = v.getVisited();
		distance = v.getDistance();
    }
     
    public LinkedList<AdjListNode> getAdjList(){
        return adjList;
    }
    
    public int getIndex(){
    	return index;
    }
    
    public void setIndex(int n){
    	index = n;
    }
	
    public String getWord(){
    	return word;
    }
    
    public void setWord(String s){
    	word = s;
    }
	
	public boolean getVisited(){
    	return visited;
    }
    
    public void setVisited(boolean b){
    	visited = b;
    }
    
    public int getPredecessor(){
    	return predecessor;
    }
    
    public void setPredecessor(int n){
    	predecessor = n;
    }
    
	public int getDistance(){
    	return distance;
    }
    
    public void setDistance(int n){
    	distance = n;
    }
	
	public boolean getS(){
    	return S;
    }
    
    public void setS(boolean b){
    	S = b;
    }
	
    public void addToAdjList(int n, int w){
        adjList.addLast(new AdjListNode(n,w));
    }
    
    public int vertexDegree(){
        return adjList.size();
    }
}
