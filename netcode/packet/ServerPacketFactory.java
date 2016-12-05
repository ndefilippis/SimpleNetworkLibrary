package netcode.packet;

public abstract class ServerPacketFactory extends PacketFactory{

	public ServerPacketFactory(Acker acker) {
		super(acker);
	}
	
	public AcceptConnectPacket createAcceptConnectPacket(long id){
		return new AcceptConnectPacket(id, this);
	}
	
	public abstract Packet createPacket(Packet p);

}
