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
	Counter counter;
	Queue<Message> messagesToSend = new LinkedList<Message>();
	private ServerCounterViewer view;
	
	private static final int MILLI_DELAY = 100;
	private long nextID = 0L;
	private long time;
	private long id = -1L;
	
	public CounterServer(int port) throws IOException{
		super(port);
		this.counter = new Counter();
		view = new ServerCounterViewer(counter.getValue());
		counter.addObserver(view);
	}
	
	public void start() throws IOException{
		time = System.nanoTime();
		view.setVisible(true);
		while(true){
			while(messagesToSend.size() > 0 && messagesToSend.peek().readyToSend(time)){
				Message m = messagesToSend.poll();
				sendMessage(m.getPacket(), m.getAddress());
			}
			time = System.nanoTime();
			ByteBuffer buf = ByteBuffer.allocate(64);
			buf.clear();
			
			SocketAddress clientAddress = channel.receive(buf);
			if(clientAddress != null){
				if(!clients.containsKey(clientAddress)){
					clients.put(clientAddress, nextID);
				}
				buf.flip();
				processMessge(buf, System.nanoTime(), clientAddress);
			}
		}
	}
	
	public void processMessge(ByteBuffer message, long timeReceived, SocketAddress address){
		switch(Packet.lookupPacket(message)){
		case CONNECT:
			addMessage(new AcceptConnectPacket(counter.getValue(), nextID++), address);
			break;
		case DISCONNECT:
			clients.remove(address);
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
	
	private void addMessageToAll(Packet packet) {
		for(SocketAddress addr : clients.keySet()){
			addMessage(packet, addr);
		}
	}

	public void sendMessage(Packet packet, SocketAddress address){
		try {
			buf = packet.toByteBuffer(System.nanoTime());
			//System.out.println("SEND:" + ByteBufferConverter.bbArray(buf) + address.toString());
			channel.send(buf, address);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void addMessage(Packet p, SocketAddress address){
		messagesToSend.offer(new Message(p, address, MILLI_DELAY));
	}
	
	public static void main(String[] args) throws IOException{
		CounterServer s = new CounterServer(1337);
		s.start();
		
	}

	private class Message{
		private Packet p;
		private SocketAddress address;
		private int delay;
		private long time;
		
		public Message(Packet p, SocketAddress address, int milliDelay){
			this.p = p;
			this.address = address;
			this.delay = milliDelay;
			time = System.nanoTime();
		}
		
		public Packet getPacket(){
			return p;
		}
		
		public SocketAddress getAddress(){
			return address;
		}
		
		public boolean readyToSend(long currTime){
			return delay < currTime - this.time;
		}
		
	}
}
