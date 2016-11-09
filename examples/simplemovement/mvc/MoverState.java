package examples.simplemovement.mvc;

import java.util.ArrayList;
import java.util.List;

import mvc.State;

public class MoverState implements State {
	List<Mover> movers;
	
	public MoverState(){
		movers = new ArrayList<Mover>();
	}
}
