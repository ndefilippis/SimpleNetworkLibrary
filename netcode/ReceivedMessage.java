package netcode;

import java.net.SocketAddress;
import java.nio.ByteBuffer;

public class ReceivedMessage{
	private SocketAddress recvFrom;
	private ByteBuffer data;
	private long timeReceived;
	
	public ReceivedMessage(SocketAddress recvFrom, ByteBuffer rawData, long timeReceived){
		this.recvFrom = recvFrom;
		rawData.rewind();
		this.data = ByteBuffer.allocate(rawData.remaining());
		data.put(rawData);
		data.flip();
		this.timeReceived = timeReceived;
	}
	
	SocketAddress getSender(){
		return recvFrom;
	}
	
	ByteBuffer getData(){
		return data;
	}
	
	long getTimeReceived(){
		return timeReceived;
	}
}
