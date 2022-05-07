public class Monster{
	private String name;
	private String type1;
	private String type2;
	
	private Move[] moves = new Move[4];
	
	public Monster(String name, String type){
		this.name = name;
		this.type1 = type;
	}
	public Monster (String name, String type1, String type2){
		this.name = name;
		this.type1 = type1;
		this.type2 = type2;
	}
	
	public String getName(){
		return name;
	}
	
	public boolean hasType(String type){
		if (type2 == null){
			return this.type1.equals(type);
		}else{
			return this.type1.equals(type) || this.type2.equals(type);
		}
	}
		
	public String toString(){
		if (type2 == null){
			return name + " [" + type1 + "]";
		}else{
			return name + " [" + type1 + ", " + type2 + "]";
		}
	}
	
	public Move getMove(int index){
		return moves[index];
	}
	
	public void setMove(int index, Move move){
		this.moves[index] = move;
	}
}