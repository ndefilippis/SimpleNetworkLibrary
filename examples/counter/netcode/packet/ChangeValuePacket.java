package examples.counter.netcode.packet;

import java.nio.ByteBuffer;

import netcode.packet.Packet;
import netcode.packet.PacketType;

public class ChangeValuePacket extends Packet{
	
	private int value;
	private long updateTime;
	
	public ChangeValuePacket(int newValue, long lastAcknowledgedTime){
		super(PacketType.NEWVALUE);
		this.value = newValue;
		this.updateTime = lastAcknowledgedTime;
	}

	public ChangeValuePacket(long timeReceived, ByteBuffer data) {
		super(timeReceived, data);
		this.value = data.getInt();
		this.updateTime = data.getLong();
	}

	@Override
	protected void encodeData(ByteBuffer buffer) {
		buffer.putInt(value);
		buffer.putLong(updateTime);
	}

	
	public int getValue() {
		return value;
	}
	
	public long getUpdateTime(){
		return updateTime;
	}

}
