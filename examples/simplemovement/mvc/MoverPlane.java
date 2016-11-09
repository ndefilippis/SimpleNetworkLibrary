package examples.simplemovement.mvc;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import mvc.TimedModel;

public class MoverPlane extends TimedModel<MoverState> implements Observer{
	
	public MoverPlane(){
		this.state = new MoverState();
	}
	
	public void addMover(Mover mover){
		state.movers.add(mover);
		mover.addObserver(this);
		this.setChanged();
		this.notifyObservers();
	}
	
	public List<Mover> getMovers(){
		return state.movers;
	}

	@Override
	public void update(Observable o, Object arg) {
		if(o instanceof Mover){
			this.setChanged();
			this.notifyObservers();
		}
	}

	public void update(double dt) {
		for(Mover mover : state.movers){
			mover.update(dt);
		}
	}
}
