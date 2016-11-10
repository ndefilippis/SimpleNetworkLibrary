package examples.simplemovement;

import examples.simplemovement.mvc.Mover;
import examples.simplemovement.mvc.MoverController;
import examples.simplemovement.mvc.MoverPlane;
import examples.simplemovement.mvc.MoverViewer;
import threading.GameLoop;

public class MoverRunner {
	public static void main(String[] args){
		MoverPlane model = new MoverPlane();
		Mover myMover = new Mover(1, 0, 0);
		model.addMover(myMover);
		
		MoverViewer view = new MoverViewer(model.getState());
		MoverController controller = new MoverController(model, view, myMover);
		view.setVisible(true);
		GameLoop<MoverPlane> loop = new GameLoop<MoverPlane>(model, 16);
		Thread thread = new Thread(loop);
		thread.start();
	}
}
