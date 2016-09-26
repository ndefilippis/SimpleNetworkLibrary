package simplemovement;


public class MoverRunner {
	public static void main(String[] args){
		MoverPlane model = new MoverPlane();
		Mover myMover = new Mover(1, 0, 0);
		model.addMover(myMover);
		
		MoverViewer view = new MoverViewer(model.getMovers());
		MoverController controller = new MoverController(model, view, myMover);
		view.setVisible(true);
		long time = System.nanoTime();
		while(true){
			long currentTime = System.nanoTime();
			double dt = (currentTime - time) / 1000000000D;
			controller.update(dt);
			time = currentTime;
		}
	}
}
