package examples.simplemovement.netcode.packet;

import java.nio.ByteBuffer;

import examples.simplemovement.mvc.Mover;
import netcode.packet.Packet;
import netcode.packet.PacketType;

public class MoverChangePacket extends Packet {
	private int moverID;
	private int newX, newY;
	private long updateTime;
	
	public MoverChangePacket(long timeReceived, ByteBuffer data) {
		super(timeReceived, data);
		byte[] arr = new byte[data.remaining()];
		data.get(arr);
		moverID = data.getInt();
		newX = data.getInt();
		newY = data.getInt();
		this.updateTime = data.getLong();
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
	protected void encodeData(ByteBuffer data) {
		data.putInt(moverID);
		data.putInt(newX);
		data.putInt(newY);
		data.putLong(updateTime);
	}

	public long getUpdateTime() {
		return updateTime;
	}

}
