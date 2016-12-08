package examples.simplemovement.mvc;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import mvc.State;
import netcode.Serializable;

public class MoverState implements State, Serializable {
	List<Mover> movers;
	
	public MoverState(){
		movers = new ArrayList<Mover>();
	}

	@Override
	public void serializeRead(ByteBuffer buffer) {
		movers = new ArrayList<Mover>();
		int size = buffer.getInt();
		for(int i = 0; i < size; i++){
			movers.add(new Mover(buffer));
		}
	}
	
	public static MoverState interpolate(MoverState state1, MoverState state2, double delta){
		MoverState result = new MoverState();
		for(int a=0, b=0; a<state1.movers.size() && b < state2.movers.size(); a++, b++){
			if(a < state1.movers.size() && b < state2.movers.size()){
				Mover moverA = state1.movers.get(a);
				Mover moverB = state2.movers.get(b);
				if(moverA.getID() != moverB.getID()){
					if(moverA.getID() < moverB.getID()){
						while(a < state1.movers.size() && moverA.getID() < moverB.getID()){
							result.movers.add(moverA);
							a++;
							if(a < state1.movers.size()){
								moverA = state1.movers.get(a);
							}
						}
					}
					else{
						while(b < state2.movers.size() && moverB.getID() < moverA.getID()){
							result.movers.add(moverB);
							b++;
							if(b < state2.movers.size()){
								moverB = state2.movers.get(b);
							}
						}
					}
				}
				if(moverA.getID() == moverB.getID()){
					result.movers.add(Mover.interpolate(moverA, moverB, delta));
				}
			}
			else{
				while(a < state1.movers.size()){
					result.movers.add(state1.movers.get(a));
				}
				while(b < state2.movers.size()){
					result.movers.add(state2.movers.get(b));
				}
				break;
			}
		}
		return result;
	}

	@Override
	public void serializeWrite(ByteBuffer buffer) {
		buffer.putInt(movers.size());
		for(int i = 0; i < movers.size(); i++){
			movers.get(i).serializeWrite(buffer);
		}
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder("State: \n");
		for(Mover m : movers){
			sb.append(m.toString()+"\n");
		}
		return sb.toString();
	}
}
