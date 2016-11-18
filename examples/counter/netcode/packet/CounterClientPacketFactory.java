package examples.counter.netcode.packet;

import examples.counter.mvc.CounterInput;
import netcode.packet.Acker;
import netcode.packet.ClientPacketFactory;

public class CounterClientPacketFactory extends ClientPacketFactory{

	public CounterClientPacketFactory(Acker acker) {
		super(acker);
	}
	
	public CounterPacket createCounterPacket(CounterInput input){
		return new CounterPacket(input, this);
	}

}
