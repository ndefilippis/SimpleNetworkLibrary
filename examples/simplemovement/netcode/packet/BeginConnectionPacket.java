package examples.simplemovement.netcode.packet;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import examples.simplemovement.mvc.Mover;
import netcode.packet.Packet;
import netcode.packet.PacketType;

public class BeginConnectionPacket extends Packet{
	private List<Mover> movers;
	private int clientID;
	
	public BeginConnectionPacket(long timeReceived, ByteBuffer data) {
		super(timeReceived, data);
		movers = new ArrayList<Mover>();
		byte[] arr = new byte[data.remaining()];
		data.get(arr);
		int count = byteArrayToInt(arr, 0);
		for(int i = 0; i < count; i++){
			Mover m;
			int id = byteArrayToInt(arr, 4 + i * 12);
			int x =  byteArrayToInt(arr, 8 + i * 12);
			int y =  byteArrayToInt(arr, 12+ i * 12);
			m = new Mover(id, x, y);
			movers.add(m);
		}
		this.clientID = byteArrayToInt(arr, arr.length - 4);
	}
	
	public BeginConnectionPacket(List<Mover> movers, long clientID){
		super(PacketType.ACCEPTCONNECT);
		this.movers = movers;
		this.clientID = (int)clientID;
	}

	public List<Mover> getMovers(){
		return movers;
	}
	
	public int getID(){
		return clientID;
	}
	
	@Override
	protected byte[] encodeData() {
		byte[] arr = new byte[movers.size()*3*4 + 8];
		intToByteArray(movers.size(), arr, 0);
		for(int i = 0; i < movers.size(); i++){
			Mover m = movers.get(i);
			intToByteArray(m.getID(), arr, 4 + i * 12);
			intToByteArray(m.getX(),  arr, 8 + i * 12);
			intToByteArray(m.getY(),  arr, 12+ i * 12);
		}
		intToByteArray(clientID, arr, arr.length - 4);
		return arr;
	}
	
}
