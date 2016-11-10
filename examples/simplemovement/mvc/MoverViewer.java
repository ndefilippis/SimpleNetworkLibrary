package examples.simplemovement.mvc;

import java.awt.BorderLayout;
import java.awt.event.KeyListener;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import mvc.View;

public class MoverViewer extends View<MoverState>{
	private JFrame frame = new JFrame();
	private MoverPanel screen = new MoverPanel();
	
	public MoverViewer(MoverState state){
		super(state);
		frame.setTitle("Simple Mover Example");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300, 400);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(screen, BorderLayout.CENTER);
		screen.update(state.movers);
	}
	
	
	public void setVisible(boolean value){
		frame.setVisible(value);
	}
	
	public void addWindowListener(WindowListener listener){
		frame.addWindowListener(listener);
	}

	
	public void addInputListener(KeyListener listener){
		this.frame.addKeyListener(listener);
	}

	@Override
	protected void update(MoverState state) {
		screen.update(state.movers);
	}
}
