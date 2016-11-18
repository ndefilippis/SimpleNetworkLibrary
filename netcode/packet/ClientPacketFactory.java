package netcode.packet;

public abstract class ClientPacketFactory extends PacketFactory{

	public ClientPacketFactory(Acker acker) {
		super(acker);
	}
	
	public ConnectPacket createConnectPacket(){
		return new ConnectPacket(this);
	}
	
	
	public DisconnectPacket	createDisconnectPacket(){
		return new DisconnectPacket(this);
	}

}
