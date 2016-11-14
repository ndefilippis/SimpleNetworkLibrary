package examples.counter.netcode;

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

import examples.counter.mvc.Counter;
import examples.counter.mvc.CounterController;
import examples.counter.mvc.CounterInput;
import examples.counter.mvc.CounterViewer;
import examples.counter.netcode.packet.ChangeValuePacket;
import examples.counter.netcode.packet.CounterPacket;
import netcode.Client;
import netcode.packet.AcceptConnectPacket;
import netcode.packet.ConnectPacket;
import netcode.packet.DisconnectPacket;
import netcode.packet.Packet;

public class CounterClient extends Client{
	private Counter counter;
	private CounterViewer viewer;
	private CounterController controller;
	private Queue<CounterInput> knownInputs = new LinkedList<CounterInput>();
	private long sendTime = 0;
	private long delay;
	
	private long id;
	
	public CounterClient(String address, int port) throws IOException{
		super(address, port);
	}
	
	@Override
	public void run(){
		addMessage(new ConnectPacket());
		sendTime = System.nanoTime();
		super.run();
	}

	@Override
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
	
	@Override
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
		this.delay = packet.getTimeReceived() - sendTime;
	}
	
	private void initialize(int value){
		counter = new Counter(value);
		viewer = new CounterViewer(counter.getState());
		viewer.addWindowListener(new DisconnectListener(this));
		viewer.addIncrementListener(new IncrementListener());
		viewer.addDecrementListener(new DecrementListener());
		controller = new CounterController(counter, viewer);
		viewer.setVisible(true);
	}
	
	private void handleChangeValue(ChangeValuePacket packet){
		if(counter == null){
			initialize(packet.getValue());
		}
		counter.setValue(packet.getValue());
		//why???
		//while(knownInputs.size() > 0 && packet.getUpdateTime() - knownInputs.peek().getTime() > 0){
		//	knownInputs.poll();
		//}
		//for(CounterInput input : knownInputs){
		//	controller.handleInput(input);
		//}
	}
	
	class IncrementListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			CounterInput input = new CounterInput(true);
			addMessage(new CounterPacket(input));
			knownInputs.offer(input);
		}
		
	}
	class DecrementListener implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			CounterInput input = new CounterInput(false);
			addMessage(new CounterPacket(input));
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
			addMessage(new DisconnectPacket());
			client.kill();
			e.getWindow().dispose();
			System.exit(0);
		}
	}
}
