package simplemovement;

import simplemovement.mvc.MoverPlane;

public class GameLoop implements Runnable{
	private boolean shouldRun = true;
	private MoverPlane model;
	private final long TARGET_FRAME_RATE_MS = 1;
	
	public GameLoop(MoverPlane model){
		this.model = model;
	}
	
	@Override
	public void run() {
		long currentTime;
		long previousTime = System.nanoTime();
		long delta = 0;
		while(this.shouldRun()){
			currentTime = System.nanoTime();
			long elapsed = currentTime - previousTime;
			delta += elapsed;
			while(delta >= TARGET_FRAME_RATE_MS * 1000000){
				model.update(TARGET_FRAME_RATE_MS/1000D);
				delta -= TARGET_FRAME_RATE_MS * 1000000;
			}
			previousTime = currentTime;
		}
	}
	
	
	
	private boolean shouldRun(){
		return shouldRun;
	}
	
	public void end(){
		shouldRun = false;
	}
	
}
