package netcode;

import java.net.SocketAddress;

import netcode.packet.Acker;
import netcode.packet.ServerPacketFactory;

public abstract class Handler<F extends ServerPacketFactory>{
	protected Acker acker;
	protected int id;
	protected SocketAddress clientAddress;
	private F factory;
	
	public Handler(SocketAddress address, int id){
		this.acker = new Acker();
		this.id = id;
		this.clientAddress = address;
		this.factory = createPacketFactory(acker);
	}
	
	public SocketAddress getAddress(){
		return clientAddress;
	}
	
	protected abstract F createPacketFactory(Acker acker);
	
	public final F getPacketFactory(){
		return factory;
	}
}
