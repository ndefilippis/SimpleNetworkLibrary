package examples.simplemovement.netcode.packet;

import java.util.List;

import examples.simplemovement.mvc.Mover;
import examples.simplemovement.mvc.MoverState;
import netcode.packet.Acker;
import netcode.packet.ServerPacketFactory;

public class MoverServerPacketFactory extends ServerPacketFactory{

	public MoverServerPacketFactory(Acker acker) {
		super(acker);
	}
	
	public BeginConnectionPacket createBeginConnectionPacket(List<Mover> movers, int id){
		return new BeginConnectionPacket(movers, id, this);
	}

	public NewMoverPacket createNewMoverPacket(Mover m){
		return new NewMoverPacket(m, this);
	}
	
	
	public NewStatePacket createNewStatePacket(MoverState state){
		return new NewStatePacket(state, this);
	}
}
