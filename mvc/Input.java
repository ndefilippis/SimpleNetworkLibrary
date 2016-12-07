package mvc;

public abstract class Input {
	private int id;
	private static int nextID = 0;
	
	
	public Input(){
		this.id = nextID++;
	}
	
	public int getID(){
		return id;
	}
}
