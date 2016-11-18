package threading;

import mvc.TimedModel;

public class GameLoop<M extends TimedModel<?>> extends TimedRunnableLoop{
	private M model;
	
	public GameLoop(M model, long milliTickRate){
		super(milliTickRate);
		this.model = model;
	}

	@Override
	protected void update(double dt) {
		model.update(dt);
	}

	
}
