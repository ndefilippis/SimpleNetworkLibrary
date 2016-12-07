package examples.steering.mvc;

import mvc.Input;

public class MoverInput extends Input{
	public boolean left;
	public boolean right;
	public boolean up;
	public boolean down;
	private double dt;
	private int id;
	
	public MoverInput(boolean left, boolean right, boolean up, boolean down, double dt){
		super();
		this.left = left;
		this.right = right;
		this.down = down;
		this.up = up;
		this.dt = dt;
	}
	
	public MoverInput(MoverInput other, int id){
		super();
		this.left = other.left;
		this.right = other.right;
		this.down = other.down;
		this.up = other.up;
		this.id = id;
	}
	
	public double getDt(){
		return dt;
	}
	
	public int getID(){
		return id;
	}
	
	
	@Override
	public String toString(){
		return "left: " + left + ", right: " + right + ", up: " + up + ", down: " + down;
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof MoverInput){
			MoverInput other = (MoverInput)obj;
			return this.left == other.left && 
					this.right == other.right && 
					this.down == other.down && 
					this.up == other.up;
		}
		return false;
	}
}
