package mvc;

public class CounterRunner {
	public static void main(String[] args){
		Counter model = new Counter();
		CounterViewer view = new CounterViewer(model.getValue());
		CounterController controller = new CounterController(model, view);
		view.setVisible(true);
	}
}
