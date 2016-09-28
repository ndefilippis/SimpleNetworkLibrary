package mvc;

public class CounterInput {
	private boolean isIncrement;
	private long time;
	
	public CounterInput(boolean isIncrement){
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
