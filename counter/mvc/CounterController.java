package counter.mvc;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import mvc.Controller;

public class CounterController extends Controller<Counter, CounterViewer, CounterInput>{
	
	public CounterController(Counter counter, CounterViewer view){
		super(counter, view);
		this.view.addIncrementListener(new IncrementListener());
		this.view.addDecrementListener(new DecrementListener());
	}
	
	@Override
	public void handleInput(CounterInput input){
		if(input.isIncrement()){
			model.incrementValue();
		}
		else{
			model.decrementValue();
		}
	}
	
	class IncrementListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			handleInput(new CounterInput(true));
		}
	}
	
	class DecrementListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			handleInput(new CounterInput(false));
		}
	}
}
