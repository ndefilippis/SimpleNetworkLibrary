package mvc;
import java.util.Observable;
import java.util.Observer;


public abstract class View<S extends State> implements Observer{
	
	protected S state;
	
	public View(S initialState){
		this.state = initialState;
	}
	
	protected abstract void update(S state);
	
	@Override
	public void update(Observable o, Object arg){
		if(o instanceof Model){
			update((S)((Model) o).getState());
		}
	}
}
