package examples.forces.mvc;

import java.util.ArrayList;
import java.util.List;

import mvc.State;

public class ParticleState implements State{
	List<Particle> particles;
	
	public ParticleState(){
		this.particles = new ArrayList<Particle>();
	}
}
