package netcode.packet;

public enum PacketType{
	CONNECT(00),
	ACCEPTCONNECT(01),
	
	COUNTER(02),
	NEWVALUE(03),
	
	DISCONNECT(04),
	INVALID(05);
	
	private byte packetID;
	
	private PacketType(int id){
		this.packetID = (byte)id;
	}
	
	public byte getID(){
		return packetID;
	}
	
	public static PacketType lookupPacket(byte id){
		for(PacketType p : PacketType.values()){
			if(p.getID() == id){
				return p;
			}
		}
		return PacketType.INVALID;
	}
}