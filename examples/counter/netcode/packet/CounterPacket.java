package examples.counter.netcode.packet;

import java.nio.ByteBuffer;

import examples.counter.mvc.CounterInput;
import netcode.packet.Packet;
import netcode.packet.PacketType;

public class CounterPacket extends Packet{
	private CounterInput input;
	
	public CounterPacket(long timeReceived, ByteBuffer data) {
		super(timeReceived, data);
		byte[] counterData = new byte[data.remaining()];
		data.get(counterData, 0, counterData.length);
		this.input = new CounterInput(counterData[0] == 1 ? true : false);
	}
	
	CounterPacket(CounterInput input, CounterClientPacketFactory factory){
		super(PacketType.INPUT, factory);
		this.input = input;
	}

	@Override
	protected void encodeData(ByteBuffer buffer) {
		buffer.put(this.input.isIncrement() ? (byte)1 : (byte)0);
	}

	public boolean isIncrement() {
		return this.input.isIncrement();
	}

}
