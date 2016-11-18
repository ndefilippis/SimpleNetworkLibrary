package examples.simplemovement.netcode.packet;

import java.nio.ByteBuffer;

import examples.simplemovement.mvc.MoverInput;
import netcode.packet.Packet;
import netcode.packet.PacketType;

public class MoverInputPacket extends Packet {

	private MoverInput input;
	private int moverID;

	public MoverInputPacket(long timeReceived, ByteBuffer data) {
		super(timeReceived, data);
		byte input = data.get();
		boolean down = ((input >> 0) & 0x1) == 0;
		boolean left = ((input >> 1) & 0x1) == 0; 
		boolean right = ((input >> 2) & 0x1) == 0; 
		boolean up = ((input >> 3) & 0x1) == 0;
		this.input = new MoverInput(left, right, up, down, 0);
		this.moverID = data.getInt();
	}
	
	public MoverInputPacket(MoverInput input, int id, MoverClientPacketFactory factory){
		super(PacketType.NEWVALUE, factory);
		this.input = input;
		this.moverID = id;
	}
	
	public int getMoverID(){
		return moverID;
	}
	
	public MoverInput getInput(){
		return input;
	}

	@Override
	protected void encodeData(ByteBuffer buffer) {
		buffer.put((byte)( (input.down ? 1 : 0) | (input.left ?  2 : 0) | (input.right ?  4 : 0) | (input.up ?  8 : 0) ));
		buffer.putInt(moverID);
	}

}
