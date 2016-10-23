package simplemovement.mvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MoverPlane extends Observable implements Observer{
	private List<Mover> movers = new ArrayList<Mover>();
	
	public void addMover(Mover mover){
		movers.add(mover);
		mover.addObserver(this);
		this.setChanged();
		this.notifyObservers();
	}
	
	public List<Mover> getMovers(){
		return movers;
	}

	@Override
	public void update(Observable o, Object arg) {
		if(o instanceof Mover){
			this.setChanged();
			this.notifyObservers();
		}
	}

	public void update(double dt) {
		for(Mover mover : movers){
			mover.update(dt);
		}
	}
}
