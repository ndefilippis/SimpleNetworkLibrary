package examples.simplemovement.mvc;

import java.awt.event.KeyEvent;

import mvc.Controller;

public class MoverController extends Controller<MoverPlane, MoverViewer, MoverInput>{
	private Mover currentMover;
	private InputListener inputListener;
	
	public MoverController(MoverPlane model, MoverViewer view, Mover currentMover){
		super(model, view);
		this.currentMover = currentMover;
		this.inputListener = new SingleInputListener();
		this.view.addInputListener(inputListener);
	}
	
	public MoverController(MoverPlane model, MoverViewer view, Mover currentMover, InputListener listener){
		super(model, view);
		this.currentMover = currentMover;
		this.inputListener = listener;
		this.view.addInputListener(inputListener);
	}
	
	public void update(){
		handleInput(inputListener.getState());
	}
	
	public void handleInput(MoverInput input){
		int dx = 0;
		int dy = 0;
		if(input.left){
			dx -= 1;
		}
		if(input.right){
			dx += 1;
		}
		if(input.down){
			dy -= 1;
		}
		if(input.up){
			dy += 1;
		}
		currentMover.setSpeed(dx, dy);
	}
	
	private class SingleInputListener extends InputListener{
		@Override
		public void keyPressed(KeyEvent e) {
			MoverInput newState = getNewState(e, true);
			currentState = newState;
			handleInput(currentState);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			MoverInput newState = getNewState(e, false);
			currentState = newState;
			handleInput(currentState);
		}
	}
}

