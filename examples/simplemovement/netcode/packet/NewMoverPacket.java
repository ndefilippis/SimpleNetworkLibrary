package examples.simplemovement.netcode.packet;

import java.nio.ByteBuffer;

import examples.simplemovement.mvc.Mover;
import netcode.packet.Packet;
import netcode.packet.PacketType;

public class NewMoverPacket extends Packet {
	
	private Mover m;
	
	public NewMoverPacket(long timeReceived, ByteBuffer data) {
		super(timeReceived, data);
		byte[] arr = new byte[data.remaining()];
		data.get(arr);
		int id = byteArrayToInt(arr, 0);
		int x = byteArrayToInt(arr, 4);
		int y = byteArrayToInt(arr, 8);
		this.m = new Mover(id, x, y);
	}
	
	public NewMoverPacket(Mover m){
		super(PacketType.NEWPLAYER);
		this.m = m;
	}
	
	public Mover getNewMover(){
		return m;
	}

	@Override
	protected byte[] encodeData() {
		byte[] arr = new byte[12];
		intToByteArray(m.getID(), arr, 0);
		intToByteArray(m.getX(), arr, 4);
		intToByteArray(m.getY(), arr, 8);
		return arr;
	}

}
