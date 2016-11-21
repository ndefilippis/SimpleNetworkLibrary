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
import netcode.packet.ServerPacketFactory;
import threading.RunnableLoop;

public abstract class Server<F extends ServerPacketFactory, H extends Handler<F>> extends RunnableLoop{
	private DatagramChannel channel;
	private Map<SocketAddress, H> clients;
	private Thread messageSendThread;
	private Thread messageRecvThread;
	protected MessageReceiver messageRecvQueue;
	protected MessageSender messageSendQueue; 
	protected int port;
	private int nextID = 0;
	
	private static final int MILLIS_DELAY = 00;
	
	public Server(int port) throws IOException{
		channel = DatagramChannel.open();
		channel.socket().bind(new InetSocketAddress(port));
		channel.configureBlocking(false);
		messageSendQueue = new MessageSender(channel);
		messageSendThread = new Thread(messageSendQueue);
		messageRecvQueue = new MessageReceiver(channel);
		messageRecvThread = new Thread(messageRecvQueue);	
		clients = new HashMap<SocketAddress, H>();
	}
	
	@Override
	public void run(){
		messageSendThread.start();
		messageRecvThread.start();
		super.run();
	}
	
	protected abstract void beforeAddClient(SocketAddress address, F factory);
	
	protected void addClient(SocketAddress address){
		H handler = createNewHandler(address, nextID++);
		beforeAddClient(address, handler.getPacketFactory());
		clients.put(address, handler);
	}
	
	protected abstract H createNewHandler(SocketAddress address, int id);
	
	protected void removeClient(SocketAddress address){
		clients.remove(address);
	}
	
	@Override
	public void kill(){
		messageSendQueue.kill();
		messageRecvQueue.kill();
		super.kill();
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
	
	protected void addMessage(Packet packet, SocketAddress address){
		messageSendQueue.addMessage(packet, address, MILLIS_DELAY);
	}

	protected <E extends Packet> void addMessageToAll(Class<E> packetType, Object... params) {
		for(SocketAddress addr : getClients()){
			Packet packet = clients.get(addr).getPacketFactory().createPacket(packetType, params);
			messageSendQueue.addMessage(packet, addr, MILLIS_DELAY);
		}
	}
	
	@Override
	protected void update(){
		if(messageRecvQueue.hasMessages()){
			ReceivedMessage message = messageRecvQueue.getLatestData();
			if(clients.containsKey(message.getSender())){
				processMessage(message.getData(), message.getSender(), message.getTimeReceived(), clients.get(message.getSender()));
			}
			else{
				addClient(message.getSender());
			}
			
		}
	}
	
	public abstract void processMessage(ByteBuffer message, SocketAddress address, long timeReceived, H handler);
	
	public Collection<SocketAddress> getClients(){
		return clients.keySet();
	}
	
	public int getPort(){
		return port;
	}
	
}
