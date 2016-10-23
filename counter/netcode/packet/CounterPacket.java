package counter.netcode.packet;

import java.nio.ByteBuffer;

import counter.mvc.CounterInput;
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
	
	public CounterPacket(CounterInput input){
		super(PacketType.COUNTER);
		this.input = input;
	}

	@Override
	protected byte[] encodeData() {
		return new byte[]{this.input.isIncrement() ? (byte)1 : (byte)0};
	}

	public boolean isIncrement() {
		return this.input.isIncrement();
	}

}
