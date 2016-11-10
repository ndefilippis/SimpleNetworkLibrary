package netcode.packet;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public abstract class Packet {
	protected int packetID;
	private byte packetTypeID;
	private long timeReceived, timeSent;
	private int remoteAckBitField;
	
	protected static int remotePacketID;
	private static int ackBitfield;
	
	/* |----| 		packetID		 0	 3
 	 * |-|			packetTypeID	 4	 4
	 * |--------|	timeSent		 5	12	
	 * |----|		remotePacketID	13	16	
	 * |----|		ackBitfield		17	20
	 */
	
	private static int nextPacketID = 0;
	
	private static int getNextID(){
		return nextPacketID++;
	}
	
	public Packet(long timeReceived, ByteBuffer data) {
		this.timeReceived = timeReceived;
		
		remotePacketID = data.getInt();
		packetTypeID = data.get();
		this.timeReceived = timeReceived;
		timeSent = data.getLong();
		remotePacketID = data.getInt();
		remoteAckBitField = data.getInt();
		ackBitfield = getAckBitfield();
		
	}
	
	private int getAckBitfield(){
		int newACK = 0;
		int lastRemotePacketID = remotePacketID;
		for(int i = remotePacketID - lastRemotePacketID - 1; i < 32; i++){
			if(((ackBitfield >> i) & 1) == 1){
				newACK |= 1 << i;
			}
		}
		return newACK;
	}
	
	public void toByteBuffer(ByteBuffer buffer, long timeSent){
		buffer.putInt(packetID);
		buffer.put(packetTypeID);
		buffer.putLong(timeSent);
		buffer.putInt(remotePacketID);
		buffer.putInt(ackBitfield);
		this.encodeData(buffer);
	}
	
	public static Map<Integer, Boolean> getReceivedPackets(){
		Map<Integer, Boolean> _res = new HashMap<Integer, Boolean>();
		for(int i = 0; i < 32; i++){
			_res.put(remotePacketID - 1 - i,  ((ackBitfield >> i) & 1) == 1);
		}
		return _res;
	}
	
	public Packet(PacketType type){
		this.packetID = getNextID();
		this.packetTypeID = type.getID();
	}	
	
	public PacketType getPacketType(){
		return PacketType.lookupPacket(packetTypeID);
	}
	
	protected abstract void encodeData(ByteBuffer buffer);
	
	public int getRemotePacketID(){
		return remotePacketID;
	} 
	
	public static PacketType lookupPacket(ByteBuffer data){
		byte packetID = (byte) data.get(4);
		return PacketType.lookupPacket(packetID);
	}
}
