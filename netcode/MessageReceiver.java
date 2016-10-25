package netcode;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Queue;

public class MessageReceiver extends RunnableLoop{
	private DatagramChannel channel;
	private ByteBuffer buf;
	private Queue<Message> receivedMessages;
	
	public MessageReceiver(DatagramChannel channel){
		super();
		this.channel = channel;
	}

	@Override
	protected void update() {
		try {
			SocketAddress address = channel.receive(buf);
			receivedMessages.offer(new Message(address, buf));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public SocketAddress getLatestData(ByteBuffer dst){
		if(receivedMessages.isEmpty()){
			return null;
		}
		Message nextMessage = receivedMessages.poll();
		dst.put(nextMessage.data);
		return nextMessage.recvFrom;
	}
	
	private class Message{
		private SocketAddress recvFrom;
		private byte[] data;
		
		public Message(SocketAddress recvFrom, ByteBuffer rawData){
			this.recvFrom = recvFrom;
			this.data = new byte[rawData.remaining()];
			rawData.get(data);
		}
	}
}
