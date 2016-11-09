package mvc;

public abstract class TimedModel<S extends State> extends Model<S>{
	
	public abstract void update(double dt);
}
