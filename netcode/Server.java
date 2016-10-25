package netcode;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Collection;
import java.util.Map;

import netcode.packet.Packet;

public abstract class Server extends RunnableLoop{
	private DatagramChannel channel;
	private Map<SocketAddress, Integer> clients;
	private Thread messageSendThread;
	private Thread messageRecvThread;
	protected ByteBuffer buf;
	protected MessageReceiver messageRecvQueue;
	protected MessageSender messageSendQueue;
	protected int port;
	
	private int nextID = 0;
	
	public Server(int port) throws IOException{
		messageSendQueue = new MessageSender(channel);
		messageSendThread = new Thread(messageSendQueue);
		messageRecvQueue = new MessageReceiver(channel);
		messageRecvThread = new Thread(messageRecvQueue);
		channel = DatagramChannel.open();
		channel.socket().bind(new InetSocketAddress(port));
		channel.configureBlocking(false);
		buf = ByteBuffer.allocate(1024);
	}
	
	@Override
	public void run(){
		messageSendThread.start();
		messageRecvThread.start();
		super.run();
		messageSendQueue.kill();
		messageRecvQueue.kill();
	}
	
	public void addMessageToAll(Packet packet, int delay) {
		for(SocketAddress addr : getClients()){
			messageSendQueue.addMessage(packet, addr, delay);
		}
	}
	
	@Override
	protected void update(){
		SocketAddress sender = messageRecvQueue.getLatestData(buf);
		if(sender != null){
			processMessage(buf, System.nanoTime(), sender);
		}
	}
	
	public abstract void processMessage(ByteBuffer message, long timeReceived, SocketAddress address);
	
	public Collection<SocketAddress> getClients(){
		return clients.keySet();
	}
	
	public int getPort(){
		return port;
	}
	
}
