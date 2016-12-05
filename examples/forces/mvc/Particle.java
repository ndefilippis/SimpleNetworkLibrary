package examples.forces.mvc;

import java.util.Collection;
import java.util.Observable;
import mvc.State;

public class Particle extends Observable implements State{
	private final static double K_CONST =  8.9875517873681764E6;
	private final static double  DECREASE = 0.001;
	private double x, y;
	private double mass;
	private double charge;
	private double dx, dy;
	
	public Particle(double x, double y, double charge){
		this(x, y, 1.0, charge, 0, 0);
	}
	
	public Particle(double x, double y, double mass, double charge, double dx, double dy){
		this.x = x;
		this.y = y;
		this.mass = mass;
		this.charge = charge;
		this.dx = dx;
		this.dy = dy;
	}
	
	public void update(Collection<Particle> particles, double dt){
		double fx = 0;
		double fy = 0;
		for(Particle p : particles){
			if(p == this){
				continue;
			}
			double angle = Math.atan2(p.y - this.y, p.x - this.x);
			double distance2 = (p.x - this.x) * (p.x - this.x) + (p.y - this.y) *  (p.y - this.y);
			fx -= K_CONST * charge * p.charge / distance2 * Math.cos(angle);
			fy -= K_CONST * charge * p.charge / distance2 * Math.sin(angle);
		}
		fx /= mass;
		fy /= mass;
		dx += fx * dt;
		dy += fy * dt;
		dx *= 1 - dt * DECREASE;
		dy *= 1 - dt * DECREASE;
		x += dx * dt;
		y += dy * dt;
	}

	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	public double getCharge(){
		return charge;
	}
	
	public double getMass(){
		return mass;
	}
}
