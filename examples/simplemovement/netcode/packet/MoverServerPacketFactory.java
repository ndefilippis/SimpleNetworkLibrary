package examples.simplemovement.netcode.packet;

import java.util.List;

import examples.simplemovement.mvc.Mover;
import examples.simplemovement.mvc.MoverState;
import netcode.packet.Acker;
import netcode.packet.Packet;
import netcode.packet.ServerPacketFactory;

public class MoverServerPacketFactory extends ServerPacketFactory{
	private int inputID;
	
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
		return new NewStatePacket(state, inputID++, this);
	}

	@Override
	public Packet createPacket(Packet p) {
		if(p instanceof NewStatePacket){
			return new NewStatePacket((NewStatePacket)p, inputID++, this);
		}
		if(p instanceof NewMoverPacket){
			return new NewMoverPacket((NewMoverPacket)p, this);
		}
		return null;
	}
}
