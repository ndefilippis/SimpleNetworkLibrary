package netcode.packet;

import java.nio.ByteBuffer;

public class NewPlayerPacket extends Packet {

	private int id;
	
	public NewPlayerPacket(long timeReceived, ByteBuffer data) {
		super(timeReceived, data);
		this.id = data.getInt();
	}
	
	public NewPlayerPacket(int id){
		super(PacketType.NEWPLAYER);
		this.id = id;
	}

	@Override
	protected void encodeData(ByteBuffer buffer) {
		buffer.putInt(id);
	}

}
