package simplemovement;

import java.awt.event.KeyAdapter;
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
		this.inputListener = new InputListener();
		this.view.addInputListener(inputListener);
	}
	
	public void update(double dt){
		handleInput(inputListener.getState(), dt);
	}
	
	public void handleInput(Input input, double dt){
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
		currentMover.move(dx, dy, dt);
	}
	
	private class InputListener extends KeyAdapter{
		private Input currentState = new Input(false, false, false, false);
		
		@Override
		public void keyPressed(KeyEvent e) {
			Input newState = getNewState(e, true);
			currentState = newState;			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			Input newState = getNewState(e, false);
			currentState = newState;
		}
		
		private Input getNewState(KeyEvent e, boolean isPressed){
			Input newState = new Input(currentState);
			switch(e.getKeyCode()){
				case KeyEvent.VK_W:
					newState.up = isPressed;
					break;
				case KeyEvent.VK_S:
					newState.down = isPressed;
					break;
				case KeyEvent.VK_A:
					newState.left = isPressed;
					break;
				case KeyEvent.VK_D:
					newState.right = isPressed;
					break;
			}
			return newState;
		}
	
		public Input getState(){
			return currentState;
		}
	}
}

