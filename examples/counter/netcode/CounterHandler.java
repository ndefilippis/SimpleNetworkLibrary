package examples.counter.netcode;

import java.net.SocketAddress;

import examples.counter.netcode.packet.CounterServerPacketFactory;
import netcode.Handler;
import netcode.packet.Acker;

public class CounterHandler extends Handler<CounterServerPacketFactory>{

	public CounterHandler(SocketAddress address, int id) {
		super(address, id);
	}

	@Override
	protected CounterServerPacketFactory createPacketFactory(Acker acker) {
		return new CounterServerPacketFactory(acker);
	}
}
