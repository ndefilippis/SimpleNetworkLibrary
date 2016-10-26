package netcode;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import netcode.packet.Packet;

public abstract class Client extends RunnableLoop{
	private DatagramChannel channel;
	private Thread messageSendThread;
	private Thread messageRecvThread;
	protected MessageReceiver messageRecvQueue;
	protected MessageSender messageSendQueue;
	
	public Client(String address, int port) throws IOException{
		messageSendQueue = new MessageSender(channel);
		messageSendThread = new Thread(messageSendQueue);
		messageRecvQueue = new MessageReceiver(channel);
		messageRecvThread = new Thread(messageRecvQueue);
		channel = DatagramChannel.open();
		channel.connect(new InetSocketAddress(address, port));
		channel.configureBlocking(false);
	}

	@Override
	public void run(){
		messageSendThread.start();
		messageRecvThread.start();
		super.run();
		messageSendQueue.kill();
		messageRecvQueue.kill();
	}

	@Override
	protected void update() {
		ReceivedMessage latest;
		if((latest = messageRecvQueue.getLatestData()) != null){
			Packet packet = messageToPacket(latest.getData(), latest.getTimeReceived());
			handlePacket(packet);
		}
	}
	
	protected abstract Packet messageToPacket(byte[] message, long timeReceived); 

	protected abstract void handlePacket(Packet packet);
}
