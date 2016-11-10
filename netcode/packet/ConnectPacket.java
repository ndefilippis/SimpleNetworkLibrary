package netcode.packet;

import java.nio.ByteBuffer;

public class ConnectPacket extends Packet{
	
	
	
	public ConnectPacket(long timeReceived, ByteBuffer data){
		super(timeReceived, data);
	}

	public ConnectPacket() {
		super(PacketType.CONNECT);
	}

	@Override
	protected void encodeData(ByteBuffer buffer) {
		return;
	}
	
	
}
