package netcode;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import netcode.packet.Packet;

public abstract class Client extends RunnableLoop{
	private DatagramChannel channel;
	private ByteBuffer buf;
	protected MessageSender messageSendQueue;
	protected MessageReceiver messageRecvQueue;
	
	private long id;
	
	public Client(String address, int port) throws IOException{
		channel = DatagramChannel.open();
		channel.connect(new InetSocketAddress(address, port));
		channel.configureBlocking(false);
		buf.allocate(1024);
	}

	@Override
	protected void update() {
		if(messageRecvQueue.getLatestData(buf) != null){
			Packet packet = messageToPacket(buf, System.nanoTime());
			handlePacket(packet);
		}
	}
	
	protected abstract Packet messageToPacket(ByteBuffer message, long timeReceived); 

	protected abstract void handlePacket(Packet packet);
}
