package netcode.packet;

import java.nio.ByteBuffer;

public class DisconnectPacket extends Packet{

	public DisconnectPacket(){
		super(PacketType.DISCONNECT);
	}

	public DisconnectPacket(long timeReceived, ByteBuffer data) {
		super(timeReceived, data);
	}

	@Override
	protected byte[] encodeData() {
		return new byte[0];
	}

}
