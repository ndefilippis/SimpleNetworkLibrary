package examples.simplemovement.netcode.packet;

import examples.simplemovement.mvc.MoverInput;
import netcode.packet.Acker;
import netcode.packet.ClientPacketFactory;

public class MoverClientPacketFactory extends ClientPacketFactory{

	public MoverClientPacketFactory(Acker acker) {
		super(acker);
	}

	public MoverInputPacket createMoverInputPacket(long tick, MoverInput input, int id){
		return new MoverInputPacket(tick, input, id, this);
	}
	
}
