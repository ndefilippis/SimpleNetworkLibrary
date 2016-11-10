package examples.simplemovement.netcode.packet;

import java.nio.ByteBuffer;

import examples.simplemovement.mvc.Mover;
import netcode.packet.Packet;
import netcode.packet.PacketType;

public class NewMoverPacket extends Packet {
	
	private Mover m;
	
	public NewMoverPacket(long timeReceived, ByteBuffer data) {
		super(timeReceived, data);
		this.m = new Mover(data);
	}
	
	public NewMoverPacket(Mover m){
		super(PacketType.NEWPLAYER);
		this.m = m;
	}
	
	public Mover getNewMover(){
		return m;
	}

	@Override
	protected void encodeData(ByteBuffer buffer) {
		m.serializeWrite(buffer);
	}

}
