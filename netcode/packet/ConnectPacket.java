package netcode.packet;

import java.nio.ByteBuffer;

public class ConnectPacket extends Packet{
	
	public ConnectPacket(long timeReceived, ByteBuffer data){
		super(timeReceived, data);
	}

	public ConnectPacket(PacketFactory factory) {
		super(PacketType.CONNECT, factory);
	}

	@Override
	protected void encodeData(ByteBuffer buffer) {
		return;
	}
	
	
}
