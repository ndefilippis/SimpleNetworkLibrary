package simplemovement;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.LinkedList;
import java.util.Queue;

import netcode.CounterClient;
import netcode.packet.AcceptConnectPacket;
import netcode.packet.ChangeValuePacket;
import netcode.packet.ConnectPacket;
import netcode.packet.DisconnectPacket;
import netcode.packet.Packet;

public class MoverClient {
	private DatagramChannel channel;
	
	private Mover myMover;
	private MoverPlane model;
	private MoverViewer viewer;
	private MoverController controller;
	
	private ByteBuffer bufin;
	private ByteBuffer bufout;
	private Queue<Input> knownInputs = new LinkedList<Input>();
	
	private long id;
	
	public MoverClient(String address, int port) throws IOException{
		channel = DatagramChannel.open();
		channel.connect(new InetSocketAddress(address, port));
		channel.configureBlocking(false);
		sendMessage(new ConnectPacket());
	}
	
	public void start(){
		while(true){
			ByteBuffer message = recieveMessage();
			if(message != null){
				long timeReceived = System.nanoTime();
				Packet packet = messageToPacket(message, timeReceived);
				handlePacket(packet);
			}
			
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
			return new AcceptConnectPacket(timeReceived, message);
		case NEWVALUE:
			return new ChangeValuePacket(timeReceived, message);
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
				break;
			}
		}
		viewer.addWindowListener(new DisconnectListener(this));
		viewer.addInputListener(new InputListener());
		controller = new MoverController(model, viewer, myMover);
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
		for(Input input : knownInputs){
			controller.handleInput(input, input.getDt());
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
	
	public static void main(String[] args) throws IOException{
		CounterClient c = new CounterClient("localhost", 1337);
		c.start();
	}
	
	private class InputListener extends KeyAdapter{
		private Input currentState = new Input(false, false, false, false, 0);
		
		@Override
		public void keyPressed(KeyEvent e) {
			Input newState = getNewState(e, true);
			currentState = newState;
			knownInputs.add(currentState);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			Input newState = getNewState(e, false);
			currentState = newState;
			knownInputs.add(currentState);
		}
		
		private Input getNewState(KeyEvent e, boolean isPressed){
			Input newState = new Input(currentState);
			switch(e.getKeyCode()){
				case KeyEvent.VK_W:
					newState.up = isPressed;
					break;
				case KeyEvent.VK_S:
					newState.down = isPressed;
					break;
				case KeyEvent.VK_A:
					newState.left = isPressed;
					break;
				case KeyEvent.VK_D:
					newState.right = isPressed;
					break;
			}
			return newState;
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
