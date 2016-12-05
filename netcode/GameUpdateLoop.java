package netcode;

import threading.TimedRunnableLoop;

public abstract class GameUpdateLoop extends TimedRunnableLoop{
	private long tick;
	
	public GameUpdateLoop(long milliDelay) {
		this(milliDelay, 0);
	}
	
	public GameUpdateLoop(long milliDelay, long startTick){
		super(milliDelay);
		this.tick = startTick;
	}

	@Override
	protected void update(double dt) {
		//server.addMessageToAll(type, model.getState());
		tick++;
		onUpdate();
	}
	
	protected abstract void onUpdate();
	
	public long getTick(){
		return tick;
	}

}
