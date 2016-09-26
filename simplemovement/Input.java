package simplemovement;

public class Input {
	public boolean left;
	public boolean right;
	public boolean up;
	public boolean down;
	
	public Input(boolean left, boolean right, boolean up, boolean down){
		this.left = left;
		this.right = right;
		this.down = down;
		this.up = up;
	}
	
	public Input(Input other){
		this.left = other.left;
		this.right = other.right;
		this.down = other.down;
		this.up = other.up;
	}
	
	@Override
	public String toString(){
		return "left: " + left + ", right: " + right + ", up: " + up + ", down: " + down;
	}
}
