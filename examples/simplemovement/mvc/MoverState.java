package examples.simplemovement.mvc;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import mvc.State;
import netcode.Serializable;

public class MoverState implements State, Serializable {
	List<Mover> movers;
	
	public MoverState(){
		movers = new ArrayList<Mover>();
	}

	@Override
	public void serializeRead(ByteBuffer buffer) {
		movers = new ArrayList<Mover>();
		int size = buffer.getInt();
		for(int i = 0; i < size; i++){
			movers.add(new Mover(buffer));
		}
	}

	@Override
	public void serializeWrite(ByteBuffer buffer) {
		buffer.putInt(movers.size());
		for(int i = 0; i < movers.size(); i++){
			serializeWrite(buffer);
		}
	}
}
