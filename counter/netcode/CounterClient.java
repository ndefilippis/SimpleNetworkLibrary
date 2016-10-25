package counter.netcode;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.LinkedList;
import java.util.Queue;

import counter.mvc.Counter;
import counter.mvc.CounterController;
import counter.mvc.CounterInput;
import counter.mvc.CounterViewer;
import counter.netcode.packet.CounterPacket;
import netcode.packet.AcceptConnectPacket;
import netcode.packet.ChangeValuePacket;
import netcode.packet.ConnectPacket;
import netcode.packet.DisconnectPacket;
import netcode.packet.Packet;

public class CounterClient{
	private DatagramChannel channel;
	private Counter counter;
	private CounterViewer viewer;
	private CounterController controller;
	private ByteBuffer bufin;
	private ByteBuffer bufout;
	private Queue<CounterInput> knownInputs = new LinkedList<CounterInput>();
	
	private long id;
	
	public CounterClient(String address, int port) throws IOException{
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
				handleConnect((AcceptConnectPacket)packet);
				break;
			case NEWVALUE:
				handleChangeValue((ChangeValuePacket)packet);
				break;
			default:
				break;
		}
	}
	
	private void handleConnect(AcceptConnectPacket packet){
		this.id = packet.getID();
		counter = new Counter(packet.getState());
		viewer = new CounterViewer(counter.getState());
		viewer.addWindowListener(new DisconnectListener(this));
		viewer.addIncrementListener(new IncrementListener());
		viewer.addDecrementListener(new DecrementListener());
		controller = new CounterController(counter, viewer);
		viewer.setVisible(true);
	}
	
	private void handleChangeValue(ChangeValuePacket packet){
		counter.setValue(packet.getValue());
		while(knownInputs.size() > 0 && knownInputs.peek().getTime() < packet.getUpdateTime()){
			knownInputs.poll();
		}
		for(CounterInput input : knownInputs){
			controller.handleInput(input);
		}
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
	
	class IncrementListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			CounterInput input = new CounterInput(true);
			sendMessage(new CounterPacket(input));
			knownInputs.offer(input);
		}
		
	}
	class DecrementListener implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			CounterInput input = new CounterInput(false);
			sendMessage(new CounterPacket(input));
			knownInputs.offer(input);
		}
	}
	class DisconnectListener extends WindowAdapter{
		private CounterClient client;
		public DisconnectListener(CounterClient c){
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