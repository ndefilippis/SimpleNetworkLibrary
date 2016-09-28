package simplemovement;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import mvc.Counter;
import mvc.ServerCounterViewer;
import netcode.packet.AcceptConnectPacket;
import netcode.packet.ChangeValuePacket;
import netcode.packet.CounterPacket;
import netcode.packet.Packet;

public class MoverServer {
	MoverPlane model;
	DatagramChannel channel;
	Map<SocketAddress, Long> clients = new HashMap<SocketAddress, Long>();
	Queue<Message> messagesToSend = new LinkedList<Message>();
	ByteBuffer buf;
	
	private static final int MILLI_DELAY = 100;
	private long nextID = 0L;
	private long time;
	
	public MoverServer(int port) throws IOException{
		this.model = new MoverPlane();
		channel = DatagramChannel.open();
		channel.socket().bind(new InetSocketAddress(port));
		channel.configureBlocking(false);
	}
	
	public void start() throws IOException{
		time = System.nanoTime();
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
			addMessage(new BeginConnectionPacket(model.getMovers(), nextID++), address);
			break;
		case DISCONNECT:
			clients.remove(address);
			break;
		case COUNTER:
			Mover temp = handleInput(new InputPacket(timeReceived, message), 0);
			addMessageToAll(new MoverChangePacket(temp, temp.getX(), temp.getY(), System.nanoTime()));
			break;
		default:
			break;
		}
	}
	
	private Mover handleInput(InputPacket packet, double dt){
		for(Mover m : model.getMovers()){
			if(m.getID() == packet.getMoverID()){
				handleInput(m, packet.getInput(), dt);
				return m;
			}
		}
		return null;
	}
	
	private void handleInput(Mover mover, Input input, double dt){
		int dx = 0;
		int dy = 0;
		if(input.left){
			dx -= 1;
		}
		if(input.right){
			dx += 1;
		}
		if(input.down){
			dy -= 1;
		}
		if(input.up){
			dy += 1;
		}
		mover.move(dx, dy, dt);
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
		MoverServer s = new MoverServer(1337);
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
