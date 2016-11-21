package examples.simplemovement.netcode;

import java.net.SocketAddress;

import examples.simplemovement.netcode.packet.MoverServerPacketFactory;
import netcode.Handler;
import netcode.packet.Acker;

public class MoverHandler extends Handler<MoverServerPacketFactory>{

	public MoverHandler(SocketAddress address, int id) {
		super(address, id);
	}

	@Override
	protected MoverServerPacketFactory createPacketFactory(Acker acker) {
		return new MoverServerPacketFactory(acker);
	}

}
