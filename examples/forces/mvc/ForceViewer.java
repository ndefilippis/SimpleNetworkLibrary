package examples.forces.mvc;

import java.awt.BorderLayout;
import java.awt.event.KeyListener;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import mvc.View;

public class ForceViewer extends View<ParticleState>{
	private JFrame frame;
	private ForcePanel screen;
	
	public ForceViewer(ParticleState initialState) {
		super(initialState);
		this.frame = new JFrame("Forces!");
		this.screen = new ForcePanel();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000, 750);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(screen, BorderLayout.CENTER);
		screen.update(state.particles);
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
	protected void update(ParticleState state) {
		screen.update(state.particles);
	}

}
