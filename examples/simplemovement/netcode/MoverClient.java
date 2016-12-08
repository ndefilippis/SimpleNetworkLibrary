package examples.simplemovement.netcode;

import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import examples.simplemovement.mvc.InputListener;
import examples.simplemovement.mvc.Mover;
import examples.simplemovement.mvc.MoverController;
import examples.simplemovement.mvc.MoverInput;
import examples.simplemovement.mvc.MoverPlane;
import examples.simplemovement.mvc.MoverState;
import examples.simplemovement.mvc.MoverViewer;
import examples.simplemovement.netcode.packet.BeginConnectionPacket;
import examples.simplemovement.netcode.packet.MoverClientPacketFactory;
import examples.simplemovement.netcode.packet.NewMoverPacket;
import examples.simplemovement.netcode.packet.NewStatePacket;
import netcode.Client;
import netcode.InputUpdateLoop;
import netcode.packet.Packet;
import threading.GameLoop;

public class MoverClient extends Client{
	private Mover myMover;
	private MoverPlane model;
	private MoverViewer viewer;
	private MoverController controller;
	private InputListener listener;
	private MoverClientPacketFactory factory;
	private GameLoop<MoverPlane> gameLoop;
	
	private volatile LinkedList<MoverInput> knownInputs = new LinkedList<MoverInput>();
	
	private long id;
	
	public MoverClient(String address, int port) throws IOException{
		super(address, port);
		this.factory = new MoverClientPacketFactory(this.acker);
		
	}
	
	
	@Override
	public void run(){
		addMessage(factory.createConnectPacket());
		super.run();
	}
	
	@Override
	public Packet messageToPacket(ByteBuffer message, long timeReceived){
		switch(Packet.lookupPacket(message)){
		case ACCEPTCONNECT:
			return new BeginConnectionPacket(timeReceived, message);
		case NEWVALUE:
			return new NewStatePacket(timeReceived, message);
		case NEWPLAYER:
			return new NewMoverPacket(timeReceived, message);
		default:
			return null;
		}
	}
	
	@Override
	public void handlePacket(Packet packet){
		switch(packet.getPacketType()){
			case ACCEPTCONNECT:
				handleConnect((BeginConnectionPacket)packet);
				break;
			case NEWVALUE:
				handleChangeValue((NewStatePacket)packet);
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
		for(Mover m : packet.getMovers()){
			if(m.getID() == this.id){
				this.myMover = m;
			}
			model.addMover(m);
		}
		viewer = new MoverViewer(model.getState());
		viewer.addWindowListener(new DisconnectListener(this));
		listener = new LagCompensatedInputListener();
		viewer.addInputListener(listener);
		gameLoop = new GameLoop<MoverPlane>(model, 16);
		new Thread(gameLoop).start();
		new Thread(new MoverInputUpdate(30, this)).start();
		controller = new MoverController(model, viewer, myMover, listener);
		
		viewer.setVisible(true);
	}
	
	private long interpolationDelay = 100;
	private MoverState lastState = new MoverState();
	private void handleChangeValue(NewStatePacket packet){
		model.setState(MoverState.interpolate(lastState, packet.getState(), gameLoop.getFractionBetweenTicks(0, 1))); //fix
		while(knownInputs.size() > 0 && knownInputs.peek().getID() < packet.getID()){
			knownInputs.poll();
		}
		synchronized(knownInputs){
			for(Iterator<MoverInput> it = knownInputs.iterator(); it.hasNext();){
				MoverInput i = it.next();
				controller.handleInput(i);
			}
		}
	}
	
	private void handleNewPlayer(NewMoverPacket packet){
		model.addMover(packet.getNewMover());
	}
	
	private class LagCompensatedInputListener extends InputListener{
		
		@Override
		public void keyPressed(KeyEvent e) {
			MoverInput newState = getNewState(e, true);
			currentState = newState;
			controller.handleInput(currentState);
			synchronized(knownInputs){
				knownInputs.add(currentState);
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			MoverInput newState = getNewState(e, false);
			currentState = newState;
			controller.handleInput(currentState);
			synchronized(knownInputs){
				knownInputs.add(currentState);
			}
		}
	
		public MoverInput getState(){
			return currentState;
		}
	}
	
	private class MoverInputUpdate extends InputUpdateLoop{

		public MoverInputUpdate(long milliDelay, Client client) {
			super(milliDelay, client);
		}

		@Override
		protected Packet serializeInput() {
			return factory.createMoverInputPacket(gameLoop.getCurrentTick(), listener.getState(), (int)id);
		}

		
	}
	
	class DisconnectListener extends WindowAdapter{
		private MoverClient client;
		public DisconnectListener(MoverClient c){
			this.client = c;
		}
		
		@Override
		public void windowClosing(WindowEvent e) {
			client.addMessage(factory.createDisconnectPacket());
			kill();
			e.getWindow().dispose();
		}
	}
}
