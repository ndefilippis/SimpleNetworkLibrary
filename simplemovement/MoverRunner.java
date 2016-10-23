package simplemovement;

import simplemovement.mvc.Mover;
import simplemovement.mvc.MoverController;
import simplemovement.mvc.MoverPlane;
import simplemovement.mvc.MoverViewer;

public class MoverRunner {
	public static void main(String[] args){
		MoverPlane model = new MoverPlane();
		Mover myMover = new Mover(1, 0, 0);
		model.addMover(myMover);
		
		MoverViewer view = new MoverViewer(model.getMovers());
		MoverController controller = new MoverController(model, view, myMover);
		view.setVisible(true);
		GameLoop loop = new GameLoop(model);
		Thread thread = new Thread(loop);
		thread.start();
	}
}
