package netcode.packet;

import java.nio.ByteBuffer;

public class NewPlayerPacket extends Packet {

	private long id;
	
	public NewPlayerPacket(long timeReceived, ByteBuffer data) {
		super(timeReceived, data);
		this.id = data.getLong();
	}
	
	public NewPlayerPacket(int id, PacketFactory factory){
		super(PacketType.NEWPLAYER, factory);
		this.id = id;
	}

	@Override
	protected void encodeData(ByteBuffer buffer) {
		buffer.putLong(id);
	}

}
