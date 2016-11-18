package examples.counter.netcode.packet;

import netcode.packet.Acker;
import netcode.packet.ServerPacketFactory;

public class CounterServerPacketFactory extends ServerPacketFactory{

	public CounterServerPacketFactory(Acker acker) {
		super(acker);
	}
	public ChangeValuePacket createChangeValuePacket(int value){
		return new ChangeValuePacket(value, this);
	}

}
