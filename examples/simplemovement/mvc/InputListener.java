package examples.simplemovement.mvc;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public abstract class InputListener extends KeyAdapter{
		private int id = 0;
		protected MoverInput currentState = new MoverInput(false, false, false, false, 0);
		
		@Override
		public abstract void keyPressed(KeyEvent e);

		@Override
		public abstract void keyReleased(KeyEvent e);
		
		protected MoverInput getNewState(KeyEvent e, boolean isPressed){
			MoverInput newState = new MoverInput(currentState, id++);
			switch(e.getKeyCode()){
				case KeyEvent.VK_W:
				case KeyEvent.VK_UP:
					newState.up = isPressed;
					break;
				case KeyEvent.VK_S:
				case KeyEvent.VK_DOWN:
					newState.down = isPressed;
					break;
				case KeyEvent.VK_A:
				case KeyEvent.VK_LEFT:
					newState.left = isPressed;
					break;
				case KeyEvent.VK_D:
				case KeyEvent.VK_RIGHT:
					newState.right = isPressed;
					break;
			}
			return newState;
		}
	
		public MoverInput getState(){
			return currentState;
		}
}
