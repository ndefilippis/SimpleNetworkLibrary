package mvc;
import java.util.Observable;

public abstract class Model<S extends State> extends Observable{
	
	protected S state;
	
	public Model(){
		
	}
	
	public Model(S state){
		this.state = state;
	}
	
	public S getState(){
		return state;
	}
	
	public void setState(S state){
		this.state = state;
	}
}
