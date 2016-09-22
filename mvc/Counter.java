package mvc;
import java.util.Observable;

public class Counter extends Observable{
	private int value;
	
	public Counter(){
		this.value = 0;
	}
	
	public Counter(int value){
		this.value = value;
	}
	
	public void setValue(int value){
		this.value = value;
		this.setChanged();
		this.notifyObservers();
	}
	public void incrementValue(){
		value++;
		this.setChanged();
		this.notifyObservers();
	}	
	public void decrementValue(){
		value--;
		this.setChanged();
		this.notifyObservers();
	}
	public int getValue(){
		return value;
	}
}
