package simplemovement;

import java.awt.BorderLayout;
import java.awt.event.KeyListener;
import java.awt.event.WindowListener;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

public class MoverViewer implements Observer{
	private JFrame frame = new JFrame();
	private MoverPanel screen = new MoverPanel();
	
	public MoverViewer(List<Mover> movers){
		frame.setTitle("Simple Mover Example");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300, 400);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(screen, BorderLayout.CENTER);
		screen.update(movers);
	}
	
	public void setVisible(boolean value){
		frame.setVisible(value);
	}
	
	public void addWindowListener(WindowListener listener){
		frame.addWindowListener(listener);
	}

	@Override
	public void update(Observable o, Object arg) {
		if(o instanceof MoverPlane){
			screen.update(((MoverPlane)o).getMovers());
		}
	}
	
	public void addInputListener(KeyListener listener){
		this.frame.addKeyListener(listener);
	}
}
