package examples.counter.mvc;

import mvc.Model;

public class Counter extends Model<CounterState>{
	
	public Counter(){
		this.state = new CounterState();
	}
	
	public Counter(int value){
		this.state = new CounterState(value);
	}
	
	public void setValue(int value){
		state.value = value;
		this.setChanged();
		this.notifyObservers();
	}
	public void incrementValue(){
		state.value++;
		this.setChanged();
		this.notifyObservers();
	}	
	public void decrementValue(){
		state.value--;
		this.setChanged();
		this.notifyObservers();
	}
	public int getValue(){
		return state.value;
	}
}
