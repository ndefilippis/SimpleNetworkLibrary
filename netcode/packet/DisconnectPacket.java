package netcode.packet;

import java.nio.ByteBuffer;

public class DisconnectPacket extends Packet{

	public DisconnectPacket(long timeReceived, ByteBuffer data) {
		super(timeReceived, data);
	}
	
	public DisconnectPacket(PacketFactory factory){
		super(PacketType.DISCONNECT, factory);
	}

	@Override
	protected void encodeData(ByteBuffer buffer) {
		return;
	}

}
