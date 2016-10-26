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
	protected MessageReceiver messageRecvQueue;
	protected MessageSender messageSendQueue;
	protected int port;
	private int nextID = 0;
	
	private static final int MILLIS_DELAY = 0;
	
	public Server(int port) throws IOException{
		messageSendQueue = new MessageSender(channel);
		messageSendThread = new Thread(messageSendQueue);
		messageRecvQueue = new MessageReceiver(channel);
		messageRecvThread = new Thread(messageRecvQueue);
		channel = DatagramChannel.open();
		channel.socket().bind(new InetSocketAddress(port));
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
	
	protected void addClient(SocketAddress address){
		clients.put(address, nextID++);
	}
	
	protected void removeClient(SocketAddress address){
		clients.remove(address);
	}
	
	protected void addMessage(Packet packet, SocketAddress address){
		messageSendQueue.addMessage(packet, address, MILLIS_DELAY);
	}
	
	protected void addMessageToAll(Packet packet) {
		for(SocketAddress addr : getClients()){
			messageSendQueue.addMessage(packet, addr, MILLIS_DELAY);
		}
	}
	
	@Override
	protected void update(){
		ReceivedMessage message = messageRecvQueue.getLatestData();
		if(message != null){
			if(!clients.containsKey(message.getSender())){
				addClient(message.getSender());
			}
			processMessage(message.getData(), message.getSender(), message.getTimeReceived());
		}
	}
	
	public abstract void processMessage(ByteBuffer message, SocketAddress address, long timeReceived);
	
	public Collection<SocketAddress> getClients(){
		return clients.keySet();
	}
	
	public int getPort(){
		return port;
	}
	
}
