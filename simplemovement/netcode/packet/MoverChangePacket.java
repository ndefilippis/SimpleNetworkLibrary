package simplemovement.netcode.packet;

import java.nio.ByteBuffer;

import netcode.packet.Packet;
import netcode.packet.PacketType;
import simplemovement.mvc.Mover;

public class MoverChangePacket extends Packet {
	private int moverID;
	private int newX, newY;
	private long updateTime;
	
	public MoverChangePacket(long timeReceived, ByteBuffer data) {
		super(timeReceived, data);
		byte[] arr = new byte[data.remaining()];
		data.get(arr);
		moverID = byteArrayToInt(arr, 0);
		newX = byteArrayToInt(arr, 4);
		newY = byteArrayToInt(arr, 8);
		this.updateTime = byteArrayToLong(arr, 12);
	}
	
	public MoverChangePacket(Mover m, long updateTime){
		super(PacketType.NEWVALUE);
		this.moverID = m.getID();
		this.newX = m.getX();
		this.newY = m.getY();
		this.updateTime = updateTime;
	}
	
	public int getMoverID(){
		return moverID;
	}
	
	public int getX(){
		return newX;
	}
	
	public int getY(){
		return newY;
	}

	@Override
	protected byte[] encodeData() {
		byte[] arr = new byte[20];
		intToByteArray(moverID, arr, 0);
		intToByteArray(newX, arr, 4);
		intToByteArray(newY, arr, 8);
		longToByteArray(updateTime, arr, 12);
		return arr;
	}

	public long getUpdateTime() {
		return updateTime;
	}

}
