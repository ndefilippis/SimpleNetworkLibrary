package netcode.packet;

public abstract class ServerPacketFactory extends PacketFactory{

	public ServerPacketFactory(Acker acker) {
		super(acker);
	}
	
	public AcceptConnectPacket createAcceptConnectPacket(long id){
		return new AcceptConnectPacket(id, this);
	}
	
	public Packet createPacket(Class<?> c, Object[] params){
		Packet p = createPacketDefault(c, params);
		if(p == null){
			p = createPacketFromData(c, params);
		}
		return p;
	}
	
	public abstract Packet createPacketFromData(Class<?> packetType, Object[] params);

	public Packet createPacketDefault(Class<?> c, Object[] params) {
		if(c == AcceptConnectPacket.class){
			return createAcceptConnectPacket((int)params[0]);
		}
		return null;
	}
	
	

}
