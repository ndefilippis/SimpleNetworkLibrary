package threading;

public abstract class TimedRunnableLoop extends RunnableLoop{
	private long milliDelay;
	private int maxIterations;
	
	public TimedRunnableLoop(long milliDelay){
		this(milliDelay, 0);
	}
	
	public TimedRunnableLoop(long milliDelay, int maxIterations){
		super();
		this.milliDelay = milliDelay;
		this.maxIterations = maxIterations;
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
			int iterations = 0;
			while(delta >= milliDelay * 1000000 && (maxIterations == 0 || iterations < maxIterations)){
				update(milliDelay/1000D);
				delta -= milliDelay * 1000000;
				iterations++;
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
