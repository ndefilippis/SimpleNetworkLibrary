package mvc;

public abstract class Controller<M extends Model<?>, V extends View<?>, I extends Input> {
	
	protected M model;
	protected V view;
	
	public Controller(M model, V view){
		this.model = model;
		this.view = view;
		this.model.addObserver(view);
	}
	
	public abstract void handleInput(I input);
}
