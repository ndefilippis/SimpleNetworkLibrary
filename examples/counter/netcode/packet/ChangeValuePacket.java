package examples.counter.netcode.packet;

import java.nio.ByteBuffer;

import netcode.packet.Packet;
import netcode.packet.PacketType;

public class ChangeValuePacket extends Packet{
	private static int nextPacketID;
	
	private int value;
	private int id;
	
	
	public ChangeValuePacket(int newValue, CounterServerPacketFactory factory){
		super(PacketType.NEWVALUE, factory);
		this.value = newValue;
		this.id = nextPacketID++;
	}

	public ChangeValuePacket(long timeReceived, ByteBuffer data) {
		super(timeReceived, data);
		this.value = data.getInt();
		this.id = data.getInt();
	}

	@Override
	protected void encodeData(ByteBuffer buffer) {
		buffer.putInt(value);
		buffer.putInt(id);
	}

	
	public int getValue() {
		return value;
	}
	
	public long getID(){
		return id;
	}

}
