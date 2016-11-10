package examples.counter.netcode;

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

import examples.counter.mvc.Counter;
import examples.counter.mvc.ServerCounterViewer;
import examples.counter.netcode.packet.ChangeValuePacket;
import examples.counter.netcode.packet.CounterPacket;
import netcode.Server;
import netcode.packet.AcceptConnectPacket;
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
		switch(Packet.lookupPacket(message)){
		case CONNECT:
			addMessage(new AcceptConnectPacket(nextID++), address);
			addMessage(new ChangeValuePacket(counter.getValue(), System.nanoTime()), address);
			break;
		case DISCONNECT:
			removeClient(address);
			break;
		case INPUT:
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
