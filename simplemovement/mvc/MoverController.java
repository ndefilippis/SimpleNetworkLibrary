package simplemovement.mvc;

import java.awt.event.KeyEvent;

public class MoverController {
	
	private MoverPlane model;
	private Mover currentMover;
	private MoverViewer view;
	private InputListener inputListener;
	
	public MoverController(MoverPlane model, MoverViewer view, Mover currentMover){
		this.model = model;
		this.view = view;
		this.currentMover = currentMover;
		this.model.addObserver(view);
		this.inputListener = new SingleInputListener();
		this.view.addInputListener(inputListener);
	}
	
	public MoverController(MoverPlane model, MoverViewer view, Mover currentMover, InputListener listener){
		this.model = model;
		this.view = view;
		this.currentMover = currentMover;
		this.model.addObserver(view);
		this.inputListener = listener;
		this.view.addInputListener(inputListener);
	}
	
	public void update(){
		handleInput(inputListener.getState());
	}
	
	public void handleInput(Input input){
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
			Input newState = getNewState(e, true);
			currentState = newState;
			handleInput(currentState);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			Input newState = getNewState(e, false);
			currentState = newState;
			handleInput(currentState);
		}
	}
}

