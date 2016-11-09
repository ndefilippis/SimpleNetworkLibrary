package examples.counter.mvc;

import mvc.State;

public class CounterState implements State{
	public int value;
	
	public CounterState(int value) {
		this.value = value;
	}
	
	public CounterState(){
		this.value = 0;
	}
	
	
}
