package netcode;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import netcode.packet.Packet;
import threading.RunnableLoop;

public abstract class Client extends RunnableLoop{
	private DatagramChannel channel;
	private SocketAddress serverAddress;
	private Thread messageSendThread;
	private Thread messageRecvThread;
	protected MessageReceiver messageRecvQueue;
	protected MessageSender messageSendQueue;
	
	private static final int CLIENT_DELAY_MILLIS = 500;
	
	public Client(String address, int port) throws IOException{
		channel = DatagramChannel.open();
		this.serverAddress = new InetSocketAddress(address, port);
		channel.connect(serverAddress);
		channel.configureBlocking(false);
		messageSendQueue = new MessageSender(channel);
		messageSendThread = new Thread(messageSendQueue);
		messageRecvQueue = new MessageReceiver(channel);
		messageRecvThread = new Thread(messageRecvQueue);
	}
	
	@Override
	public void kill(){
		super.kill();
		messageSendQueue.kill();
		messageRecvQueue.kill();
		try {
			messageSendThread.join();
			messageRecvThread.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		try {
			channel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run(){
		messageSendThread.start();
		messageRecvThread.start();
		super.run();
	}

	@Override
	protected void update() {
		if(messageRecvQueue.hasMessages()){
			ReceivedMessage latest = messageRecvQueue.getLatestData();
			Packet packet = messageToPacket(latest.getData(), latest.getTimeReceived());
			handlePacket(packet);
		}
	}
	
	protected void addMessage(Packet packet){
		messageSendQueue.addMessage(packet, serverAddress, CLIENT_DELAY_MILLIS);
	}
	
	protected abstract Packet messageToPacket(ByteBuffer message, long timeReceived); 

	protected abstract void handlePacket(Packet packet);
}
