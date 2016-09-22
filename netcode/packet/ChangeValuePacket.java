package netcode.packet;

import java.nio.ByteBuffer;

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
		byte[] newValue = new byte[data.remaining()];
		data.get(newValue, 0, newValue.length);
		this.value = byteArrayToInt(newValue, 0);
		this.updateTime = byteArrayToLong(newValue, 4);
	}

	@Override
	protected byte[] encodeData() {
		byte[] valueArray = new byte[12];
		intToByteArray(value, valueArray, 0);
		longToByteArray(updateTime, valueArray, 4);
		return valueArray;
	}

	
	public int getValue() {
		return value;
	}
	
	public long getUpdateTime(){
		return updateTime;
	}

}
