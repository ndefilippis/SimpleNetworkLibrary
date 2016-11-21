package netcode;

import mvc.State;
import mvc.TimedModel;
import netcode.packet.Packet;
import threading.TimedRunnableLoop;

public abstract class GameUpdateLoop<M extends TimedModel<S>, S extends State> extends TimedRunnableLoop{
	private Server<?,?> server;
	private Class<? extends Packet> type;
	private long tick;
	private M model;
	
	public GameUpdateLoop(long milliDelay, Server<?,?> server, M model, Class<? extends Packet> packetType) {
		this(milliDelay, server, model, packetType, 0);
	}
	
	public GameUpdateLoop(long milliDelay, Server<?,?> server, M model, Class<? extends Packet> packetType, long startTick){
		super(milliDelay);
		this.server = server;
		this.model = model;
		this.type = packetType;
		this.tick = startTick;
	}

	@Override
	protected void update(double dt) {
		server.addMessageToAll(type, model.getState());
		tick++;
	}
	
	public long getTick(){
		return tick;
	}

}
