package netcode;

import netcode.packet.Packet;
import threading.TimedRunnableLoop;

public abstract class InputUpdateLoop extends TimedRunnableLoop {
	private Client client;
	
	public InputUpdateLoop(long milliDelay, Client client) {
		super(milliDelay);
		this.client = client;
	}

	@Override
	protected void update(double dt) {
		client.addMessage(serializeInput());
	}
	
	protected abstract Packet serializeInput();

}
