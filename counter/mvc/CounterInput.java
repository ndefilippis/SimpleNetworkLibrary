package counter.mvc;

import mvc.Input;

public class CounterInput extends Input{
	private boolean isIncrement;
	
	public CounterInput(boolean isIncrement){
		super();
		this.isIncrement = isIncrement;
	}
	public boolean isIncrement(){
		return isIncrement;
	}
}
