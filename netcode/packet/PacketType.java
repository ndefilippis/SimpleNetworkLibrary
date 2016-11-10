package netcode.packet;

public enum PacketType{
	INVALID(-1),		//SERVER, CLIENT
	
	CONNECT(05),		//CLIENT
	ACCEPTCONNECT(01),	//SERVER
	
	INPUT(02),			//CLIENT
	NEWVALUE(03),		//SERVER
	
	DISCONNECT(04),		//CLIENT
	NEWPLAYER(00);		//SERVER
	
	
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