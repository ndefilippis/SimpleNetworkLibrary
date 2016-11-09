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
		byte[] arr = new byte[data.remaining()];
		data.get(arr);
		boolean down = ((arr[0] >> 0) & 0x1) == 0;
		boolean left = ((arr[0] >> 1) & 0x1) == 0; 
		boolean right = ((arr[0] >> 2) & 0x1) == 0; 
		boolean up = ((arr[0] >> 3) & 0x1) == 0;
		this.input = new MoverInput(left, right, up, down, 0);
		this.moverID = byteArrayToInt(arr, 1);
	}
	
	public MoverInputPacket(MoverInput input, int id){
		super(PacketType.NEWVALUE);
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
	protected byte[] encodeData() {
		byte[] data = new byte[13];
		data[0] = (byte)( (input.down ? 1 : 0) | (input.left ?  2 : 0) | (input.right ?  4 : 0) | (input.up ?  8 : 0) );
		intToByteArray(moverID, data, 1);
		return data;
	}

}
