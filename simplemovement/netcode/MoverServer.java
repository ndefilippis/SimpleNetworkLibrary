package simplemovement.netcode;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import netcode.packet.Packet;
import simplemovement.GameLoop;
import simplemovement.mvc.Input;
import simplemovement.mvc.Mover;
import simplemovement.mvc.MoverPlane;
import simplemovement.netcode.packet.BeginConnectionPacket;
import simplemovement.netcode.packet.InputPacket;
import simplemovement.netcode.packet.MoverChangePacket;
import simplemovement.netcode.packet.NewMoverPacket;

public class MoverServer {
	MoverPlane model;
	DatagramChannel channel;
	Map<SocketAddress, Integer> clients = new HashMap<SocketAddress, Integer>();
	Map<Mover, Input> currentInputs = new HashMap<Mover, Input>();
	Queue<Message> messagesToSend = new LinkedList<Message>();
	ByteBuffer buf;
	
	private static final int MILLI_DELAY = 0;
	private int nextID = 0;
	private long time;
	
	public MoverServer(int port) throws IOException{
		this.model = new MoverPlane();
		channel = DatagramChannel.open();
		channel.socket().bind(new InetSocketAddress(port));
		channel.configureBlocking(false);
	}
	
	public void start() throws IOException{
		time = System.nanoTime();
		long lastTime = time;
		new Thread(new GameLoop(model)).start();
		while(true){
			lastTime = time;
			time = System.nanoTime();
			while(messagesToSend.size() > 0 && messagesToSend.peek().readyToSend(time)){
				Message m = messagesToSend.poll();
				sendMessage(m.getPacket(), m.getAddress());
			}
			for(Mover m : model.getMovers()){
				addMessageToAll(new MoverChangePacket(m, time - lastTime));
			}
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
			Mover m = new Mover(nextID++, (int)(100 * Math.random()), (int)(-100 * Math.random()));
			currentInputs.put(m, new Input(false, false, false, false, 0));
			model.addMover(m);
			addMessage(new BeginConnectionPacket(model.getMovers(), m.getID()), address);
			addMessageToAll(new NewMoverPacket(m));
			break;
		case DISCONNECT:
			clients.remove(address);
			break;
		case NEWVALUE:
			handleInput(new InputPacket(timeReceived, message), 15/1000D);
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
