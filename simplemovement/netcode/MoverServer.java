package simplemovement.netcode;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.HashMap;
import java.util.Map;

import netcode.Server;
import netcode.packet.Packet;
import simplemovement.GameLoop;
import simplemovement.mvc.Input;
import simplemovement.mvc.Mover;
import simplemovement.mvc.MoverPlane;
import simplemovement.netcode.packet.BeginConnectionPacket;
import simplemovement.netcode.packet.InputPacket;
import simplemovement.netcode.packet.MoverChangePacket;
import simplemovement.netcode.packet.NewMoverPacket;

public class MoverServer extends Server{
	MoverPlane model;
	DatagramChannel channel;
	Map<SocketAddress, Integer> clients = new HashMap<SocketAddress, Integer>();
	Map<Mover, Input> currentInputs = new HashMap<Mover, Input>();
	ByteBuffer buf;
	
	private int nextID = 0;
	private long time;
	
	public MoverServer(int port) throws IOException{
		this.model = new MoverPlane();
		channel = DatagramChannel.open();
		channel.socket().bind(new InetSocketAddress(port));
		channel.configureBlocking(false);
	}
	
	public void start() throws IOException{
		time = System.nanoTime();
		new Thread(new GameLoop(model)).start();
		while(true){
			time = System.nanoTime();
			ByteBuffer buf = ByteBuffer.allocate(64);
			buf.clear();
			for(Mover m : model.getMovers()){
				addMessageToAll(new MoverChangePacket(m, time - lastTime));
			}
			SocketAddress clientAddress = channel.receive(buf);
			if(clientAddress != null){
				if(!clients.containsKey(clientAddress)){
					clients.put(clientAddress, nextID);
				}
				buf.flip();
				processMessge(buf, System.nanoTime(), clientAddress);
			}
		}
	}
	
	public void processMessge(ByteBuffer message, long timeReceived, SocketAddress address){
		switch(Packet.lookupPacket(message)){
		case CONNECT:
			Mover m = new Mover(nextID++, (int)(100 * Math.random()), (int)(-100 * Math.random()));
			currentInputs.put(m, new Input(false, false, false, false, 0));
			model.addMover(m);
			addMessage(new BeginConnectionPacket(model.getMovers(), m.getID()), address);
			addMessageToAll(new NewMoverPacket(m));
			break;
		case DISCONNECT:
			clients.remove(address);
			break;
		case NEWVALUE:
			handleInput(new InputPacket(timeReceived, message), 15/1000D);
			break;
		default:
			break;
		}
	}
	
	private Mover handleInput(InputPacket packet, double dt){
		for(Mover m : model.getMovers()){
			if(m.getID() == packet.getMoverID()){
				handleInput(m, packet.getInput(), dt);
				return m;
			}
		}
		return null;
	}
	
	private void handleInput(Mover mover, Input input, double dt){
		int dx = 0;
		int dy = 0;
		if(input.left){
			dx += 1;
		}
		if(input.right){
			dx -= 1;
		}
		if(input.down){
			dy += 1;
		}
		if(input.up){
			dy -= 1;
		}
		mover.setSpeed(dx, dy);
	}

	public void sendMessage(Packet packet, SocketAddress address){
		try {
			buf = packet.toByteBuffer(System.nanoTime());
			//System.out.println("SEND:" + ByteBufferConverter.bbArray(buf) + address.toString());
			channel.send(buf, address);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}