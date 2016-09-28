package simplemovement;

public class Input {
	public boolean left;
	public boolean right;
	public boolean up;
	public boolean down;
	private double dt;
	private long time;
	
	public Input(boolean left, boolean right, boolean up, boolean down, double dt){
		this.left = left;
		this.right = right;
		this.down = down;
		this.up = up;
		this.dt = dt;
		this.time = System.nanoTime();
	}
	
	public Input(Input other){
		this.left = other.left;
		this.right = other.right;
		this.down = other.down;
		this.up = other.up;
		this.time = System.nanoTime();
		this.dt = (this.time - other.time)/1000000000D;
	}
	
	public double getDt(){
		return dt;
	}
	
	public long getTime(){
		return this.time;
	}
	
	@Override
	public String toString(){
		return "left: " + left + ", right: " + right + ", up: " + up + ", down: " + down;
	}
}
