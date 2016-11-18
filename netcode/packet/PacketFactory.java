package netcode.packet;

public abstract class PacketFactory {
	private int nextPacketID = 0;
	private Acker acker;
	
	public PacketFactory(Acker acker){
		this.acker = acker;
		
	}
	
	public int getNextID(){
		return nextPacketID++;
	}

	public int getRemotePacketID() {
		return acker.getRemotePacketID();
	}
	
	public int getAckBitfield(){
		return acker.getAckBitfield();
	}
}
