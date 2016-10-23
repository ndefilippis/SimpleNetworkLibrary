package netcode.packet;

import java.nio.ByteBuffer;

public class NewPlayerPacket extends Packet {

	private int id;
	
	public NewPlayerPacket(long timeReceived, ByteBuffer data) {
		super(timeReceived, data);
		byte[] array = new byte[data.remaining()];
		data.get(array, 0, 4);
		this.id = byteArrayToInt(array, 0);
	}
	
	public NewPlayerPacket(int id){
		super(PacketType.NEWPLAYER);
		this.id = id;
	}

	@Override
	protected byte[] encodeData() {
		byte[] b = new byte[4];
		intToByteArray(id, b, 0);
		return b;
	}

}
