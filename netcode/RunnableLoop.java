package netcode;

public abstract class RunnableLoop implements Runnable{
	private boolean isRunning;
	
	public RunnableLoop(){
		this.isRunning = true;
	}
	
	public void kill(){
		this.isRunning = false;
	}
	
	private boolean shouldRun(){
		return isRunning;
	}
	
	@Override
	public void run() {
		while(shouldRun()){
			this.update();
		}
	}
	
	protected  abstract void update();

}
