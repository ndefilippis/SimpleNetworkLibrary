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
		byte[] temp = new byte[data.remaining()];
		data.get(temp, 0, temp.length);
		state = byteArrayToInt(temp, 0);
		this.id = byteArrayToLong(temp, 4);
	}

	@Override
	protected byte[] encodeData() {
		byte[] stateArray = new byte[12];
		intToByteArray(state, stateArray, 0);
		longToByteArray(id, stateArray, 4);
		return stateArray;
	}
	
	public int getState(){
		return state;
	}
	
	public long getID(){
		return id;
	}
}
