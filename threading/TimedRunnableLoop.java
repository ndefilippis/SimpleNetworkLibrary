package threading;

public abstract class TimedRunnableLoop extends RunnableLoop{
	private long milliDelay;
	
	public TimedRunnableLoop(long milliDelay){
		super();
		this.milliDelay = milliDelay;
	}
	
	@Override
	public void run(){
		long currentTime;
		long previousTime = System.nanoTime();
		long delta = 0;
		while(shouldRun()){
			currentTime = System.nanoTime();
			long elapsed = currentTime - previousTime;
			delta += elapsed;
			while(delta >= milliDelay * 1000000){
				update(milliDelay/1000D);
				delta -= milliDelay * 1000000;
			}
			previousTime = currentTime;
		}
	}
	
	@Override
	public void update(){
		update(milliDelay/1000D);
	}
	
	protected abstract void update(double dt);
		
}
