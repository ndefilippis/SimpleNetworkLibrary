package mvc;

public class Input {
	private boolean isIncrement;
	private long time;
	
	public Input(boolean isIncrement){
		this.isIncrement = isIncrement;
		this.time = System.nanoTime();
	}
	
	public boolean isIncrement(){
		return isIncrement;
	}
	
	public long getTime(){
		return time;
	}
}
