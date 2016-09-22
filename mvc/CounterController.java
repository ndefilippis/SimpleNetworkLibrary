package mvc;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CounterController {
	
	private Counter model;
	private CounterViewer view;
	
	public CounterController(Counter counter, CounterViewer view){
		this.model = counter;
		this.view = view;
		this.model.addObserver(view);
		this.view.addIncrementListener(new IncrementListener());
		this.view.addDecrementListener(new DecrementListener());
	}
	
	public void handleInput(Input input){
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
			handleInput(new Input(true));
		}
	}
	
	class DecrementListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			handleInput(new Input(false));
		}
	}
}
