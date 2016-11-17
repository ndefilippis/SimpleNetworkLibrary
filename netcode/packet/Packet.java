package netcode.packet;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import netcode.MessageSender;

public abstract class Packet {	
	protected int packetID;
	private byte packetTypeID;
	private long timeReceived, timeSent;
	
	public Packet(long timeReceived, ByteBuffer data) {
		this.packetID = data.getInt();
		this.packetTypeID = data.get();
		this.timeSent = data.getLong();
		this.timeReceived = timeReceived;
		
	}
	
	
	public void toByteBuffer(int packetID, ByteBuffer buffer, long timeSent){
		this.packetID = packetID;
		buffer.putInt(packetID);
		buffer.put(packetTypeID);
		buffer.putLong(timeSent);
		this.encodeData(buffer);
	}
	
	
	
	public Packet(PacketType type){
		this.packetTypeID = type.getID();
	}	
	
	public PacketType getPacketType(){
		return PacketType.lookupPacket(packetTypeID);
	}
	
	protected abstract void encodeData(ByteBuffer buffer);
	
	public static PacketType lookupPacket(ByteBuffer data){
		byte packetID = (byte) data.get(4);
		return PacketType.lookupPacket(packetID);
	}

	public long getTimeSent() {
		return timeSent;
	}
	
	public long getTimeReceived(){
		return timeReceived;
	}
}
