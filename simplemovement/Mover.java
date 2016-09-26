package simplemovement;

import java.util.Observable;

public class Mover extends Observable{
	private int id;
	private double x, y;
	
	private static double speed = 100;
	
	public Mover(int id, int x, int y){
		this.id = id;
		this.x = x;
		this.y = y;
	}
	
	public void move(double dx, double dy, double dt){
		if(dx != 0 || dy != 0){
			double length = Math.sqrt(dx * dx + dy * dy);
			dx *= dt * speed/length;
			dy *= dt * speed/length;
		}
		x += dx;
		y += dy;
		this.setChanged();
		this.notifyObservers();
	}
	
	public int getID(){
		return id;
	}
	
	public int getX(){
		return (int)x;
	}
	
	public int getY(){
		return (int)y;
	}
	
	@Override
	public String toString(){
		return id + ": ["+x + ", " + y + "]";
	}
	
	public void setCoords(int x, int y){
		this.x = x;
		this.y = y;
		this.setChanged();
		this.notifyObservers();
	}
}
