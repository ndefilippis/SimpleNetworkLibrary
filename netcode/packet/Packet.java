package netcode.packet;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import netcode.ByteBufferConverter;

public abstract class Packet {
	private long timeReceived;
	
	protected int packetID;
	
	protected static int remotePacketID;
	private static int ackBitfield;
	
	private byte packetTypeID;
	
	/* |----| 		packetID		 0	 3
 	 * |-|			packetTypeID	 4	 4
	 * |--------|	timeSent		 5	12	
	 * |----|		remotePacketID	13	16	
	 * |----|		ackBitfield		17	20
	 */
	
	
	private static int nextPacketID = 0;
	private static final int HEADER_SIZE = 21;
	
	private static int getNextID(){
		return nextPacketID++;
	}
	
	public Packet(long timeReceived, ByteBuffer data) {
		this.timeReceived = timeReceived;
		byte[] header = new byte[HEADER_SIZE];
		data.get(header, 0, HEADER_SIZE);
		
		int lastRemotePacketID = remotePacketID;
		remotePacketID = byteArrayToInt(header, 0);
		int newACK = 0;
		for(int i = remotePacketID - lastRemotePacketID - 1; i < 32; i++){
			if(((ackBitfield >> i) & 1) == 1){
				newACK |= 1 << i;
			}
		}
		ackBitfield = newACK;
		packetTypeID = header[4];
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
	
	protected static void intToByteArray(int value, byte[] array, int start){
		for(int i = 0; i < 4; i++){
			array[i + start] = (byte) (value >>> (8 * i));
		}
	}
	
	protected int byteArrayToInt(byte[] array, int start){
		int ret = 0;
		for(int i = 0; i < 4; i++){
			ret |= (array[i + start] & 0xff) << (8 * i) ;
		}
		return ret;
	}
	
	protected void longToByteArray(long value, byte[] array, int start){
		for(int i = 0; i < 8; i++){
			array[i + start] = (byte) (value >>> (8 * i));
		}
	}
	
	protected long byteArrayToLong(byte[] array, int start){
		long ret = 0;
		for(int i = 0; i <  8; i++){
			ret |= ((long) array[i + start] & 0xffL) << (8 * i) ;
		}
		return ret;
	}

	public ByteBuffer toByteBuffer(long timeSent){
		byte[] header = new byte[HEADER_SIZE];
		
		intToByteArray(packetID, header, 0);
		header[4] = packetTypeID;
		
		longToByteArray(timeSent, header, 5);
		intToByteArray(remotePacketID, header, 13);
		intToByteArray(ackBitfield, header, 17);
		
		byte[] data = encodeData();
		ByteBuffer buf = ByteBuffer.allocate(header.length + data.length);
		buf.put(header);
		buf.put(data);
		buf.flip();
		return buf;	
	}
	
	public PacketType getPacketType(){
		return PacketType.lookupPacket(packetTypeID);
	}
	
	protected abstract byte[] encodeData();
	
	public int getRemotePacketID(){
		return remotePacketID;
	} 
	
	public static PacketType lookupPacket(ByteBuffer data){
		byte packetID = (byte) data.get(4);
		return PacketType.lookupPacket(packetID);
	}
}
