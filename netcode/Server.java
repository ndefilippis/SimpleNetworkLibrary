package netcode;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Collection;
import java.util.HashMap;
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
		channel = DatagramChannel.open();
		channel.socket().bind(new InetSocketAddress(port));
		channel.configureBlocking(false);
		messageSendQueue = new MessageSender(channel);
		messageSendThread = new Thread(messageSendQueue);
		messageRecvQueue = new MessageReceiver(channel);
		messageRecvThread = new Thread(messageRecvQueue);	
		clients = new HashMap<SocketAddress, Integer>();
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
	
	@Override
	public void kill(){
		super.kill();
		try {
			channel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		if(messageRecvQueue.hasMessages()){
			ReceivedMessage message = messageRecvQueue.getLatestData();
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
