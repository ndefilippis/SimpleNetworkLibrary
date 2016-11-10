package netcode;

import mvc.State;
import mvc.TimedModel;
import netcode.packet.Packet;
import threading.TimedRunnableLoop;

public abstract class GameUpdateLoop<M extends TimedModel<S>, S extends State> extends TimedRunnableLoop{
	private Server server;
	private M model;
	
	public GameUpdateLoop(long milliDelay, Server server, M model) {
		super(milliDelay);
		this.server = server;
		this.model = model;
	}

	@Override
	protected void update(double dt) {
		server.addMessageToAll(serializeState(model.getState()));
	}
	
	protected abstract Packet serializeState(S state);

}
