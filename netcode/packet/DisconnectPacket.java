package netcode.packet;

import java.nio.ByteBuffer;

public class DisconnectPacket extends Packet{

	public DisconnectPacket(long timeReceived, ByteBuffer data) {
		super(timeReceived, data);
	}
	
	public DisconnectPacket(){
		super(PacketType.DISCONNECT);
	}

	@Override
	protected void encodeData(ByteBuffer buffer) {
		return;
	}

}
