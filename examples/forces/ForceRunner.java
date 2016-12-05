package examples.forces;

import examples.forces.mvc.ForceModel;
import examples.forces.mvc.ForceViewer;
import examples.forces.mvc.Particle;
import threading.GameLoop;

public class ForceRunner {
	public static void main(String[] args){
		ForceModel model = new ForceModel();
		for(int i = 0; i < 2; i++){
			double x = 1000 * Math.random();
			double y = 750 * Math.random();
			double charge = 20 * Math.random() - 10;
			double mass = 50 * Math.random();
			double dx = 5 * Math.random();
			double dy = 5 * Math.random();
			Particle p = new Particle(x, -y, mass, charge, dx, dy);
			model.addParticle(p);
		}
		ForceViewer viewer = new ForceViewer(model.getState());
		model.addObserver(viewer);
		GameLoop<ForceModel> loop = new GameLoop<ForceModel>(model, 1, 5);
		viewer.setVisible(true);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new Thread(loop).start();
		
	}
}
