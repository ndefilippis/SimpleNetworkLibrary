package netcode;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import netcode.packet.Acker;
import threading.RunnableLoop;

public class MessageReceiver extends RunnableLoop{
	private DatagramChannel channel;
	private ByteBuffer buf;
	private Acker acker;
	private volatile Queue<ReceivedMessage> receivedMessages;
	
	public MessageReceiver(DatagramChannel channel){
		super();
		this.acker = new Acker();
		buf = ByteBuffer.allocate(512);
		this.channel = channel;
		this.receivedMessages = new ConcurrentLinkedQueue<ReceivedMessage>();
	}

	@Override
	protected void update() {
		SocketAddress address = null;
		try {
			address = channel.receive(buf);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(address != null){
			receivedMessages.offer(new ReceivedMessage(address, buf, System.nanoTime()));
			buf.rewind();
		}
	}
	
	public ReceivedMessage getLatestData(){
		return receivedMessages.poll();
	}

	public boolean hasMessages() {
		return !receivedMessages.isEmpty();
	}
	
	
}
