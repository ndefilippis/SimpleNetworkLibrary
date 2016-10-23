package mvc;

public abstract class Input {
	private long time;
	
	public Input(){
		this.time = System.nanoTime();
	}
	
	public long getTime(){
		return time;
	}
}
