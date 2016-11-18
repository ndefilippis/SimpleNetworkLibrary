package examples.simplemovement.netcode.packet;

import java.nio.ByteBuffer;
import java.util.ArrayList;
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
		int count = data.getInt();
		for(int i = 0; i < count; i++){
			Mover m = new Mover(data);
			movers.add(m);
		}
		this.clientID = data.getInt();
	}
	
	BeginConnectionPacket(List<Mover> movers, long clientID, MoverServerPacketFactory factory){
		super(PacketType.ACCEPTCONNECT, factory);
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
	protected void encodeData(ByteBuffer buffer) {
		buffer.putInt(movers.size());
		for(int i = 0; i < movers.size(); i++){
			movers.get(i).serializeWrite(buffer);
		}
		buffer.putInt(clientID);
	}
	
}
