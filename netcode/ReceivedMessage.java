package netcode;

import java.net.SocketAddress;
import java.nio.ByteBuffer;

public class ReceivedMessage{
	private SocketAddress recvFrom;
	private byte[] data;
	private long timeReceived;
	
	public ReceivedMessage(SocketAddress recvFrom, ByteBuffer rawData, long timeReceived){
		this.recvFrom = recvFrom;
		this.data = new byte[rawData.remaining()];
		rawData.get(data);
		this.timeReceived = timeReceived;
	}
	
	SocketAddress getSender(){
		return recvFrom;
	}
	
	byte[] getData(){
		return data;
	}
	
	long getTimeReceived(){
		return timeReceived;
	}
}
