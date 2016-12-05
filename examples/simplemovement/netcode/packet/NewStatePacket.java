package examples.simplemovement.netcode.packet;

import java.nio.ByteBuffer;

import examples.simplemovement.mvc.MoverState;
import netcode.packet.Packet;
import netcode.packet.PacketType;

public class NewStatePacket extends Packet {
	private MoverState state;
	private int id;
	
	
	public NewStatePacket(long timeReceived, ByteBuffer data) {
		super(timeReceived, data);
		state.serializeRead(data);
		id = data.getInt();
	}
	
	public NewStatePacket(MoverState state, int id, MoverServerPacketFactory factory){
		super(PacketType.NEWVALUE, factory);
		this.id = id;
		this.state = state;
	}
	
	private NewStatePacket(MoverState state){
		super(PacketType.NEWVALUE);
		this.state = state;
	}

	public NewStatePacket(NewStatePacket p, int id, MoverServerPacketFactory factory) {
		super(p, factory);
		this.id = id;
		this.state = p.state;
	}

	public MoverState getState(){
		return state;
	}

	@Override
	protected void encodeData(ByteBuffer buffer) {
		state.serializeWrite(buffer);
		buffer.putInt(id);
	}
	
	public int getID(){
		return id;
	}

	public static NewStatePacket createDefaultPacket(MoverState state) {
		return new NewStatePacket(state);
	}

}
