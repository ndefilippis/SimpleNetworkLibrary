package examples.simplemovement.netcode.packet;

import java.nio.ByteBuffer;

import examples.simplemovement.mvc.Mover;
import netcode.packet.Packet;
import netcode.packet.PacketType;

public class MoverChangePacket2 extends Packet {
	private int moverID;
	private int newX, newY;
	private long updateTime;
	
	public MoverChangePacket2(long timeReceived, ByteBuffer data) {
		super(timeReceived, data);
		byte[] arr = new byte[data.remaining()];
		data.get(arr);
		moverID = data.getInt();
		newX = data.getInt();
		newY = data.getInt();
		this.updateTime = data.getLong();
	}
	
	MoverChangePacket2(Mover m, long updateTime, MoverServerPacketFactory factory){
		super(PacketType.NEWVALUE, factory);
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
