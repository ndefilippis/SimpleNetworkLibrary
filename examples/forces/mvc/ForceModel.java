package examples.forces.mvc;

import mvc.TimedModel;

public class ForceModel extends TimedModel<ParticleState>{
	
	public ForceModel(){
		this.state = new ParticleState();		
	}
	
	public void addParticle(Particle p){
		this.state.particles.add(p);
	}

	@Override
	public void update(double dt) {
		for(Particle p : state.particles){
			p.update(this.state.particles, dt);
		}
		this.setChanged();
		this.notifyObservers();
	}
	
	
}
