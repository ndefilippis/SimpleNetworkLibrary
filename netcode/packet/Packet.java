package netcode.packet;

import java.nio.ByteBuffer;

public abstract class Packet {	
	protected int packetID;
	private byte packetTypeID;
	private long timeReceived, timeSent;
	private int remotePacketID;
	private int ackBitfield;
	
	private PacketFactory factory;
	
	protected Packet(PacketType type){
		this.packetTypeID = type.getID();
	}
	
	protected Packet(PacketType type, PacketFactory factory){
		this.factory = factory;
		this.packetID = factory.getNextID();
		this.packetTypeID = type.getID();
	}
	
	public Packet(Packet other, PacketFactory factory){
		this.factory = factory;
		this.packetID = other.packetID;
		this.packetTypeID = other.packetTypeID;
	}
	
	public Packet(long timeReceived, ByteBuffer data) {
		this.packetID = data.getInt();
		this.packetTypeID = data.get();
		this.timeSent = data.getLong();
		this.remotePacketID = data.getInt();
		this.ackBitfield = data.getInt();
		this.timeReceived = timeReceived;
	}
	
	
	public void toByteBuffer(ByteBuffer buffer, long timeSent){
		buffer.putInt(factory.getNextID());
		buffer.put(packetTypeID);
		buffer.putLong(timeSent);
		buffer.putInt(factory.getRemotePacketID());
		buffer.putInt(factory.getAckBitfield());
		this.encodeData(buffer);
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

	public int getRemotePacketID(){
		return remotePacketID;
	}
	
	public int getAckBitfield(){
		return ackBitfield;
	}
	
	public int getPacketID() {
		return packetID;
	}
}
