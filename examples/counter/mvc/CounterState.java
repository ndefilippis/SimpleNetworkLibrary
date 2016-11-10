package examples.counter.mvc;

import java.nio.ByteBuffer;

import mvc.State;
import netcode.Serializable;

public class CounterState implements State, Serializable{
	public int value;
	
	public CounterState(int value) {
		this.value = value;
	}
	
	public CounterState(){
		this.value = 0;
	}

	@Override
	public void serializeRead(ByteBuffer buffer) {
		this.value = buffer.getInt();
	}

	@Override
	public void serializeWrite(ByteBuffer buffer) {
		buffer.putInt(value);
	}
	
	
}
