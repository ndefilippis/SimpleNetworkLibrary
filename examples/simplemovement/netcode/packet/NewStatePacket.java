package examples.simplemovement.netcode.packet;

import java.nio.ByteBuffer;

import examples.simplemovement.mvc.MoverState;
import netcode.ByteBufferConverter;
import netcode.packet.Packet;
import netcode.packet.PacketType;

public class NewStatePacket extends Packet {
	private MoverState state;
	
	public NewStatePacket(long timeReceived, ByteBuffer data) {
		super(timeReceived, data);
	}
	
	public NewStatePacket(MoverState state){
		super(PacketType.NEWVALUE);
		this.state = state;
	}

	@Override
	protected void encodeData(ByteBuffer buffer) {
		state.serializeWrite(buffer);
	}

}
