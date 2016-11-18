package examples.simplemovement.netcode;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;

import examples.simplemovement.mvc.Mover;
import examples.simplemovement.mvc.MoverInput;
import examples.simplemovement.mvc.MoverPlane;
import examples.simplemovement.mvc.MoverState;
import examples.simplemovement.netcode.packet.MoverInputPacket;
import examples.simplemovement.netcode.packet.MoverServerPacketFactory;
import netcode.GameUpdateLoop;
import netcode.Server;
import netcode.packet.Packet;
import threading.GameLoop;

public class MoverServer extends Server{
	private MoverPlane model;
	private MoverServerPacketFactory factory;

	private int nextID = 0;
	
	public MoverServer(int port) throws IOException{
		super(port);
		this.model = new MoverPlane();
		this.factory = new MoverServerPacketFactory(acker);
	}
	
	@Override
	public void run(){
		new Thread(new GameLoop<MoverPlane>(model, 16)).start();
		new Thread(new MoverUpdateLoop(50L, this, model));
		super.run();
	}
	
	public void processMessage(ByteBuffer message, SocketAddress address, long timeReceived){
		switch(Packet.lookupPacket(message)){
		case CONNECT:
			Mover m = new Mover(nextID++, (int)(100 * Math.random()), (int)(-100 * Math.random()));
			model.addMover(m);
			addMessageToAll(factory.createNewMoverPacket(m));
			addMessage(factory.createBeginConnectionPacket(model.getMovers(), m.getID()), address);
			break;
		case DISCONNECT:
			removeClient(address);
			break;
		case NEWVALUE:
			handleInput(new MoverInputPacket(timeReceived, message), 15/1000D);
			break;
		default:
			break;
		}
	}
	
	private Mover handleInput(MoverInputPacket packet, double dt){
		for(Mover m : model.getMovers()){
			if(m.getID() == packet.getMoverID()){
				handleInput(m, packet.getInput(), dt);
				return m;
			}
		}
		return null;
	}
	
	private void handleInput(Mover mover, MoverInput input, double dt){
		int dx = 0;
		int dy = 0;
		if(input.left){
			dx += 1;
		}
		if(input.right){
			dx -= 1;
		}
		if(input.down){
			dy += 1;
		}
		if(input.up){
			dy -= 1;
		}
		mover.setSpeed(dx, dy);
	}

	private class MoverUpdateLoop extends GameUpdateLoop<MoverPlane, MoverState>{

		public MoverUpdateLoop(long milliDelay, Server server, MoverPlane model) {
			super(milliDelay, server, model);
		}

		@Override
		protected Packet serializeState(MoverState state) {
			return factory.createNewStatePacket(state);
		}


		
	}
}
