package simplemovement.netcode.packet;

import java.nio.ByteBuffer;

import netcode.packet.Packet;
import netcode.packet.PacketType;
import simplemovement.mvc.Input;

public class InputPacket extends Packet {

	private Input input;
	private int moverID;

	public InputPacket(long timeReceived, ByteBuffer data) {
		super(timeReceived, data);
		byte[] arr = new byte[data.remaining()];
		data.get(arr);
		boolean down = ((arr[0] >> 0) & 0x1) == 0;
		boolean left = ((arr[0] >> 1) & 0x1) == 0; 
		boolean right = ((arr[0] >> 2) & 0x1) == 0; 
		boolean up = ((arr[0] >> 3) & 0x1) == 0;
		this.input = new Input(left, right, up, down, 0);
		this.moverID = byteArrayToInt(arr, 1);
	}
	
	public InputPacket(Input input, int id){
		super(PacketType.NEWVALUE);
		this.input = input;
		this.moverID = id;
	}
	
	public int getMoverID(){
		return moverID;
	}
	
	public Input getInput(){
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
