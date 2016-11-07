package counter.netcode;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import counter.mvc.Counter;
import counter.mvc.ServerCounterViewer;
import counter.netcode.packet.CounterPacket;
import netcode.Server;
import netcode.packet.AcceptConnectPacket;
import netcode.packet.ChangeValuePacket;
import netcode.packet.Packet;

public class CounterServer extends Server{
	private Counter counter;
	private ServerCounterViewer view;
	
	private long nextID = 0L;
	
	public CounterServer(int port) throws IOException{
		super(port);
		this.counter = new Counter();
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
		System.out.println(Packet.lookupPacket(message)); 
		switch(Packet.lookupPacket(message)){
		case CONNECT:
			addMessage(new AcceptConnectPacket(counter.getValue(), nextID++), address);
			break;
		case DISCONNECT:
			removeClient(address);
			break;
		case COUNTER:
			handleInput(new CounterPacket(timeReceived, message));
			addMessageToAll(new ChangeValuePacket(counter.getValue(), System.nanoTime()));
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
