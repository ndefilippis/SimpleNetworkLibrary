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
import examples.simplemovement.netcode.packet.NewMoverPacket;
import examples.simplemovement.netcode.packet.NewStatePacket;
import netcode.GameUpdateLoop;
import netcode.Server;
import netcode.packet.Packet;
import threading.GameLoop;

public class MoverServer extends Server<MoverServerPacketFactory, MoverHandler>{
	private MoverPlane model;

	private int nextID = 0;
	
	public MoverServer(int port) throws IOException{
		super(port);
		this.model = new MoverPlane();
	}
	
	@Override
	public void run(){
		new Thread(new GameLoop<MoverPlane>(model, 16)).start();
		new Thread(new MoverUpdateLoop(50L));
		super.run();
	}
	
	public void processMessage(ByteBuffer message, SocketAddress address, long timeReceived, MoverHandler handler){
		switch(Packet.lookupPacket(message)){
		case CONNECT:
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
	
	private class MoverUpdateLoop extends GameUpdateLoop{

		public MoverUpdateLoop(long milliDelay) {
			super(milliDelay);
		}

		@Override
		protected void onUpdate() {
			addMessageToAll(NewStatePacket.createDefaultPacket(model.getState()));
		}		
	}

	@Override
	protected MoverHandler createNewHandler(SocketAddress address, int id) {
		return new MoverHandler(address, id);
	}

	@Override
	protected void beforeAddClient(SocketAddress address, MoverServerPacketFactory factory) {
		Mover m = new Mover(nextID++, (int)(100 * Math.random()), (int)(-100 * Math.random()));
		model.addMover(m);
		addMessageToAll(NewMoverPacket.createDefaultPacket(m));
		addMessage(factory.createBeginConnectionPacket(model.getMovers(), m.getID()), address);
	}
}
