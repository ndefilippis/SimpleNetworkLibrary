package examples.counter.netcode;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;

import examples.counter.mvc.Counter;
import examples.counter.mvc.ServerCounterViewer;
import examples.counter.netcode.packet.CounterPacket;
import examples.counter.netcode.packet.CounterServerPacketFactory;
import netcode.Server;
import netcode.packet.Acker;
import netcode.packet.Packet;

public class CounterServer extends Server{
	private Counter counter;
	
	private CounterServerPacketFactory factory;
	private ServerCounterViewer view;
	
	private long nextID = 0L;
	
	public CounterServer(int port) throws IOException{
		super(port);
		this.counter = new Counter();
		this.acker = new Acker();
		this.factory = new CounterServerPacketFactory(this.acker);
		view = new ServerCounterViewer(counter.getValue());
		counter.addObserver(view);
	}
	
	@Override
	public void run(){
		view.setVisible(true);
		super.run();
	}
	
	@Override
	public void processMessage(ByteBuffer message, SocketAddress address, long timeReceived) {
		switch(Packet.lookupPacket(message)){
		case CONNECT:
			addMessage(factory.createAcceptConnectPacket(nextID++), address);
			addMessage(factory.createChangeValuePacket(counter.getValue()), address);
			break;
		case DISCONNECT:
			removeClient(address);
			break;
		case INPUT:
			handleInput(new CounterPacket(timeReceived, message));
			addMessageToAll(factory.createChangeValuePacket(counter.getValue()));
			break;
		default:
			break;
		}
	}
	
	private void handleInput(CounterPacket packet){
		if(packet.isIncrement()){
			counter.incrementValue();
		}
		else{
			counter.decrementValue();
		}
	}
}
