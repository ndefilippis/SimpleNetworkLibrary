package simplemovement.netcode;

import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import netcode.packet.ConnectPacket;
import netcode.packet.DisconnectPacket;
import netcode.packet.Packet;
import simplemovement.GameLoop;
import simplemovement.mvc.Input;
import simplemovement.mvc.InputListener;
import simplemovement.mvc.Mover;
import simplemovement.mvc.MoverController;
import simplemovement.mvc.MoverPlane;
import simplemovement.mvc.MoverViewer;
import simplemovement.netcode.packet.BeginConnectionPacket;
import simplemovement.netcode.packet.InputPacket;
import simplemovement.netcode.packet.MoverChangePacket;
import simplemovement.netcode.packet.NewMoverPacket;

public class MoverClient {
	private DatagramChannel channel;
	
	private Mover myMover;
	private MoverPlane model;
	private MoverViewer viewer;
	private MoverController controller;
	private InputListener listener;
	
	private ByteBuffer bufin;
	private ByteBuffer bufout;
	private volatile Queue<Input> knownInputs = new LinkedList<Input>();
	
	private long id;
	
	public MoverClient(String address, int port) throws IOException{
		channel = DatagramChannel.open();
		channel.connect(new InetSocketAddress(address, port));
		channel.configureBlocking(false);
		sendMessage(new ConnectPacket());
	}
	
	public void start(){
		long time = System.nanoTime();
		long lastTime = time;
		while(true){
			time = System.nanoTime();
			double dt = (time - lastTime)/1000000000D;
			ByteBuffer message = recieveMessage();
			if(message != null){
				long timeReceived = System.nanoTime();
				Packet packet = messageToPacket(message, timeReceived);
				handlePacket(packet);
			}
			lastTime = time;
		}	
	}
	
	public ByteBuffer recieveMessage(){
		bufout = ByteBuffer.allocate(64);
		bufout.clear();
		SocketAddress d = null;
		try {
			d = channel.receive(bufout);
		} catch (IOException e) {
			return null;
		}
		if(d == null){
			return null;
		}
		bufout.flip();
		//System.out.println("RECV:" + ByteBufferConverter.bbArray(bufout) + d.toString());
		return bufout;
	}
	
	public Packet messageToPacket(ByteBuffer message, long timeReceived){
		switch(Packet.lookupPacket(message)){
		case ACCEPTCONNECT:
			return new BeginConnectionPacket(timeReceived, message);
		case NEWVALUE:
			return new MoverChangePacket(timeReceived, message);
		case NEWPLAYER:
			return new NewMoverPacket(timeReceived, message);
		default:
			return null;
		}
	}
	
	public void handlePacket(Packet packet){
		switch(packet.getPacketType()){
			case ACCEPTCONNECT:
				handleConnect((BeginConnectionPacket)packet);
				break;
			case NEWVALUE:
				handleChangeValue((MoverChangePacket)packet);
				break;
			case NEWPLAYER:
				handleNewPlayer((NewMoverPacket)packet);
			default:
				break;
		}
	}
	
	private void handleConnect(BeginConnectionPacket packet){
		this.id = packet.getID();
		model = new MoverPlane();
		viewer = new MoverViewer(packet.getMovers());
		for(Mover m : packet.getMovers()){
			if(m.getID() == this.id){
				this.myMover = m;
			}
			model.addMover(m);
		}
		viewer.addWindowListener(new DisconnectListener(this));
		listener = new LagCompensatedInputListener();
		viewer.addInputListener(listener);
		new Thread(new GameLoop(model)).start();
		controller = new MoverController(model, viewer, myMover, listener);
		
		viewer.setVisible(true);
	}
	
	private void handleChangeValue(MoverChangePacket packet){
		for(Mover m : model.getMovers()){
			if(m.getID() == packet.getMoverID()){
				m.setCoords(packet.getX(), packet.getY());
			}
		}
		while(knownInputs.size() > 0 && knownInputs.peek().getTime() < packet.getUpdateTime()){
			knownInputs.poll();
		}
		synchronized(knownInputs){
			for(Iterator<Input> it = knownInputs.iterator(); it.hasNext();){
				Input i = it.next();
				controller.handleInput(i);
			}
		}
	}
	
	private void handleNewPlayer(NewMoverPacket packet){
		model.addMover(packet.getNewMover());
	}
	
	public void sendMessage(Packet packet){
		bufin = packet.toByteBuffer(System.nanoTime());
		try {
			channel.write(bufin);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private class LagCompensatedInputListener extends InputListener{
		
		@Override
		public void keyPressed(KeyEvent e) {
			Input newState = getNewState(e, true);
			currentState = newState;
			controller.handleInput(currentState);
			sendMessage(new InputPacket(listener.getState(), (int)id));
			synchronized(knownInputs){
				knownInputs.add(currentState);
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			Input newState = getNewState(e, false);
			currentState = newState;
			controller.handleInput(currentState);
			sendMessage(new InputPacket(listener.getState(), (int)id));
			synchronized(knownInputs){
				knownInputs.add(currentState);
			}
		}
	
		public Input getState(){
			return currentState;
		}
	}
	class DisconnectListener extends WindowAdapter{
		private MoverClient client;
		public DisconnectListener(MoverClient c){
			this.client = c;
		}
		
		@Override
		public void windowClosing(WindowEvent e) {
			client.sendMessage(new DisconnectPacket());
			try {
				client.channel.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.getWindow().dispose();
			
			System.exit(0);
		}
	}
}
