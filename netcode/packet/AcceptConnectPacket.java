package netcode.packet;

import java.nio.ByteBuffer;

public class AcceptConnectPacket extends Packet{

	private long id;

	public AcceptConnectPacket(long id){
		super(PacketType.ACCEPTCONNECT);
		this.id = id;
	}
	public AcceptConnectPacket(long timeReceived, ByteBuffer data) {
		super(timeReceived, data);
		this.id = data.getLong();
	}

	@Override
	protected void encodeData(ByteBuffer buffer) {
		buffer.putLong(id);
	}
	
	public long getID(){
		return id;
	}
}
