package netcode;

import mvc.State;
import mvc.TimedModel;
import netcode.packet.Packet;
import threading.TimedRunnableLoop;

public abstract class GameUpdateLoop<M extends TimedModel<S>, S extends State> extends TimedRunnableLoop{
	private Server server;
	private long tick;
	private M model;
	
	public GameUpdateLoop(long milliDelay, Server server, M model) {
		this(milliDelay, server, model, 0);
	}
	
	public GameUpdateLoop(long milliDelay, Server server, M model, long startTick){
		super(milliDelay);
		this.server = server;
		this.model = model;
		this.tick = startTick;
	}

	@Override
	protected void update(double dt) {
		server.addMessageToAll(serializeState(model.getState()));
		tick++;
	}
	
	public long getTick(){
		return tick;
	}
	
	protected abstract Packet serializeState(S state);

}
