package netcode;

import java.net.SocketAddress;

import netcode.packet.Acker;
import threading.RunnableLoop;

public abstract class Handler extends RunnableLoop{
	protected Acker acker;
	protected SocketAddress clientAddress;
	
	public Handler(SocketAddress address, Server server){
		this.acker = new Acker();
		this.clientAddress = address;
	}
	
	@Override
	protected abstract void update();
	
	class Client{
		public final int id;
		public final SocketAddress address;
		
		public Client(int id, SocketAddress address){
			this.id = id;
			this.address = address;
		}
	}

}
