package netcode;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Queue;

public class MessageReceiver extends RunnableLoop{
	private DatagramChannel channel;
	private ByteBuffer buf;
	private Queue<ReceivedMessage> receivedMessages;
	
	public MessageReceiver(DatagramChannel channel){
		super();
		this.channel = channel;
	}

	@Override
	protected void update() {
		try {
			SocketAddress address = channel.receive(buf);
			if(address != null){
				receivedMessages.offer(new ReceivedMessage(address, buf, System.nanoTime()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ReceivedMessage getLatestData(){
		if(receivedMessages.isEmpty()){
			return null;
		}
		return receivedMessages.poll();
	}
	
	
}
