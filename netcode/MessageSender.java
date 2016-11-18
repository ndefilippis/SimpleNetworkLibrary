package netcode;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.LinkedList;
import java.util.Queue;

import netcode.packet.Packet;
import threading.RunnableLoop;

public class MessageSender extends RunnableLoop{
	private Queue<Message> messagesToSend;
	private ByteBuffer buf;
	private DatagramChannel channel;
	
	public MessageSender(DatagramChannel channel){
		super();
		buf = ByteBuffer.allocate(512);
		this.messagesToSend = new LinkedList<Message>();
		this.channel = channel;
	}
	
	public void addMessage(Packet p, SocketAddress address, int delay){
		synchronized(messagesToSend){
			messagesToSend.offer(new Message(p, address, delay));
		}
	}
	@Override
	protected void update() {
		long time = System.nanoTime();
		synchronized(messagesToSend){
			while(!messagesToSend.isEmpty() && messagesToSend.peek().readyToSend(time)){
				Message m = messagesToSend.poll();
				sendMessage(m.getPacket(), m.getAddress());
			}
		}
	}
	
	private void sendMessage(Packet packet, SocketAddress address){
		try {
			buf.clear();
			packet.toByteBuffer(buf, System.nanoTime());
			buf.flip();
			channel.send(buf, address);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
			return delay * 1000000 < currTime - this.time;
		}
	}
}
