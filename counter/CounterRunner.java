package counter;

import counter.mvc.Counter;
import counter.mvc.CounterController;
import counter.mvc.CounterViewer;

public class CounterRunner {
	public static void main(String[] args){
		Counter model = new Counter();
		CounterViewer view = new CounterViewer(model.getState());
		CounterController controller = new CounterController(model, view);
		view.setVisible(true);
	}
}
