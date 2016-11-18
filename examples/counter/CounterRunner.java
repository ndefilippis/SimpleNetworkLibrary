package examples.counter;

import examples.counter.mvc.Counter;
import examples.counter.mvc.CounterController;
import examples.counter.mvc.CounterViewer;

public class CounterRunner {
	public static void main(String[] args) {
		Counter model = new Counter();
		CounterViewer view = new CounterViewer(model.getState());
		@SuppressWarnings("unused")
		CounterController controller = new CounterController(model, view);
		view.setVisible(true);
	}
}
