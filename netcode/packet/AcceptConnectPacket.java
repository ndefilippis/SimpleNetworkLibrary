package netcode.packet;

import java.nio.ByteBuffer;

public class AcceptConnectPacket extends Packet{

	private int state;
	private long id;

	public AcceptConnectPacket(int state, long id){
		super(PacketType.ACCEPTCONNECT);
		this.state = state;
		this.id = id;
	}
	public AcceptConnectPacket(long timeReceived, ByteBuffer data) {
		super(timeReceived, data);
		state = data.getInt();
		this.id = data.getLong();
	}

	@Override
	protected void encodeData(ByteBuffer buffer) {
		buffer.putInt(state);
		buffer.putLong(id);
	}
	
	public int getState(){
		return state;
	}
	
	public long getID(){
		return id;
	}
}
