package examples.simplemovement.mvc;

import java.nio.ByteBuffer;
import java.util.Observable;

import mvc.State;
import netcode.Serializable;

public class Mover extends Observable implements State, Serializable{
	private int id;
	private double x, y;
	private double dx, dy;
	
	private static double speed = 400;
	
	public Mover(int id, int x, int y){
		this.id = id;
		this.x = x;
		this.y = y;
	}
	
	public Mover(ByteBuffer buffer){
		serializeRead(buffer);
	}
	
	public void update(double dt){
		double dxdt = 0;
		double dydt = 0;
		if(dx != 0 || dy != 0){
			double length = Math.sqrt(dx * dx + dy * dy);
			dxdt =  dx * dt * speed/length;
			dydt =  dy * dt * speed/length;
		}
		x += dxdt;
		y += dydt;
		this.setChanged();
		this.notifyObservers();
	}
	
	public void setSpeed(double dx, double dy){
		this.dx = dx;
		this.dy = dy;
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
	
	public double getDx(){
		return dx;
	}
	
	public double getDy(){
		return dy;
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

	
	@Override
	public void serializeRead(ByteBuffer buffer) {
		this.id = buffer.getInt();
		this.x  = buffer.getDouble();
		this.y  = buffer.getDouble();
		this.dx = buffer.getDouble();
		this.dy = buffer.getDouble();
	}

	@Override
	public void serializeWrite(ByteBuffer buffer) {
		buffer.putInt(id);
		buffer.putDouble(x);
		buffer.putDouble(y);
		buffer.putDouble(dx);
		buffer.putDouble(dy);
	}

	public static Mover interpolate(Mover moverA, Mover moverB, double delta) {
		Mover m = new Mover(
			moverA.id,
			(int)lerp(moverA.x, moverB.x, delta),
			(int)lerp(moverA.y, moverB.y, delta)
		);
		m.setSpeed(lerp(moverA.dx, moverB.dx, delta), lerp(moverA.dy, moverB.dy, delta));
		return m;
	}
	
	private static double lerp(double v1, double v2, double alpha){
		return v2 * alpha  + v1 * ( 1 - alpha);
	} 
}
