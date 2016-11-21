package mvc;

public abstract class Input {
	private long time;
	private int id;
	private static int nextID = 0;
	
	
	public Input(){
		this.time = System.nanoTime();
		this.id = nextID++;
	}
	
	public int getID(){
		return id;
	}
	
	public long getTime(){
		return time;
	}
}
