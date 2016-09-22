package netcode.packet;

import java.nio.ByteBuffer;

import mvc.Input;

public class CounterPacket extends Packet{
	private Input input;
	
	public CounterPacket(long timeReceived, ByteBuffer data) {
		super(timeReceived, data);
		byte[] counterData = new byte[data.remaining()];
		data.get(counterData, 0, counterData.length);
		this.input = new Input(counterData[0] == 1 ? true : false);
	}
	
	public CounterPacket(Input input){
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
