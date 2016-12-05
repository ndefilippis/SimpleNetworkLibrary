package examples.counter.netcode.packet;

import netcode.packet.Acker;
import netcode.packet.Packet;
import netcode.packet.ServerPacketFactory;

public class CounterServerPacketFactory extends ServerPacketFactory{

	public CounterServerPacketFactory(Acker acker) {
		super(acker);
	}
	public ChangeValuePacket createChangeValuePacket(int value){
		return new ChangeValuePacket(value, this);
	}
	@Override
	public Packet createPacket(Packet p) {
		if(p instanceof ChangeValuePacket){
			return new ChangeValuePacket((ChangeValuePacket)p, this);
		}
		return null;
	}
	

}
